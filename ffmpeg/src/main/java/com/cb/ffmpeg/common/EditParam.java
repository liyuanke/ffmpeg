package com.cb.ffmpeg.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/4/19.
 */

public class EditParam implements Parcelable{
    private long minCutDuration = 3 * 1000L;// 最小剪辑时间3s
    private long maxCutDuration = 60 * 1000L;//视频最多剪切多长时间
    private int maxCountRange = 10;//seekbar的区域内一共有多少张图片
    private String inputPath;
    private String outputPath;

    protected EditParam(Parcel in) {
        minCutDuration = in.readLong();
        maxCutDuration = in.readLong();
        maxCountRange = in.readInt();
        inputPath = in.readString();
        outputPath = in.readString();
    }

    public static final Creator<EditParam> CREATOR = new Creator<EditParam>() {
        @Override
        public EditParam createFromParcel(Parcel in) {
            return new EditParam(in);
        }

        @Override
        public EditParam[] newArray(int size) {
            return new EditParam[size];
        }
    };

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public EditParam(String inputPath) {
        this.inputPath = inputPath;
    }

    public EditParam(long minCutDuration, long maxCutDuration, String inputPath) {
        this.minCutDuration = minCutDuration;
        this.maxCutDuration = maxCutDuration;
        this.inputPath = inputPath;
    }

    public EditParam(long maxCutDuration, String inputPath) {
        this.maxCutDuration = maxCutDuration;
        this.inputPath = inputPath;
    }

    public EditParam(long minCutDuration, long maxCutDuration, int maxCountRange, String inputPath) {
        this.minCutDuration = minCutDuration;
        this.maxCutDuration = maxCutDuration;
        this.maxCountRange = maxCountRange;
        this.inputPath = inputPath;
    }


    public long getMinCutDuration() {
        return minCutDuration;
    }

    public void setMinCutDuration(long minCutDuration) {
        this.minCutDuration = minCutDuration;
    }

    public long getMaxCutDuration() {
        return maxCutDuration;
    }

    public void setMaxCutDuration(long maxCutDuration) {
        this.maxCutDuration = maxCutDuration;
    }

    public int getMaxCountRange() {
        return maxCountRange;
    }

    public void setMaxCountRange(int maxCountRange) {
        this.maxCountRange = maxCountRange;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(minCutDuration);
        dest.writeLong(maxCutDuration);
        dest.writeInt(maxCountRange);
        dest.writeString(inputPath);
        dest.writeString(outputPath);
    }
}
