package com.example.administrator.movefast.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.administrator.movefast.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class MainAdapter extends BaseAdapter {

    private Context context;
    private List<String> datas = new ArrayList<>();

    public MainAdapter(Context context, List<String> list) {
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvAddress.setText(datas.get(position));
        return convertView;
    }


    class ViewHolder {
        private TextView tvAddress;
    }
}
