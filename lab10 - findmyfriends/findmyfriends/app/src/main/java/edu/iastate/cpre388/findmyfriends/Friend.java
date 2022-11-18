package edu.iastate.cpre388.findmyfriends;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Friend {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "bt_mac_addr")
    public String BTMacAddr;

    public Friend(String name, String BTMacAddr) {
        this.name = name;
        this.BTMacAddr = BTMacAddr;
    }
}
