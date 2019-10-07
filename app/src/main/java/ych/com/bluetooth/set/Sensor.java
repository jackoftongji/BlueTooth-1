package ych.com.bluetooth.set;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/12
 *      desc   :传感器
 *      version:
 * </pre>
 */

public class Sensor {
    private String number;
    private String measuringPoint;
    private String currentValue;

    public void setNumber(String number){this.number=number;}
    public String getNumber(){return number;}

    public void setMeasuringPoint(String measuringPoint){this.measuringPoint=measuringPoint;}
    public String getMeasuringPoint(){return measuringPoint;}

    public void setCurrentValue(String currentValue){this.currentValue=currentValue;}
    public String getCurrentValue(){return currentValue;}
}
