package nl.craftsmen.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public final class ConditionalLogger {

    public void error(Logger logger, String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    public void error(Logger logger, String message, Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error(message, t);
        }
    }

    public void error(Logger logger, String message, Object... arguments) {
        if (logger.isErrorEnabled()) {
            logger.error(message, arguments);
        }
    }

    public void error(Logger logger, String message, Throwable t, Object... arguments) {
        if (logger.isErrorEnabled()) {
            message = replaceArguments(message, arguments);
            logger.error(message, t);
        }
    }

    public void warn(Logger logger, String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public void warn(Logger logger, String message, Throwable t) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, t);
        }
    }

    public void warn(Logger logger, String message, Object... arguments) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, arguments);
        }
    }

    public void warn(Logger logger, String message, Throwable t, Object... arguments) {
        if (logger.isWarnEnabled()) {
            message = replaceArguments(message, arguments);
            logger.warn(message, t);
        }
    }

    public void info(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public void info(Logger logger, String message, Throwable t) {
        if (logger.isInfoEnabled()) {
            logger.info(message, t);
        }
    }

    public void info(Logger logger, String message, Throwable t, Object... arguments) {
        if (logger.isInfoEnabled()) {
            message = replaceArguments(message, arguments);
            logger.info(message, t);
        }
    }

    public void info(Logger logger, String message, Object... arguments) {
        if (logger.isInfoEnabled()) {
            logger.info(message, arguments);
        }
    }

    public void debug(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public void debug(Logger logger, String message, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, t);
        }
    }

    public void debug(Logger logger, String message, Object... arguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, arguments);
        }
    }

    public void debug(Logger logger, String message, Throwable t, Object... arguments) {
        if (logger.isDebugEnabled()) {
            message = replaceArguments(message, arguments);
            logger.debug(message, t);
        }
    }

    public void trace(Logger logger, String message) {
        if (logger.isTraceEnabled()) {
            logger.trace(message);
        }
    }

    public void trace(Logger logger, String message, Throwable t) {
        if (logger.isTraceEnabled()) {
            logger.trace(message, t);
        }
    }

    public void trace(Logger logger, String message, Object... arguments) {
        if (logger.isTraceEnabled()) {
            logger.trace(message, arguments);
        }
    }

    public void trace(Logger logger, String message, Throwable t, Object... arguments) {
        if (logger.isTraceEnabled()) {
            message = replaceArguments(message, arguments);
            logger.trace(message, t);
        }
    }

    private String replaceArguments(String message, Object... arguments) {
        for (Object argument : arguments) {
            message = replacePlaceholder(message, argument.toString());
        }
        return message;
    }

    private String replacePlaceholder(String message, String argument) {
        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile("\\{\\d*}");
        Matcher m = p.matcher(message);
        while (m.find()) {
            m.appendReplacement(sb, argument);
            break;
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
