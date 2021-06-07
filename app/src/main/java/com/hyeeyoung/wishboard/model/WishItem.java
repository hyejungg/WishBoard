package com.hyeeyoung.wishboard.model;

/**
 * @param : 서버와 통신하여 DB에 데이터를 넣기 위해 사용되는 Class
 */

public class WishItem {
    public String user_id;
    public String folder_id;
    public String item_image; //@param : 추후 DB에서 이미지링크 가져올 경우 사용할 변수
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


    public String getItem_image() {
        return item_image;
    }
    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }
    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_url() {
        return item_url;
    }
    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public String getItem_memo() {
        return item_memo;
    }
    public void setItem_memo(String item_memo) {
        this.item_memo = item_memo;
    }


    @Override
    public String toString() {
        return "WishItem{" +
                "user_id='" + user_id + '\'' +
                ", folder_id='" + folder_id + '\'' +
                ", item_image='" + item_image + '\'' +
                ", item_name='" + item_name + '\'' +
                ", item_price='" + item_price + '\'' +
                ", item_url='" + item_url + '\'' +
                ", item_memo='" + item_memo + '\'' +
                '}';
    }
}
