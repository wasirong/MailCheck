package com.dhl.Util;

import com.dhl.Data.InventoryData;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CreateWorkbook {

    private Map<String, Integer> flightData;
    private Map<String, InventoryData> inventoryData;
    private String sheetName;

    public CreateWorkbook(Map<String, Integer> flightData, Map<String, InventoryData> inventoryData, String sheetName) {
        this.flightData = flightData;
        this.inventoryData = inventoryData;
        this.sheetName = sheetName;
    }

    public XSSFWorkbook generateExcel() throws IOException {
        //创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();

        //创建表单
        XSSFSheet sheet = genSheet(workbook, sheetName);

        //创建表单样式
        XSSFCellStyle titleStyle = genTitleStyle(workbook);//创建标题样式

        XSSFCellStyle contextStyle = genContextStyle(workbook);//创建文本样式

        //创建Excel
        genExcel_ExportData_412(sheet, titleStyle, contextStyle, sheetName);

        return workbook;
    }

//    public XSSFWorkbook generateExcel_Import412(String sheetName, List<String> objectList) throws IOException {
//
//        //创建工作薄
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        //创建表单
//        XSSFSheet sheet = genSheet(workbook, sheetName);
//
//        //创建表单样式
//        XSSFCellStyle titleStyle = genTitleStyle(workbook);//创建标题样式
//
//        XSSFCellStyle contextStyle = genContextStyle(workbook);//创建文本样式
//
//        genExcel_ImportData_412(sheet, titleStyle, contextStyle, objectList, sheetName);
//
//        return workbook;
//    }
//
//    public XSSFWorkbook generateExcel_ExportReport_5i(String sheetName, List<String> objectList) throws IOException {
//
//        //创建工作薄
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        //创建表单
//        XSSFSheet sheet = genSheet(workbook, sheetName);
//
//        //创建表单样式
//        XSSFCellStyle titleStyle = genTitleStyle(workbook);//创建标题样式
//
//        XSSFCellStyle contextStyle = genContextStyle(workbook);//创建文本样式
//
//        genExcel_ExportReport_5i(sheet, titleStyle, contextStyle, objectList, sheetName);
//
//        return workbook;
//    }
//
//    public XSSFWorkbook generateExcel_ReplacementRelease(String sheetName, List<String> objectList) throws IOException {
//
//        //创建工作薄
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        //创建表单
//        XSSFSheet sheet = genSheet(workbook, sheetName);
//
//        //创建表单样式
//        XSSFCellStyle titleStyle = genTitleStyle(workbook);//创建标题样式
//
//        XSSFCellStyle contextStyle = genContextStyle(workbook);//创建文本样式
//
//        genExcel_ReplacementReleaseData(sheet, titleStyle, contextStyle, objectList, sheetName);
//
//        return workbook;
//    }
//
//    public XSSFWorkbook generateExcel_DaysOFGoodsInWarehouse(String sheetName, List<String> objectList) throws IOException {
//
//        //创建工作薄
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        //创建表单
//        XSSFSheet sheet = genSheet(workbook, sheetName);
//
//        //创建表单样式
//        XSSFCellStyle titleStyle = genTitleStyle(workbook);//创建标题样式
//
//        XSSFCellStyle contextStyle = genContextStyle(workbook);//创建文本样式
//
//        genExcel_DaysOFGoodsInWarehouse(sheet, titleStyle, contextStyle, objectList, sheetName);
//
//        return workbook;
//    }
//
//    public XSSFWorkbook generateExcel_Order(String sheetName, Map<String, Integer> myMapReceivingOrder, Map<String, Integer> myMapTransFerOrder,
//                                            List<String> receivingOrderDataList, List<String> transFerOrderDataList) {
//
//        //创建工作薄
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        //创建表单
//        XSSFSheet sheet_receiving = genSheet(workbook, "接单");
//
//        //创建表单样式
//        XSSFCellStyle titleStyle_receiving = genTitleStyle(workbook);//创建标题样式
//
//        XSSFCellStyle contextStyle_receiving = genContextStyle(workbook);//创建文本样式
//
//        genExcel_ReceivingOrder(sheet_receiving, titleStyle_receiving, contextStyle_receiving, myMapReceivingOrder, "接单");
//
//        //创建表单
//        XSSFSheet sheet_transFer = genSheet(workbook, "转单");
//
//        //创建表单样式
//        XSSFCellStyle titleStyle_transFer = genTitleStyle(workbook);//创建标题样式
//
//        XSSFCellStyle contextStyle_transFer = genContextStyle(workbook);//创建文本样式
//
//        genExcel_TransFerOrder(sheet_transFer, titleStyle_transFer, contextStyle_transFer, myMapTransFerOrder, "转单");
//
//        //创建表单
//        XSSFSheet sheet_receiveBase = genSheet(workbook, "接单底数据");
//
//        //创建表单样式
//        XSSFCellStyle titleStyle_receiveBase = genTitleStyle(workbook);//创建标题样式
//
//        XSSFCellStyle contextStyle_receiveBase = genContextStyle(workbook);//创建文本样式
//
//        genExcel_receivingOrderBaseData(sheet_receiveBase, titleStyle_receiveBase, contextStyle_receiveBase, receivingOrderDataList, "接单底数据");
//
//        //创建表单
//        XSSFSheet sheet_transFerBase = genSheet(workbook, "转单底数据");
//
//        //创建表单样式
//        XSSFCellStyle titleStyle_transFerBase = genTitleStyle(workbook);//创建标题样式
//
//        XSSFCellStyle contextStyle_transFerBase = genContextStyle(workbook);//创建文本样式
//
//        genExcel_TransFerOrderBaseData(sheet_transFerBase, titleStyle_transFerBase, contextStyle_transFerBase, transFerOrderDataList, "转单底数据");
//
//        return workbook;
//    }

    //设置表单，并生成表单
    public XSSFSheet genSheet(XSSFWorkbook workbook, String sheetName) {
        //生成表单
        XSSFSheet sheet = workbook.createSheet(sheetName);

        //设置表单文本居中
        sheet.setHorizontallyCenter(true);
        sheet.setFitToPage(false);

        //打印时在底部右边显示文本页信息
        Footer footer = sheet.getFooter();
        footer.setRight("Page " + HeaderFooter.numPages() + " Of " + HeaderFooter.page());

        //打印时在头部右边显示Excel创建日期信息
        Header header = sheet.getHeader();
        header.setRight("Create Date " + HeaderFooter.date() + " " + HeaderFooter.time());

        //设置打印方式
        XSSFPrintSetup ps = sheet.getPrintSetup();
        ps.setLandscape(true); // true：横向打印，false：竖向打印 ，因为列数较多，推荐在打印时横向打印
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //打印尺寸大小设置为A4纸大小

        return sheet;
    }

    //生成标题样式
    public XSSFCellStyle genTitleStyle(XSSFWorkbook workbook) {

        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);

        //标题居中，没有边框，所以这里没有设置边框，设置标题文字样式
        XSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);//加粗
        titleFont.setFontHeight((short) 12);//文字尺寸
        titleFont.setFontHeightInPoints((short) 12);
        style.setFont(titleFont);

        style.setBorderBottom(BorderStyle.THIN);//设置文本边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        //set border color
        style.setTopBorderColor(new XSSFColor(java.awt.Color.BLACK));//设置文本边框颜色
        style.setBottomBorderColor(new XSSFColor(java.awt.Color.BLACK));
        style.setLeftBorderColor(new XSSFColor(java.awt.Color.BLACK));
        style.setRightBorderColor(new XSSFColor(java.awt.Color.BLACK));

        return style;
    }

    //创建文本样式
    public XSSFCellStyle genContextStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);//文本水平居中显示
        style.setVerticalAlignment(VerticalAlignment.CENTER);//文本竖直居中显示
        style.setWrapText(false);//文本自动换行

        //生成Excel表单，需要给文本添加边框样式和颜色
        /*
             CellStyle.BORDER_DOUBLE      双边线
             CellStyle.BORDER_THIN        细边线
             CellStyle.BORDER_MEDIUM      中等边线
             CellStyle.BORDER_DASHED      虚线边线
             CellStyle.BORDER_HAIR        小圆点虚线边线
             CellStyle.BORDER_THICK       粗边线
         */
        style.setBorderBottom(BorderStyle.THIN);//设置文本边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        //set border color
        style.setTopBorderColor(new XSSFColor(java.awt.Color.BLACK));//设置文本边框颜色
        style.setBottomBorderColor(new XSSFColor(java.awt.Color.BLACK));
        style.setLeftBorderColor(new XSSFColor(java.awt.Color.BLACK));
        style.setRightBorderColor(new XSSFColor(java.awt.Color.BLACK));

        return style;
    }

    public void genExcel_ExportData_412(XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, String titleContent) {

        //根据Excel列名长度，指定列名宽度  Excel总共4列

        for (int i = 0; i < 4; i++) {
            sheet.setColumnWidth(i, 4800);
        }

        //设置标题位置
        // 航班数据
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row
                0, //last row
                0, //first column
                3 //last column
        ));

        XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
        XSSFCell cell;
        cell = row.createCell(0);//创建一列
        cell.setCellValue("数据检查");//标题
        cell.setCellStyle(titleStyle);//设置标题样式

        cell = row.createCell(3);//创建一列
        cell.setCellStyle(titleStyle);//设置标题样式

        List<String> title = new ArrayList<String>();
        title.add("运单号");
        title.add("ACA件数");
        title.add("ACA重量");
        title.add("库位");

        row = sheet.createRow(1);//创建第二行
        for (int k = 0; k < title.size(); k++) {
            cell = row.createCell(k);//创建第二行第一列
            cell.setCellValue(title.get(k));
            cell.setCellStyle(contextStyle);
        }

        int i = 0;
        for (Map.Entry<String, Integer> entry : flightData.entrySet()) {
            String num = entry.getKey();
            if (inventoryData.containsKey(num)) {
                for (int j = 0; j < flightData.get(num); j++) {
                    row = sheet.createRow(i + 2);//创建第三行
                    cell = row.createCell(0);//创建第三行第一列
                    cell.setCellValue(entry.getKey());//第三行第一列的值
                    cell.setCellStyle(contextStyle);

                    cell = row.createCell(1);//创建第三行第二列
                    cell.setCellValue(inventoryData.get(entry.getKey()).getPieces());//第三行第二列的值
                    cell.setCellStyle(contextStyle);

                    cell = row.createCell(2);//创建第三行第三列
                    cell.setCellValue(inventoryData.get(entry.getKey()).getWeight());//第三行第三列的值
                    cell.setCellStyle(contextStyle);

                    cell = row.createCell(3);//创建第三行第四列
                    cell.setCellValue(inventoryData.get(entry.getKey()).getLocation());//第三行第四列的值
                    cell.setCellStyle(contextStyle);

                    i++;
                }

            }
        }

        for (Map.Entry<String, Integer> entry : flightData.entrySet()) {
            String num = entry.getKey();
            if (!inventoryData.containsKey(num)) {
                row = sheet.createRow(i + 2);//创建第三行
                cell = row.createCell(0);//创建第三行第一列
                cell.setCellValue(entry.getKey());//第三行第一列的值
                cell.setCellStyle(contextStyle);

                cell = row.createCell(1);//创建第三行第二列
                cell.setCellValue("None");//第三行第二列的值
                cell.setCellStyle(contextStyle);

                cell = row.createCell(2);//创建第三行第三列
                cell.setCellValue("None");//第三行第三列的值
                cell.setCellStyle(contextStyle);

                cell = row.createCell(3);//创建第三行第四列
                cell.setCellValue("None");//第三行第四列的值
                cell.setCellStyle(contextStyle);

                i++;
            }
        }

