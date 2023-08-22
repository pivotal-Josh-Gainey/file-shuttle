package com.jgainey.demo;


import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
@Controller
public class MyController {

    String fileName = "";
    String fullFilePath = "";

    @RequestMapping(method = RequestMethod.POST, value="/upload", consumes = {"multipart/form-data"},produces = "application/json")
    public ResponseEntity<String> importFast(@Valid @RequestParam("file") MultipartFile multiPartFile) {


        new File("/tmp/scratch").mkdirs();
        try {
            FileUtils.cleanDirectory(new File("/tmp/scratch"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        InputStream initialStream = null;
        try {
            initialStream = multiPartFile.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer = new byte[0];
        try {
            buffer = new byte[initialStream.available()];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            initialStream.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fileName = multiPartFile.getOriginalFilename();
        fullFilePath = "/tmp/scratch/" + fileName;

        File targetFile = new File(fullFilePath);

        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            try {
                outStream.write(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletResponse response) {

        InputStream inputStream = null;
        response.setHeader("Content-Disposition","attachment; filename="+fileName);
        try {
            inputStream = new FileInputStream(new File(fullFilePath));
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
