package com.tuyenvp.spring_boot_app.Services;

import com.tuyenvp.spring_boot_app.Model.Address;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    public void updateAddress(Address updated);
    public Address getAddressById(Long id);
}
