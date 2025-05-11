package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.Review;
import com.tuyenvp.spring_boot_app.Repository.ProductRepo;
import com.tuyenvp.spring_boot_app.Repository.UserRepo;
import com.tuyenvp.spring_boot_app.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;




}
