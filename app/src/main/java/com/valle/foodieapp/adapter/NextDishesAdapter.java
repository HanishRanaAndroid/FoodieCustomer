package com.valle.foodieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valle.foodieapp.R;
import com.valle.foodieapp.models.RestaurantDataModel;
import com.valle.foodieapp.utils.RoundRectCornerImageView;

import java.util.List;

public class NextDishesAdapter extends RecyclerView.Adapter<NextDishesAdapter.ViewHolder> {

    private Context context;
    private List<RestaurantDataModel> restaurantDataModels;

    public NextDishesAdapter(Context context, List<RestaurantDataModel> restaurantDataModels) {
        this.context = context;
        this.restaurantDataModels = restaurantDataModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_view_next_dihes_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            Glide.with(context).load(restaurantDataModels.get(position).getImage()).into(holder.ivImage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvName.setText(restaurantDataModels.get(position).getRestaurantName());
        holder.tvDiscount.setText(restaurantDataModels.get(position).getDiscountPercentage()+context.getResources().getString(R.string.off));
        holder.tvRating.setText(restaurantDataModels.get(position).getRating());
        holder.tvCategory.setText(restaurantDataModels.get(position).getCategory());

    }

    @Override
    public int getItemCount() {
        return restaurantDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundRectCornerImageView ivImage;
        private AppCompatTextView tvDiscount, tvRating, tvName, tvCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage=itemView.findViewById(R.id.ivImage);
            tvDiscount=itemView.findViewById(R.id.tvDiscount);
            tvRating=itemView.findViewById(R.id.tvRating);
            tvName=itemView.findViewById(R.id.tvName);
            tvCategory=itemView.findViewById(R.id.tvCategory);
        }
    }
}
