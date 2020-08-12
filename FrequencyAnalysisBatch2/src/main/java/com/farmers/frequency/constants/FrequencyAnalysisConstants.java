package com.farmers.frequency.constants;

import java.util.HashMap;
import java.util.Map;

public interface FrequencyAnalysisConstants {

    String BLANK_STRING="";
    String SPACE=" ";
    String UNDERSCORE="_";
    String ZIPEXT=".zip";


    Map<String,String> suffixAdditions = new HashMap<String,String>()
    {{
    	put("ZL","A");
    	put("PZL","AZ");
    	put("EZL","R");
    	
    }};
}
