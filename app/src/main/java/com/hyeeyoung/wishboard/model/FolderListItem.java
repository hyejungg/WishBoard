package com.hyeeyoung.wishboard.item;

public class FolderListItem {
    private int folder_image;
    private String folder_name;
    private int checkbox;

    public void FolderList(){}

    public void FolderList(int folder_image, String folder_name, int checkbox){
        this.folder_image = folder_image;
        this.folder_name = folder_name;
        this.checkbox = checkbox;
    }

    public int getFolderImage(){return folder_image;}
    public void setFolderImage(int folder_image){this.folder_image = folder_image;}
    public String getFolderName(){return folder_name;}
    public void setFolderName(String folder_name){this.folder_name = folder_name;}
    public int getCheckbox(){return checkbox; }
    public void setCheckbox(int checkbox){this.checkbox = checkbox;}
}
