package com.emc.it.configuration.write2db;

import org.apache.log4j.Logger;
import org.springframework.batch.item.file.LineCallbackHandler;

/**
 * 
 * @author chaurs
 * 
 */
public class FtpHeaderLineMapper implements LineCallbackHandler {

	private static Logger logger = Logger.getLogger(FtpHeaderLineMapper.class);

	@Override
	public void handleLine(String line) {
		logger.info("Skip the header line...");
	}
}
