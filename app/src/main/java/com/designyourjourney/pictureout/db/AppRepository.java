package com.designyourjourney.pictureout.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {
    private AppDatabase db;

    Context context;

    List<City> cities = new ArrayList<City>();

    public AppRepository(Context context) {
        this.context=context;
        db=AppDatabase.getInstance(context);
    }

    public void insertPlan(Plan plan) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Check whether plan name already exists
                // If already exists change plan name to plan name + (integer number)
                List<String> list = db.planDao().getMatchedPlanNames(plan.getPlanName());
                if (list.size()!=0) {
                    int count = getCorrectCount(list,plan);
                    plan.setPlanName(plan.getPlanName()+"("+(count+1)+")");
                }
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

    public Plan copyPlan(Plan plan) {
        Plan newPlan = new Plan(plan);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                insertPlan(newPlan);
            }
        });
        return newPlan;
    }

    private int getCorrectCount(List<String> strings,Plan newPlan) {
        String temp=newPlan.getPlanName();
        for (int i=0;i<strings.size();i++) {
            temp = strings.get(i);
            if (temp.equals(newPlan.getPlanName())) {
                break;
            }
            if (!containsMoreBrackets(temp.substring(newPlan.getPlanName().length()+1))) {
                break;
            }
        }
        int count=0;
        if (temp.equals(newPlan.getPlanName())) {
            count = 0;
        }
        else {
            Log.d("Last matched name is : ", temp);
            count = Integer.parseInt(temp.substring(newPlan.getPlanName().length() + 1, temp.length() - 1));
        }

        Log.d("Count is ", String.valueOf(count));
        return count;
    }

    private boolean containsMoreBrackets(String word) {
        for (int i=0;i<word.length();i++) {
            if (word.charAt(i)=='(') {
                return true;
            }
        }
        return false;
    }

    public List<City> getAllCities() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                cities = db.cityDao().getAllCities();
            }
        });
        return cities;
    }

    public void insertCity(City city) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.cityDao().insertCity(city);
            }
        });
    }
}
