package ych.com.bluetooth.set.realTime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.raizlabs.android.dbflow.annotation.Column;

import java.util.List;

import ych.com.bluetooth.R;
import ych.com.bluetooth.db.Record;
import ych.com.bluetooth.db.Title;
import ych.com.bluetooth.db.Type;
import ych.com.bluetooth.set.realTime.RecordActivity;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/18
 *      desc   :
 *      version:
 * </pre>
 */

public class RecordAdapter extends BaseAdapter{
    private static final int TYPE_HEADER=0;
    private static final int TYPE_ITEM=1;
    private List<Type> list;
    private LayoutInflater inflater;
    private Context mContext;

    public RecordAdapter(Context mContext,List<Type> list){
        this.list=list;
        inflater=LayoutInflater.from(mContext);
    }

    /**
     * 所有项总和
     * @return
     */
    @Override
    public int getCount() {
        int count =0;
        if (list!=null){
            for (Type type:list){
                count+=type.size();
            }
        }
        return count;
    }

    /**
     * 根据position的不同返回不同的值
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        int head =0;
        for (Type type:list){
            int size=type.size();
            int current=position-head;
            if (current<size){
                return type.getItem(current);
            }
            head+=size;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        switch (getItemViewType(position)){
            //分为两种加载item
            case TYPE_HEADER://加载标题布局
                if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=inflater.inflate(R.layout.header,parent,false);
               viewHolder.tv_project_number=convertView.findViewById(R.id.tv_project_number);
               viewHolder.tv_time=convertView.findViewById(R.id.tv_time);
               convertView.setTag(viewHolder);
                }else {
                    viewHolder= (ViewHolder) convertView.getTag();
                }
                viewHolder.tv_project_number.setText(((Title)getItem(position)).getProjectNum());
                viewHolder.tv_time.setText(((Title)getItem(position)).getTime());
                break;
            case TYPE_ITEM://加载数据项目布局
                if (convertView==null){
                viewHolder = new ViewHolder();
                convertView=inflater.inflate(R.layout.item_record,parent,false);
                viewHolder.tv_sensor_number=convertView.findViewById(R.id.tv_sensor_number);
                viewHolder.tv_measuring_point=convertView.findViewById(R.id.tv_measuring_point);
                viewHolder.tv_current_value=convertView.findViewById(R.id.tv_current_value);
                convertView.setTag(viewHolder);
                }else {
                    viewHolder= (ViewHolder) convertView.getTag();
                }
                Record record = (Record) getItem(position);
                viewHolder.tv_sensor_number.setText(record.getSensorNum());
                viewHolder.tv_measuring_point.setText(record.getMeasuringPoint());
                viewHolder.tv_current_value.setText(record.getCurrentValue());
                break;
        }
        return convertView;
    }
    /**
     *
     * @return 返回item类型数目
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    /**
     * 获取当前item的类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int head = 0;
        for (Type type : list) {
            int size = type.size();
            int current = position - head;
            if (current == 0) {
                return TYPE_HEADER;
            }
            head += size;
        }
        return TYPE_ITEM;
    }

    /**
     * 判断当前的item是否可以点击
     * @param position
     * @return
     */
    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_HEADER;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    private class ViewHolder {
        TextView tv_sensor_number;
        TextView tv_measuring_point;
        TextView tv_current_value;
        TextView tv_time;
        TextView tv_project_number;
    }

}
