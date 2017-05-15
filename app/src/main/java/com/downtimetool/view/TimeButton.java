package com.downtimetool.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.downtimetool.R;
import com.downtimetool.util.MapUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liudan on 2017/5/15.
 * 自定义的倒计时view
 */

public class TimeButton extends Button implements View.OnClickListener{
    private long lenght = 60 * 1000;// 倒计时长度,默认60秒
    private String textafter = "秒后可重发";
    private String textbefore = "获取验证码";
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private OnClickListener mOnclickListener;
    private Timer t;
    private TimerTask tt;
    private long time;
    private Context mContext;

    public TimeButton(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        this.mContext=context;
    }
    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TimeButton.this.setText(time / 1000 + textafter);
            time -= 1000;
            if (time < 0) {
                TimeButton.this.setEnabled(true);
                TimeButton.this.setText("重新获取");
                TimeButton.this.setTextColor(getResources().getColor(R.color.color_FC4750));
                clearTimer();
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (mOnclickListener != null)
            mOnclickListener.onClick(v);
        initTimer();
        this.setText(time / 1000 + textafter);
        this.setEnabled(false);
        this.setTextColor(getResources().getColor(R.color.color_AFAFAF));
        t.schedule(tt, 0, 1000);
    }
    private void initTimer() {
        time = lenght;
        t = new Timer();

        tt = new TimerTask() {

            @Override
            public void run() {
                Log.i("倒计时", time / 1000 + "");
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {

        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
        Log.i("倒计时结束", "执行clearTimer");
    }
    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (MapUtils.map == null)
            MapUtils.map = new HashMap<String, Long>();
        MapUtils.map.put(TIME, time);
        MapUtils.map.put(CTIME, System.currentTimeMillis());
        clearTimer();
        Log.i("倒计时", "onDestroy");
    }

    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate(Bundle bundle) {
        Log.i("倒计时==再次进入", MapUtils.map + "");
        if (MapUtils.map == null)
            return;
        if (MapUtils.map.size() <= 0)// 这里表示没有上次未完成的计时
            return;
        long time = System.currentTimeMillis() - MapUtils.map.get(CTIME)
                - MapUtils.map.get(TIME);
        MapUtils.map.clear();
        if (time > 0)
            return;
        else {
            initTimer();
            this.time = Math.abs(time);
            t.schedule(tt, 0, 1000);
            this.setText(time + textafter);
            this.setEnabled(false);
            this.setTextColor(getResources().getColor(R.color.color_AFAFAF));
        }
    }
}
