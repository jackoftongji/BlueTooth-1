package ych.com.bluetooth.base;



import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * Created  on 2017/10/25.
 */

public abstract class BaseFragment extends Fragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //加载视图
        initView();
        //加载数据
        initData();
        //加载监听器
        initListener();
    }
    public abstract void initView();
    public abstract void initData();
    public abstract void initListener();
}
