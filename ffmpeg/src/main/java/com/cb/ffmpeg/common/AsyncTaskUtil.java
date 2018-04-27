package com.cb.ffmpeg.common;

import android.os.AsyncTask;

import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/4/19.
 */

public class AsyncTaskUtil {
    public static void execute(final Runnable runnable) {
        AsyncTask asyncTask = new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... voids) {
                if (runnable != null) {
                    runnable.run();
                }
                return null;
            }
        };
        asyncTask.executeOnExecutor(Executors.newFixedThreadPool(5));
    }

    public static void runOnUiThread(final Runnable runnable) {
        AsyncTask asyncTask = new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(Object s) {
                super.onPostExecute(s);
                if (runnable != null) {
                    runnable.run();
                }
            }
        };
        asyncTask.executeOnExecutor(Executors.newFixedThreadPool(5));
    }
}
