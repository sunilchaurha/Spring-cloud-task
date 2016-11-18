/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emc.it.util;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFileEntryParser;
import org.apache.commons.net.ftp.parser.DefaultFTPFileEntryParserFactory;
import org.apache.commons.net.ftp.parser.ParserInitializationException;
import org.apache.commons.net.ftp.parser.UnixFTPEntryParser;


/**
 * @author Gary Russell
 *
 */
public class FixUnixFTPFileEntryParserFactory extends DefaultFTPFileEntryParserFactory {

	@Override
	public FTPFileEntryParser createFileEntryParser(String key) {
		FTPFileEntryParser fileEntryParser = super.createFileEntryParser(key);
		return fixedIfUnixParser(fileEntryParser);
	}

	@Override
	public FTPFileEntryParser createFileEntryParser(FTPClientConfig config) throws ParserInitializationException {
		FTPFileEntryParser fileEntryParser = super.createFileEntryParser(config);
		return fixedIfUnixParser(fileEntryParser);
	}

	protected FTPFileEntryParser fixedIfUnixParser(FTPFileEntryParser fileEntryParser) {
		if (fileEntryParser instanceof UnixFTPEntryParser) {
			return new FixUnixFTPEntryParser();
		}
		else {
			return fileEntryParser;
		}
	}

}
