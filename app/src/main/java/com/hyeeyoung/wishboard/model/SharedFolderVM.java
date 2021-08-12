package com.hyeeyoung.wishboard.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedFolderVM extends ViewModel {

    // @param : update 여부를 공유하는 변수
    private MutableLiveData<Boolean> isUpdated = new MutableLiveData<Boolean>();

    public LiveData<Boolean> getIsUpdated(){ // @param : getter
        return isUpdated;
    }

    public void setIsUpdated(Boolean updated){ // @param : setter
        isUpdated.setValue(updated);
    }
}
