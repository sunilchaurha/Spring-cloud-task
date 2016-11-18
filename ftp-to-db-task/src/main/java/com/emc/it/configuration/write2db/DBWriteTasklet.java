package com.emc.it.configuration.write2db;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;




public class DBWriteTasklet implements ItemWriter<FtpDTO>{

	
	private Logger logger = Logger.getLogger(DBWriteTasklet.class);

	private List<FtpDTO> pasProjectLookupDTOList;
	private StepExecution stepExecution;
	@SuppressWarnings("unused")
	private ExecutionContext stepContext;
	private static int numberOfRecordsRead = 0;
	
	@Autowired
	@Qualifier("aicDataSource")
	public DataSource aicDataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	 @Bean
	 public JdbcTemplate jdbcTemplate() {
		 return new JdbcTemplate(aicDataSource);
	 }
	
	public ExitStatus afterStep(StepExecution stepExecution) {
		numberOfRecordsRead += stepExecution.getReadCount();
		logger.info("Number of records processed." + numberOfRecordsRead + ": "
				+ stepExecution.getExitStatus().getExitCode());
		ExecutionContext jobContext = stepExecution.getJobExecution()
				.getExecutionContext();
		if (stepExecution.getExitStatus().getExitCode().equals("FAILED")) {
			logger.info("File Read step FAILED.. setting the jobStatus to FAILED");
		}
		jobContext.put("pasProjectLookupDTOList", pasProjectLookupDTOList);
		return stepExecution.getExitStatus();
	}


	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		this.stepContext = stepExecution.getExecutionContext();
		logger.info("Number of records processing @beforeStep :"+ stepExecution.getReadCount());
		ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
	}

	@Override
	public void write(List<? extends FtpDTO> dataList) {
		
		try {
			if(dataList != null && dataList.size() > 0){
				logger.info("The Current Chunk Record Size: "+ dataList.size());
				logger.info("Writing to the EAS DB table.");
				for (FtpDTO dto : dataList) {
					
					jdbcTemplate.update("insert into ftp_users (name, email, mobile) VALUES(?,?,?)", new String[]{dto.getName(), dto.getEmail(), dto.getMobile()});
				}
			}else{
				logger.info("There is no file data to process so no databse insert is performed.");
			}

		} catch (Exception e) {
			logger.info("Error while executing insert operation: ", e);
			stepExecution.setExitStatus(new ExitStatus("FAILED"));
		}

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
