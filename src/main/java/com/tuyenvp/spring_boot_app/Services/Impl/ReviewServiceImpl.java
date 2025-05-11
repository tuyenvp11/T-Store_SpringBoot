package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Review;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private DbConnect DbConnect;


    @Override
    public void saveReview(Review review) {
        review.setReviewTime(LocalDateTime.now());
        DbConnect.reviewRepo.save(review);
    }

    @Override
    public List<Review> getReviewsByVariant(Integer variantId) {
        return DbConnect.reviewRepo.findByProductVariant_VariantIdOrderByReviewTime(variantId);
    }
}
