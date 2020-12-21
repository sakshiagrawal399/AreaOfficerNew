package com.nic.areaofficer.demo2;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Task2.class}, version = 1)
public abstract class AppDatabase2 extends RoomDatabase {
    public abstract TaskDao2 taskDao2();
}