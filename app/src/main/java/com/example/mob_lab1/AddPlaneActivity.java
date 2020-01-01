package com.example.mob_lab1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPlaneActivity extends AppCompatActivity {
    EditText nameEt;
    EditText aviaEt;
    EditText fromEt;
    EditText toEt;
    EditText speedEt;
    EditText distanceEt;
    EditText registrationEt;

    Button addBtn;

    ImageView photoIv;

    ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plane);
        nameEt = findViewById(R.id.name_et);
        fromEt = findViewById(R.id.from_et);
        toEt = findViewById(R.id.to_et);
        speedEt = findViewById(R.id.speed_et);
        distanceEt = findViewById(R.id.distance_et);
        aviaEt = findViewById(R.id.avia_company_et);
        addBtn = findViewById(R.id.add_btn);
        registrationEt = findViewById(R.id.registration_et);
        photoIv = findViewById(R.id.photo_iv);
        Picasso.get().load("https://www.telegraph.co.uk/content/dam/Travel/2018/January/white-plane-sky.jpg?imwidth=450")
                .into(photoIv);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString();
                String from = fromEt.getText().toString();
                String to = toEt.getText().toString();
                String speed = speedEt.getText().toString();
                String distance = distanceEt.getText().toString();
                String aviaCompany = aviaEt.getText().toString();
                String registration = registrationEt.getText().toString();

                if (name.isEmpty()) {
                    nameEt.setError("Введіть назву");
                } else if (aviaCompany.isEmpty()) {
                    distanceEt.setError("Введіть назву авіакомпанії");
                } else if (from.isEmpty()) {
                    fromEt.setError("Введіть адресу з");
                } else if (to.isEmpty()) {
                    toEt.setError("Введіть адресу куди");
                } else if (registration.isEmpty()) {
                    distanceEt.setError("Введіть інфо про реєстрацію");
                } else if (speed.isEmpty()) {
                    speedEt.setError("Введіть швидкість");
                } else if (distance.isEmpty()) {
                    distanceEt.setError("Введіть відстань");
                } else {
                    Trip trip = new Trip(name, from, to, aviaCompany, speed, registration, distance,
                            "https://www.telegraph.co.uk/content/dam/Travel/2018/January/white-plane-sky.jpg?imwidth=450");
                    if (service == null) {
                        createService();
                    }
                    final ProgressDialog progressDialog = new ProgressDialog(AddPlaneActivity.this);
                    progressDialog.setTitle("Upload new Item");
                    progressDialog.show();
                    service.addNewPlane(trip).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            progressDialog.dismiss();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(AddPlaneActivity.this, "Невідома помилка", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });
    }

    private void createService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://backend-261116.appspot.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }
}
