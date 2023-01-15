package com.dhl.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public String GetIntFromStr(String str){
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
