/*
package com.tuyenvp.spring_boot_app.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface SystemStorageService {
    void init();
    void store(MultipartFile file);
}
*/

package com.tuyenvp.spring_boot_app.Services;

import org.springframework.web.multipart.MultipartFile;

public interface SystemStorageService {
    void init();
    void store(MultipartFile file, String type);
    String getFilePath(String fileName, String type);
}

