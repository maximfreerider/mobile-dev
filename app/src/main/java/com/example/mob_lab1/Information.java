package com.example.mob_lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Information extends AppCompatActivity {
    private ImageView imageView_inf;
    public TextView come_from_inf, come_to_inf, avia_company_inf, speed_inf, flight_distance_inf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Trip trip = getIntent().getParcelableExtra("trip");

        come_from_inf = findViewById(R.id.come_from_inf);
        come_to_inf = findViewById(R.id.come_to_inf);
        avia_company_inf = findViewById(R.id.avia_company_inf);
        speed_inf = findViewById(R.id.speed_inf);
        flight_distance_inf = findViewById(R.id.flight_distance_inf);
        imageView_inf = findViewById(R.id.image_inf);

        come_to_inf.setText("come_to: " + trip.getCome_to());
        come_from_inf.setText("come_from: " + trip.getCome_from());
        avia_company_inf.setText("avia_company: " + trip.getAvia_company());
        speed_inf.setText("speed: " + trip.getSpeed());
        flight_distance_inf.setText("flight_distance: " + trip.getFlight_distance());
        Picasso.get().load(trip.getImg()).into(imageView_inf);



    }
}
