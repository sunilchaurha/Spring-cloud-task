package com.emc.it.configuration.write2db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * 
 * This class reads file from NAS location.
 * 
 * @author chaurs
 * 
 * 
 */
public class FtpMultiSourceItemReader<T> extends MultiResourceItemReader<T>
		implements StepExecutionListener {

	/**
	 * logger.
	 */
	private static Logger logger = Logger
			.getLogger(FtpMultiSourceItemReader.class);
	private StepExecution stepExecution;

	private List<String> currentReadFile = new ArrayList<String>();

	private ExecutionContext stepContext;

	private DirectoryScanner scanner;

	private String filePattern;
	
	private ResourceAwareItemReaderItemStream<? extends T> delegate;

	/**
	 * @param currentReadFile
	 *            the currentReadFile to set
	 */
	public void setCurrentReadFile(List<String> currentReadFile) {
		this.currentReadFile = currentReadFile;
	}

	/**
	 * @return the scanner
	 */
	public DirectoryScanner getScanner() {
		return scanner;
	}

	/**
	 * @param scanner
	 *            the scanner to set
	 */
	public void setScanner(DirectoryScanner scanner) {
		this.scanner = scanner;
	}

	/**
	 * @return the stepContext
	 */
	public ExecutionContext getStepContext() {
		return stepContext;
	}

	/**
	 * @param stepContext
	 *            the stepContext to set
	 */
	public void setStepContext(ExecutionContext stepContext) {
		this.stepContext = stepContext;
	}

	/**
	 * @return the stepExecution
	 */
	public StepExecution getStepExecution() {
		return stepExecution;
	}

	/**
	 * @param stepExecution
	 *            the stepExecution to set
	 */
	public void setStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	/**
	 * @return the delegate
	 */
	public ResourceAwareItemReaderItemStream<? extends T> getDelegate() {
		return delegate;
	}

	/**
	 * @param delegate
	 *            the delegate to set
	 */
	public void setDelegate(
			ResourceAwareItemReaderItemStream<? extends T> delegate) {
		this.delegate = delegate;
	}

	/**
	 * @param filePattern the filePattern to set
	 */
	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		stepContext = stepExecution.getExecutionContext();
		logger.info("The Current Read File is @beforeStep : " + currentReadFile);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (currentReadFile != null
				&& stepExecution.getExitStatus().getExitCode().equals("FAILED")) {
			logger.info("Error Reading File. @afterStep :" + currentReadFile);
		}
		ExecutionContext jobContext = stepExecution.getJobExecution()
				.getExecutionContext();
		jobContext.put("currentReadFile", this.currentReadFile);
		logger.info("Current read file : " + currentReadFile);
		return stepExecution.getExitStatus();
	}

	@Override
	public void open(ExecutionContext stepContext) {
		super.setResources(getScannedResource(filePattern));
		super.setDelegate(delegate);

		try {
			super.open(stepContext);
		} catch (Exception e) {
			logger.error("Exception occured while opening the resource, Cause : "
					+ e.getMessage() + e.getCause());
			
		}
	}

	/**
	 * This method scannes given directory
	 * 
	 * @return Resource array
	 */
	public Resource[] getScannedResource(final String filePattern) {
		String includes = scanner.getBasedir().getName();
		//String absolute = scanner.getBasedir().getAbsolutePath();
		scanner.setIncludes(new String[] { includes , filePattern});
		//scanner.setBasedir(absolute.substring(0, absolute.lastIndexOf(includes)));
		scanner.scan();
		String[] files = scanner.getIncludedFiles();
		FileSystemResource[] fileArr = new FileSystemResource[files.length];
		for (int i = 0; i < files.length; i++) {
			fileArr[i] = new FileSystemResource(scanner.getBasedir()
					+ File.separator + files[i]);
			this.currentReadFile.add(scanner.getBasedir() + File.separator
					+ files[i]);
		}
		return fileArr;
	}
}
