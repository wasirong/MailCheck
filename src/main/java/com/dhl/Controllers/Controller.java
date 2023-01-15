package com.dhl.Controllers;

import com.dhl.Data.InventoryData;
import com.dhl.Util.CreateWorkbook;
import com.dhl.Util.ReadTXTFile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Controller {
    @FXML
    private Button SelectInputDicID;
    @FXML
    private Button SelectHWBDicID;
    @FXML
    private Button SelectOutputDicID;
    @FXML
    private Button GenerateCsvID;
    @FXML
    private TextField inputDirPathID; // 库存
    @FXML
    private TextField batchHWBQueryID; // 航班
    @FXML
    private TextField outputDirPathID;
    @FXML
    private Label ActionTarget;

    public static String inputDirPath = "";
    public static String hwbDirPath = "";
    public static String outputDirPath = "";
//    Logger LOG = LoggerFactory.getLogger(Controller.class);

    @FXML
    public void SelectInputDicAction() {
        // 库存
        DirectoryChooser inputDirChooser = new DirectoryChooser();
        inputDirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        inputDirChooser.setTitle("选择【库存数据】所在文件夹");
        File selectedDirectory = inputDirChooser.showDialog((Stage) SelectInputDicID.getScene().getWindow());
        if (selectedDirectory != null) {
            inputDirPath = selectedDirectory.getAbsolutePath();
            if (!inputDirPath.equals("")) {
                File dir = new File(inputDirPath);
                File[] directoryListing = dir.listFiles();
                int count = 0;
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        int extentionIndex = child.getName().lastIndexOf(".");
                        if (extentionIndex == -1 || !child.getName().contains(".txt")) {
                            continue;
                        }
                        if (child.getName().contains(".txt")) {
                            count++;
                        }
                    }
                } else {
                    if (!dir.isDirectory()) {
                        ActionTarget.setText("Not a directory");
                    }
                }
                if (count < 1) {
                    inputDirPathID.setText("");
                    ActionTarget.setText("请确认当前文件夹是否有正确的文件! !");
                } else {
                    inputDirPathID.setText(inputDirPath);
                    ActionTarget.setText("");
                }
            }
        }
    }

    @FXML
    public void SelectHWBDicAction() {
        DirectoryChooser inputDirChooser = new DirectoryChooser();
        inputDirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        inputDirChooser.setTitle("选择【邮件数据】所在文件夹");
        File selectedDirectory = inputDirChooser.showDialog((Stage) SelectHWBDicID.getScene().getWindow());
        if (selectedDirectory != null) {
            hwbDirPath = selectedDirectory.getAbsolutePath();
            // 检索当前文件夹下的hwb文件
            File dir = new File(hwbDirPath);
            File[] directoryListing = dir.listFiles();
            int count = 0;
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    int extentionIndex = child.getName().lastIndexOf(".");
                    if (extentionIndex == -1 || !child.getName().contains(".msg")) {
                        continue;
                    }
                    if (child.getName().contains(".msg")) {
                        count++;
                    }
                }
            } else {
                if (!dir.isDirectory()) {
                    System.out.println("Not a directory");
                    ActionTarget.setText("Not a directory");
                }
            }
            if (count < 1) {
                batchHWBQueryID.setText("");
                ActionTarget.setText("请确认当前文件夹是否有正确的文件! !");
            } else {
                batchHWBQueryID.setText(hwbDirPath);
                ActionTarget.setText("");
            }
        }
    }

    @FXML
    public void SelectOutputDicAction() {
        DirectoryChooser outputDirChooser = new DirectoryChooser();
        outputDirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        outputDirChooser.setTitle("选择文件出力文件夹");
        File selectedDirectory = outputDirChooser.showDialog((Stage) SelectOutputDicID.getScene().getWindow());
        if (selectedDirectory != null) {
            outputDirPath = selectedDirectory.getAbsolutePath();
            if (!outputDirPath.equals("")) {
                outputDirPathID.setText(outputDirPath);
            }
        }
    }

    FileOutputStream out = null;

    XSSFWorkbook workbook = null;

    @FXML
    public void ConvertPDFTOCSV() throws IOException {
        ReadTXTFile readTXTFile = new ReadTXTFile();

        if (!inputDirPath.equals("") && !hwbDirPath.equals("") && !outputDirPath.equals("")) {
            ActionTarget.setText("Processing...");

//            File file = new File("C:\\Temp\\航班库存数据分析.xlsx");
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String dateString = formatter.format(currentTime);
            File file = new File(outputDirPathID.getText() + "\\邮件库存数据分析_" + dateString + ".xlsx");

            if (file.exists()) {
                file.delete();
            }
            // 邮件
            Map<String, Integer> mailData = readTXTFile.GetFlightData(GetList2(batchHWBQueryID.getText()));

            // 库存
            Map<String, InventoryData> inventoryData = readTXTFile.GetInventoryData(GetList(inputDirPathID.getText()));

            CreateWorkbook createWorkbook = new CreateWorkbook(mailData, inventoryData, "邮件库存数据分析");

            try {
                workbook = createWorkbook.generateExcel();
                out = new FileOutputStream(file);
                workbook.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }

            ActionTarget.setText("Done!");
        } else {
            ActionTarget.setText("Please specify input/hwbDirPath/output directories.");
        }
    }

    public List<String> GetList(String path) {
        List<String> fileList = new ArrayList<String>();
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        String firFileName = "";
        int count = 0;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                int extentionIndex = child.getName().lastIndexOf(".");
                if (extentionIndex == -1 || !child.getName().contains(".txt")) {
                    continue;
                }
                if (child.getName().contains(".txt")) {
                    firFileName = child.getName();
                    fileList.add(path + "\\" + firFileName);
                    count++;
                }
            }
        } else {
            if (!dir.isDirectory()) {
                System.out.println("Not a directory");
                ActionTarget.setText("Not a directory");
            }
        }
        return fileList;
    }

    public List<String> GetList2(String path) {
        List<String> fileList = new ArrayList<String>();
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        String firFileName = "";
        int count = 0;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                int extentionIndex = child.getName().lastIndexOf(".");
                if (extentionIndex == -1 || !child.getName().contains(".msg")) {
                    continue;
                }
                if (child.getName().contains(".msg")) {
                    firFileName = child.getName();
                    fileList.add(firFileName);
                    count++;
                }
            }
        } else {
            if (!dir.isDirectory()) {
                System.out.println("Not a directory");
                ActionTarget.setText("Not a directory");
            }
        }
        return fileList;
    }
}
