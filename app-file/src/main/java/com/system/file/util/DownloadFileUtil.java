package com.system.file.util;

import com.system.base.exception.BusinessException;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

public class DownloadFileUtil {
    public static void dowmload(HttpServletResponse response, File file, String fileName) {
        // 清空response
        response.reset();
        // 设置response的Header
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (Exception e) {
            throw new BusinessException("文件下载失败");
        }
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        byte[] buffer;
        //InputStream -> byte[]
        try (
                InputStream inputStream = new FileInputStream(file);
                InputStream fis = new BufferedInputStream(inputStream)
        ) {
            buffer = new byte[fis.available()];
            fis.read(buffer);
        } catch (Exception e) {
            throw new BusinessException("文件下载失败");
        }
        //byte[] -> OutputStream
        try (
                OutputStream outputStream = response.getOutputStream();
                OutputStream toClient = new BufferedOutputStream(outputStream)
        ) {
            toClient.write(buffer);
            toClient.flush();
        } catch (Exception e) {
            throw new BusinessException("文件下载失败");
        }
    }
}
