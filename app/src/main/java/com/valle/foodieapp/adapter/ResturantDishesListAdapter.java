package com.valle.foodieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valle.foodieapp.R;
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.activity.SliderActivity;
import com.valle.foodieapp.activity.SplashActivity;
import com.valle.foodieapp.listeners.AddToCartListner;
import com.valle.foodieapp.models.MenuItemListModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.prefs.SharedPrefModule;
import com.valle.foodieapp.utils.RoundRectCornerImageView;

import java.util.List;

public class ResturantDishesListAdapter extends RecyclerView.Adapter<ResturantDishesListAdapter.ViewHolder> {

    private Context context;
    private List<MenuItemListModel.responseData.ItemsData> items;
    private AddToCartListner addToCartListner;
    private final String IN_STOCK = "In Stock";
    private MenuItemListModel.responseData.ItemsData itemsDataRecom;

    public ResturantDishesListAdapter(Context context, List<MenuItemListModel.responseData.ItemsData> items, MenuItemListModel.responseData.ItemsData itemsDataRecom, AddToCartListner addToCartListner) {
        this.context = context;
        this.items = items;
        if (itemsDataRecom != null) {
            this.itemsDataRecom = itemsDataRecom;
            this.items.add(0, itemsDataRecom);
        }
        this.addToCartListner = addToCartListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_menu_data_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (!TextUtils.isEmpty(items.get(position).Item_Image)) {
                Glide.with(context).load(Apis.IMAGE_URL + items.get(position).Item_Image).placeholder(context.getResources().getDrawable(R.drawable.as)).into(holder.ivItemImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.llMainItem.setBackgroundColor(itemsDataRecom != null && itemsDataRecom.Id.equalsIgnoreCase(items.get(position).Id) ? context.getResources().getColor(R.color.colorAccent) : context.getResources().getColor(R.color.white));
        holder.tvItemName.setText(items.get(position).Item_Name.trim());
        holder.tvItemDesc.setText(items.get(position).Item_Description.trim());
        holder.tvItemPrice.setText("$" + items.get(position).Item_Price);

        holder.tvAddDish.setOnClickListener(v -> {
            addToCartListner.addTocart(items.get(position));
        });
        holder.tvOutOfStock.setVisibility(items.get(position).Item_Status.equalsIgnoreCase(IN_STOCK) ? View.GONE : View.VISIBLE);
        holder.tvAddDish.setVisibility(!items.get(position).Item_Status.equalsIgnoreCase(IN_STOCK) ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvItemName, tvItemPrice, tvAddDish, tvOutOfStock, tvItemDesc;
        private RoundRectCornerImageView ivItemImage;
        private RelativeLayout llMainItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvAddDish = itemView.findViewById(R.id.tvAddDish);
            tvOutOfStock = itemView.findViewById(R.id.tvOutOfStock);
            tvItemDesc = itemView.findViewById(R.id.tvItemDesc);
            llMainItem = itemView.findViewById(R.id.llMainItem);
        }
    }

}
