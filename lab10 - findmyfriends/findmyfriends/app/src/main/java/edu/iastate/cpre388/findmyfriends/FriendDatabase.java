package edu.iastate.cpre388.findmyfriends;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Friend.class}, version = 1)
public abstract class FriendDatabase extends RoomDatabase {
    public abstract FriendDao friendDao();
}
