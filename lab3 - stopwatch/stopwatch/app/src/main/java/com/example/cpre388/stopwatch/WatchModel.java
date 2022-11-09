package com.example.cpre388.stopwatch;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WatchModel extends ViewModel {
    private MutableLiveData<Long> watchLiveData = new MutableLiveData<>();
    private Long mwatchLiveData = 0L;

    public WatchModel(){}

    public void calculate(Long currentTime){
        watchLiveData.setValue(currentTime);
    }

    public void storeData(Long Time){
        mwatchLiveData = Time;
    }

    public Long getPreviousValue(){
        return mwatchLiveData;
    }

    public MutableLiveData<Long> getWatchLiveData(){
        return watchLiveData;
    }
}
