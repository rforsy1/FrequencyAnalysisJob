package com.farmers.frequency.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FrequencyAnalysisUtil {

	@Value("${input.dir.path:C:/appl/FrequencyAnalysisBatch/input}")
	private String inputDirPath;
	@Value("${input.file.name:exercisedocument.txt}")
	private String inputFileName;
	@Value("${stop.word.input.file.name:stopwords.txt}")
	private String stopWordInputFileName;
	@Value("${output.dir.path:C:/appl/FrequencyAnalysisBatch/output}")
	private String outputDirPath;
	@Value("${output.file.name:FrequencyAnalysisReport.txt}")
	private String outputFileName;
	@Value("${move.file.pattern:FrequencyAnalysisBatchOutput.txt}")
	private String moveFileName;
	@Value("${archive.dir.path:C:/appl/FrequencyAnalysisBatch/archive}")
	private String archiveDirPath;
	@Value("${archive.file.name:FrequencyAnalysisBatchOutput.txt}")
	private String archiveFileName;
	
	@Value("${exclude.stop.words:true}")
	private Boolean excludeStopWords;
	@Value("${suffix.strings:L,LZ,EVM,ZQ}")
	private String suffixStrings;
	
	public String getInputdirPath() {
		return inputDirPath;
	}
	public String getInputFileName() {
		return inputFileName;
	}
	public String getStopWordInputFileName() {
		return stopWordInputFileName;
	}
	public String getOutputDirPath() {
		return outputDirPath;
	}
	public String getOutputFileName() {
		return outputFileName;
	}
	public String getOutputFile() {
	    return outputDirPath+"/"+outputFileName;
	}
	public String getMoveFileName() {
		return moveFileName;
	}
	public String getArchiveDirPath() {
		return archiveDirPath;
	}
	public Boolean excludeStopWords() {
		return excludeStopWords;
	}
	public List<String> getSuffixStrings() {
		return Arrays.asList(suffixStrings.split("[,]"));
	}
	public String getInputFile() {
	    return inputDirPath+"/"+inputFileName;
	}
	
	public String getStopWordInputFile() {
	    return inputDirPath+"/"+stopWordInputFileName;
	}
	public String getArchiveFile() {
	    return archiveDirPath+"/"+archiveFileName;
	}
	
}
