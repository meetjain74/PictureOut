package com.designyourjourney.pictureout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.designyourjourney.pictureout.db.AppDatabase;
import com.designyourjourney.pictureout.db.AppRepository;
import com.designyourjourney.pictureout.db.Plan;
import com.designyourjourney.pictureout.db.PlanDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MakePlan extends AppCompatActivity {
    private ArrayList<City> cities;
    private EditText plan_name;
    private AutoCompleteTextView citydropdown;
    private TextView start_date;
    private DatePicker datePicker_start;
    private TextView end_date;
    private  DatePicker datePicker_end;
    private TextView add_destination;
    private LinearLayout layout;
    private Button submit;
    private ArrayList<City> citiesSelected=new ArrayList<>();
    private Calendar dateStart=Calendar.getInstance();
    private Calendar dateEnd=Calendar.getInstance();
    private City currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan);
        getData(); //To get data through intent
        plan_name=findViewById(R.id.planName);

        Log.d("DATE: ",dateStart+" "+dateEnd);

        cities = new ArrayList<>();
        initialise_data();

        // Get all attributes and add corresponding properties to it
        start_date = findViewById(R.id.startDate);
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate();
            }
        });

        end_date=findViewById(R.id.endDate);
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=start_date.getText().toString();
                if (text==null || text.length()==0) {
                    Toast.makeText(MakePlan.this, "Enter start date first", Toast.LENGTH_SHORT).show();
                } else {
                    setEndDate();
                }
            }
        });

        add_destination=findViewById(R.id.addDestination);
        add_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfPreviousFieldsFilled()) {
                    addDestination();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Already available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        layout=findViewById(R.id.AddLayout);

        submit=findViewById(R.id.create_plan);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getCitiesSelected()) {
                    Toast.makeText(MakePlan.this, "Enter a valid city", Toast.LENGTH_SHORT).show();
                }
                else if (checkAllDetailsEntered()) {
                    addPlan();
                    Intent intent=new Intent(getApplicationContext(),IdealisePlan.class);
                    passData(intent);
                    startActivity(intent);
                } else {
                    Toast.makeText(MakePlan.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData() {
        Intent intent=getIntent();
        currentCity= (City) intent.getSerializableExtra("com.designyourjourney.pictureout.MainActivity.CURRENTLOCATION");
    }

    private void initialise_data() {
        //Set up a progress dialog until the response arrives
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Create your own plan");
        progressDialog.show();

        //  for stop dialog dismiss on click hardware back button
        progressDialog.setCancelable(false);
        // for stop dialog dismiss on click screen any where
        progressDialog.setCanceledOnTouchOutside(false);

        // Creating a request queue and adding json request to the given URL
        String url = "https://raw.githubusercontent.com/dr5hn/countries-states-cities-database/master/countries%2Bcities.json";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject data = null;
                            String country;
                            JSONArray list;
                            JSONObject cityData;
                            try {
                                data = response.getJSONObject(i);
                                country = data.getString("name");
                                list = data.getJSONArray("cities");
                                for (int j = 0; j < list.length(); j++) {
                                    City temp = new City();
                                    cityData = list.getJSONObject(j);
                                    temp.setId(++count);
                                    temp.setCountry(country);
                                    temp.setName(cityData.getString("name"));
                                    temp.setLatitude(Double.parseDouble(cityData.getString("latitude")));
                                    temp.setLongitude(Double.parseDouble(cityData.getString("longitude")));
                                    cities.add(temp);
                                    //Log.d("Temp: ", String.valueOf(cities));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                        setFields();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FetchCity", "Error: " + error);
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void setFields() {
        citydropdown = findViewById(R.id.cityListDropdown);
        Log.d("Cities Retrieved", String.valueOf(cities));

        // Set autocomplete values to text view using adapter
        setCityAdapter(citydropdown);
    }

    private void setCityAdapter(AutoCompleteTextView textView) {
        ArrayAdapter<City> adapter = new ArrayAdapter<>
                (getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cities);
        textView.setAdapter(adapter);

        // Set tag as the city id to the corresponding textview
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity= (City) parent.getItemAtPosition(position);
                textView.setTag(selectedCity.getId());
            }
        });
    }

    private void setStartDate() {
        int day=dateStart.get(Calendar.DAY_OF_MONTH);
        int month=dateStart.get(Calendar.MONTH);
        int year=dateStart.get(Calendar.YEAR);

        DatePickerDialog start=new DatePickerDialog(
                this,R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateStart.set(year,month,dayOfMonth);
                        datePicker_start=new DatePicker(getApplicationContext());
                        datePicker_start.init(year,month,dayOfMonth,null);
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month,dayOfMonth-1);
                        int week_number=gregorianCalendar.get(Calendar.DAY_OF_WEEK);
                        String week=getWeek(week_number);
                        String date=week+" "+dayOfMonth+"/"+month+"/"+(year%100);
                        start_date.setText(date);

                        // If start date becomes larger than end date (after update) ask user to set it again
                        if (dateStart.compareTo(dateEnd)>0) {
                            end_date.setText("");
                        }
                    }
                },
                year,month,day
        );
        //Disable date before today
        start.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Set date to previously selected date
        if (datePicker_start != null) {
            start.updateDate(datePicker_start.getYear(),datePicker_start.getMonth(),datePicker_start.getDayOfMonth());
        }
        start.show();
    }

    private void setEndDate() {
        int day=dateEnd.get(Calendar.DAY_OF_MONTH);
        int month=dateEnd.get(Calendar.MONTH);
        int year=dateEnd.get(Calendar.YEAR);

        DatePickerDialog end=new DatePickerDialog(
                this,R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEnd.set(year,month,dayOfMonth);
                        datePicker_end=new DatePicker(getApplicationContext());
                        datePicker_end.init(year,month,dayOfMonth,null);
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month,dayOfMonth-1);
                        int week_number=gregorianCalendar.get(Calendar.DAY_OF_WEEK);
                        String week=getWeek(week_number);
                        String date=week+" "+dayOfMonth+"/"+month+"/"+(year%100);
                        end_date.setText(date);
                    }
                },
                year,month,day
        );
        //Disable date before today
        end.getDatePicker().setMinDate(dateStart.getTimeInMillis());
        // Set date to previously selected date
        if (datePicker_end != null) {
            end.updateDate(datePicker_end.getYear(),datePicker_end.getMonth(),datePicker_end.getDayOfMonth());
        }
        end.show();
    }

    private String getWeek(int n) {
        switch(n) {
            case 1: return "Mon"; //Monday
            case 2: return "Tue"; //Tuesday
            case 3: return "Wed"; //Wednesday
            case 4: return "Thu"; //Thursday
            case 5: return "Fri"; //Friday
            case 6: return "Sat"; //Saturday
            case 7: return "Sun"; //Sunday
            default: return "Error"; //Invalid day
        }
    }

    private void addDestination() {
        View newDestination=getLayoutInflater().inflate(R.layout.add_destination, null,false);
        AutoCompleteTextView textView=(AutoCompleteTextView) newDestination.findViewById(R.id.cityLinearLayout);
        ImageView close=(ImageView) newDestination.findViewById(R.id.close);

        // Set autocomplete values to text view using adapter
        setCityAdapter(textView);

        // Delete the textview on close button clicked
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDestination(newDestination);
            }
        });

        layout.addView(newDestination);
    }

    private void removeDestination(View v) {
        layout.removeView(v);
    }

    private boolean checkIfPreviousFieldsFilled() {
        String text;
        text=citydropdown.getText().toString();
        if (text == null || text.equals("")) {
            return false;
        }

        for (int i=0;i<layout.getChildCount();i++) {
            View destination = layout.getChildAt(i);
            AutoCompleteTextView textView = (AutoCompleteTextView) destination.findViewById(R.id.cityLinearLayout);
            text = textView.getText().toString();
            if (text == null || text.equals("")) {
                return false;
            }
        }

        return true;
    }

    // If all valid cities are entered it adds those cities to the selected cities array list and returns true
    private boolean getCitiesSelected() {
        citiesSelected.clear();
        String text;
        text=citydropdown.getText().toString();
        int tag;
        if (text!=null && text.length()!=0) {
            // Check if tag is set to the textview
            if (citydropdown.getTag()==null) {
                return false;
            }
            tag= (int) citydropdown.getTag();
            // Add the corresponding city to list
            citiesSelected.add(cities.get(tag-1));
        }

        for (int i=0;i<layout.getChildCount();i++) {
            View destination = layout.getChildAt(i);
            AutoCompleteTextView textView = (AutoCompleteTextView) destination.findViewById(R.id.cityLinearLayout);
            text = textView.getText().toString();
            if (text != null && text.length()!=0) {
                // Check if tag is set to the textview
                if (textView.getTag()==null) {
                    return false;
                }
                tag= (int) textView.getTag();
                // Add the corresponding city to list
                citiesSelected.add(cities.get(tag-1));
            }
        }

        return true;
    }

    private boolean checkAllDetailsEntered() {
        String text;
        text=plan_name.getText().toString();
        if (text == null || text.equals("")) {
            return false;
        }

        text=citydropdown.getText().toString();
        if (text == null || text.equals("")) {
            return false;
        }

        text=start_date.getText().toString();
        if (text == null || text.equals("")) {
            return false;
        }

        text=end_date.getText().toString();
        if (text == null || text.equals("")) {
            return false;
        }

        return true;
    }

    private void passData(Intent intent) {
        intent.putExtra("com.designyourjourney.pictureout.CITIES_SELECTED",citiesSelected);
        intent.putExtra("com.designyourjourney.pictureout.PLAN_NAME",plan_name.getText().toString());
        intent.putExtra("com.designyourjourney.pictureout.START_DATE",start_date.getText().toString());
        intent.putExtra("com.designyourjourney.pictureout.END_DATE",end_date.getText().toString());
        intent.putExtra("com.designyourjourney.pictureout.MakePlan.CURRENTLOCATION",currentCity);
    }

    private void addPlan() {
        Plan plan = new Plan();
        plan.setPlanName(plan_name.getText().toString());
        plan.setStartDate(start_date.getText().toString());
        plan.setEndDate(end_date.getText().toString());
        plan.setDestinations(citiesSelected);

        AppRepository appRepository = new AppRepository(getApplicationContext());
        appRepository.insertPlan(plan);
    }
}