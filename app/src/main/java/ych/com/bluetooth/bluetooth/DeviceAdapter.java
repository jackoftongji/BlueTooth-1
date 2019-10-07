package ych.com.bluetooth.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ych.com.bluetooth.R;
import ych.com.bluetooth.utils.bluetooth.ClsUtils;

/**
 * AUTHOR    : ych
 * CREATETIME: 2018/8/6
 * DESC      :
 */

public class DeviceAdapter extends BaseAdapter {
    private List<BluetoothDevice> list;
    private Context mContext;

    public DeviceAdapter(Context mContext, List<BluetoothDevice> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            rootView = LayoutInflater.from(mContext).inflate(R.layout.item_device, null);
            viewHolder.tv_device_name = rootView.findViewById(R.id.tv_device_name);
            viewHolder.iv_close = rootView.findViewById(R.id.iv_close);

            //点击取消配对
//            viewHolder.iv_close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        ClsUtils.removeBond(list.get(position).getClass(),list.get(position));
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
            if (list != null) {
                if ("".equals(list.get(position).getName()) || list.get(position).getName() == null) {
                    viewHolder.tv_device_name.setText(list.get(position).getAddress());
                } else {
                    viewHolder.tv_device_name.setText(list.get(position).getName());
                }
                //判断是否配对过
//                if (10==list.get(position).getBondState()){
//                    //未配对
//                }else if (12==list.get(position).getBondState()){
//                    //已配对
//                    viewHolder.iv_close.setImageResource(R.drawable.sscg_lj);
//                }
            }
        } else {
            rootView = convertView;
        }
        return rootView;
    }

    class ViewHolder {
        private TextView tv_device_name;
        private ImageView iv_close;
    }
}
