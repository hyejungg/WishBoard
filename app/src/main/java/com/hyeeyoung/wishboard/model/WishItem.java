package com.hyeeyoung.wishboard.item;

public class WishItem {
    //private String item_image @param : 추후 DB에서 이미지링크 가져올 경우 사용할 변수
    //private Drawable item_image; @deprecated : 상품이미지를 drawble 내 복사 후 실제 이미지로 테스트 가능. 태스트 시 주석 제거 후 사용
    private int item_image; // @deprecated : 상품이미지를 기본 안드로이드 아이콘을 이미지로 적용
    private String item_name;
    private String item_price;

    public WishItem() {
    }

    public WishItem(int item_image, String item_name, String item_price) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_price = item_price;
    }

    /* @deprecated : Drawable 타입의 이미지로 테스트할 경우 주석 제거 후 사용
    public WishItem(Drawable item_image, String item_name, String item_price) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_price = item_price;
    }

    public Drawable getItem_image() {
        return item_image;
    }

    public void setItem_image(Drawable item_image) {
        this.item_image = item_image;
    }*/

    public int getItem_image() {
        return item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_price() {
        return item_price;
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
}
