package org.springframework.samples.petclinic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class log4jtest {

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        logger.trace("trace level");
        logger.debug("debug level");
        logger.info("info level");
        logger.warn("warn level");
        logger.error("error level");
        logger.fatal("fatal level");
    }

}
