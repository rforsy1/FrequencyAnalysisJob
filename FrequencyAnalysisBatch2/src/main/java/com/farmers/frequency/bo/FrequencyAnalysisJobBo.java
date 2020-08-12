package com.farmers.frequency.bo;

import java.io.IOException;
import java.util.Map;

public interface FrequencyAnalysisJobBo {

	Map<String,Integer> getTopWords(Boolean excludeStopWords);
	int writeToOutput(Map<String,Integer> sortedMap) throws IOException;
}
