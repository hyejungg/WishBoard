package com.hyeeyoung.wishboard.model;

public class CartItem {
    private int item_image; // @param : 서버연동 후 String으로 변경 예정
    private String item_name;
    private String total;
    private int qty;

    public CartItem() {
    }

    public CartItem(int item_image, String item_name, String total, int qty) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.total = total;
        this.qty = qty;
    }

    public int getItem_image() {
        return item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getTotal() {
        return total;
    }

    public int getQty() {
        return qty;
    }

    public void setItem_image(int item_image) {
        this.item_image = item_image;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