//        cell.setCellStyle(contextStyle);


//        row = sheet.createRow(1);//创建第二行
//        List<String> title = new ExportData_412().titleList();
//        for (int k = 0; k < title.size(); k++) {
//            cell = row.createCell(k);//创建第二行第一列
//            cell.setCellValue(title.get(k));
//            cell.setCellStyle(contextStyle);
//        }
//
//        for (int i = 0; i < objList.size(); i++) {
//            ExportData_412 exportData_412 = (ExportData_412) (objList.get(i));
//            row = sheet.createRow(i + 2);//创建第三行
//            for (int j = 0; j < exportData_412.dataList().size(); j++) {
//                cell = row.createCell(j);//创建第三行第一列
//                cell.setCellValue(exportData_412.dataList().get(j));//第三行第一列的值
//                cell.setCellStyle(contextStyle);
//            }
//        }
    }

    public int bigLength(List<Integer> list) {
        return (int) Collections.max(list);
    }
//    public void genExcel_ImportData_412(XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, List<ImportData_412> objList, String titleContent) {
//
//        //根据Excel列名长度，指定列名宽度  Excel总共10列
//        for (int i = 0; i < 14; i++) {
//            if (i == 0) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 1) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 2) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 3) {
//                sheet.setColumnWidth(i, 3200);
//            } else if (i == 4) {
//                sheet.setColumnWidth(i, 1600);
//            } else if (i == 5) {
//                sheet.setColumnWidth(i, 1600);
//            } else if (i == 6) {
//                sheet.setColumnWidth(i, 2800);
//            } else if (i == 7) {
//                sheet.setColumnWidth(i, 3200);
//            } else if (i == 8) {
//                sheet.setColumnWidth(i, 7200);
//            } else if (i == 9) {
//                sheet.setColumnWidth(i, 3200);
//            } else if (i == 10) {
//                sheet.setColumnWidth(i, 12800);
//            } else if (i == 11) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 12) {
//                sheet.setColumnWidth(i, 25600);
//            } else {
//                sheet.setColumnWidth(i, 6400);
//            }
//        }
//
//        //设置标题位置
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row
//                0, //last row
//                0, //first column
//                13 //last column
//        ));
//
//        XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
//        XSSFCell cell;
//        cell = row.createCell(0);//创建一列
//        cell.setCellValue(titleContent);//标题
//        cell.setCellStyle(titleStyle);//设置标题样式
//
//        row = sheet.createRow(1);//创建第二行
//        List<String> title = new ImportData_412().titleList();
//        for (int k = 0; k < title.size(); k++) {
//            cell = row.createCell(k);//创建第二行第一列
//            cell.setCellValue(title.get(k));
//            cell.setCellStyle(contextStyle);
//        }
//
//        for (int i = 0; i < objList.size(); i++) {
//            ImportData_412 importData_412 = (ImportData_412) (objList.get(i));
//            row = sheet.createRow(i + 2);//创建第三行
//            for (int j = 0; j < importData_412.dataList().size(); j++) {
//                cell = row.createCell(j);//创建第三行第一列
//                cell.setCellValue(importData_412.dataList().get(j));//第三行第一列的值
//                cell.setCellStyle(contextStyle);
//            }
//        }
//    }
//
//    public void genExcel_ExportReport_5i(XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, List<Export_Report_5i> objList, String titleContent) {
//
//        //根据Excel列名长度，指定列名宽度  Excel总共9列
//        for (int i = 0; i < 9; i++) {
//            if (i == 0) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 1) {
//                sheet.setColumnWidth(i, 12960);
//            } else if (i == 2) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 3) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 4) {
//                sheet.setColumnWidth(i, 9920);
//            } else if (i == 5) {
//                sheet.setColumnWidth(i, 9800);
//            } else if (i == 6) {
//                sheet.setColumnWidth(i, 34000);
//            } else if (i == 7) {
//                sheet.setColumnWidth(i, 31000);
//            } else {
//                sheet.setColumnWidth(i, 7200);
//            }
//        }
//
//        //设置标题位置
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row
//                0, //last row
//                0, //first column
//                9 //last column
//        ));
//
//        XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
//        XSSFCell cell;
//        cell = row.createCell(0);//创建一列
//        cell.setCellValue(titleContent);//标题
//        cell.setCellStyle(titleStyle);//设置标题样式
//
//        row = sheet.createRow(1);//创建第二行
//        List<String> title = new Export_Report_5i().titleList();
//        for (int k = 0; k < title.size(); k++) {
//            cell = row.createCell(k);//创建第二行第一列
//            cell.setCellValue(title.get(k));
//            cell.setCellStyle(contextStyle);
//        }
//
//        for (int i = 0; i < objList.size(); i++) {
//            Export_Report_5i export_report_5i = (Export_Report_5i) (objList.get(i));
//            row = sheet.createRow(i + 2);//创建第三行
//            for (int j = 0; j < export_report_5i.dataList().size(); j++) {
//                cell = row.createCell(j);//创建第三行第一列
//                cell.setCellValue(export_report_5i.dataList().get(j));//第三行第一列的值
//                cell.setCellStyle(contextStyle);
//            }
//        }
//    }
//
//    public void genExcel_ReplacementReleaseData(XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, List<ReplacementReleaseData> objList, String titleContent) {
//
//        //根据Excel列名长度，指定列名宽度  Excel总共9列
//        for (int i = 0; i < 5; i++) {
//            if (i == 0) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 1) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 2) {
//                sheet.setColumnWidth(i, 4800);
//            } else if (i == 3) {
//                sheet.setColumnWidth(i, 4800);
//            } else {
//                sheet.setColumnWidth(i, 11000);
//            }
//        }
//
//        //设置标题位置
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row
//                0, //last row
//                0, //first column
//                4 //last column
//        ));
//
//        XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
//        XSSFCell cell;
//        cell = row.createCell(0);//创建一列
//        cell.setCellValue(titleContent);//标题
//        cell.setCellStyle(titleStyle);//设置标题样式
//
//        row = sheet.createRow(1);//创建第二行
//        List<String> title = new ReplacementReleaseData().titleList();
//        for (int k = 0; k < title.size(); k++) {
//            cell = row.createCell(k);//创建第二行第一列
//            cell.setCellValue(title.get(k));
//            cell.setCellStyle(contextStyle);
//        }
//
//        for (int i = 0; i < objList.size(); i++) {
//            ReplacementReleaseData replacementReleaseData = (ReplacementReleaseData) (objList.get(i));
//            row = sheet.createRow(i + 2);//创建第三行
//            for (int j = 0; j < replacementReleaseData.dataList().size(); j++) {
//                cell = row.createCell(j);//创建第三行第一列
//                cell.setCellValue(replacementReleaseData.dataList().get(j));//第三行第一列的值
//                cell.setCellStyle(contextStyle);
//            }
//        }
//    }
//
//    public void genExcel_DaysOFGoodsInWarehouse(XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, List<DaysOFGoodsInWarehouse> objList, String titleContent) {
//
//        //根据Excel列名长度，指定列名宽度  Excel总共9列
//        for (int i = 0; i < 27; i++) {
//            if (i == 8) {
//                sheet.setColumnWidth(i, 7200);
//            } else if (i == 19) {
//                sheet.setColumnWidth(i, 6400);
//            } else {
//                sheet.setColumnWidth(i, 4800);
//            }
//        }
//
//        //设置标题位置
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row
//                0, //last row
//                0, //first column
//                26 //last column
//        ));
//
//        XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
//        XSSFCell cell;
//        cell = row.createCell(0);//创建一列
//        cell.setCellValue(titleContent);//标题
//        cell.setCellStyle(titleStyle);//设置标题样式
//
//        row = sheet.createRow(1);//创建第二行
//        List<String> title = new DaysOFGoodsInWarehouse().titleList();
//        for (int k = 0; k < title.size(); k++) {
//            cell = row.createCell(k);//创建第二行第一列
//            cell.setCellValue(title.get(k));
//            cell.setCellStyle(contextStyle);
//        }
//
//        for (int i = 0; i < objList.size(); i++) {
//            DaysOFGoodsInWarehouse daysOFGoodsInWarehouse = (DaysOFGoodsInWarehouse) (objList.get(i));
//            row = sheet.createRow(i + 2);//创建第三行
//            for (int j = 0; j < daysOFGoodsInWarehouse.dataList().size(); j++) {
//                cell = row.createCell(j);//创建第三行第一列
//                cell.setCellValue(daysOFGoodsInWarehouse.dataList().get(j));//第三行第一列的值
//                cell.setCellStyle(contextStyle);
//            }
//        }
//    }

    public void genExcel_ReceivingOrder(XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, Map<String, Integer> myMapReceivingOrder, String titleContent) {

        //根据Excel列名长度，指定列名宽度  Excel总共2列
        for (int i = 0; i < 2; i++) {
            sheet.setColumnWidth(i, 4800);
        }

        //设置标题位置
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row
                0, //last row
                0, //first column
                1 //last column
        ));

        XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
        XSSFCell cell;
        cell = row.createCell(0);//创建一列
        cell.setCellValue(titleContent);//标题
        cell.setCellStyle(titleStyle);//设置标题样式

        row = sheet.createRow(1);//创建第二行
        cell = row.createCell(0);//创建第二行第一列
        cell.setCellValue("Name");
        cell.setCellStyle(contextStyle);

        cell = row.createCell(1);//创建第二行第二列
        cell.setCellValue("Count");
        cell.setCellStyle(contextStyle);

        int lineNum = 2;

        for (String key : myMapReceivingOrder.keySet()) {
            System.out.println("Key: " + key + ", Value: " + myMapReceivingOrder.get(key));

            row = sheet.createRow(lineNum);//创建第三行

            cell = row.createCell(0);//创建第三行第一列
            cell.setCellValue(key);//第三行第一列的值
            cell.setCellStyle(contextStyle);

            cell = row.createCell(1);//创建第三行第一列
            cell.setCellValue(myMapReceivingOrder.get(key));//第三行第一列的值
            cell.setCellStyle(contextStyle);

            lineNum++;
        }
    }

    public void genExcel_TransFerOrder(XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, Map<String, Integer> myMapTransFerOrder, String titleContent) {

        //根据Excel列名长度，指定列名宽度  Excel总共2列
        for (int i = 0; i < 2; i++) {
            sheet.setColumnWidth(i, 4800);
        }

        //设置标题位置
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row
                0, //last row
                0, //first column
                1 //last column
        ));

        XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
        XSSFCell cell;
        cell = row.createCell(0);//创建一列
        cell.setCellValue(titleContent);//标题
        cell.setCellStyle(titleStyle);//设置标题样式

        row = sheet.createRow(1);//创建第二行
        cell = row.createCell(0);//创建第二行第一列
        cell.setCellValue("Name");
        cell.setCellStyle(contextStyle);

        cell = row.createCell(1);//创建第二行第二列
        cell.setCellValue("Count");
        cell.setCellStyle(contextStyle);

        int lineNum = 2;

        for (String key : myMapTransFerOrder.keySet()) {
            System.out.println("Key: " + key + ", Value: " + myMapTransFerOrder.get(key));

            row = sheet.createRow(lineNum);//创建第三行

            cell = row.createCell(0);//创建第三行第一列
            cell.setCellValue(key);//第三行第一列的值
            cell.setCellStyle(contextStyle);

            cell = row.createCell(1);//创建第三行第一列
            cell.setCellValue(myMapTransFerOrder.get(key));//第三行第一列的值
            cell.setCellStyle(contextStyle);

            lineNum++;
        }
    }

