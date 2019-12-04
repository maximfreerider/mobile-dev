package com.example.mob_lab1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {
    private List<Trip> tripList;
    private Context context;

    public TripAdapter(List<Trip> tripList, Context context) {
        this.tripList = tripList;
        this.context = context;
    }

    public TripAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView come_from, come_to, avia_company, speed, flight_distance;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            come_from = itemView.findViewById(R.id.come_from);
            come_to = itemView.findViewById(R.id.come_to);
            avia_company = itemView.findViewById(R.id.avia_company);
            speed = itemView.findViewById(R.id.speed);
            image = itemView.findViewById(R.id.image);
            flight_distance = itemView.findViewById(R.id.flight_distance);

        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iteam_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.come_to.setText(trip.getCome_to());
        holder.come_from.setText(trip.getCome_from());
        holder.avia_company.setText(trip.getAvia_company());
        holder.speed.setText("Speed: " + trip.getSpeed());
        holder.flight_distance.setText("distance :" + trip.getFlight_distance());
        Picasso.get().load(trip.getImg()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }


}
