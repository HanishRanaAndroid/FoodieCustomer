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
import com.valle.foodieapp.listeners.UpdateCartListener;
import com.valle.foodieapp.models.CartModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.utils.RoundRectCornerImageView;

import java.util.List;

public class CartItemsListAdapter extends RecyclerView.Adapter<CartItemsListAdapter.ViewHolder> {

    private Context context;
    private List<CartModel.responseData.ItemsList> items;
    private UpdateCartListener updateCartListener;

    public CartItemsListAdapter(Context context, List<CartModel.responseData.ItemsList> items, UpdateCartListener updateCartListener) {
        this.context = context;
        this.items = items;
        this.updateCartListener = updateCartListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_layout_cart_list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvMinus.setOnClickListener(v -> {
            int count = Integer.parseInt(holder.tvItemCount.getText().toString());

            --count;

            holder.tvItemCount.setText(String.valueOf(count));
            if (count == 0) {
                updateCartListener.RemoveItem(items.get(position));
            } else {
                updateCartListener.updateCart(items.get(position), String.valueOf(count));
            }
        });

        holder.tvPlus.setOnClickListener(v -> {
            int count = Integer.parseInt(holder.tvItemCount.getText().toString());
            ++count;
            holder.tvItemCount.setText(String.valueOf(count));
            updateCartListener.updateCart(items.get(position), String.valueOf(count));
        });

        try {
            if (!TextUtils.isEmpty(items.get(position).Item_Details.get(0).Item_Image) && !items.get(position).Item_Details.get(0).Item_Image.equalsIgnoreCase("0")) {
                Glide.with(context).load(Apis.IMAGE_URL + items.get(position).Item_Details.get(0).Item_Image).into(holder.ivItemImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvItemCount.setText(items.get(position).Item_Quantity);
        holder.tvItemName.setText(items.get(position).Item_Details.get(0).Item_Name + "  x" + items.get(position).Item_Quantity);
        holder.tvItemFlavor.setText(!TextUtils.isEmpty(items.get(position).Item_Flavor_Type) ? items.get(position).Item_Flavor_Type: "");
        holder.tvItemPrice.setText("$" + items.get(position).Item_Details.get(0).Item_Price);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvMinus, tvItemCount, tvPlus, tvItemName, tvItemPrice, tvItemFlavor;
        private RoundRectCornerImageView ivItemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMinus = itemView.findViewById(R.id.tvMinus);
            tvItemCount = itemView.findViewById(R.id.tvItemCount);
            tvPlus = itemView.findViewById(R.id.tvPlus);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvItemFlavor = itemView.findViewById(R.id.tvItemFlavor);
        }
    }
}
