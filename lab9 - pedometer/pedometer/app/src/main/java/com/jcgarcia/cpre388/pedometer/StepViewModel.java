package com.jcgarcia.cpre388.pedometer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StepViewModel extends ViewModel {
    private MutableLiveData<Float> stepLiveData = new MutableLiveData<>();
    private final static Float RESET_COUNTER = 0f;

    public StepViewModel(){
        stepLiveData.setValue(RESET_COUNTER);
    }

    public void storeData(Float steps){
        stepLiveData.setValue(steps);
    }

    public void release(){
        stepLiveData.setValue(RESET_COUNTER);
    }

    public MutableLiveData<Float> getStepLiveData(){
        return stepLiveData;
    }

}
