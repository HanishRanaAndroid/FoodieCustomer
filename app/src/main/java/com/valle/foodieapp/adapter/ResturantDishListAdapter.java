package com.valle.foodieapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.valle.foodieapp.R;
import com.valle.foodieapp.listeners.AddToCartListner;
import com.valle.foodieapp.models.MenuItemListModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.utils.RoundRectCornerImageView;

import java.util.List;

public class ResturantDishListAdapter extends RecyclerView.Adapter<ResturantDishListAdapter.ViewHolder> {

    private Context context;
    private List<MenuItemListModel.responseData.ItemsData> items;
    private AddToCartListner addToCartListner;
    private final String IN_STOCK = "In Stock";

    public ResturantDishListAdapter(Context context, List<MenuItemListModel.responseData.ItemsData> items, AddToCartListner addToCartListner) {
        this.context = context;
        this.items = items;
        this.addToCartListner = addToCartListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_layout_dishes_resturant_list, parent, false));
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

        holder.tvItemName.setText(items.get(position).Item_Name.trim());
        holder.tvItemDesc.setText(items.get(position).Item_Description.trim());
        holder.tvItemPrice.setText("$" + items.get(position).Item_Price);

        holder.tvAddItem.setOnClickListener(v -> {
            addToCartListner.addTocart(items.get(position));
        });

        holder.tvOutOfStock.setVisibility(items.get(position).Item_Status.equalsIgnoreCase(IN_STOCK) ? View.GONE : View.VISIBLE);
        holder.tvAddItem.setVisibility(!items.get(position).Item_Status.equalsIgnoreCase(IN_STOCK) ? View.GONE : View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundRectCornerImageView ivItemImage;
        private AppCompatTextView tvItemName, tvItemPrice, tvAddItem, tvOutOfStock,tvItemDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvAddItem = itemView.findViewById(R.id.tvAddItem);
            tvOutOfStock = itemView.findViewById(R.id.tvOutOfStock);
            tvItemDesc = itemView.findViewById(R.id.tvItemDesc);
        }
    }

}
