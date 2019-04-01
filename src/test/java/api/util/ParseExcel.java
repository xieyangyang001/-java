package api.util;

import api.canstants.FileErrorCodeCanstant;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by xieyangyang on 2018/11/8.
 */
public class ParseExcel {

    Logger logger = Logger.getLogger(ParseExcel.class);

    public static final String suffix_xls = ".xls";
    public static final String suffix_xlsx = ".xlsx";

    /**
     * 转换为ExcelName
     *
     * @param filePath
     * @return ExcelName
     */
    public String getExcelName(String filePath) {
        String[] names;
        if (filePath.contains("\\")) {
            names = filePath.split("\\\\");
        } else if (filePath.contains("/")) {
            names = filePath.split("/");
        } else {
            return filePath;
        }
        return names[names.length - 1];
    }


    /**
     * 判断是不是一个exlce
     *
     * @param ExcelName
     * @return boolean
     */
    public boolean checkExcelName(String ExcelName) {
        if (ExcelName.length() > 0) {
            String[] ExcelNameAndSuffix = ExcelName.split("\\.");
            String suffix = ExcelNameAndSuffix[ExcelNameAndSuffix.length - 1];
            if (suffix_xls.contains(suffix) || suffix_xlsx.contains(suffix)) {
                return true;
            }
        }
        logger.error("文件名称：" + ExcelName + "_" + FileErrorCodeCanstant.FILE_ERROR_FORMAT + "_" + FileErrorCodeCanstant.FILE_ERROR_FORMAT.getDesc());
        return false;
    }

    /**
     * 判断是不是一个目录
     *
     * @param filePath
     * @return boolean
     */
    public boolean checkExcelFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            logger.info("文件目录：" + filePath);
            return true;
        }
        logger.error("文件目录：" + filePath + ";" + FileErrorCodeCanstant.FILE_ERROR_NOTFOUND + "_" + FileErrorCodeCanstant.FILE_ERROR_NOTFOUND.getDesc());
        return false;
    }

    /**
     * 读取excel
     * @param filePath
     * @return
     * @throws Exception
     */
    public Workbook getWorkBook(String filePath) throws Exception {
        Workbook workbook = null;
        if (!checkExcelFile(filePath)) {
            throw new Exception(filePath + FileErrorCodeCanstant.FILE_ERROR_NOTFOUND.getDesc());
        }
        String excelName = getExcelName(filePath);
        if (!checkExcelName(excelName)) {
            throw new Exception(filePath + FileErrorCodeCanstant.FILE_ERROR_FORMAT.getDesc());
        }
        //转换workbook
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        if (excelName.contains(suffix_xls)) {

            workbook = new XSSFWorkbook(fileInputStream);
        } else if (excelName.contains(suffix_xlsx)) {
            workbook = new HSSFWorkbook(fileInputStream);
        }
        return workbook;
    }


    /**
     * 将excel转换为List<Sheet>
     *
     * @param filePath
     * @return List
     */
    public List<Sheet> readExcel(String filePath) throws Exception {
        List<Sheet> list = new ArrayList<>();
        Workbook workbook = getWorkBook(filePath);
        Iterator<Sheet> sheets = workbook.sheetIterator();
        while (sheets.hasNext()) {
            list.add(sheets.next());
        }
        return list;
    }


    /**
     * sheet转换为map一维数组
     *
     * @return Map[]
     */
    public Map[][] rowToMap(Sheet sheet) throws Exception {
        logger.info("访问的sheet的名字：" + sheet.getSheetName());
        if (sheet.getLastRowNum() < 1) {
            logger.error(sheet.getSheetName() + ":该表格row无数据");
            throw new Exception(sheet.getSheetName() + ":该表格row无数据");
        }

        //case的可用存入list
        List<String> mapKeys = new ArrayList<>();
        Row rowMapkeys = sheet.getRow(0);
        if (rowMapkeys.getLastCellNum() <= 1) {
            logger.error(sheet.getSheetName() + ":该表格只有案例case_id");
            throw new Exception(sheet.getSheetName() + ":该表格只有案例case_id");
        }
        for (int mapKeyNum = 0; mapKeyNum < rowMapkeys.getLastCellNum(); mapKeyNum++) {
            String mapKey = String.valueOf(getCellValue(rowMapkeys.getCell(mapKeyNum)));
            mapKeys.add(mapKey);
        }

        //判断返回的excel读取的行数
        int lastRow = 1;
        p:
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            for (int cellNum = 0; cellNum < mapKeys.size(); cellNum++) {
                //如果
                if (cellNum == 0 && row.getCell(cellNum) == null) {
                    break p;
                }
            }
            lastRow++;
        }
        logger.info(sheet.getSheetName() + "可读取行数为：" + (lastRow - 1));
        Map<String, String>[][] objects = new Map[lastRow - 1][];

        int index = 0;
        for (int i = 1; i < lastRow; i++) {
            //参数
            Map[] maps = new HashMap[1];
            Row row = sheet.getRow(i);
            Map<String, String> testData = new HashMap<>();
            for (int cellNum = 0; cellNum < mapKeys.size(); cellNum++) {
                if (row.getCell(cellNum) == null) {
                    testData.put(mapKeys.get(cellNum), "");
                } else {
                    String cellValue = String.valueOf(getCellValue(row.getCell(cellNum)));
                    testData.put(mapKeys.get(cellNum), cellValue);
                }
            }
            maps[0] = testData;
            objects[index] = maps;
            index++;
        }
        return objects;
    }

    public Object getCellValue(Cell cell) {
        Object object = null;
        switch (cell.getCellType()) {
            case BOOLEAN:
                object = cell.getBooleanCellValue();
                break;
            case ERROR:
                object = cell.getErrorCellValue();
                break;
            case NUMERIC:
                object = cell.getNumericCellValue();
                break;
            case FORMULA:
                object = cell.getCellFormula();
                break;
            case STRING:
                object = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return object;
    }

    /**
     * 集成测试用不用写多余的方法，把更多的逻辑交给excel
     *
     * @return
     * @throws Exception
     */
    @DataProvider
    public Object[][] excelToMap() throws Exception {
        String path = ParseExcel.class.getResource("/").getFile() + "excel/1.xlsx";
       // String path="src\\resources\\test\\excel\\1.xlsx";
        List<Sheet> sheets = readExcel(path);
        //为maps长度
        int rows=0;
        for (int i = 0; i < sheets.size(); i++) {
            rows+=sheets.get(i).getLastRowNum();
        }
        Map<String, String>[][] maps = new HashMap[rows][];
        //为maps赋值
        int row=0;
        for (int i = 0; i < sheets.size(); i++) {
            Map<String, String>[][] sheetMaps=rowToMap(sheets.get(i));
            for (int j = 0; j < sheetMaps.length; j++) {
                maps[row] = sheetMaps[j];
                row+=1;
            }
        }
        return maps;
    }

    /**
     * 方便测试 指定测试excel和sheetName
     *
     * @param method
     * @return
     */
    @DataProvider
    public Object[][] excelToMapSingle(Method method) {
        //根据类的名字读取excel
        //String excelName=this.getClass().getName();
        //自定义excel名字
        String excelName="";

        String path = ParseExcel.class.getResource("/").getFile() + "excel/"+excelName+".xlsx";
        Map<String,String>[][] maps=null;
        try {
            Workbook workbook=getWorkBook(path);
            Sheet sheet=workbook.getSheet(method.getName());
            maps=rowToMap(sheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }
}
