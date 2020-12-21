package com.nic.areaofficer.demo2;

import android.arch.persistence.room.Room;
import android.content.Context;



public class DatabaseClient2 {

    private Context mCtx;
    private static DatabaseClient2 mInstance;

    //our app database object
    private AppDatabase2 appDatabase;

    private DatabaseClient2(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase2.class, "MyToDos1").build();
    }

    public static synchronized DatabaseClient2 getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient2(mCtx);
        }
        return mInstance;
    }

    public AppDatabase2 getAppDatabase() {
        return appDatabase;
    }
}