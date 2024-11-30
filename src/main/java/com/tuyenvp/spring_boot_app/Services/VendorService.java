package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface VendorService {
    public List<Vendor> ListVendor() ;
    Optional<Vendor> findVendorById(int vendor_id);
    public Vendor addVendor(Vendor add_vendor);
    public Vendor updateVendor(Vendor edit_vendor);
    public Vendor deleteVendor(int vendor_id);
    public List<Vendor> searchVendor(String vendor_name);
    Page<Vendor> getAll(Pageable pageable);
}
