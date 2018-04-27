package com.cb.ffmpeg.common;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.cb.ffmpeg.jniinterface.FFmpegBridge;

import java.io.File;

public class JianXiCamera {
    /**
     * 应用包名
     */
    private static String mPackageName;
    /**
     * 应用版本名称
     */
    private static String mAppVersionName;
    /**
     * 应用版本号
     */
    private static int mAppVersionCode;
    /**
     * 视频缓存路径
     */
    private static String mVideoCachePath;

    /**
     * 执行FFMPEG命令保存路径
     */
    public final static String FFMPEG_LOG_FILENAME_TEMP = "jx_ffmpeg.log";

    /**
     * @param debug   debug模式
     * @param logPath 命令日志存储地址
     */
    public static void initialize(boolean debug, String logPath) {

        if (debug && TextUtils.isEmpty(logPath)) {
            logPath = mVideoCachePath + "/" + FFMPEG_LOG_FILENAME_TEMP;
        } else if (!debug) {
            logPath = null;
        }
        FFmpegBridge.initJXFFmpeg(debug, logPath);

    }


    /**
     * 获取视频缓存文件夹
     */
    public static String getVideoCachePath() {
        return mVideoCachePath;
    }

    /**
     * 设置视频缓存路径
     */
    public static void setVideoCachePath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        mVideoCachePath = path;

    }

    private static String getSystemFilePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
//            cachePath = context.getExternalCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        } else {
            cachePath = context.getFilesDir().getAbsolutePath();
//            cachePath = context.getCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        }
        return cachePath;
    }

    public static String getCacheFilePath() {
        long key = System.currentTimeMillis();
        //String path = getSystemFilePath(context) +File.separator+ "ffmpeg"+File.separator+ key + File.separator;
        String path = getVideoCachePath() + File.separator + key + File.separator;
        File f = new File(path);
        if (!FileUtils.checkFile(f)) {
            f.mkdirs();
        }
        return path + key;
    }
}
