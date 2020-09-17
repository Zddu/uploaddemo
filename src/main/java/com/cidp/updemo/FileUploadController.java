package com.cidp.updemo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {


    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/upload")
    public String uploadfile(MultipartFile file, HttpServletRequest request){
        String realPath = request.getSession().getServletContext().getRealPath("/img");
        System.out.println(realPath);
        File folder = new File(realPath);
        if (!folder.exists()){
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.indexOf("."));
        try {
            file.transferTo(new File(folder,newName));
            String url = request.getScheme() + "://" + request.getServerName()+":"
                    + request.getServerPort() + "/img/" + newName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    @GetMapping("/download/{filename}")
    public void download(@PathVariable String filename, HttpServletRequest req, HttpServletResponse res){
        String url = req.getServletContext().getRealPath("/img");
        try {
            FileInputStream getfiel = new FileInputStream(new File(url, filename));
            ServletOutputStream outputStream = res.getOutputStream();
            res.setContentType("application/x-download");
            res.addHeader("Content-Disposition","attachment;filename="+filename);
            IOUtils.copy(getfiel,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
