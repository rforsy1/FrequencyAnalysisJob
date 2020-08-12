# FrequencyAnalysis

# Solution Overview
This batch job will take an input file with the name "exercisedocument.txt" and find the top 25 most used root words. The file is first cleared of all non-alphabetic characters, extra spacing, and stop words taken from input file "stopwords.txt". This initial cleansed input is written to the output file "ReplaceNonAlphaChar.txt" to make sure no non-alphabetic, extra spaces, or stop words remain. Then, every word is screened to make sure it does not end with a suffix or that the suffix is replaced to form a root word where applicable. The result from this step is written to an output file called "GrammarOutput.txt" to ensure that grammar rules are being followed. Finally, words are tallied up (tallies of all words are written to output file "UnsortedMap.txt" before they are sorted, and "SortedMap.txt" after they are sorted) and the top 25 most used words are printed to the output file "FrequencyAnalysisReport.txt".

# Libraries/Frameworks Used
This batch job utilizes the following libraries:

org.springframework

org.springframework.batch

org.aspectj

org.apache.cxf

org.slf4j

log4j

junit

# Setup Instructions
As this was tested in my local directory, the input files "exercisedocument.txt" and "stopwords.txt" were located in my home directory "C:/appl/FrequencyAnalysisBatch/input". When deployed to a server, an input directory can be set up to house these files. 

I also set up an archive ("C:/appl/FrequencyAnalysisBatch/archive") and output directory ("C:/appl/FrequencyAnalysisBatch/output").

# Execution Instructions 
For local testing purposes, I used junit testing local to Eclipse. I created the test class FrequencyAnalysisJobTest to kick off the FrequencyAnalysisJob. When the job is deployed to a server via a jar file, .ksh files can be used to kick off the job remotely. 
