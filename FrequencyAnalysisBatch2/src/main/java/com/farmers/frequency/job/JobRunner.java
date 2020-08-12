package com.farmers.frequency.job;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JobRunner {

	private static Logger LOGGER = LoggerFactory.getLogger(JobRunner.class);

	private static final String RUN_ID_KEY = "runId";
	private static final String JOB_NAME_KEY = "jobName";
	
	public static void main(String[] args) {
		MDC.put(RUN_ID_KEY, System.currentTimeMillis());
		int rc = -1;
		StopWatch watch = new StopWatch(); 
		watch.start();
		ApplicationContext context = null;
		
		if(ArrayUtils.isEmpty(args)){
			LOGGER.error("Job >>> Missing argument - 'Job Name'");
			System.exit(rc);
		}
		
		String jobName = args[0];
		MDC.put(JOB_NAME_KEY, jobName);
		LOGGER.debug("Job >>> {} has been requested to be run", jobName);

		try {
			context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");

			Job job = (Job) context.getBean(jobName);
			LOGGER.debug("Job >>> {} has started", jobName);
			rc = job.start(args);

		} catch (Exception e) {
			LOGGER.error("Job >>> {} has failed with error >>> {}", jobName,
					e.getMessage(), e);
		} finally {
			watch.stop();
			String time = watch.toString();
			LOGGER.debug(
					"Job >>> {} has completed with a return code >>> {} and time >>> {}...",
					jobName, rc, time);
			((ConfigurableApplicationContext) context).close();
		}

		System.exit(rc);
	}
}
