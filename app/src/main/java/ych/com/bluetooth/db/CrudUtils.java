package ych.com.bluetooth.db;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import ych.com.bluetooth.utils.BaseUtils;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/15
 *      desc   :
 *      version:
 * </pre>
 */

public class CrudUtils {
    private static CrudUtils INSTANCE=null;
    public static CrudUtils getINSTANCE(){
        if (INSTANCE==null){
            synchronized (BaseUtils.class){
                if (INSTANCE==null){
                    INSTANCE = new CrudUtils();
                }
            }
        }
        return INSTANCE;
    }
    //添加
    public void insert(Record record) {

//        SQLite.insert(Record.class)
//                .columnValues(Record_Table.projectNum.eq(record.getProjectNum()))
//                .columnValues(Record_Table.collectorNum.eq(record.getCollectorNum()))
//                .columnValues(Record_Table.createTime.eq(record.getCreateTime()))
//                .columnValues(Record_Table.currentValue.eq(record.getCurrentValue()))
//                .columnValues(Record_Table.measuringPoint.eq(record.getMeasuringPoint()))
//                .columnValues(Record_Table.saveTime.eq(record.getSaveTime()))
//                .columnValues(Record_Table.sensorNum.eq(record.getSensorNum()))
//                .execute();

            //方法一
           FlowManager.getModelAdapter(Record.class).insert(record);


    }

    //删除
    public void delete() {
//        //方法一  先查后删除
//        Product product = SQLite.select()
//                .from(Product.class)
//                .where(Product_Table.name.eq("yy"))
//                .querySingle();
//        if (product!=null){
//            product.delete();
//        }
//        //方法二 直接删除
//        SQLite.delete(Product.class)
//                .where(Product_Table.name.eq("yy"))
//                .execute();
    }

    //修改
    public void update() {
//        //方法一 先查后改
//        Product product = SQLite.select()
//                .from(Product.class)
//                .where(Product_Table.name.eq("yy"))
//                .querySingle();//区别于queryList(),返回的是实体
//        if (product != null) {
//            product.name = "yy1";
//            product.update();
//        }
//
//        //方法二 直接修改
//        SQLite.update(Product.class)
//                .set(Product_Table.name.eq("yy1"))
//                .where(Product_Table.name.eq("yy"))
//                .execute();
    }

    //查询全部
    public List<Record> selectAll() {
        //方法一
        List<Record> records = SQLite.select()
                .from(Record.class)
                .where()
                // .orderBy(Product_Table.id,true)//按照升序
                // .limit(5)//限制条数
                .queryList();//返回的list不为空，但是可能为empty
        return records;
    }

    //查询单个
    public Record selectOne(Record r) {
        Record record = SQLite.select()
                .from(Record.class)
                .where(Record_Table.projectNum.eq(r.getProjectNum()),
                        Record_Table.sensorNum.eq(r.getSensorNum()),
                        Record_Table.currentValue.eq(r.getCurrentValue()),
                        Record_Table.measuringPoint.eq(r.getMeasuringPoint()))//条件
                .querySingle();//返回单个实体
        return record;
    }
    //分组查询
    public List<Record> selectTimeAndPj(){
        //方法一
        List<Record> records = SQLite.select()
                .from(Record.class)
                .where()
                .groupBy(Record_Table.createTime)
                // .orderBy(Product_Table.id,true)//按照升序
                // .limit(5)//限制条数
                .queryList();//返回的list不为空，但是可能为empty
        return records;
    }
    //按时间查询
    public List<Record> selectTime(String time){
        //方法一
        List<Record> records = SQLite.select()
                .from(Record.class)
                .where(Record_Table.createTime.eq(time))
                //.groupBy(Record_Table.createTime)
                // .orderBy(Product_Table.id,true)//按照升序
                // .limit(5)//限制条数
                .queryList();//返回的list不为空，但是可能为empty
        return records;
    }
}
