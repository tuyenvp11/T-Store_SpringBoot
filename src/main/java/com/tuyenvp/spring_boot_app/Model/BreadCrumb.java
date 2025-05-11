package com.tuyenvp.spring_boot_app.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BreadCrumb {
    private String name;
    private String url;
    private boolean last;
}
