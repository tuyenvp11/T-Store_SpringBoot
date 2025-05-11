package com.tuyenvp.spring_boot_app.Services.Impl;

import com.tuyenvp.spring_boot_app.Model.Address;
import com.tuyenvp.spring_boot_app.Repository.DbConnect;
import com.tuyenvp.spring_boot_app.Services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private DbConnect DbConnect;

    @Override
    public void updateAddress(Address updated) {
        Address existing = DbConnect.addressRepo.findById(updated.getAddressId()).orElse(null);
        if (existing != null) {
            existing.setType(updated.getType());
            existing.setReceiverName(updated.getReceiverName());
            existing.setReceiverPhone(updated.getReceiverPhone());
            existing.setFullAddress(updated.getFullAddress());
            existing.setDefault(updated.isDefault());
            DbConnect.addressRepo.save(existing);
        }
    }

    @Override
    public Address getAddressById(Long id) {
        return DbConnect.addressRepo.findById(id).orElse(null);
    }
}
