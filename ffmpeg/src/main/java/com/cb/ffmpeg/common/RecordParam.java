package com.cb.ffmpeg.common;

/**
 * Created by Administrator on 2018/4/20.
 */

public class RecordParam {
    /**
     * 录制最长时间
     */
    private int maxRecordTime;

    public RecordParam(int maxRecordTime) {
        this.maxRecordTime = maxRecordTime;
    }

    public int getMaxRecordTime() {
        return maxRecordTime;
    }

    public void setMaxRecordTime(int maxRecordTime) {
        this.maxRecordTime = maxRecordTime;
    }
}
