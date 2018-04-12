package com.example.administrator.movefast.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.administrator.movefast.R;
import com.example.administrator.movefast.entity.WayBill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class MainAdapter extends BaseAdapter {

    private Context context;
    private List<WayBill> datas = new ArrayList<>();

    public MainAdapter(Context context, List<WayBill> list) {
        this.context = context;
        datas = list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_main, null);
            holder = new ViewHolder();
            holder.tvAddress = convertView.findViewById(R.id.tv_address);
            holder.tvName = convertView.findViewById(R.id.tv_show_name);
            holder.tvPhone = convertView.findViewById(R.id.tv_show_phone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvAddress.setText(datas.get(position).getAddress());
        holder.tvName.setText(datas.get(position).getName());
        holder.tvPhone.setText(datas.get(position).getPhone());
        return convertView;
    }


    class ViewHolder {
        private TextView tvAddress;
        private TextView tvName;
        private TextView tvPhone;
    }
}
