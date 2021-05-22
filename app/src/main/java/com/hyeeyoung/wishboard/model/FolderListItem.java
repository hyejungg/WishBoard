package com.hyeeyoung.wishboard.model;

public class FolderListItem {
    private int folder_image;
    private String folder_name;
    private boolean btn_check;

    public FolderListItem(){}

    public FolderListItem(int folder_image, String folder_name, boolean btn_check){
        this.folder_image = folder_image;
        this.folder_name = folder_name;
        this.btn_check = btn_check;
    }

    public int getFolderImage(){return folder_image;}
    public void setFolderImage(int folder_image){this.folder_image = folder_image;}
    public String getFolderName(){return folder_name;}
    public void setFolderName(String folder_name){this.folder_name = folder_name;}
    public boolean isChecked(){return btn_check; }
    public void setCheckedState(boolean btn_check){this.btn_check = btn_check;}
}
