package com.tuyenvp.spring_boot_app.Controller.Admin;

import com.tuyenvp.spring_boot_app.Model.Vendor;
import com.tuyenvp.spring_boot_app.Services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class VendorController {
    @Autowired
    public VendorService vendorService;

    @GetMapping("/vendor")
    public String vendor(Model model) {
        List<Vendor> list = vendorService.ListVendor();
        model.addAttribute("ListVendor", list);
        return "admin/vendor/vendor";
    }

    // thêm nhà cung cấp
    @GetMapping("/vendor/add_vendor")
    public String add_vendor(Model model) {
        Vendor add_vendor = new Vendor();
        model.addAttribute("add_vendor", add_vendor);
        return "admin/vendor/add_vendor";
    }

    @PostMapping("/vendor/add_vendor")
    public String save_vendor(Model model, @ModelAttribute("vendor") Vendor save_vendor) {
        vendorService.addVendor(save_vendor);
        return "redirect:/admin/vendor";
    }

    // sửa nhà cung cấp
    @GetMapping("/vendor/edit_vendor/{vendor_id}")
    public String edit_vendor(Model model, @PathVariable("vendor_id")Integer vendor_id) {
        Optional<Vendor> vendor = vendorService.findVendorById(vendor_id);
        model.addAttribute("edit_vendor", vendor.get());
        return "admin/vendor/edit_vendor";
    }
    @PostMapping("/vendor/edit_vendor")
    public String update_vendor(Model model, @ModelAttribute("edit_vendor") Vendor save_vendor) {
        vendorService.updateVendor(save_vendor);
        return "redirect:/admin/vendor";
    }

    // xoá nhà cung cấp
    @GetMapping("/vendor/del_vendor/{vendor_id}")
    public String del_vendor(Model model, @PathVariable("vendor_id")Integer vendor_id){
        vendorService.deleteVendor(vendor_id);
        return "redirect:/admin/vendor";
    }

}
