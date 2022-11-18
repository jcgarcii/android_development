package edu.iastate.cpre388.findmyfriends;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executor;

public class FMFViewModel extends ViewModel {

    private FriendDatabase db;
    private Executor executor;

    public FMFViewModel(Context context, Executor executor) {
        db = getFriendDatabase(context);
        this.executor = executor;
    }

    private FriendDatabase getFriendDatabase(Context context) {
        if(db == null) {
            db = Room.databaseBuilder(context, FriendDatabase.class, "friends")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }

    public void addFriend(Friend friend) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.friendDao().insert(friend);
            }
        });
    }

    public void deleteFriend(Friend friend) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.friendDao().delete(friend);
            }
        });
    }

    public void updateFriend(Friend friend) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.friendDao().update(friend);
            }
        });
    }

    public LiveData<List<Friend>> getFriendsList() {
        return db.friendDao().getAll();
    }
}