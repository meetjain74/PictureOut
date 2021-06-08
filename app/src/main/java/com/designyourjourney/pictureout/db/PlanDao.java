package com.designyourjourney.pictureout.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlanDao {

    @Query("SELECT * FROM plans_table ORDER BY planId DESC")
    List<Plan> getAllPlans();

    @Insert
    void insertPlan(Plan plan);

    @Delete
    void deletePlan(Plan plan);

    // Get last plan which corresponds to it
    // For concatenation of '%' with value || is used
    @Query("SELECT name FROM plans_table WHERE name  LIKE (:value || '%') ORDER BY name DESC")
    List<String> getMatchedPlanNames(String value);
}
