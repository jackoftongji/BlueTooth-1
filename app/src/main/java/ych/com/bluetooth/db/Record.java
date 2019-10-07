package ych.com.bluetooth.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/15
 *      desc   :
 *      version:
 * </pre>
 */

@Table(database = DataBase.class)
public class Record extends BaseModel{
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String collectorNum;
    @Column
    private String projectNum;
    @Column
    private String sensorNum;
    @Column
    private String measuringPoint;
    @Column
    private String currentValue;
    @Column
    private String createTime;
    @Column
    private String saveTime;
    @Column
    private String isSave;

    public String getIsSave() {
        return isSave;
    }

    public String getCollectorNum() {
        return collectorNum;
    }

    public void setCollectorNum(String collectorNum) {
        this.collectorNum = collectorNum;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }

    public void setId(long id){this.id=id;}
    public long getId(){return id;}

    public void setProjectNum(String projectNum){this.projectNum=projectNum;}
    public String getProjectNum(){return projectNum;}

    public void setSensorNum(String sensorNum){this.sensorNum=sensorNum;}
    public String getSensorNum(){return sensorNum;}

    public void setMeasuringPoint(String measuringPoint){this.measuringPoint=measuringPoint;}
    public String getMeasuringPoint(){return measuringPoint;}

    public void setCurrentValue(String currentValue){this.currentValue=currentValue;}
    public String getCurrentValue(){return currentValue;}

    public void setCreateTime(String createTime){this.createTime=createTime;}
    public String getCreateTime(){return createTime;}

    public void setSaveTime(String saveTime){this.saveTime=saveTime;}
    public String getSaveTime(){return saveTime;}

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", collectorNum='" + collectorNum + '\'' +
                ", projectNum='" + projectNum + '\'' +
                ", sensorNum='" + sensorNum + '\'' +
                ", measuringPoint='" + measuringPoint + '\'' +
                ", currentValue='" + currentValue + '\'' +
                ", createTime='" + createTime + '\'' +
                ", saveTime='" + saveTime + '\'' +
                ", isSave='" + isSave + '\'' +
                '}';
    }
}
