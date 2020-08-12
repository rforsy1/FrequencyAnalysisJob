package com.farmers.frequency.job.test;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.farmers.frequency.job.Job;



	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
	public class FrequencyAnalysisJobTest {
		
		private static Logger LOGGER = LoggerFactory.getLogger(FrequencyAnalysisJobTest.class);
		

		@BeforeClass
		public static void setUp() throws Exception {
			System.setProperty("JOB_DEFAULT_PROP", "classpath:batch-job-default.properties");
			System.setProperty("JOB_OVERRIDE_PROP", "classpath:frequencyAnalysis.properties");
		}
		
		@Test
		public void getFrequencyAnalysis() {
			try {
				String jobName = "frequencyAnalysisJob";

				AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.farmers.frequency");
				Job o = (Job)ctx.getBean(jobName);
				o.start(new String[] {});
				LOGGER.debug("{} job has completed successfully...");
			} catch (Exception e) {
				LOGGER.error("Test failed...", e);
				fail(e.getMessage());
			}
		}
}
