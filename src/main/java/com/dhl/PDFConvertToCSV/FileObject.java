package com.dhl.PDFConvertToCSV;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class FileObject {
    private File f;
    private List<String> lines = new ArrayList<String>();
    private List<Boolean> lineVisible = new ArrayList<Boolean>();
    private boolean isInitialized = false;

    public FileObject(File _f) {
        f = _f;
        this.isInitialized = false;
    }

    public boolean init() {
        if (this.isInitialized) {
            return false;
        }
        try {
            if (getFileEnding().equals("docx")) {
                readWordDocument();
            } else if (getFileEnding().equals("xlsx")) {
                readExcelDocument();
            } else if (getFileEnding().equals("pdf")) {
                readPDFDocument();
            } else {
                lines = Files
                        .readAllLines(f.toPath(), Charset.defaultCharset());
            }
            resetLineVisibility();
            this.isInitialized = true;
            return true;
        } catch (IOException e) {
            System.err.println("File could not be read.");
            return false;
        }
    }

    private void readPDFDocument() {
        try {
            //FileInputStream fs = new FileInputStream(f);
            String text = "";
            PDFParser parser = new PDFParser(new RandomAccessFile(f, "r"));
            parser.parse();
            COSDocument cosDoc = parser.getDocument();
            PDFTextStripper pdfStripper = new PDFTextStripper();
            PDDocument pdDoc = new PDDocument(cosDoc);
            text = pdfStripper.getText(pdDoc);
            String[] docxLines = text.split(System.lineSeparator());
            for (String line : docxLines) {
                lines.add(line);
            }
            cosDoc.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fehler in readPDFDocument",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void readExcelDocument() {
        try {
            FileInputStream fs = new FileInputStream(f);
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sh;
            String text = "";
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                sh = wb.getSheetAt(i);
                for (int j = sh.getFirstRowNum(); j <= sh.getLastRowNum(); j++) {
                    XSSFRow currRow = sh.getRow(j);
                    if (currRow == null || currRow.getFirstCellNum() == -1) {
                        continue;
                    } else {
                        for (int k = currRow.getFirstCellNum(); k < currRow
                                .getLastCellNum(); k++) {
                            if (currRow.getCell(k, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) == null) {
                                continue;
                            } else {
                                text += currRow.getCell(k) + "; ";
                            }
                        }
                        text += System.lineSeparator();
                    }
                }
            }
            fs.close();
            wb.close();
            String[] xlsxLines = text.split(System.lineSeparator());
            for (String line : xlsxLines) {
                lines.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Fehler in readExcelDocument",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void readWordDocument() {
        try {
            FileInputStream fs = new FileInputStream(f);
            XWPFDocument document;
            System.out.println("document = new XWPFDocument(OPCPackage.open(fs));");
            document = new XWPFDocument(OPCPackage.open(fs));
            XWPFWordExtractor docxReader = new XWPFWordExtractor(document);
            String text = docxReader.getText();
            docxReader.close();
            String[] docxLines = text.split("\n");
            for (String line : docxLines) {
                lines.add(line);
            }
            fs.close();
        } catch (InvalidFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "InvalidFormatException in readWordDocument", "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "FileNotFoundException in readWordDocument", "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "IOException in readWordDocument", "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String getFileEnding() {
        int index = f.getName().lastIndexOf(".");
        //System.out.println(f.getName());
        return f.getName().substring(index + 1);
    }

    private void resetLineVisibility() {
        lineVisible.clear();
        for (int i = 0; i < lines.size(); i++) {
            lineVisible.add(false);
        }
    }

    public boolean getVisibilityAt(int index) {
        if (index < 0 || index >= lineVisible.size()) {
            return false;
        }
        return lineVisible.get(index);
    }

    public List<String> getLines() {
        return lines;
    }

    public File getFile() {
        return f;
    }
}