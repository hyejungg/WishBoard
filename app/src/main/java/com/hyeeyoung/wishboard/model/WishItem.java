package com.hyeeyoung.wishboard.model;

public class WishItem {
    public String user_id;
    public String folder_id;
    public String item_image; //@param : 추후 DB에서 이미지링크 가져올 경우 사용할 변수
    public int item_img; // @deprecated : 상품이미지를 테스트용 자산으로 적용
    public String item_name;
    public String item_price;
    public String item_url;
    public String item_memo;

    public WishItem() {
    }

    //@brief : 추후 folder_id 서버에서 결과 값 받아서 사용, 타입은 서버에서 형변환 예정
    public WishItem(String user_id, String folder_id, String item_image, String item_name, String item_price, String item_url, String item_memo) {
        this.user_id = user_id;
        this.folder_id = folder_id;
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_url = item_url;
        this.item_memo = item_memo;
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

    public int getItem_img() {
        return item_img;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_price() {
        return item_price;
    }
//
//    public void setItem_img(int item_img) {
//        this.item_img = item_img;
//    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    @Override
    public String toString() {
        return "WishItem{" +
                "user_id='" + user_id + '\'' +
                ", folder_id='" + folder_id + '\'' +
                ", item_image='" + item_image + '\'' +
                ", item_img=" + item_img +
                ", item_name='" + item_name + '\'' +
                ", item_price='" + item_price + '\'' +
                ", item_url='" + item_url + '\'' +
                ", item_memo='" + item_memo + '\'' +
                '}';
    }
}
