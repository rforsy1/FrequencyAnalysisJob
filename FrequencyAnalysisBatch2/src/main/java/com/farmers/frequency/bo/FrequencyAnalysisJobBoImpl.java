package com.farmers.frequency.bo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farmers.frequency.constants.FrequencyAnalysisConstants;
import com.farmers.frequency.util.FrequencyAnalysisUtil;

@Component("FrequencyAnalysisJobBo")
public class FrequencyAnalysisJobBoImpl implements FrequencyAnalysisJobBo{

	private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyAnalysisJobBoImpl.class);
	
	@Autowired
	private FrequencyAnalysisUtil frequencyUtil;

	public Map<String, Integer> getTopWords(Boolean excludeStopWords) {
		String inputFile;
		Map<String,Integer> topWordMap = new HashMap<String,Integer>();
		Map<String,Integer> sortedMap = new HashMap<String,Integer>();


		try {
			LOGGER.info("Reading input file: {}...",frequencyUtil.getInputFile());
			inputFile = new String(Files.readAllBytes(Paths.get(frequencyUtil.getInputFile())));


			if(excludeStopWords) {

				List<String> stopWordList = new ArrayList<String>();
				try {
					LOGGER.info("Getting stop words file");
					stopWordList = Files.readAllLines(Paths.get(frequencyUtil.getStopWordInputFile()), StandardCharsets.UTF_8);
 
					for(String stopWord : stopWordList) {
						LOGGER.info("Replacing stop word "+stopWord+ " with blank string");
						String regex = "\\b"+stopWord+"\\b"; 
						inputFile = inputFile.replaceAll(regex, FrequencyAnalysisConstants.BLANK_STRING);
					} 

				}catch(Exception e) {
					LOGGER.error("Could not get stop word input file. Error is: ",e.getMessage());
				}
			}

			LOGGER.info("Replacing non-alphabetic characters with blank string");
			inputFile = inputFile.replaceAll("[^a-zA-Z]", FrequencyAnalysisConstants.SPACE);
			inputFile = inputFile.replaceAll("\n", FrequencyAnalysisConstants.BLANK_STRING).replaceAll("\r", FrequencyAnalysisConstants.BLANK_STRING);
			
			
			LOGGER.info("Writing test file to output to see that non-alpha characters are replaces with blanks");
			BufferedWriter writer = new BufferedWriter(new FileWriter(frequencyUtil.getOutputDirPath()+"/"+"ReplaceNonAlphaChar.txt"));
			StringBuilder writeToFile = new StringBuilder();			
			writeToFile.append(inputFile);			 
			writer.write(writeToFile.toString());			
			writer.close();
			
			  
			String[] wordList = inputFile.split(FrequencyAnalysisConstants.SPACE);

			String[] editedWordList = editForGrammar(wordList);
			
			LOGGER.info("Writing test file to output to see that grammar is taken care of ");
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(frequencyUtil.getOutputDirPath()+"/"+"GrammarOutput.txt"));
			
			StringBuilder writeToFile2 = new StringBuilder(); 

			for(String st : editedWordList) {
				writeToFile2.append(st).append(System.lineSeparator());
			}
			writer2.write(writeToFile2.toString());
			
			writer2.close();
			
			
			for(String st : editedWordList) {
				if(st.trim().isEmpty()) {
					continue;
				} 
				if(topWordMap.containsKey(st)) {
					int count = topWordMap.get(st);
					topWordMap.put(st, count+1);
				}else {
					topWordMap.put(st, 1);
				}	
			}

			sortedMap = getSortedMap(topWordMap);
			
			

		} catch (IOException e1) {
			LOGGER.error("Could not get input file. Error is: ",e1.getMessage());
		}
		
		return sortedMap;
	}
	
	private String[] editForGrammar(String[] wordList) {
		
		List<String> suffixStrings = frequencyUtil.getSuffixStrings();
		LOGGER.info("Editing file for grammar..");
		
		for(String str : suffixStrings) {
			for(int i = 0; i< wordList.length; i++) {
				if(wordList[i].endsWith(str)) {
					LOGGER.info("Removing suffix "+str);
					String s = wordList[i].substring(0, wordList[i].length() - str.length());
					wordList[i] = s;
 				}
			}
		}
		
		Map<String,String> suffixReplacements = FrequencyAnalysisConstants.suffixAdditions;
		
		for(Map.Entry<String, String> entry : suffixReplacements.entrySet()) {
			String suffix = entry.getKey();
			for(int i = 0; i< wordList.length; i++) {
				if(wordList[i].endsWith(suffix)) {
					LOGGER.info("Replacing suffix '"+suffix+"' with "+entry.getValue());
					String s = wordList[i].replace(suffix, entry.getValue());
					wordList[i] = s;
 				}
			}
		}

		return wordList;
	}

	private Map<String,Integer>  getSortedMap(final Map<String,Integer> sortMap){
		LOGGER.info("Writing unsorted map to output file for reference");
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(frequencyUtil.getOutputDirPath()+"/"+"UnsortedMap.txt"));			
			sortMap.forEach((key,value) -> {
				try { writer.write(key + ":" + value + System.lineSeparator()); }
		        catch (IOException ex) { throw new UncheckedIOException(ex); }
		    });
			writer.close();
		} catch (IOException e) {
			LOGGER.error("Error when trying to write to output file {}", e.getMessage());
		}
		
		
		
		Comparator<String> valComparator = new Comparator<String>() {
			public int compare(String s1, String s2) {
				int i = sortMap.get(s1).compareTo(sortMap.get(s2));
				if(i == 0) {
					return 1;  
				}else {
					return i;
				}
			}

		};
		
		Map<String,Integer> sortedMap = new TreeMap<String,Integer>(valComparator);
		sortedMap.putAll(sortMap);
		
		
		LOGGER.info("Writing sorted map to output file for reference");
		BufferedWriter writer2;
		try {
			writer2 = new BufferedWriter(new FileWriter(frequencyUtil.getOutputDirPath()+"/"+"SortedMap.txt"));			
			sortedMap.forEach((key,value) -> {
				try { writer2.write(key + ":" + value + System.lineSeparator()); }
		        catch (IOException ex) { throw new UncheckedIOException(ex); }
		    });
			writer2.close();
		} catch (IOException e) {
			LOGGER.error("Error when trying to write to output file {}", e.getMessage());
		}
		
		
		return sortedMap;
	}

	public int writeToOutput(Map<String,Integer> sortedMap) throws IOException {
		
		Set<String> keys = sortedMap.keySet();
		String[] keyArr = keys.toArray(new String[keys.size()]);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(frequencyUtil.getOutputFile()));
		
		StringBuilder writeToFile = new StringBuilder();
		
		for(int i=keyArr.length-1; i>0 && i > keyArr.length-26; i--) {
			writeToFile.append(keyArr[i]);
			writeToFile.append(System.getProperty("line.separator"));
		}
		
		writer.write(writeToFile.toString());
		
		writer.close();
		
		return 0;
	}
	
}
