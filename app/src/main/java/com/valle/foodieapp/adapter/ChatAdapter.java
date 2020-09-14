package com.valle.foodieapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.models.ChatModel;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private ChatModel chatBeanModels;
    private boolean alreadyComes = false;

    public ChatAdapter(Context context, ChatModel chatBeanModels) {
        this.context = context;
        this.chatBeanModels = chatBeanModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_chat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel.responseData.MessagesList messagesList = chatBeanModels.response.response.get(position);
        if (TextUtils.isEmpty(messagesList.Delivery_Boy_Message)) {
            holder.tvMeMesaage.setText(messagesList.Customer_Message);
            holder.tvMeMesaage.setVisibility(View.VISIBLE);
            holder.llYourMessage.setVisibility(View.GONE);

        } else {
            holder.tvYourMessage.setText(messagesList.Delivery_Boy_Message);
            holder.llYourMessage.setVisibility(View.VISIBLE);
            holder.tvMeMesaage.setVisibility(View.GONE);
            holder.ivYouIcon.setVisibility(alreadyComes ? View.GONE : View.VISIBLE);
            alreadyComes = true;
        }
    }

    @Override
    public int getItemCount() {
        return chatBeanModels.response.response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvYourMessage, tvDate, tvMeMesaage;
        private LinearLayoutCompat llYourMessage;
        private AppCompatImageView ivYouIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvYourMessage = itemView.findViewById(R.id.tvYourMessage);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMeMesaage = itemView.findViewById(R.id.tvMeMesaage);
            llYourMessage = itemView.findViewById(R.id.llYourMessage);
            ivYouIcon = itemView.findViewById(R.id.ivYouIcon);
        }
    }
}


