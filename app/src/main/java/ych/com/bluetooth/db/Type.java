package ych.com.bluetooth.db;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/18
 *      desc   :
 *      version:
 * </pre>
 */

public class Type {
    private Title title;
    private List<Record> list;

    public Type(Title title) {
        this.title = title;
        list = new ArrayList<>();
    }

    /**
     * 添加项目
     */
    public void addItem(Record record) {
        list.add(record);
    }

    /**
     * 获取项目
     */
    public Object getItem(int position) {
        if (position == 0) {
            return title;
        }else {
            return list.get(position-1);
        }
    }

    /**
     * item数目，为集合大小+1
     */
    public int size(){
        return list.size()+1;
    }
}
