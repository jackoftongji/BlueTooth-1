package ych.com.bluetooth.set;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ych.com.bluetooth.R;
import ych.com.bluetooth.db.Record;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/12
 *      desc   : 传感器适配器
 *      version:
 * </pre>
 */

public class sensorAdapter extends BaseAdapter {
    private List<Record> list;
    private Context mContext;

    private static sensorAdapter INSTANCE = null;

    public static sensorAdapter getINSTANCE(Context mContext, List<Record> list) {
        if (INSTANCE == null) {
            INSTANCE = new sensorAdapter(mContext, list);
        }
        return INSTANCE;
    }

    public sensorAdapter(Context mContext, List<Record> list) {
        this.mContext = mContext;
        this.list = list;
        for (int i=0;i<list.size();i++){
            Log.e("list--",list.get(i).toString());
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Record getItem(int position) {
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
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sensor, null);
            viewHolder.tv_sensor_number = convertView.findViewById(R.id.tv_sensor_number);
            viewHolder.tv_measuring_point = convertView.findViewById(R.id.tv_measuring_point);
            viewHolder.tv_current_value = convertView.findViewById(R.id.tv_current_value);
            convertView.setTag(viewHolder);
        }else {
           viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_sensor_number.setText(list.get(position).getSensorNum());
        viewHolder.tv_measuring_point.setText(list.get(position).getMeasuringPoint());
        viewHolder.tv_current_value.setText(list.get(position).getCurrentValue());

        return convertView;
    }

    static class ViewHolder {
        TextView tv_sensor_number;
        TextView tv_measuring_point;
        TextView tv_current_value;
    }
}
