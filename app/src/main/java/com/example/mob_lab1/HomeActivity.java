package com.example.mob_lab1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeActivity extends AppCompatActivity {
    private static final String URL_DATA = "https://us-central1-globo-a0b72.cloudfunctions.net/test";
    private List<Trip> tripList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trips);
        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG);

        ImageView imageView1 = findViewById(R.id.image);


        recyclerView = findViewById(R.id.recyclerView);
        tripAdapter = new TripAdapter(tripList);

        RecyclerView.LayoutManager tLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(tLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(tripAdapter);


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tripAdapter.notifyDataSetChanged();
                loadRecyclerData(new onLoadListener() {
                    @Override
                    public void onLoadSuccess() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        manager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                snackBar.dismiss();
                loadRecyclerData(null);
                Log.d("tag", "onNetwork available");
            }

            @Override
            public void onLosing(@NonNull Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
                Log.d("tag", "onNetwork losing");
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                snackBar.show();
                Log.d("tag", "onNetwork lost");
            }
        });
        loadRecyclerData(null);

    }

    public void loadRecyclerData(final onLoadListener listener) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("flights");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Trip trip = new Trip(
                                        object.getString("name"),
                                        object.getString("come_from"),
                                        object.getString("come_to"),
                                        object.getString("avia_company"),
                                        object.getString("speed"),
                                        object.getString("registration_info"),
                                        object.getString("flight_distance"),
                                        object.getString("img")
                                );
                                tripList.add(trip);
                            }
                            tripAdapter = new TripAdapter(tripList, getApplicationContext());
                            recyclerView.setAdapter(tripAdapter);
                            Log.d("tag", "data loaded successful");
                            if (listener != null) {
                                listener.onLoadSuccess();
                            }
                        } catch (JSONException e) {
                            Log.d("tag", "exception " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(findViewById(android.R.id.content), "Download error", Snackbar.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    interface onLoadListener {
        void onLoadSuccess();
    }
}
