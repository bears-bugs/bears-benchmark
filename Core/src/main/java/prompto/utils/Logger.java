package prompto.utils;

import java.util.function.Supplier;

import org.slf4j.LoggerFactory;

/* adds lambda support to slf4j */
public class Logger {

	org.slf4j.Logger logger;
	
	public Logger() {
		StackTraceElement elem = new Throwable().getStackTrace()[1];
		logger = LoggerFactory.getLogger(elem.getClassName());
	}
	
	public String getLoggedClass() {
		return logger.getName();
	}
	
	public void info(Supplier<String> message) {
		if(logger.isInfoEnabled())
			logger.info(message.get());
	}
	
	public void debug(Supplier<String> message) {
		if(logger.isDebugEnabled())
			logger.debug(message.get());
	}

	public void debug(Supplier<String> message, Throwable t) {
		if(logger.isDebugEnabled())
			logger.debug(message.get(), t);
	}

	public void trace(Supplier<String> message) {
		if(logger.isTraceEnabled())
			logger.trace(message.get());
	}

	public void trace(Supplier<String> message, Throwable t) {
		if(logger.isTraceEnabled())
			logger.trace(message.get(), t);
	}

	public void warn(Supplier<String> message) {
		if(logger.isWarnEnabled())
			logger.warn(message.get());
	}

	public void warn(Supplier<String> message, Throwable t) {
		if(logger.isWarnEnabled())
			logger.warn(message.get(), t);
	}
	
	public void error(Supplier<String> message) {
		if(logger.isErrorEnabled())
			logger.error(message.get());
	}

	public void error(Supplier<String> message, Throwable t) {
		if(logger.isErrorEnabled())
			logger.error(message.get(), t);
	}
	

}
