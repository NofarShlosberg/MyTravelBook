package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Travel;

import java.util.List;

public class TravelsAdapter extends RecyclerView.Adapter<TravelsAdapter.TravelsViewHolder> {

    @NonNull
    @Override
    public TravelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.travel_item,parent,false);
       return new TravelsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelsViewHolder holder, int position) {
        Travel next = travels.get(position);
        holder.bind(next,onTravelClicked);
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }

    public interface OnTravelClicked {
        void onTravelClicked(Travel travel);
    }


    private List<Travel> travels;
    private OnTravelClicked onTravelClicked;
    public TravelsAdapter(List<Travel> travelList, OnTravelClicked onTravelClicked) {
        this.travels = travelList;
        this.onTravelClicked = onTravelClicked;
    }

    static class TravelsViewHolder extends RecyclerView.ViewHolder {


        private TextView destinationTv;
        public TravelsViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationTv = itemView.findViewById(R.id.travelCard_destinationTv);
        }

        public void bind(Travel travel,OnTravelClicked onTravelClicked) {
            itemView.setOnClickListener(view -> onTravelClicked.onTravelClicked(travel));
            destinationTv.setText(travel.getDestination());
        }
    }
}
