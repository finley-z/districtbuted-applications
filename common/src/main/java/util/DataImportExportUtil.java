package util;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author zyf
 * @date 2017/3/8
 */
public class DataImportExportUtil {

    /***
     * excel数据导出
     *
     * @param response
     * @param workbook
     * @param title
     * @return
     * @throws IOException
     */
    public static boolean exportExcel(HttpServletResponse response, Workbook workbook, String title) throws IOException {
        boolean success = true;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            workbook.write(os);
            byte[] byteStream = os.toByteArray();
            InputStream is = new ByteArrayInputStream(byteStream);

            //设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xls").getBytes(), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();

            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buffer = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buffer, 0, buffer.length))) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        return success;
    }


    /***
     * excel数据导出
     *
     * @param response
     * @param workbook
     * @param title
     * @param title
     * @return
     * @throws IOException
     */
    public static boolean exportEncryptionExcel(HttpServletResponse response, Workbook workbook, String title,String password) throws IOException {
        boolean success = true;

        try {

            //设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xlsx").getBytes(), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();

            //把工作薄输出到字节里面
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            workbook.write(bout);
            bout.flush();
            ByteArrayInputStream Workbookinput = new ByteArrayInputStream(bout.toByteArray());
            //创建POIFS文件系统  加密文件
            POIFSFileSystem fs = new POIFSFileSystem();
            EncryptionInfo info = new EncryptionInfo(fs, EncryptionMode.agile);
            Encryptor enc = info.getEncryptor();
            enc.confirmPassword(password);
            //然后把字节输入到输入流，然后输入到OPC包里面
            OPCPackage opc = OPCPackage.open(Workbookinput);
            OutputStream os = enc.getDataStream(fs);
            opc.save(os);
            opc.close();
            fs.writeFilesystem(out);
            out.flush();
            out.close();

        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

}
