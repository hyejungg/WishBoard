package com.hyeeyoung.wishboard.model;

public class FolderListItem {
    private int folder_image;
    private String folder_name;
    private boolean checkbox;

    public void FolderListItem(){}

    public void FolderListItem(int folder_image, String folder_name, boolean checkbox){
        this.folder_image = folder_image;
        this.folder_name = folder_name;
        this.checkbox = checkbox;
    }

    public int getFolderImage(){return folder_image;}
    public void setFolderImage(int folder_image){this.folder_image = folder_image;}
    public String getFolderName(){return folder_name;}
    public void setFolderName(String folder_name){this.folder_name = folder_name;}
    public boolean isCheckbox(){return checkbox; }
    public void setCheckboxState(boolean checkbox){this.checkbox = checkbox;}
}
