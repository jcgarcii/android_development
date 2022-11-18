package edu.iastate.cpre388.findmyfriends;

import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FMFApplication extends Application {
    public Executor executor = Executors.newSingleThreadExecutor();
}
