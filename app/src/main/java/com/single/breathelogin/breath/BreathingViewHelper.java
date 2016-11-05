package com.single.breathelogin.breath;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.single.breathelogin.R;

import java.util.Date;

public class BreathingViewHelper {

    private static final String TAG = BreathingViewHelper.class.getSimpleName();
    private AsyncTask<Void, Integer, Void> mAsyncTask;
    private EditText ed;
    private int color;
    private String text;
    private int breathColor;
    private boolean isCancel = true;
    private boolean isAnimation;
    private View cardView;
    private Context context;
    private Animation animation;

    public BreathingViewHelper(View view, int color, String text, int breathColor, Context context) {
        this(view, color, text, breathColor, false, null, context);
    }

    public BreathingViewHelper(View view, int color, String text, int breathColor, boolean isAnimation, View cardView, Context context) {
        if (view instanceof EditText) {
            ed = (EditText) view;
        } else {
            throw new IllegalArgumentException("this is apply to EditText");
        }
        this.color = color;
        this.text = text;
        this.breathColor = breathColor;
        this.isAnimation = isAnimation;
        this.cardView = cardView;
        this.context = context;
    }

    public void setBreathingBackgroundColor() {
        isCancel = false;
        Date firstDate = new Date();
        final long firstTime = firstDate.getTime();
        mAsyncTask = new AsyncTask<Void, Integer, Void>() {
            int n = 1, t = 3000;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(TAG, "onPreExecute");
                if (color == breathColor) {
                    this.cancel(true);
                    throw new IllegalArgumentException("the color must be diffrent from breathColor");
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                while (!isCancelled()) {
                    Date currentDate = new Date();
                    long diffTime = currentDate.getTime() - firstTime;
                    Log.d(TAG, "diffTime:" + diffTime);
                    //如果全过程超过3秒钟的时间，则直接跳出循环
                    if (diffTime > 3000 || isCancel) {
                        //return后就直接
                        return null;
                    }
                    double y = getBreathingY(diffTime, n, t);
                    Log.d(TAG, "getBreathingY:" + y);
                    int alpha = (int) ((y * 0.618f + 0.382f) * 255);
                    int textAlpha = (int) ((y * 0.618f + 0.282f) * 255);
                    Log.d(TAG, "alpha:" + alpha);
                    int resultColor = setAlphaComponent(color, alpha);
                    int textColor = setAlphaComponent(breathColor, textAlpha);
                    publishProgress(resultColor, textColor);
                    try {
                        Thread.sleep(38);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                ed.setBackgroundColor(values[0]);
                if (ed != null) {
                    ed.setTextColor(values[1]);
                    ed.setText(text);
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "onPostExecute");
                if (ed != null) {
                    ed.setText("");
                }
                ed.setBackgroundColor(Color.WHITE);
                if (isAnimation && animation != null) {
                    animation.cancel();
                }

                this.cancel(true);
                isCancel = true;
            }
        };
        executeAsyncTask(mAsyncTask);
        if (isAnimation) {
            animation = AnimationUtils.loadAnimation(context, R.anim.warning_animation);
            cardView.startAnimation(animation);
        }
    }

    public boolean isCancelled() {
        return isCancel;
    }

    public void setCancel() {
        isCancel = true;
    }

    @SafeVarargs
    private static <Params, Progress, Result> void executeAsyncTask(
            AsyncTask<Params, Progress, Result> task, Params... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    private static double getBreathingY(long time, int n, int t) {
        float k = 1.0f / 3;
        float pi = 3.1415f;
        float x = time / 1000.0f;
        t = (int) (t / 1000.0f);
        if (x >= ((n - 1) * t) && x < ((n - (1 - k)) * t)) {
            double i = pi / (k * t) * ((x - (0.5f * k * t)) - (n - 1) * t);
            return 0.5f * Math.sin(i) + 0.5f;
        } else if (x >= ((n - (1 - k)) * t) && x < n * t) {
            double j = pi / ((1 - k) * t) * ((x - (0.5f * (3 - k) * t)) - (n - 1) * t);
            double one = 0.5f * Math.sin(j) + 0.5f;
            return one * one;
        }
        return 0;
    }

    /**
     * 将一个普通的颜色值带上透明度
     *
     * @param color
     * @param alpha
     * @return
     */
    public static int setAlphaComponent(int color, int alpha) {
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("alpha must be between 0 and 255.");
        }
        return (color & 0x00ffffff) | (alpha << 24);
    }

}
