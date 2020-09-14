package com.valle.foodieapp.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

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
import com.valle.foodieapp.activity.HomeTabActivity;
import com.valle.foodieapp.fragment.OrderStatusFragment;
import com.valle.foodieapp.fragment.RateOrderFragment;
import com.valle.foodieapp.listeners.WishlistListener;
import com.valle.foodieapp.models.OrderPlacedModel;
import com.valle.foodieapp.network.Apis;
import com.valle.foodieapp.utils.CommonUtils;
import com.valle.foodieapp.utils.RoundRectCornerImageView;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MyOrdersListAdapter extends RecyclerView.Adapter<MyOrdersListAdapter.ViewHolder> {

    private Context context;
    private OrderPlacedModel orderPlacedModel;
    private final String ORDER_DELIVERED = "Order Delivered";
    private final String ORDER_REJECT = "Order Reject";
    private WishlistListener wishlistListener;
    private final String YES = "Yes";
    private final String NO = "No";

    public MyOrdersListAdapter(Context context, OrderPlacedModel orderPlacedModel, WishlistListener wishlistListener) {
        this.context = context;
        this.orderPlacedModel = orderPlacedModel;
        this.wishlistListener = wishlistListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_my_orders_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderPlacedModel.responseData.orders_InfoData orders_infoData = orderPlacedModel.response.orders_Info.get(position);

        if (orders_infoData.Ordered_Items == null) {
            return;
        }

        if (orders_infoData.Wishlist.equalsIgnoreCase(YES)) {
            holder.ivFavourite.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
            holder.ivFavourite.setTag(NO);
        } else {
            holder.ivFavourite.setImageDrawable(context.getResources().getDrawable(R.drawable.un_favourite));
            holder.ivFavourite.setTag(YES);
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

        if (orders_infoData.Order_Status.equalsIgnoreCase(ORDER_DELIVERED)) {
            holder.tvTrackOrder.setVisibility(View.GONE);
            holder.rlItemDelivered.setVisibility(View.VISIBLE);
            holder.tvOrderCancelled.setVisibility(View.GONE);

            try {
                JSONObject jsonObject = new JSONObject(new Gson().toJson(orders_infoData.Is_Rating));
                holder.rlRateOrder.setVisibility(!jsonObject.has("Rating") ? View.VISIBLE : View.GONE);
                holder.rlAlreadyRated.setVisibility(jsonObject.has("Rating") ? View.VISIBLE : View.GONE);
                holder.tvOrderRating.setText(jsonObject.has("Rating") ? "★" + orders_infoData.Is_Rating.Rating : "★0.0");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (orders_infoData.Order_Status.equalsIgnoreCase(ORDER_REJECT)) {
            holder.tvTrackOrder.setVisibility(View.GONE);
            holder.rlItemDelivered.setVisibility(View.GONE);
            holder.tvOrderCancelled.setVisibility(View.VISIBLE);
        } else {
            holder.tvTrackOrder.setVisibility(View.VISIBLE);
            holder.rlItemDelivered.setVisibility(View.GONE);
            holder.tvOrderCancelled.setVisibility(View.GONE);
            manageBlinkEffect(holder.tvTrackOrder);
        }

        holder.ivFavourite.setOnClickListener(v -> {
            holder.ivFavourite.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
            orders_infoData.Wishlist = holder.ivFavourite.getTag().toString();
            wishlistListener.OnClickItem(orders_infoData, holder.ivFavourite.getTag().toString());
            notifyDataSetChanged();
        });

        holder.tvTrackOrder.setOnClickListener(v -> {
            ((HomeTabActivity) context).replaceFragmentWithBackStack(new OrderStatusFragment(orders_infoData), null);
        });

        holder.tvRateOrder.setOnClickListener(v -> {
            ((HomeTabActivity) context).replaceFragmentWithBackStack(new RateOrderFragment(orders_infoData), null);
        });
    }

    @SuppressLint("WrongConstant")
    private void manageBlinkEffect(AppCompatTextView textView) {
        ObjectAnimator anim = ObjectAnimator.ofInt(textView, "backgroundColor", Color.WHITE, context.getResources().getColor(R.color.dark_grey),
                Color.WHITE);
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }

    @Override
    public int getItemCount() {
        return orderPlacedModel.response.orders_Info.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayoutCompat llOrderMainLayout, llOrderedItems;
        private AppCompatImageView ivFavourite;
        private RoundRectCornerImageView ivRestaurantImage;
        private AppCompatTextView tvTrackOrder, tvDeliveryAddress, tvRestaurantAdddress, tvDeliveryDate, tvDeliveryTotal, tvRestaurantName,
                tvOrderCancelled, tvRateOrder, tvOrderRating, tvOrderNumber;
        private RelativeLayout rlItemDelivered, rlRateOrder, rlAlreadyRated, rlProgressDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llOrderMainLayout = itemView.findViewById(R.id.llOrderMainLayout);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            tvTrackOrder = itemView.findViewById(R.id.tvTrackOrder);
            rlItemDelivered = itemView.findViewById(R.id.rlItemDelivered);
            llOrderedItems = itemView.findViewById(R.id.llOrderedItems);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            tvDeliveryDate = itemView.findViewById(R.id.tvDeliveryDate);
            tvDeliveryTotal = itemView.findViewById(R.id.tvDeliveryTotal);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvRestaurantAdddress = itemView.findViewById(R.id.tvRestaurantAdddress);
            ivRestaurantImage = itemView.findViewById(R.id.ivRestaurantImage);
            tvOrderCancelled = itemView.findViewById(R.id.tvOrderCancelled);
            tvRateOrder = itemView.findViewById(R.id.tvRateOrder);
            rlRateOrder = itemView.findViewById(R.id.rlRateOrder);
            rlAlreadyRated = itemView.findViewById(R.id.rlAlreadyRated);
            tvOrderRating = itemView.findViewById(R.id.tvOrderRating);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            rlProgressDialog = itemView.findViewById(R.id.rlProgressDialog);
        }
    }
}
