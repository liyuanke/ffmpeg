package com.cb.ffmpeg;

import android.app.Activity;
import android.content.Intent;

import com.cb.ffmpeg.common.Constants;
import com.cb.ffmpeg.common.DeviceUtils;
import com.cb.ffmpeg.common.EditParam;
import com.cb.ffmpeg.common.LocalMediaCompress;
import com.cb.ffmpeg.jniinterface.FFmpegCallBack;
import com.cb.ffmpeg.model.AutoVBRMode;
import com.cb.ffmpeg.model.BaseMediaBitrateConfig;
import com.cb.ffmpeg.model.LocalMediaConfig;
import com.cb.ffmpeg.model.MediaRecorderConfig;
import com.cb.ffmpeg.videocut.ExtractVideoInfoUtil;
import com.cb.ffmpeg.videocut.VideoEditActivity;

import static com.cb.ffmpeg.MediaRecorderActivity.MEDIA_RECORDER_CONFIG_KEY;

/**
 * Created by Administrator on 2018/4/18.
 */

public class FFmpeg {
    private Activity activity;
    private String inputPath;
    private LocalMediaConfig mediaConfig;
    private int requestCode;
    private long maxDuration;//视频最长播放时间
    private int width;//视频宽度
    private int height;//视频高度
    private int maxFrameRate;//最大帧率
    private int videoBitrate;//视频比特率
    /**
     * 录制最长时间
     */
    private int maxRecordTime;
    private MediaRecorderConfig recorderConfig;

    private FFmpeg(Builder builder) {
        this.activity = builder.activity;
        this.inputPath = builder.inputPath;
        this.mediaConfig = builder.mediaConfig;
        this.recorderConfig = builder.recorderConfig;
        this.requestCode = builder.requestCode;
        this.maxDuration = builder.maxDuration;
        this.maxRecordTime = builder.maxRecordTime;
        this.width = builder.width;
        this.height = builder.height;
        this.maxFrameRate = builder.maxFrameRate;
        this.videoBitrate = builder.videoBitrate;
    }

    public static class Builder {
        private Activity activity;
        private String inputPath;
        private LocalMediaConfig mediaConfig;
        private MediaRecorderConfig recorderConfig;
        private int requestCode;
        private long maxDuration = 60 * 1000L;//视频最长播放时间毫秒
        /**
         * 录制最长时间 毫秒
         */
        private int maxRecordTime = 10 * 1000;
        private int width = 640;//视频宽度
        private int height = 960;//视频高度
        private int maxFrameRate = 20;//最大帧率
        private int videoBitrate = 580000;//视频比特率

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setMaxFrameRate(int maxFrameRate) {
            this.maxFrameRate = maxFrameRate;
            return this;
        }

        public Builder setVideoBitrate(int videoBitrate) {
            this.videoBitrate = videoBitrate;
            return this;
        }

        public Builder bind(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder setInputPath(String inputPath) {
            this.inputPath = inputPath;
            return this;
        }

        public Builder setMediaConfig(LocalMediaConfig config) {
            this.mediaConfig = config;
            return this;
        }

        public Builder setRecorderConfig(MediaRecorderConfig recorderConfig) {
            this.recorderConfig = recorderConfig;
            return this;
        }

        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder setMaxDuration(long maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        public Builder setMaxRecordTime(int maxRecordTime) {
            this.maxRecordTime = maxRecordTime;
            return this;
        }

        public FFmpeg build() {
            return new FFmpeg(this);
        }
    }

    /**
     * 压缩视频
     *
     * @param callBack 回调
     */
    public void compress(FFmpegCallBack callBack) {
        if (this.mediaConfig == null) {
            BaseMediaBitrateConfig compressMode = new AutoVBRMode();
            compressMode.setVelocity("ultrafast");
            this.mediaConfig = new LocalMediaConfig.Buidler()
                    .setVideoPath(inputPath)
                    .captureThumbnailsTime(1)
                    .doH264Compress(compressMode)
                    .setFramerate(0)
                    .setScale(1)
                    .build();
        }
        new LocalMediaCompress(this.mediaConfig).start(callBack);
    }


    /**
     * 计算视频长度
     *
     * @param path
     */
    public static long getVideoLength(final String path) {
        return Long.valueOf(new ExtractVideoInfoUtil(path).getVideoLength());
    }


    /**
     * 视频剪切
     */
    public void cut() {
        if (null == activity) {
            throw new NullPointerException("acitivty 必须 bind");
        }
        EditParam param = new EditParam(maxDuration, inputPath);
        Intent intent = new Intent(activity, VideoEditActivity.class);
        intent.putExtra(Constants.PARAM, param);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 视频录制
     */
    public void recordr() {
        if (null == activity) {
            throw new NullPointerException("acitivty 必须 bind");
        }
        if (this.recorderConfig == null) {
            this.recorderConfig = new MediaRecorderConfig.Buidler()
                    .smallVideoWidth(Integer.valueOf(width))
                    .smallVideoHeight(Integer.valueOf(height))
                    .recordTimeMax(Integer.valueOf(maxRecordTime))
                    .maxFrameRate(Integer.valueOf(maxFrameRate))
                    .videoBitrate(Integer.valueOf(videoBitrate))
                    .captureThumbnailsTime(1)
                    .build();
        }
        activity.startActivityForResult(new Intent(activity, MediaRecorderActivity.class).putExtra(MEDIA_RECORDER_CONFIG_KEY, this.recorderConfig), requestCode);
    }

}
