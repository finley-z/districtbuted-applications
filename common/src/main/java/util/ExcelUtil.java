package util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zyf
 * @date 2017/3/8
 */
public class ExcelUtil {

    /**
     * 将excel表格数据转换成对象集合
     *
     * @param fields
     * @return
     * @throws Exception
     */
    public static <T> List<T> parseData(InputStream inputStream,String fileName, String[] fields, Class<T> cls) throws Exception {

        //文件扩展名
        String subfix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        Workbook wb = null;

        if (subfix.endsWith("xlsx")) {
            wb = new XSSFWorkbook(inputStream);   // 操作Excel2007的版本，扩展名是.xlsx
        } else if (subfix.endsWith("xls")) {
            wb = new HSSFWorkbook(inputStream);  // 操作Excel2003以前（包括2003）的版本，扩展名是.xls
        }
        if (wb==null) {
            throw new Exception("文件格式不支持");
        }
        List<T> datas = new ArrayList<T>();     //先保存到临时表里面，再处理到正式表，最后清空临时表

        Sheet sheet = wb.getSheetAt(0);
        int count = sheet.getLastRowNum() + 1;

        for (int i = 1; i < count; i++) {
            Row row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            int filedLength = fields.length;

            if (lastCellNum != filedLength) {
                throw new Exception("表格格式不正确");
            }

            T data = (T) cls.newInstance();
            for (int j = 0; j < lastCellNum; j++) {
                String filedName = fields[j];
                Field filed = cls.getDeclaredField(filedName);      //获取字段名
                filed.setAccessible(true);
                Cell cell = row.getCell(j);                 //获取单元格
                try {
                    String fieldTypeName=filed.getType().getSimpleName();
                    if(fieldTypeName.equals("Integer")||fieldTypeName.equals("Double")||fieldTypeName.equals("Short")
                         ||fieldTypeName.equals("Long")||fieldTypeName.equals("Float")||fieldTypeName.equals("Byte")){
                        cell .setCellType(Cell.CELL_TYPE_NUMERIC);
                        filed.set(data, cell.getNumericCellValue());
                    }else if(fieldTypeName.equals("Date")){
                        filed.set(data, cell.getDateCellValue());
                    }else{
                        cell .setCellType(Cell.CELL_TYPE_STRING);
                        filed.set(data, cell.getStringCellValue());
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
            datas.add(data);
        }
        return datas;
    }

    /***
     * 将数据转换成excel表格
     *
     * @param list
     * @param cls
     * @param columnNames
     * @param fieldsName
     * @return
     * @throws Exception
     */
    public static <T> Workbook createWorkBook(List<T> list, Class<T> cls, String columnNames[], String[] fieldsName) throws Exception {

        Workbook wb = new XSSFWorkbook();           // 创建excel工作簿
        Sheet sheet = wb.createSheet("sheet1");    // 创建第一个sheet（页），并命名
        Row row = sheet.createRow(0);                // 创建第一行

        CellStyle cs = wb.createCellStyle();         // 创建两种单元格格式
        CellStyle cs2 = wb.createCellStyle();

        Font f = wb.createFont();                     // 创建两种字体
        Font f2 = wb.createFont();

        f.setFontHeightInPoints((short) 10);           // 创建第一种字体样式（用于列名）
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        f2.setFontHeightInPoints((short) 10);          // 创建第二种字体样式（用于值）
        f2.setColor(IndexedColors.BLACK.getIndex());

        cs.setFont(f);                                 // 设置第一种单元格的样式（用于列名）
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);

        //设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }

        //设置每行每列的值
        for (int i = 0; i < list.size(); i++) {

            //Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            Row row1 = sheet.createRow(i + 1);
            sheet.setColumnWidth((short) i, (short) (35.7 * 120));
            T data = list.get(i);

            //在row行上创建一个方格
            for (int j = 0; j < fieldsName.length; j++) {
                Cell cell = row1.createCell(j);
                Field field = cls.getDeclaredField(fieldsName[j]);
                field.setAccessible(true);
                String fieldTypeName=field.getType().getSimpleName();
                if("Date".equals(fieldTypeName)){
                    cell.setCellValue(field.get(data) == null ? " " : DateFormatterUtil.getStandardTime((Date) field.get(data)));
                }else{
                    cell.setCellValue(field.get(data) == null ? " " : field.get(data).toString());
                }
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }


    /**
     * 创建excel文档，
     * @param list 数据
     * @param columnNames excel的列名
     * */
    public static Workbook createWorkBook1(List<Object[]> list,String columnNames[]) {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet("xx");
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        //sheet.setColumnWidth((short) 1, (short) (35.7 * 150));

        // 创建第一行
        Row row = sheet.createRow(0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

//        Font f3=wb.createFont();
//        f3.setFontHeightInPoints((short) 10);
//        f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for(int i=0;i<columnNames.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (int i = 0; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow(i+1);
            sheet.setColumnWidth((short) i, (short) (35.7 * 120));
            // 在row行上创建一个方格
            for(int j=0;j<list.get(i).length;j++){
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i)[j] == null?" ": list.get(i)[j].toString());
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }


}
