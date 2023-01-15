package com.dhl.Util;

import com.dhl.Data.InventoryData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ReadTXTFile {

    public Map<String, Integer> GetFlightData(List<String> fileList) {
        Map<String, Integer> FlightDataMap = new HashMap<String, Integer>();

        try {
            for (String fileName : fileList) {
//                EmlUtil emlUtil = new EmlUtil();
//                Map<Object, Object> map = emlUtil.parserFile(filePath);
                String hawb = new StringUtil().GetIntFromStr(fileName);
                if (!FlightDataMap.containsKey(hawb)){
                    FlightDataMap.put(hawb, 1);
                } else {
                    FlightDataMap.replace(hawb, FlightDataMap.get(hawb) + 1);
                }
//                //构造一个BufferedReader类来读取文件
//                BufferedReader br = new BufferedReader(new FileReader(file));
//
//                String s = null;
//
//                //使用readLine方法，一次读一行
//                while ((s = br.readLine()) != null) {
//                    if (s.length() > 30) {
//                        String hawb = s.substring(0, 11).trim();
//                        if (ISHawb(hawb)) {
//                            Integer Pieces = Integer.parseInt(s.substring(14, 17).trim());
//                            if (!FlightDataMap.containsKey(hawb)) {
//                                FlightDataMap.put(hawb, Pieces);
//                            } else {
//                                FlightDataMap.replace(hawb, FlightDataMap.get(hawb) + Pieces);
//                            }
//                        }
//                    }
//                }
//
//                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FlightDataMap;
    }

    public Map<String, InventoryData> GetInventoryData(List<String> fileList) {

        Map<String, InventoryData> FlightDataMap = new HashMap<String, InventoryData>();

        try {
            for (String filePath : fileList) {
                File file = new File(filePath);

                //构造一个BufferedReader类来读取文件
                BufferedReader br = new BufferedReader(new FileReader(file));

                String s = null;

                //使用readLine方法，一次读一行
                while ((s = br.readLine()) != null) {
                    if (s.length() > 30) {
                        String hawb = s.substring(0, 11).trim();
                        if (ISHawb(hawb)) {
                            if (!FlightDataMap.containsKey(hawb)) {
                                InventoryData inventoryData = new InventoryData();
                                inventoryData.setPieces(Integer.parseInt(s.substring(98, 101).trim()));
                                inventoryData.setLocation(s.substring(11, 19).trim());
                                inventoryData.setWeight(s.substring(80, 87).trim());
                                FlightDataMap.put(hawb, inventoryData);
                            } else {
                                InventoryData inventoryData = FlightDataMap.get(hawb);
                                inventoryData.addPieces(Integer.parseInt(s.substring(98, 101).trim()));
                                inventoryData.addLocation(s.substring(11, 19).trim());
                                inventoryData.setWeight(s.substring(80, 87).trim());
                                FlightDataMap.replace(hawb, inventoryData);
                            }
                        }
                    }
//
//                    if (s.contains("H/")) {
//                        String hawb = s.substring(0, 10).trim();
//                        if (ISHawb(hawb)) {
//                            if (!FlightDataMap.containsKey(hawb)) {
//                                InventoryData inventoryData = new InventoryData();
//                                inventoryData.setPieces(Integer.parseInt(s.substring(98, 101).trim()));
//                                inventoryData.setLocation(s.substring(11, 19).trim());
//                                FlightDataMap.put(hawb, inventoryData);
//                            } else {
//                                InventoryData inventoryData = FlightDataMap.get(hawb);
//                                inventoryData.addPieces(Integer.parseInt(s.substring(98, 101).trim()));
//                                inventoryData.addLocation(s.substring(11, 19).trim());
//                                FlightDataMap.replace(hawb, inventoryData);
//                            }
//                        }
//                    }
                }

                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FlightDataMap;
    }

    public static boolean isNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if (str == null || str.equals("")) {
            return false;
        }
        return pattern.matcher(str).matches();
    }

    public static boolean ISHawb(String hawb) {
        return isNum(hawb) && hawb.length() == 10;
    }
}
