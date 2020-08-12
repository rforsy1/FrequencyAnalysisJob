package com.farmers.frequency.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farmers.frequency.constants.FrequencyAnalysisConstants;

@Component
public class ArchiveFileUtil {

	@Autowired
	private FrequencyAnalysisUtil frequencyUtil;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveFileUtil.class);

	
	public int archiveFile() {

		String fileLoc = frequencyUtil.getArchiveDirPath();
	      
	     LocalDateTime dt = LocalDateTime.of(LocalDate.now(), LocalTime.now());
	     String datetime = dt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		
		StringBuilder sb= new StringBuilder(); 
		sb.append(fileLoc); 
		sb.append("/");
		sb.append("FrequencyAnalysisReport");
		sb.append(FrequencyAnalysisConstants.UNDERSCORE);
		sb.append(datetime);
		sb.append(FrequencyAnalysisConstants.ZIPEXT);
		String zipAbsoluteFilePath=sb.toString();
		
		try { 
			compressZipfile(zipAbsoluteFilePath,frequencyUtil.getOutputFile());
			LOGGER.info("output file archived from {} to {} .",frequencyUtil.getOutputFile(),zipAbsoluteFilePath);
		} catch (IOException e) {
			LOGGER.error("Error while archive files from {} to {} .",frequencyUtil.getOutputFile(),zipAbsoluteFilePath, e);
		}
	return 0;

	}
	
	 
	private void compressZipfile(String rootDir, String sourceDir) throws IOException{
	    ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(frequencyUtil.getOutputFile()));
	    
	    ZipEntry entry = new ZipEntry(rootDir);
	    zipFile.putNextEntry(entry);

        FileInputStream in = new FileInputStream(frequencyUtil.getOutputFile());
        IOUtils.copy(in, zipFile);
        IOUtils.closeQuietly(in);

	    IOUtils.closeQuietly(zipFile);
	}
}
