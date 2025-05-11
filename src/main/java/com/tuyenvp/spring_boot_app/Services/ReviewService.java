package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Review;

import java.util.List;

public interface ReviewService {
    void saveReview(Review review);
    List<Review> getReviewsByVariant(Integer variantId);
}
