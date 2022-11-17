package com.jcgarcia.cpre388.pedometer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StepViewModel extends ViewModel {
    private MutableLiveData<Long> stepLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> sensorLiveData = new MutableLiveData<>();

    private final static Long RESET_COUNTER = 0L;

    public StepViewModel(){}

    public void step_storeData(Long steps){
        stepLiveData.setValue(steps);
    }
    public void sen_storeData(Long steps){
        sensorLiveData.setValue(steps);
    }

    public void release(){
        stepLiveData.setValue(RESET_COUNTER);
        sensorLiveData.setValue(RESET_COUNTER);
    }

    public MutableLiveData<Long> getStepLiveData(){
        return stepLiveData;
    }

    public MutableLiveData<Long> getSensorLiveData() {
        return sensorLiveData;
    }
}