//    public void genExcel_receivingOrderBaseData(XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, List<ReceivingOrderData> receivingOrderDataList, String titleContent) {
//
//        //根据Excel列名长度，指定列名宽度  Excel总共9列
//        for (int i = 0; i < 6; i++) {
//            sheet.setColumnWidth(i, 4800);
//        }
//
//        //设置标题位置
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row
//                0, //last row
//                0, //first column
//                5 //last column
//        ));
//
//        XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
//        XSSFCell cell;
//        cell = row.createCell(0);//创建一列
//        cell.setCellValue(titleContent);//标题
//        cell.setCellStyle(titleStyle);//设置标题样式
//
//        row = sheet.createRow(1);//创建第二行
//        List<String> title = new ReceivingOrderData().titleList();
//        for (int k = 0; k < title.size(); k++) {
//            cell = row.createCell(k);//创建第二行第一列
//            cell.setCellValue(title.get(k));
//            cell.setCellStyle(contextStyle);
//        }
//
//        for (int i = 0; i < receivingOrderDataList.size(); i++) {
//            ReceivingOrderData receivingOrderData = (ReceivingOrderData) (receivingOrderDataList.get(i));
//            row = sheet.createRow(i + 2);//创建第三行
//            for (int j = 0; j < receivingOrderData.dataList().size(); j++) {
//                cell = row.createCell(j);//创建第三行第一列
//                cell.setCellValue(receivingOrderData.dataList().get(j));//第三行第一列的值
//                cell.setCellStyle(contextStyle);
//            }
//        }
//    }
//
//    public void genExcel_TransFerOrderBaseData(XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, List<TransFerOrderData> transFerOrderDataList, String titleContent) {
//
//        //根据Excel列名长度，指定列名宽度  Excel总共9列
//        for (int i = 0; i < 6; i++) {
//            sheet.setColumnWidth(i, 4800);
//        }
//
//        //设置标题位置
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row
//                0, //last row
//                0, //first column
//                5 //last column
//        ));
//
//        XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
//        XSSFCell cell;
//        cell = row.createCell(0);//创建一列
//        cell.setCellValue(titleContent);//标题
//        cell.setCellStyle(titleStyle);//设置标题样式
//
//        row = sheet.createRow(1);//创建第二行
//        List<String> title = new TransFerOrderData().titleList();
//        for (int k = 0; k < title.size(); k++) {
//            cell = row.createCell(k);//创建第二行第一列
//            cell.setCellValue(title.get(k));
//            cell.setCellStyle(contextStyle);
//        }
//
//        for (int i = 0; i < transFerOrderDataList.size(); i++) {
//            TransFerOrderData transFerOrderData = (TransFerOrderData) (transFerOrderDataList.get(i));
//            row = sheet.createRow(i + 2);//创建第三行
//            for (int j = 0; j < transFerOrderData.dataList().size(); j++) {
//                cell = row.createCell(j);//创建第三行第一列
//                cell.setCellValue(transFerOrderData.dataList().get(j));//第三行第一列的值
//                cell.setCellStyle(contextStyle);
//            }
//        }
//    }

}

























