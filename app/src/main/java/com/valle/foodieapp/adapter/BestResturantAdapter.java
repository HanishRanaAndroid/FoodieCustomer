package com.valle.foodieapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.fragment.ResturantFragment;
import com.valle.foodieapp.models.RestaurantModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.utils.RoundRectCornerImageView;

import java.util.List;

public class BestResturantAdapter extends RecyclerView.Adapter<BestResturantAdapter.ViewHolder> {

    private Context context;
    private List<RestaurantModel.responseData.responseList> restaurantDataModels;

    public BestResturantAdapter(Context context, List<RestaurantModel.responseData.responseList> restaurantDataModels) {
        this.context = context;
        this.restaurantDataModels = restaurantDataModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_layout_popular_dishes_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        try {
            if (!TextUtils.isEmpty(restaurantDataModels.get(position).Profile_Image)) {
                Glide.with(context).load(Apis.IMAGE_URL + restaurantDataModels.get(position).Profile_Image).into(holder.ivRestImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvRestname.setText(restaurantDataModels.get(position).Restaurant_Name);
        if (!TextUtils.isEmpty(restaurantDataModels.get(position).Discount_Percentage) && !TextUtils.isEmpty(restaurantDataModels.get(position).Discount_Code)) {
            holder.tvRestPromoCode.setText(context.getResources().getString(R.string.flat) + restaurantDataModels.get(position).Discount_Percentage + context.getResources().getString(R.string.use_code) + restaurantDataModels.get(position).Discount_Code);
        } else {
            holder.tvRestPromoCode.setText("");
        }

        try {

            List<RestaurantModel.responseData.responseList.ItemCategoryData> itemCategory = restaurantDataModels.get(position).ItemCategory;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < itemCategory.size(); i++) {
                stringBuilder.append(itemCategory.get(i).Item_Category);
                stringBuilder.append(",");
            }

            holder.tvRestCategory.setText(stringBuilder.toString().substring(0, stringBuilder.length() - 1));

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvRestRating.setText("★" + restaurantDataModels.get(position).Overall_Rating);
        holder.llRestaurantLayout.setOnClickListener(v -> {
            Bundle bundle=new Bundle();
            bundle.putString("data","1");
            bundle.putString("type","Not-Filter");
            ((HomeTabActivity) context).replaceFragmentWithBackStack(new ResturantFragment(restaurantDataModels.get(holder.getAdapterPosition())), bundle);
        });

    }

    @Override
    public int getItemCount() {
        return restaurantDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayoutCompat llRestaurantLayout;
        private RoundRectCornerImageView ivRestImage;
        private AppCompatTextView tvRestname, tvRestRating, tvRestCategory, tvRestPromoCode;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            llRestaurantLayout = itemView.findViewById(R.id.llRestaurantLayout);
            ivRestImage = itemView.findViewById(R.id.ivRestImage);
            tvRestname = itemView.findViewById(R.id.tvRestname);
            tvRestRating = itemView.findViewById(R.id.tvRestRating);
            tvRestCategory = itemView.findViewById(R.id.tvRestCategory);
            tvRestPromoCode = itemView.findViewById(R.id.tvRestPromoCode);
        }
    }
}
