package com.valle.foodieapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.valle.foodieapp.R;
import com.valle.foodieapp.listeners.WishlistListener;
import com.valle.foodieapp.models.OrderPlacedModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.utils.CommonUtils;
import com.valle.foodieapp.utils.RoundRectCornerImageView;

import java.util.List;

public class WishlishAdapter extends RecyclerView.Adapter<WishlishAdapter.ViewHolder> {

    private Context context;
    private List<OrderPlacedModel.responseData.orders_InfoData> orders_Info;
    private WishlistListener wishlistListener;
    private final String NO = "No";

    public WishlishAdapter(Context context, List<OrderPlacedModel.responseData.orders_InfoData> orders_Info, WishlistListener wishlistListener) {
        this.context = context;
        this.orders_Info = orders_Info;
        this.wishlistListener = wishlistListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_layout_wishlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderPlacedModel.responseData.orders_InfoData orders_infoData = orders_Info.get(position);

        holder.llRepeatOrder.setOnClickListener(v -> {
            Toast.makeText(context, context.getResources().getString(R.string.item_added_to_cart_plz_check), Toast.LENGTH_SHORT).show();
            wishlistListener.OnRepeatOrder(orders_infoData);
        });

        if (orders_infoData.Ordered_Items == null) {
            return;
        }

        try {
            holder.tvDeliveryAddress.setText(!TextUtils.isEmpty(orders_infoData.Ordered_Items.Delivery_Address.Address) ? orders_infoData.Ordered_Items.Delivery_Address.Address : "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvDeliveryDate.setText(!TextUtils.isEmpty(orders_infoData.createdDtm) ? orders_infoData.createdDtm : "");
        holder.tvDeliveryTotal.setText(!TextUtils.isEmpty(orders_infoData.Grand_Total) ? "$" + orders_infoData.Grand_Total : "");
        holder.tvRestaurantName.setText(!TextUtils.isEmpty(orders_infoData.Rest_Id.Restaurant_Name) ? orders_infoData.Rest_Id.Restaurant_Name : "");
        holder.tvRestaurantAdddress.setText(!TextUtils.isEmpty(orders_infoData.Rest_Id.Address) ? orders_infoData.Rest_Id.Address : "");
        holder.tvOrderNumber.setText(!TextUtils.isEmpty(orders_infoData.Order_Number) ? orders_infoData.Order_Number : "");

        try {
            if (!TextUtils.isEmpty(orders_infoData.Rest_Id.Profile_Image)) {
                Glide.with(context).load(Apis.IMAGE_URL + orders_infoData.Rest_Id.Profile_Image).placeholder(context.getResources().getDrawable(R.drawable.as)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.rlProgressDialog.setVisibility(View.GONE);
                        holder.ivRestaurantImage.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.rlProgressDialog.setVisibility(View.GONE);
                        holder.ivRestaurantImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(holder.ivRestaurantImage);
            } else {
                holder.rlProgressDialog.setVisibility(View.GONE);
                holder.ivRestaurantImage.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            if (holder.llOrderedItems.getChildCount() > 0) {
                holder.llOrderedItems.removeAllViews();
            }

            for (int i = 0; i < orders_infoData.Ordered_Items.Items.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.custom_item_view, null, false);
                AppCompatTextView tvOrderItems = view.findViewById(R.id.tvOrderItems);
                AppCompatTextView tvFlavor = view.findViewById(R.id.tvFlavor);
                LinearLayoutCompat llFlavor = view.findViewById(R.id.llFlavor);
                tvOrderItems.append(CommonUtils.getColoredString(context, orders_infoData.Ordered_Items.Items.get(i).Item_Name, ContextCompat.getColor(context, R.color.light_black)));
                tvOrderItems.append(CommonUtils.getColoredString(context, "  x" + orders_infoData.Ordered_Items.Items.get(i).Quantity, ContextCompat.getColor(context, R.color.green)));
                String ItemFlavor = orders_infoData.Ordered_Items.Items.get(i).Item_Flavor_Type;
                llFlavor.setVisibility(!TextUtils.isEmpty(ItemFlavor) ? View.VISIBLE : View.GONE);
                if (!TextUtils.isEmpty(ItemFlavor)) {
                    tvFlavor.setText("sabor: " + ItemFlavor);
                }
                holder.llOrderedItems.addView(view);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.ivFavourite.setOnClickListener(v -> {
            wishlistListener.OnClickItem(orders_infoData, NO);
        });

    }

    @Override
    public int getItemCount() {
        return orders_Info.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayoutCompat llOrderMainLayout, llOrderedItems, llRepeatOrder;
        private AppCompatImageView ivFavourite;
        private RoundRectCornerImageView ivRestaurantImage;
        private AppCompatTextView tvDeliveryAddress, tvRestaurantAdddress, tvDeliveryDate, tvDeliveryTotal, tvRestaurantName, tvOrderNumber;
        private RelativeLayout rlProgressDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llRepeatOrder = itemView.findViewById(R.id.llRepeatOrder);
            llOrderMainLayout = itemView.findViewById(R.id.llOrderMainLayout);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            llOrderedItems = itemView.findViewById(R.id.llOrderedItems);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            tvDeliveryDate = itemView.findViewById(R.id.tvDeliveryDate);
            tvDeliveryTotal = itemView.findViewById(R.id.tvDeliveryTotal);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvRestaurantAdddress = itemView.findViewById(R.id.tvRestaurantAdddress);
            ivRestaurantImage = itemView.findViewById(R.id.ivRestaurantImage);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            rlProgressDialog = itemView.findViewById(R.id.rlProgressDialog);
        }
    }
}
