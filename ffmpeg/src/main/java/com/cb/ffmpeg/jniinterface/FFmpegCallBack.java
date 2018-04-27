package com.cb.ffmpeg.jniinterface;

/**
 * Created by Administrator on 2018/4/18.
 */

public interface FFmpegCallBack {
    void onSuccess(String outPath);

    void onError(String error);

    void onFinish();
}
