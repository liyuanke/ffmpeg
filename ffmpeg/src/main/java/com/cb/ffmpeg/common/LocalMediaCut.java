package com.cb.ffmpeg.common;

import android.text.TextUtils;

import com.cb.ffmpeg.jniinterface.FFmpegBridge;
import com.cb.ffmpeg.jniinterface.FFmpegCallBack;
import com.cb.ffmpeg.model.LocalMediaCutConfig;

/**
 * Created by Administrator on 2018/4/19.
 * 本地视频剪切
 */

public class LocalMediaCut {
    /**
     * 开始时间
     */
    private long start;
    /**
     * 截止时间
     */
    private long end;
    /**
     * 输入路径
     */
    private String inputPath;
    /**
     * 输出路径
     */
    private String outputPath;

    public LocalMediaCut(LocalMediaCutConfig cutConfig) {
        this.start = cutConfig.getStart();
        this.end = cutConfig.getEnd();
        this.inputPath = cutConfig.getInputPath();
        this.outputPath = cutConfig.getOutputPath();
    }

    /**
     * 异步执行
     *
     * @param callBack
     */
    public void start(final FFmpegCallBack callBack) {
        AsyncTaskUtil.execute(new Runnable() {
            @Override
            public void run() {
                startCut(callBack);
            }
        });
    }

    private void startCut(FFmpegCallBack callBack) {
        int resultCode = FFmpegBridge.jxFFmpegCMDRun(getCmd());
        if (callBack != null) {
            switch (resultCode) {
                case 0:
                    callBack.onSuccess(outputPath);
                    break;
                default:
                    callBack.onError("");
            }
            callBack.onFinish();
        }
    }

    public String getCmd() {
        if (TextUtils.isEmpty(outputPath)) {
            //默认地址
        }
        return String.format("ffmpeg -ss %s -t %s -i %s -vcodec copy -acodec copy %s", getTime(start), getTime(end - start), inputPath, outputPath);
    }

    private String getTime(long time) {
        if (time > 24 * 3600000) {
            time = 24 * 3600000;
        }
        long hours = time / 3600000;
        time -= time / 3600000 * hours;
        long mins = time / 60000;
        time -= time / 60000 * mins;
        long seconds = time / 1000;
        return new StringBuilder().append(fillZero(hours))
                .append(':')
                .append(fillZero(mins))
                .append(':')
                .append(fillZero(seconds)).toString();
    }

    private String fillZero(long num) {
        return num >= 10 ? String.valueOf(num) : "0" + num;
    }
}
