package com.tuyenvp.spring_boot_app.Util;

public enum OrderStatus {
    IN_PROGRESS(1, "Đang xử lý"), ORDER_RECEIVED(2, "Đã xác nhận"), PRODUCT_PACKED(3, "Sản phẩm đang được đóng gói"),
    OUT_FOR_DELIVERY(4, "Đang giao hàng"), DELIVERED(5, "Đã giao hàng"),CANCEL(6,"Đã huỷ"),SUCCESS(7,"Giao thành công");

    private Integer id;

    private String name;

    private OrderStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
