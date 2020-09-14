package com.valle.foodieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.models.ListQueryModel;

import java.util.List;

public class AdapterListQuery extends RecyclerView.Adapter<AdapterListQuery.ViewHolderListQuery> {

    private Context context;
    private List<ListQueryModel.ResponseData.ItemsData> itemsDataList;

    public AdapterListQuery(Context activity, List<ListQueryModel.ResponseData.ItemsData> items) {
        context = activity;
        itemsDataList = items;
    }

    @NonNull
    @Override
    public ViewHolderListQuery onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderListQuery(LayoutInflater.from(context).inflate(R.layout.adapter_layout_list_query, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderListQuery holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemsDataList.size();
    }

    public class ViewHolderListQuery extends RecyclerView.ViewHolder {

        public ViewHolderListQuery(@NonNull View itemView) {
            super(itemView);
        }
    }
}
