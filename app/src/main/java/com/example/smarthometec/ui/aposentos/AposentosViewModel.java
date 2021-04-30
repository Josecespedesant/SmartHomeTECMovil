package com.example.smarthometec.ui.aposentos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AposentosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AposentosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is aposentos fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}