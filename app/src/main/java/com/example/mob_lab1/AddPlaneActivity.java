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
    private EditText name;
    private EditText aviacompany;
    private EditText comeFrom;
    private EditText comeTo;
    private EditText speed;
    private EditText distance;
    private EditText registrationInfo;

    private Button addBtn;

    private ImageView photoIv;

    private ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plane);
        name = findViewById(R.id.name_et);
        comeFrom = findViewById(R.id.from_et);
        comeTo = findViewById(R.id.to_et);
        speed = findViewById(R.id.speed_et);
        distance = findViewById(R.id.distance_et);
        aviacompany = findViewById(R.id.avia_company_et);
        addBtn = findViewById(R.id.add_btn);
        registrationInfo = findViewById(R.id.registration_et);
        photoIv = findViewById(R.id.photo_iv);
        Picasso.get().load("https://www.telegraph.co.uk/content/dam/Travel/2018/January/white-plane-sky.jpg?imwidth=450")
                .into(photoIv);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name.getText().toString();
                String from = comeFrom.getText().toString();
                String to = comeTo.getText().toString();
                String speed = speed.getText().toString();
                String distance = distance.getText().toString();
                String aviaCompany = aviacompany.getText().toString();
                String registration = registrationInfo.getText().toString();

                if (name.isEmpty()) {
                    name.setError("Введіть назву");
                } else if (aviaCompany.isEmpty()) {
                    distance.setError("Введіть назву авіакомпанії");
                } else if (from.isEmpty()) {
                    comeFrom.setError("Введіть адресу з");
                } else if (to.isEmpty()) {
                    comeTo.setError("Введіть адресу куди");
                } else if (registration.isEmpty()) {
                    distance.setError("Введіть інфо про реєстрацію");
                } else if (speed.isEmpty()) {
                    speed.setError("Введіть швидкість");
                } else if (distance.isEmpty()) {
                    distance.setError("Введіть відстань");
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
