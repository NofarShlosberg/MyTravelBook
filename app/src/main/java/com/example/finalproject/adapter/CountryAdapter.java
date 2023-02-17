package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<Integer> countryFlags;
    public CountryAdapter(List<Integer> countryFlags) {
        this.countryFlags = countryFlags;
    }


    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.residing_item,parent,false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Integer countryFlagResourceId = countryFlags.get(position);
        holder.bind(countryFlagResourceId);
    }

    @Override
    public int getItemCount() {
        return this.countryFlags.size();
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {

        private final ImageView countryIv;
        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            countryIv = itemView.findViewById(R.id.countryIv);
        }

        public void bind(Integer countryFlagResourceId) {
            this.countryIv.setImageResource(countryFlagResourceId);
        }
    }

}
