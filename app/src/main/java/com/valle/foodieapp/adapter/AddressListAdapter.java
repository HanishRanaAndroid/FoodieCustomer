package com.valle.foodieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.listeners.SelectAddressInterface;
import com.valle.foodieapp.models.AddressModel;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {

    private Context context;
    private List<AddressModel.responseData.ItemsData> selectedAddress;
    private SelectAddressInterface selectAddressInterface;

    public AddressListAdapter(Context context, List<AddressModel.responseData.ItemsData> selectedAddress, SelectAddressInterface selectAddressInterface) {
        this.context = context;
        this.selectedAddress = selectedAddress;
        this.selectAddressInterface = selectAddressInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_layout_address_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvAddressType.setText(selectedAddress.get(position).Type);
        holder.tvAddress.setText(selectedAddress.get(position).Address2);

        if (selectedAddress.get(position).Is_Primary.equalsIgnoreCase("1")) {
            holder.ivSelect.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelect.setVisibility(View.GONE);
        }

        holder.llAddress.setOnClickListener(v -> {
            selectAddressInterface.OnAddressSelected(selectedAddress.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return selectedAddress.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvAddressType, tvAddress;
        private AppCompatImageView ivSelect;
        private LinearLayoutCompat llAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressType = itemView.findViewById(R.id.tvAddressType);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            llAddress = itemView.findViewById(R.id.llAddress);
        }
    }
}
