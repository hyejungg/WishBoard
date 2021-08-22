package com.hyeeyoung.wishboard.config;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedItemVM extends ViewModel {
    // @param : update 여부를 공유하는 변수
    // @brief : 데이터를 감싸기 위한 wrapper클래스

    private MutableLiveData<Boolean> isUpdated = new MutableLiveData<Boolean>();
    private MutableLiveData<String> item_name = new MutableLiveData<String>();
    private MutableLiveData<String> item_price = new MutableLiveData<String>();
    public LiveData<String> getItem_name() {
        return item_name;
    }
    public LiveData<String> getItem_price() {
        return item_price;
    }
    public LiveData<Boolean> getIsUpdated(){ // @param : getter
        return isUpdated;
    }
    public void setItem_name(String name) {
        item_name.setValue(name);
    }
    public void setItem_price(String price) {
        item_price.setValue(price);
    }
}
