package com.tuyenvp.spring_boot_app.Repository;

import com.tuyenvp.spring_boot_app.Model.Product;
import com.tuyenvp.spring_boot_app.Model.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificationRepo extends JpaRepository<Specification, Integer> {
    //Specification findByProduct(Product product);
    @Query("SELECT p FROM Product p JOIN FETCH p.specification")
    List<Product> findAllWithSpecification();
}
