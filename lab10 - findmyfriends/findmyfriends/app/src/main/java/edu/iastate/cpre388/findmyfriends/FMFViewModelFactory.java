package edu.iastate.cpre388.findmyfriends;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

public class FMFViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    private Executor executor;

    public FMFViewModelFactory(Context context, Executor executor) {
        this.context = context;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FMFViewModel(context, executor);
    }
}
