package ych.com.bluetooth.base;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Description:手势activity基类
 * Created by wk on 2018/7/12.
 */

public abstract class BaseGestureActivity
        extends BaseActivity
        implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{
    private GestureDetector detector;
    private OnGestureListener listener;
    private boolean isEffective = true;//手势控制开启标志值
    private boolean ActivityIsEffective = true;//手势控制开启标志值
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将该Activity上的触碰事件交给GestureDetector处理
        return detector.onTouchEvent(event);
    }
    public void setOnBackListener(OnGestureListener listener){
        detector = new GestureDetector(this, this);
        this.listener = listener;
    }
    public void onBack(){
        if(listener!=null){
            this.listener.onBack(true);
        }
    }
    public void onLeft(){
        if(listener!=null){
            this.listener.onLeft(true);
        }
    }
    public void onClickTwice(){
        if(listener!=null){
            this.listener.onClickTwice(true);
        }
    }
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if (motionEvent1.getX()-motionEvent.getX() > 75 && Math.abs(motionEvent1.getY()-motionEvent.getY())<200) {
            onBack();
            ActivityIsEffective = false;
            return true;
        }else if(motionEvent.getX() -motionEvent1.getX()> 75 && Math.abs(motionEvent1.getY()-motionEvent.getY())<200){
            ActivityIsEffective = false;
            onLeft();
            return true;
        }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        onClickTwice();
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isEffective)
            detector.onTouchEvent(ev); // 让GestureDetector响应触碰事件
        if (ActivityIsEffective)
            super.dispatchTouchEvent(ev); // 让Activity响应触碰事件
        ActivityIsEffective = true;
        return false;
    }

    /**
     * 设置手势生效标志位，供其子类调用
     * @param isEffective 标志位
     */
    public void setEffective(boolean isEffective){
        this.isEffective = isEffective;
    }
}
