package com.emc.it.configuration.write2db;



import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class CleanCSVTasklet implements Tasklet, StepExecutionListener{

	
	private Logger logger = Logger.getLogger(CleanCSVTasklet.class);
	private StepExecution stepExecution;
	@SuppressWarnings("unused")
	private ExecutionContext stepContext;

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		if (stepExecution.getExitStatus().getExitCode().equals("FAILED")) {
			logger.info("Exiting the Job. ");
		}
		return this.stepExecution.getExitStatus();
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		this.stepContext = stepExecution.getExecutionContext();
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)  {
		try {
			logger.info("CLEAN THE CSV FILE....");
			File file = new File("/tmp/user.csv");

    		if(file.delete()){
    			logger.info(file.getName() + " is deleted!");
    		}else{
    			logger.info("Delete operation is failed.");
    		}

		} catch (Exception e) {
			logger.info("Error while executing job process: ",e);
			stepExecution.setExitStatus(new ExitStatus("FAILED"));
		}
		return RepeatStatus.FINISHED;
	}

	/**
	 * @param stepExecution
	 *            the stepExecution to set
	 */
	public void setStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	/**
	 * @param stepContext
	 *            the stepContext to set
	 */
	public void setStepContext(ExecutionContext stepContext) {
		this.stepContext = stepContext;
	}

	

}
