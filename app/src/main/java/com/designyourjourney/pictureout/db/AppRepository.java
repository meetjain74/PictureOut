package com.designyourjourney.pictureout.db;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class AppRepository {
    private AppDatabase db;

    Context context;

    public AppRepository(Context context) {
        this.context=context;
        db=AppDatabase.getInstance(context);
    }

    public void insertPlan(Plan plan) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.planDao().insertPlan(plan);
            }
        });
    }

    public List<Plan> getMyPlans() {
        List<Plan> myPlans=db.planDao().getAllPlans();
        return myPlans;
    }

    public void deletePlan(Plan plan) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.planDao().deletePlan(plan);
            }
        });
    }

}
