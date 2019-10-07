package ych.com.bluetooth.set;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/21
 *      desc   :
 *      version:
 * </pre>
 */

public interface WorkSettingListener {
    void setWorkStyle(String style);
    void address(String address);
    void port(String port);
}
