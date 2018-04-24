package com.example.administrator.movefast.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.movefast.R;
import com.example.administrator.movefast.entity.WayBill;
import com.example.administrator.movefast.utils.Helper;

import org.w3c.dom.Text;

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_main, null);
            holder = new ViewHolder();
            holder.tvAddress = convertView.findViewById(R.id.tv_address);
            holder.tvName = convertView.findViewById(R.id.tv_show_name);
            holder.tvPhone = convertView.findViewById(R.id.tv_show_phone);
            holder.tvDate = convertView.findViewById(R.id.tv_show_date);
            holder.ivFinish = convertView.findViewById(R.id.iv_state);
            holder.flItem = convertView.findViewById(R.id.fl_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvAddress.setText(datas.get(position).getAddress());
        holder.tvName.setText(datas.get(position).getName());
        holder.tvPhone.setText(datas.get(position).getPhone());
        holder.tvDate.setText(datas.get(position).getCreate_time());

        if (position == datas.size() - 1){
            holder.flItem.setPadding(Helper.dip2px(10),Helper.dip2px(10),Helper.dip2px(10),Helper.dip2px(10));
        }

        if (datas.get(position).getIs_end() == 0){
            holder.ivFinish.setBackground(context.getResources().getDrawable(R.drawable.no_finish));
        }else if (datas.get(position).getIs_end() == 1){
            holder.ivFinish.setBackground(context.getResources().getDrawable(R.drawable.finish));
        }
        return convertView;
    }


    class ViewHolder {
        private TextView tvAddress;
        private TextView tvName;
        private TextView tvPhone;
        private TextView tvDate;
        private ImageView ivFinish;
        private FrameLayout flItem;
    }
}
