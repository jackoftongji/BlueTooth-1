package ych.com.bluetooth.set;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/20
 *      desc   :
 *      version:
 * </pre>
 */

public interface PjSettingLisener {
    void projectNum(String projectNum);
    void sensorNum1(String sensorNum1);
    void measuringPoint1(String measuringPoint1);
    void sensorNum2(String sensorNum2);
    void measuringPoint2(String measuringPoint2);
    void setAuto(String auto,String rate);
}
