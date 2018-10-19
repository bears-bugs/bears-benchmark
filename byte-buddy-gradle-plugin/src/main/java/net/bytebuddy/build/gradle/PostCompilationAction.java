package net.bytebuddy.build.gradle;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.tasks.compile.AbstractCompile;

/**
 * A compilation action that applies a class file transformation after a compilation task.
 */
public class PostCompilationAction implements Action<AbstractCompile> {

    /**
     * The current project.
     */
    private final Project project;

    /**
     * The Byte Buddy extension of this build.
     */
    private final ByteBuddyExtension byteBuddyExtension;

    /**
     * Creates a new post compilation action.
     *
     * @param project            The current project.
     * @param byteBuddyExtension The Byte Buddy extension of this build.
     */
    protected PostCompilationAction(Project project, ByteBuddyExtension byteBuddyExtension) {
        this.project = project;
        this.byteBuddyExtension = byteBuddyExtension;
    }

    /**
     * Creates a post compilation action.
     *
     * @param project The project to apply the action upon.
     * @return An appropriate action.
     */
    public static Action<AbstractCompile> of(Project project) {
        return new PostCompilationAction(project, project.getExtensions().create("byteBuddy", ByteBuddyExtension.class, project));
    }

    /**
     * {@inheritDoc}
     */
    public void execute(AbstractCompile task) {
        if (byteBuddyExtension.implies(task)) {
            task.doLast(new TransformationAction(project, byteBuddyExtension, task));
        } else {
            project.getLogger().info("Skipping non-specified task {}", task.getName());
        }
    }
}
