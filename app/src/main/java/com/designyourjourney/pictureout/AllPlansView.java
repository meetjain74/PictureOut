package com.designyourjourney.pictureout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.designyourjourney.pictureout.db.AppRepository;
import com.designyourjourney.pictureout.db.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllPlansView extends AppCompatActivity {
    private RecyclerView allMyPlansRecyclerView;
    private ArrayList<Plan> myPlans;
    private ShowAllPlansCustomAdapter customAdapter;
    private EditText search;
    private ArrayList<Plan> myPlans_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_plans_view);

        allMyPlansRecyclerView = findViewById(R.id.allMyPlans);
        getAllMyPlans();

        search = findViewById(R.id.searchPlans);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                filterPlans(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getAllMyPlans() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        allMyPlansRecyclerView.setLayoutManager(layoutManager);
        new LoadPlanTask().execute();
    }

    class LoadPlanTask extends AsyncTask<Void,Void,Void> {
        AppRepository appRepository;
        List<Plan> plans;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appRepository=new AppRepository(getApplicationContext());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            plans=appRepository.getMyPlans();
            myPlans=new ArrayList<>();
            myPlans_search=new ArrayList<>();

            myPlans.addAll(plans);
            myPlans_search.addAll(plans);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            customAdapter = new ShowAllPlansCustomAdapter(AllPlansView.this,myPlans);
            allMyPlansRecyclerView.setAdapter(customAdapter);
        }
    }


    // Filter plans by plan name or destination name
    public void filterPlans(String text) {
        if (text==null){
            return;
        }
        text=text.toLowerCase(Locale.getDefault());
        myPlans.clear(); // Clear the list
        if (text.length()==0) {
            // Load all data
            myPlans.addAll(myPlans_search);
        }
        else {
            // Load only those data matching the filter
            for (Plan plan: myPlans_search) {
                String name = plan.getPlanName().toLowerCase(Locale.getDefault());
                List<City> destinations=plan.getDestinations();
                if (name.contains(text) || listContainsText(destinations,text)) {
                    myPlans.add(plan);
                }
            }
            customAdapter.notifyDataSetChanged();
        }
    }

    private boolean listContainsText(List<City> dest,String text) {
        String nameOfCity;
        for (int i=0;i<dest.size();i++) {
            nameOfCity=dest.get(i).getName().toLowerCase(Locale.getDefault());
            if (nameOfCity.contains(text)) {
                return true;
            }
        }
        return false;
    }
}