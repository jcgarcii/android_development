package edu.iastate.cpre388.findmyfriends;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FriendDao {
    @Query("SELECT * FROM friend")
    LiveData<List<Friend>> getAll();

    @Insert
    public void insert(Friend friend);

    @Update
    public void update(Friend friend);

    @Delete
    public void delete(Friend friend);
}
