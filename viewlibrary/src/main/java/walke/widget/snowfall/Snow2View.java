package walke.widget.snowfall;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

/**
 * change by walke on 16-12-1.
 */
public class Snow2View extends View {

    private Snow2Utils mSnow2Utils;

    public static final int MSG_PRODUCE_SNOW = 1;
    public static final int MSG_UPDATE_SNOW = 2;
    public static final int MSG_INVALIDATE_VIEW = 3;

    public static final int SPEED_ACCELARATE = 1;
    public static final int SPEED_DECELARATE = 2;

    public int REFRESH_VIEW_INTERVAL = 30;



    private CountDownLatch mMeasureLatch = new CountDownLatch(1);

    public Snow2View(Context context) {
        super(context);
        initSnowFlakes();
    }

    public Snow2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSnowFlakes();
    }

    public Snow2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSnowFlakes();
    }

    public Snow2View(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSnowFlakes();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mSnow2Utils != null){
            mSnow2Utils.draw(canvas);
        }
    }

    private void initSnowFlakes(){
        mSnow2Utils = new Snow2Utils(getContext());

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onGlobalLayout() {
                mSnow2Utils.init(getMeasuredWidth(), getMeasuredHeight());
                mMeasureLatch.countDown();
            }
        });
    }

    public void produceSnowFlake(){
        mSnow2Utils.produceSnowFlake();
    }

    private int getProduceSnowInterval(){
        return mSnow2Utils.getProduceSnowInterval();
    }

    public void startSnowAnim(int level){
        mSnow2Utils.setSnowLevel(level);
        startThread.start();
    }

    private void startSnowAnim(){
        mSnowHandler.removeMessages(MSG_PRODUCE_SNOW);
        mSnowHandler.obtainMessage(MSG_PRODUCE_SNOW).sendToTarget();
    }

    public void stopAnim(){
        mSnow2Utils.removeAllSnowFlake();
        mSnowHandler.removeCallbacksAndMessages(null);
    }

    public void changeSnowLevel(int level){
        mSnow2Utils.setSnowLevel(level);
        stopAnim();
        startSnowAnim();
    }

    private SnowHandler mSnowHandler = new SnowHandler(this);

    public  class SnowHandler extends Handler {

        private WeakReference<Snow2View> mSnowView;

        public SnowHandler(Snow2View view){
            mSnowView = new WeakReference<Snow2View>(view);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_PRODUCE_SNOW:
                    if(mSnowView.get() != null){
                        mSnowView.get().produceSnowFlake();
                        removeMessages(MSG_PRODUCE_SNOW);
                        sendMessageDelayed(obtainMessage(MSG_PRODUCE_SNOW), mSnowView.get().getProduceSnowInterval());

                        removeMessages(MSG_INVALIDATE_VIEW);
                        sendMessage(obtainMessage(MSG_INVALIDATE_VIEW));
                    }
                    break;
                case MSG_UPDATE_SNOW:

                    break;
                case MSG_INVALIDATE_VIEW:
                    if(mSnowView.get() != null){
                        mSnowView.get().postInvalidateOnAnimation();
                        removeMessages(MSG_INVALIDATE_VIEW);
                        sendMessageDelayed(obtainMessage(MSG_INVALIDATE_VIEW), mSnowView.get().REFRESH_VIEW_INTERVAL);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Thread startThread = new Thread(new Runnable() {
        @Override
        public void run() {
            if(getContext() != null && !((Activity)getContext()).isDestroyed()){
                try {
                    mMeasureLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(getContext() != null && !((Activity)getContext()).isDestroyed()){
                    startSnowAnim();
                }
            }
        }
    });
}
