package com.valle.foodieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.valle.foodieapp.R;
import com.valle.foodieapp.fragment.ResturantFragment;
import com.valle.foodieapp.models.MenuItemListModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdapterFlavour extends BaseAdapter {

    private Context context;
    private List<String> list;

    public AdapterFlavour(Context context, MenuItemListModel.responseData.ItemsData itemsData) {
        this.context = context;
        list = new ArrayList<>();
        String[] listFlavor = itemsData.Item_Flavor.split(",");
        if (listFlavor.length > 0) {
            list.addAll(Arrays.asList(listFlavor));
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_layout_flavour, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvFlavourName.setText(list.get(position));

        viewHolder.ivSelected.setVisibility(ResturantFragment.flavour.equalsIgnoreCase(list.get(position)) ? View.VISIBLE : View.GONE);
        viewHolder.tvFlavourName.setTextColor(ResturantFragment.flavour.equalsIgnoreCase(list.get(position)) ? context.getResources().getColor(R.color.foodie_color) : context.getResources().getColor(R.color.light_black));

        convertView.setOnClickListener(v -> {
            ResturantFragment.flavour = list.get(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public static class ViewHolder {

        private AppCompatTextView tvFlavourName;
        private AppCompatImageView ivSelected;


        public ViewHolder(View view) {
            tvFlavourName = view.findViewById(R.id.tvFlavourName);
            ivSelected = view.findViewById(R.id.ivSelected);

        }
    }
}
