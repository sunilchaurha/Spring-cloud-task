package com.emc.it.util;

import java.io.File;

/**
 * Message gateway for the FTP File Download Spring Integration Flow
 *
*/
public interface FTPDownloadGateway {
	
	public File downloadFilesFromFTP(String request);	
	
}
