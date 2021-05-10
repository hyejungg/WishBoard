package com.hyeeyoung.wishboard.item;

import androidx.annotation.NonNull;

public class FoldersItem {
    private int folder_id; // @params : 카테고리 사진 번호 (DB 통신 O) @TODO : (key-value)형태의 자료구조로 변경?
    private int folder_image; // @params : folder_id에 매칭되는 카테고리 사진
    private String folder_name; // @params : 카테고리명 (DB 통신 O)
    private int item_count; // @params : 카테고리 내 아이템 항목 수 (DB 통신 X / 전달받은 값들의 합만 구하여 출력 시 이용)

    public FoldersItem() {
        //super();
    }

    //    @see : count는 지워도 될 것 같기도 함.. 여기서 db 통신 후에 값만 개수만 카운팅하도록 ...?
    public FoldersItem(int folder_id, int folder_image, String folder_item, int folder_count) {
        this.folder_id = folder_id;
        this.folder_image = folder_image;
        this.folder_name = folder_item;
        this.item_count = folder_count;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public int getFolder_image() {
        return folder_image;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public void setFolder_image(int folder_image) {
        this.folder_image = folder_image;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

}

