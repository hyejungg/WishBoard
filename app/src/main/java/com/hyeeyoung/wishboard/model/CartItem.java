package com.hyeeyoung.wishboard.model;

public class CartItem {
    // @parmas : 서버와 연동하기 위해 사용될 변수
    // @see : public으로 선언한 이유는 다른 클래스에서 접근하기 위함
    public String user_id;
    public String item_id;

    private int item_image; // @param : 서버연동 후 String으로 변경 예정
    private String item_name;
    private String item_price;
    private int qty;

    public CartItem() {
    }

    public CartItem(String user_id, String item_id) {
        this.user_id = user_id;
        this.item_id = item_id;
    }

    public CartItem(int item_image, String item_name, String item_price, int qty) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_price = item_price;
        this.qty = qty;
    }

    public int getItem_image() {
        return item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_price() {
        return item_price;
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

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
