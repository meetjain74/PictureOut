package com.designyourjourney.pictureout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlanDao {

    @Query("SELECT * FROM plans_table")
    List<Plan> getAllPlans();

    @Insert
    void insertPlan(Plan plan);
}
