package com.example.finalproject.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;




public class TravelersAdapter extends RecyclerView.Adapter<TravelersAdapter.TravelersViewHolder> {

    public interface ITraveler {
        boolean isMarkedTraveler(User user);
        void markTraveler(User user, boolean marked);
    }
    private List<User> travelers;
    private ITraveler iTraveler;
    public TravelersAdapter(List<User> travelers, ITraveler iTraveler) {
        this.travelers = travelers;
        this.iTraveler = iTraveler;
    }

    @NonNull
    @Override
    public TravelersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.traveler_item,
                parent,
                false
        );
        return new TravelersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelersViewHolder holder, int position) {
        User traveler = travelers.get(position);
        holder.bind(traveler, iTraveler);
    }

    @Override
    public int getItemCount() {
        return travelers.size();
    }

    static class TravelersViewHolder extends RecyclerView.ViewHolder {
        private final ImageView travelerIv;
        private final TextView travelerNameTv;
        private final CheckBox travelerChecked;
        public TravelersViewHolder(@NonNull View itemView) {
            super(itemView);
            this.travelerIv = itemView.findViewById(R.id.travelerIv);
            this.travelerNameTv = itemView.findViewById(R.id.travelerName);
            this.travelerChecked = itemView.findViewById(R.id.travelerChecked);
        }

        public void bind(User user,ITraveler iTraveler) {
            this.travelerNameTv.setText(user.getName());
            Picasso.get().load(user.getEmail()).into(travelerIv);
            boolean isMarked = iTraveler.isMarkedTraveler(user);
            this.travelerChecked.setChecked(isMarked);
            this.travelerChecked.setOnCheckedChangeListener((compoundButton, marked) -> {
                iTraveler.markTraveler(user, marked);
            });
        }
    }

}
