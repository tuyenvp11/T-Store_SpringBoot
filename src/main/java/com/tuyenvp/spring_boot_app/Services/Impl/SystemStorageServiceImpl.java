/*
package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Services.SystemStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class SystemStorageServiceImpl implements SystemStorageService {
    private final Path rootLocation;
    public SystemStorageServiceImpl(){
        rootLocation= Paths.get("src/main/resources/static/be/img/products");

    }
    @Override
    public void init() {
        try{
            Files.createDirectories(rootLocation);
        }catch (Exception e){

        }
    }

    @Override
    public void store(MultipartFile file) {
        try {
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            try(InputStream inputStream=file.getInputStream()){
                Files.copy(inputStream,destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
*/


package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Services.SystemStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class SystemStorageServiceImpl implements SystemStorageService {
    private final Path categoryLocation;
    private final Path productLocation;
    private final Path avatarLocation;

    public SystemStorageServiceImpl() {
        // Định nghĩa thư mục lưu trữ
        this.categoryLocation = Paths.get("uploads/categories");
        this.productLocation = Paths.get("uploads/products");
        this.avatarLocation = Paths.get("uploads/avatars");

        // Tạo thư mục nếu chưa tồn tại
        init();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(categoryLocation);
            Files.createDirectories(productLocation);
            Files.createDirectories(avatarLocation);
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục lưu trữ ảnh!", e);
        }
    }

    public void store(MultipartFile file, String type) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File ảnh rỗng!");
            }

            // Chọn thư mục lưu trữ
            Path destinationDir;
            if ("category".equalsIgnoreCase(type)) {
                destinationDir = categoryLocation;
            } else if ("product".equalsIgnoreCase(type)) {
                destinationDir = productLocation;
            }
            else if ("avatar".equalsIgnoreCase(type)) {
                destinationDir = avatarLocation;
            } else {
                throw new IllegalArgumentException("Loại ảnh không hợp lệ!");
            }

            Path destinationFile = destinationDir.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file ảnh!", e);
        }
    }

    public String getFilePath(String fileName, String type) {
        if ("category".equalsIgnoreCase(type)) {
            return "/uploads/categories/" + fileName;
        } else if ("product".equalsIgnoreCase(type)) {
            return "/uploads/products/" + fileName;
        }else if ("avatar".equalsIgnoreCase(type)) {
            return "/uploads/avatar/" + fileName;
        }
        return null;
    }
}

