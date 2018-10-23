/*
 * Copyright 2016 MiLaboratory.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.milaboratory.cli;

import com.beust.jcommander.*;

import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;

public class JCommanderBasedMain implements ActionHelper {
    // LinkedHashMap to preserve order of actions
    protected final Map<String, Action> actions = new LinkedHashMap<>();
    protected final String command;
    protected boolean printHelpOnError = false;
    protected boolean printStackTrace = false;
    protected Runnable shortVersionInfoCallback = null;
    protected Runnable fullVersionInfoCallback = null;
    protected PrintStream outputStream = System.err;
    protected String[] arguments;

    public JCommanderBasedMain(String command, Action... actions) {
        this.command = command;
        for (Action action : actions)
            reg(action);
    }

    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public PrintStream getDefaultPrintStream() {
        return outputStream;
    }

    @Override
    public String getCommandLineArguments() {
        StringBuilder builder = new StringBuilder();
        for (String arg : arguments) {
            if (builder.length() != 0)
                builder.append(" ");
            builder.append(arg);
        }
        return builder.toString();
    }

    public boolean isPrintStackTrace() {
        return printStackTrace;
    }

    public void setPrintStackTrace(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }

    protected void reg(Action a) {
        actions.put(a.command(), a);
    }

    public ProcessResult main(String... args) throws Exception {
        // Saving current arguments
        this.arguments = args;

        if (args.length == 0) {
            printGlobalHelp();
            return ProcessResult.Help;
        }

        // Setting up JCommander
        MainParameters mainParameters = getMainParameters();
        JCommander commander = new JCommander(mainParameters);
        commander.setProgramName(command);
        for (Action a : actions.values())
            addCommand(commander, a);

        // Getting command name
        String commandName = args[0];

        // Getting corresponding action
        Action action = actions.get(commandName);

        try {
            if (action != null && (action instanceof ActionParametersParser)) {
                ((ActionParametersParser) action).parseParameters(Arrays.copyOfRange(args, 1, args.length));
            } else {
                commander.parse(args);

                // Print Version information if requested and exit.
                if (mainParameters instanceof MainParametersWithVersion &&
                        ((MainParametersWithVersion) mainParameters).shortVersion()) {
                    shortVersionInfoCallback.run();
                    return ProcessResult.Version;
                }

                // Print Version information if requested and exit.
                if (mainParameters instanceof MainParametersWithVersion &&
                        ((MainParametersWithVersion) mainParameters).fullVersion()) {
                    fullVersionInfoCallback.run();
                    return ProcessResult.Version;
                }

                // Print complete help if requested
                if (mainParameters.help()) {
                    // Creating new instance of jCommander to add only non-hidden actions
                    printGlobalHelp();
                    return ProcessResult.Help;
                }

                if (args.length == 1 && !args[0].startsWith("-")) {
                    action = actions.get(commandName);
                    if (!action.getClass().isAnnotationPresent(AllowNoArguments.class)) {
                        System.out.println("Error: missing required arguments.\n");
                        printActionHelp(commander, action);
                        return ProcessResult.Error;
                    }
                }

                // Getting parsed command
                // assert parsedCommand.equals(commandName)
                final String parsedCommand = commander.getParsedCommand();

                // Processing potential errors
                if (parsedCommand == null || !actions.containsKey(parsedCommand)) {
                    if (parsedCommand == null)
                        outputStream.println("No command specified.");
                    else
                        outputStream.println("Command " + parsedCommand + " not supported.");
                    outputStream.println("Use -h option to get a list of supported commands.");
                    return ProcessResult.Error;
                }

                action = actions.get(parsedCommand);
            }

            if (action.params().help()) {
                printActionHelp(commander, action);
            } else {
                printDeprecationNotes(commander, action);
                action.params().validate();
                action.go(this);
            }
        } catch (ParameterException | ProcessException pe) {
            printException(pe, commander, action);
            return ProcessResult.Error;
        }
        return ProcessResult.Ok;
    }

    private static void addCommand(JCommander commander, Action action) {
        commander.addCommand(action.command(), action.params());

        try {
            // processing force hide; this is a dirty hack to overcome
            // issues (by design) with reflection and inheritance in JCommander
            JCommander innerCommander = commander.getCommands().get(action.command());
            out:
            for (String hiddenOpt : action.params().forceHideParameters()) {
                for (ParameterDescription pd : innerCommander.getParameters()) {
                    if (!pd.getLongestName().equals(hiddenOpt))
                        continue;

                    WrappedParameter oldWP = pd.getParameter();
                    WrappedParameter newWP = new WrappedParameter(oldWP.getParameter()) {
                        @Override
                        public boolean hidden() { return true; }
                    };

                    Field field = pd.getClass().getDeclaredField("wrappedParameter");
                    field.setAccessible(true);
                    field.set(pd, newWP);
                    field.setAccessible(false);

                    // this would change the global behaviour, don't use it for now,
                    // leave here for possible use in future
                    //
                    // Parameter annotation = pd.getParameter().getParameter();
                    // pd.getParameter().hidden()
                    // changeAnnotationValue(annotation, "hidden", true);

                    assert pd.getParameter().hidden();
                    continue out;
                }
                throw new RuntimeException();
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Changes the annotation value for the given key of the given annotation to newValue and returns the previous
     * value.
     *
     * https://stackoverflow.com/a/28118436/946635
     */
    @SuppressWarnings("unchecked")
    private static Object changeAnnotationValue(Annotation annotation, String key, Object newValue) {
        Object handler = Proxy.getInvocationHandler(annotation);
        Field f;
        try {
            f = handler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(e);
        }
        f.setAccessible(true);
        Map<String, Object> memberValues;
        try {
            memberValues = (Map<String, Object>) f.get(handler);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        Object oldValue = memberValues.get(key);
        if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
            throw new IllegalArgumentException();
        }
        memberValues.put(key, newValue);
        return oldValue;
    }

    private MainParameters getMainParameters() {
        return shortVersionInfoCallback != null ?
                new MainParametersWithVersion() :
                new MainParameters();
    }

    protected void printGlobalHelp() {
        // Creating new instance of jCommander to add only non-hidden actions
        JCommander tmpCommander = new JCommander(getMainParameters());
        tmpCommander.setProgramName(command);
        for (Action a : actions.values())
            if (!a.getClass().isAnnotationPresent(HiddenAction.class))
                addCommand(tmpCommander, a);
        StringBuilder builder = new StringBuilder();
        tmpCommander.usage(builder);
        outputStream.print(builder);
    }

    protected void printActionHelp(JCommander commander, Action action) {
        StringBuilder builder = new StringBuilder();
        if (action instanceof ActionHelpProvider) {
            if (((ActionHelpProvider) action).printDefaultHelp()) {
                commander.usage(action.command(), builder);
                builder.append("\n");
            }
            ((ActionHelpProvider) action).printHelp(builder);
        } else
            commander.usage(action.command(), builder);
        outputStream.print(builder);
    }

    protected void printException(RuntimeException e,
                                  JCommander commander, Action action) {
        outputStream.println("Error: " + e.getMessage());
        if (printStackTrace)
            e.printStackTrace(new PrintStream(outputStream));
        if (printHelpOnError)
            printActionHelp(commander, action);
    }

    protected static void printDeprecationNotes(JCommander commander, Action action) {
        JCommander ajc = commander.getCommands().get(action.command());
        List<ParameterDescription> aParameters = ajc.getParameters();
        ActionParameters params = action.params();
        for (Field field : params.getClass().getFields()) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter == null)
                continue;
            DeprecatedParameter deprecated = field.getAnnotation(DeprecatedParameter.class);
            if (deprecated == null)
                continue;
            try {
                Object value = field.get(params);
                if (value == null)
                    continue;

                ParameterDescription pd = null;
                for (ParameterDescription p : aParameters)
                    if (Objects.equals(parameter, p.getParameter().getParameter())) {
                        pd = p;
                        break;
                    }
                if (pd == null || !pd.isAssigned())
                    continue;
                
                String message = "WARNING: " + Arrays.toString(parameter.names()) + " is deprecated";
                if (!deprecated.version().isEmpty())
                    message += " (since version " + deprecated.version() + ").";
                else
                    message += ".";

                message += " ";
                message += deprecated.value();
                System.err.println(message);
            } catch (IllegalAccessException e) {}
        }
    }

    public enum ProcessResult {
        Ok, Version, Help, Error
    }

    /**
     * Enables -v / --version parameter.
     *
     * Sets callback that will be invoked if this option is specified by user.
     *
     * @param versionInfoCallback callback to be will be invoked if user specified -v option
     */
    public void setVersionInfoCallback(Runnable versionInfoCallback) {
        if (versionInfoCallback == null)
            throw new NullPointerException();
        this.shortVersionInfoCallback = versionInfoCallback;
        this.fullVersionInfoCallback = versionInfoCallback;
    }

    /**
     * Enables -v / --version parameter.
     *
     * Sets separate callbacks that will be invoked if -v or --version this option is specified by user.
     *
     * @param shortVersionInfoCallback callback to be will be invoked if user specified -v option
     * @param fullVersionInfoCallback  callback to be will be invoked if user specified --version option
     */
    public void setVersionInfoCallback(Runnable shortVersionInfoCallback, Runnable fullVersionInfoCallback) {
        if (shortVersionInfoCallback == null || fullVersionInfoCallback == null)
            throw new NullPointerException();
        this.shortVersionInfoCallback = shortVersionInfoCallback;
        this.fullVersionInfoCallback = fullVersionInfoCallback;
    }

    /**
     * Removes -v / --version option callbacks, and removes option by itself.
     */
    public void removeVersionInfoCallback() {
        this.shortVersionInfoCallback = null;
        this.fullVersionInfoCallback = null;
    }

    public static class MainParameters {
        @Parameter(names = {"-h", "--help"}, help = true, description = "Displays this help message.")
        public boolean help;

        public boolean help() {
            return help;
        }
    }

    public static class MainParametersWithVersion extends MainParameters {
        @Parameter(names = {"-v"}, help = true, description = "Output short version information.")
        public boolean shortVersion = false;

        @Parameter(names = {"--version"}, help = true, description = "Output full version information.")
        public boolean fullVersion = false;

        public boolean shortVersion() {
            return shortVersion;
        }

        public boolean fullVersion() {
            return fullVersion;
        }
    }
}
