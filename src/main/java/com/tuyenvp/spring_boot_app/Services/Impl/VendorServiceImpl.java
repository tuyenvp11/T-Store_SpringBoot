package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Vendor;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorServiceImpl implements com.tuyenvp.spring_boot_app.Services.VendorService {
    @Autowired
    private DbConnect DbConnect;
    @Override
    public List<Vendor> ListVendor() {
        return DbConnect.vendorRepo.findAll();
    }

    @Override
    public Optional<Vendor> findVendorById(int vendor_id) {
        return DbConnect.vendorRepo.findById(vendor_id);
    }

    @Override
    public Vendor addVendor(Vendor add_vendor) {
        return DbConnect.vendorRepo.save(add_vendor);
    }

    @Override
    public Vendor updateVendor(Vendor edit_vendor) {
        Optional<Vendor>vendor = DbConnect.vendorRepo.findById(edit_vendor.getVendor_id());
        if(vendor.isEmpty()){
            return null;
        }
        Vendor update_vendor = vendor.get();
        update_vendor.setVendor_name(edit_vendor.getVendor_name());
        update_vendor.setVendor_hotline(edit_vendor.getVendor_hotline());
        update_vendor.setVendor_address(edit_vendor.getVendor_address());
        DbConnect.vendorRepo.save(update_vendor);
        return update_vendor;
    }

    @Override
    public Vendor deleteVendor(int vendor_id) {
        Optional<Vendor>vendor = DbConnect.vendorRepo.findById(vendor_id);
        if(vendor.isEmpty()) {
            return null;
        }
        DbConnect.vendorRepo.deleteById(vendor_id);
        return vendor.get();
    }

    @Override
    public List<Vendor> searchVendor(String vendor_name) {
        return List.of();
    }

    @Override
    public Page<Vendor> getAll(Pageable pageable) {
        return null;
    }
}
