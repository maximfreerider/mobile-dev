package com.example.mob_lab1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Information extends AppCompatActivity {
    private ImageView imageView_inf;
    public TextView come_from_inf, come_to_inf, avia_company_inf, speed_inf, flight_distance_inf;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        String tripId = getIntent().getStringExtra("id");
        String textData = getIntent().getStringExtra("textData");
        Trip trip = getIntent().getParcelableExtra("trip");

        if (trip != null) {
            updateTrip(trip);
        }

        if (textData != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Сповіщення")
                    .setMessage(textData)
                    .show();
        }

        come_from_inf = findViewById(R.id.come_from_inf);
        come_to_inf = findViewById(R.id.come_to_inf);
        avia_company_inf = findViewById(R.id.avia_company_inf);
        speed_inf = findViewById(R.id.speed_inf);
        flight_distance_inf = findViewById(R.id.flight_distance_inf);
        imageView_inf = findViewById(R.id.image_inf);

        if (tripId != null) {
            FirebaseFirestore.getInstance().collection("trips").document(tripId.trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Trip trip = task.getResult().toObject(Trip.class);


                    updateTrip(trip);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Information.this, "Невідома помилка", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void updateTrip(Trip trip) {
        come_to_inf.setText("come_to: " + trip.getCome_to());
        come_from_inf.setText("come_from: " + trip.getCome_from());
        avia_company_inf.setText("avia_company: " + trip.getAvia_company());
        speed_inf.setText("speed: " + trip.getSpeed());
        flight_distance_inf.setText("flight_distance: " + trip.getFlight_distance());
        Picasso.get().load(trip.getImg()).into(imageView_inf);
    }
}
