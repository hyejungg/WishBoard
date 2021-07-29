package com.hyeeyoung.wishboard.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;


public class FolderItem implements Parcelable {
    private String user_id; // @param : user_id
    private String folder_id; // @param : db에 저장될 폴더 번호
    private int folder_image; // @param : 카테고리 사진
    private String folder_name; // @param : 카테고리명
    private int item_count; // @param : 카테고리 내 아이템 항목 수

    public FolderItem() {
    }

    public FolderItem(String folder_id, int folder_image, String folder_item, int folder_count) {
        this.folder_id = folder_id;
        this.folder_image = folder_image;
        this.folder_name = folder_item;
        this.item_count = folder_count;
    }

    public String getFolder_id() {
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

    public void setFolder_id(String folder_id) {
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


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    @NonNull
    @Override
    public String toString() {
        return "FolderItem{" +
                "user_id='" + user_id + '\'' +
                ", folder_id='" + folder_id + '\'' +
                ", folder_image='" + folder_image + '\'' +
                ", folder_name='" + folder_name + '\'' +
                ", item_count='" + item_count +
                '}';
    }

    /**
     * @param : Parcelable 인터페이스의 메소드 및 변경생성자
     * @see : intent로 객체 및 ArrayList로 전달 시 사용
     */

    @Override
    public int describeContents() {
        return 0;
    }

    protected FolderItem(Parcel in) {
        folder_id = in.readString();
        folder_image = in.readInt();
        folder_name = in.readString();
        item_count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.folder_id);
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

