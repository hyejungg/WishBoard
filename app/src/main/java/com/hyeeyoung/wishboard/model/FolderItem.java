package com.hyeeyoung.wishboard.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;


public class FolderItem implements Parcelable {
    private int folder_id; // @params : db에 저장될 폴더 번호
    private int folder_image; // @params : folder_id에 매칭되는 카테고리 사진
    private String folder_name; // @params : 카테고리명 (DB 통신 O)
    private int item_count; // @params : 카테고리 내 아이템 항목 수 (DB 통신 X / 전달받은 값들의 합만 구하여 출력 시 이용)

    public FolderItem() {
    }

    public FolderItem(int folder_id, int folder_image, String folder_item, int folder_count) {
        this.folder_id = folder_id;
        this.folder_image = folder_image;
        this.folder_name = folder_item;
        this.item_count = folder_count;
    }

    protected FolderItem(Parcel in) {
        folder_id = in.readInt();
        folder_image = in.readInt();
        folder_name = in.readString();
        item_count = in.readInt();
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


    @NonNull
    @Override
    public String toString() {
        return "FolderItem{" +
                "folder_id='" + folder_id + '\'' +
                ", folder_image='" + folder_image + '\'' +
                ", folder_name='" + folder_name + '\'' +
                ", item_count='" + item_count +
                '}';
    }

    /**
     * @param : Parcelable 인터페이스의 메소드 및 변경생성자
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.folder_id);
        parcel.writeInt(this.folder_image);
        parcel.writeString(this.folder_name);
        parcel.writeInt(this.item_count);
    }

    public static final Creator<FolderItem> CREATOR = new Creator<FolderItem>() {
        @Override
        public FolderItem createFromParcel(Parcel in) {
            return new FolderItem(in);
        }

        @Override
        public FolderItem[] newArray(int size) {
            return new FolderItem[size];
        }
    };
}

