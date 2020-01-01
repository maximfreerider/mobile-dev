package com.example.mob_lab1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    private static final String URL_DATA = "https://backend-261116.appspot.com/plane/";
    private List<Trip> tripList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.uses_bt) {
            Intent intent = new Intent(HomeActivity.this, Profile.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trips);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager tLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(tLayoutManager);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecyclerData(new onLoadListener() {
                    @Override
                    public void onLoadSuccess() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        FloatingActionButton fab = findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AddPlaneActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!manager.isDefaultNetworkActive()) {
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
            }
        } else {
            loadRecyclerData(null);
        }
    }

    public void loadRecyclerData(final onLoadListener listener) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.d("tag", "loadRecyclerData");

        if (apiService == null) {
            createService();
        }
        apiService.getAllTrips().enqueue(new Callback<ServerResponce>() {
            @Override
            public void onResponse(Call<ServerResponce> call, Response<ServerResponce> response) {
                Log.d("tag", "onResponce");
                tripList = response.body().planes;
                tripAdapter = new TripAdapter(tripList, new TripAdapter.TripListener() {
                    @Override
                    public void onItemClick(Trip trip) {
                        Intent intent = new Intent(HomeActivity.this, Information.class);
                        intent.putExtra("trip", trip);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(tripAdapter);
                Log.d("tag", "data loaded successful");
                if (listener != null) {
                    listener.onLoadSuccess();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponce> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Download error", Snackbar.LENGTH_LONG).show();
                Log.d("tag", "onFailure " + t.getMessage());
            }
        });
    }

    private void createService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://backend-261116.appspot.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    interface onLoadListener {
        void onLoadSuccess();
    }

}
