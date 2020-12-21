package com.nic.areaofficer.demo2;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao2 {

    @Query("SELECT * FROM task2")
    List<Task2> getAll();

    @Insert
    void insert(Task2 task2);

    @Delete
    void delete(Task2 task2);

    @Update
    void update(Task2 task2);

}