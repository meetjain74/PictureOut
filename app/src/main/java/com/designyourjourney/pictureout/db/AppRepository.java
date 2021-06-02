package com.designyourjourney.pictureout.db;

import android.content.Context;
import android.os.AsyncTask;

public class AppRepository {
    private AppDatabase db;

    Context context;

    public AppRepository(Context context) {
        this.context=context;
        db=AppDatabase.getInstance(context);
    }

    public void insertPlan(Plan plan) {
//        new AsyncTask<Void,Void,Void>(){
//            @Override
//            protected Void doInBackground(Void... voids) {
//                db.planDao().insertPlan(plan);
//                return null;
//            }
//        }.execute();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.planDao().insertPlan(plan);
            }
        });
    }


}
