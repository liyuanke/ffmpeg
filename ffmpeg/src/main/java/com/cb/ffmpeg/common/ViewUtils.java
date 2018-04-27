package com.cb.ffmpeg.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/4/19.
 */

public class ViewUtils {
    private static ProgressDialog mProgressDialog;

    public static void showProgress(Activity activity, String title, String message) {
        showProgress(activity, title, message, 0);
    }

    public static void showProgress(Activity activity, String title, String message, int theme) {
        if (!activity.isFinishing()) {
            if (mProgressDialog == null) {
                if (theme > 0) {
                    mProgressDialog = new ProgressDialog(activity, theme);
                } else {
                    mProgressDialog = new ProgressDialog(activity);
                }
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
                mProgressDialog.setCancelable(false);
                mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
            }

            if (!StringUtils.isEmpty(title)) {
                mProgressDialog.setTitle(title);
            }
            mProgressDialog.setMessage(message);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog.show();
        }
    }

    public static void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }
}
