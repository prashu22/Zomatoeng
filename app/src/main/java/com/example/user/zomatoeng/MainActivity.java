package com.example.user.zomatoeng;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    double log, lat;
    ArrayList<model> al=new ArrayList<model>();
    private FusedLocationProviderClient mFusedLocationClient;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search=(EditText)findViewById(R.id.search);

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = "https://developers.zomato.com/api/v2.1/search?lat=" + 80.9441603 + "&lon=" +  26.9146251;
                Log.d("prashu", url);
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // response
                                try {
                                    Populate(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.d("ERROR", "error => " + error.toString());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user-key", "ac240c9c91739025e57e7e1af4cec64f");
                        params.put("Accept", "application/json");

                        return params;
                    }
                };
                queue.add(postRequest);
        Button btn=(Button)findViewById(R.id.filtertool);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dg=new Dialog(MainActivity.this);
                dg.setContentView(R.layout.filterdialog);
                final EditText city=dg.findViewById(R.id.city);
                final EditText cuisi=dg.findViewById(R.id.cuisines);
                Button btn2=(Button)dg.findViewById(R.id.filter);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String cityname=city.getText().toString();
                        final String cuisines=cuisi.getText().toString();
                        filter(cityname,cuisines);
                        dg.cancel();
                    }
                });
                dg.show();
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( (actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))){
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String url = "https://developers.zomato.com/api/v2.1/search?q="+search.getText().toString();
                    Log.d("prashu", url);
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // response
                                    try {
                                        Populate(response);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                    Log.d("ERROR", "error => " + error.toString());
                                }
                            }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user-key", "ac240c9c91739025e57e7e1af4cec64f");
                            params.put("Accept", "application/json");

                            return params;
                        }
                    };
                    queue.add(postRequest);

                    return true;
                }
                else{
                    return false;
                }
            }
        });

            }

    void Populate(JSONObject jsonObject) throws JSONException {
        ProgressBar pg=(ProgressBar)findViewById(R.id.prorecy);
        pg.setVisibility(View.VISIBLE);
        JSONArray restaurants=jsonObject.getJSONArray("restaurants");
        Log.d("prashu",jsonObject.toString());
        al.clear();
        for(int i=0;i<restaurants.length();i++)
        {
            JSONObject rest=restaurants.getJSONObject(i);
            JSONObject res=rest.getJSONObject("restaurant");
            Log.d("prashu",res.toString());
            String name=res.getString("name");
            Log.d("prashu", name);
            String img=res.getString("featured_image");
            JSONObject local=res.getJSONObject("location");
            String location=local.getString("locality_verbose");
            JSONObject rating=res.getJSONObject("user_rating");
            String u_rating=rating.getString("aggregate_rating");
            model m=new model(name,img,u_rating,location);
            al.add(m);
        }
        adapter adap=new adapter(this,al);
        RecyclerView recycler = findViewById(R.id.Recycler);
        recycler.setAdapter(adap);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layout);
        pg.setVisibility(View.GONE);
    }
    void filter(String city, final String cui)
    {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://developers.zomato.com/api/v2.1/locations?query="+city;
        Log.d("prashu", url);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                            fingcityId(response,cui);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", "ac240c9c91739025e57e7e1af4cec64f");
                params.put("Accept", "application/json");

                return params;
            }
        };
        queue.add(postRequest);

    }
    void fingcityId(final JSONObject res,String cui)
    {    int cityId=1;
        try {
        JSONArray loc = res.getJSONArray("location_suggestions");
        JSONObject ci=loc.getJSONObject(0);
       cityId=ci.getInt("city_id");
    } catch (JSONException e) {
        e.printStackTrace();
    }
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://developers.zomato.com/api/v2.1/search?entity_id="+cityId+"&entity_type=city&cuisines="+cui+"&count=15";
        Log.d("prashu", url);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Populate(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", "ac240c9c91739025e57e7e1af4cec64f");
                params.put("Accept", "application/json");

                return params;
            }
        };
        queue.add(postRequest);

    }
}