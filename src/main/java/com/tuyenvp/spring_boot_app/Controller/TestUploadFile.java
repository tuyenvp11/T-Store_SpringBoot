package com.tuyenvp.spring_boot_app.Controller;

import com.tuyenvp.spring_boot_app.Services.Impl.SystemStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class TestUploadFile {
    @Autowired
    public SystemStorageServiceImpl systemStorageServiceImpl;
    @GetMapping
    public String uploadDemo(){
        return "uploadFile";
    }
    @PostMapping
    public String save(@RequestParam("file") MultipartFile file){
        systemStorageServiceImpl.store(file);
        return "uploadFile";
    }
}
