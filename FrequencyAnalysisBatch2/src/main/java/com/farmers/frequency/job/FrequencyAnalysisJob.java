package com.farmers.frequency.job;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farmers.frequency.bo.FrequencyAnalysisJobBo;
import com.farmers.frequency.util.ArchiveFileUtil;
import com.farmers.frequency.util.FrequencyAnalysisUtil;



@Component
public class FrequencyAnalysisJob implements Job {

	@Autowired
	private FrequencyAnalysisJobBo frequencyAnalysisJobBo;

	@Autowired
	private FrequencyAnalysisUtil frequencyUtil;

	@Autowired
	private ArchiveFileUtil archiveFile;

	private static Logger LOGGER = LoggerFactory.getLogger(FrequencyAnalysisJob.class);



	public int start(String[] args) throws Exception {

		LOGGER.info("Starting to process frequency analysis...");

		Map<String,Integer> sortedWords = frequencyAnalysisJobBo.getTopWords(frequencyUtil.excludeStopWords());

		int writeSuccess = frequencyAnalysisJobBo.writeToOutput(sortedWords);

		archiveFile.archiveFile();
		return writeSuccess;

	}
}

