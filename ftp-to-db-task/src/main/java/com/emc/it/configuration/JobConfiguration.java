package com.emc.it.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.AbstractApplicationContext;

import com.emc.it.configuration.write2db.CleanCSVTasklet;
import com.emc.it.util.FileDownloadUtil;




@Configuration
@ImportResource("classpath:/config/application-config.xml")
public class JobConfiguration {

	private static final Log logger = LogFactory.getLog(JobConfiguration.class);
		
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	private FileDownloadUtil 	downloadutil;
	@Autowired
	AbstractApplicationContext context;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	
	@Autowired
	@Qualifier("cleanCSV")
	private CleanCSVTasklet cleanCSV;
	/*
	/*@Autowired
	@Qualifier("writerTasklet")
	private DBWriteTasklet writerTasklet;*/

	@Autowired
	@Qualifier("readAndWriteData")
	private Step readAndWriteData;
	
	
	@Bean
	public Job ftpDownloadJob() {
		
		return jobBuilderFactory.get("ftpDownloadJob"+"time"+System.currentTimeMillis()).start(stepBuilderFactory.get("ftpDownloadJobstep1").tasklet(new Tasklet() {
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				downloadutil.downloadFilesFromRemoteDirectory();
				logger.info("Job 1 step 1 was run!!!");
				logger.info("File is downloaded......");
				return RepeatStatus.FINISHED;
			}
		}).build()).build();
	}

	
	
	@Bean
	public Job readAndWrite2DB() {
		//Job creation
		return jobBuilderFactory.get("readAndWrite2DB"+"time"+System.currentTimeMillis()).start(readAndWriteData).next(cleanCSVFile())
			.build();
	}
	
	@Bean
	public Step cleanCSVFile(){
		//Step
		return stepBuilderFactory.get("cleanCSVFile").
		//Tasklet
		tasklet(cleanCSV)
		.build() ;
	}
	
}