package com.valle.foodieapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.fragment.ResturantFragment;
import com.valle.foodieapp.models.RestaurantModel;
import com.valle.foodieapp.models.SecondSliderModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.utils.RoundRectCornerImageView;

import java.util.List;

public class HomeScreenSilderTwoAdapter extends RecyclerView.Adapter<HomeScreenSilderTwoAdapter.ViewHolder> {

    private Context context;
    private List<SecondSliderModel.responseData> restaurantDataModels;

    public HomeScreenSilderTwoAdapter(Context context, List<SecondSliderModel.responseData> restaurantDataModels) {
        this.context = context;
        this.restaurantDataModels = restaurantDataModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_second_slider_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        try {
            if (!TextUtils.isEmpty(restaurantDataModels.get(position).Profile_Image)) {
                Glide.with(context).load(Apis.IMAGE_URL + restaurantDataModels.get(position).itemData.Item_Image).into(holder.ivRestaurantImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

      //  holder.tvRestaurantName.setText(restaurantDataModels.get(position).itemData.Item_Name);
        holder.tvItemName.setText(restaurantDataModels.get(position).itemData.Item_Name);
        holder.tvDiscountPercentage.setText(!TextUtils.isEmpty(restaurantDataModels.get(position).Discount_Percentage) ? restaurantDataModels.get(position).Discount_Percentage + "% OFF" : "");

        holder.llMainLayout.setOnClickListener(v -> {
            Bundle bundle=new Bundle();
            bundle.putString("type","Filter");
            ((HomeTabActivity) context).replaceFragmentWithBackStack(new ResturantFragment(restaurantDataModels.get(holder.getAdapterPosition())), bundle);
        });

    }

    @Override
    public int getItemCount() {
        return restaurantDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundRectCornerImageView ivRestaurantImage;
        private TextView tvRestaurantName, tvDiscountPercentage,tvItemName;

        private LinearLayoutCompat llMainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llMainLayout = itemView.findViewById(R.id.llMainLayout);
            ivRestaurantImage = itemView.findViewById(R.id.ivRestaurantImage);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvDiscountPercentage = itemView.findViewById(R.id.tvDiscountPercentage);
            tvItemName = itemView.findViewById(R.id.tvItemName);
        }
    }
}
