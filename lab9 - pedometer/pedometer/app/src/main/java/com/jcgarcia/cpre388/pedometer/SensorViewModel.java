package com.jcgarcia.cpre388.pedometer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SensorViewModel extends ViewModel {
    private MutableLiveData<Long> sensorLiveData = new MutableLiveData<>();
    private final static Long RESET_COUNTER = 0L;

    public SensorViewModel(){}

    public void storeData(Long steps){
        sensorLiveData.setValue(steps);
    }

    public void release(){
        sensorLiveData.setValue(RESET_COUNTER);
    }

    public MutableLiveData<Long> getSensorLiveData(){
        return sensorLiveData;
    }
}
