##采用ffmpeg实现视频的压缩、剪切，并提供视频录制功能

####配置权限
```
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 ```
####初始化
```   
   public static void initSmallVideo() {
        // 设置拍摄视频缓存路径
        *File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim + "/zero/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/zero/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim + "/zero/");
        }
        // 初始化拍摄
        JianXiCamera.initialize(false, null);
    }
```

####视频压缩<br>
```
    private void compress(String path) {
        FFmpeg fFmpeg = new FFmpeg.Builder()
                                  .setInputPath(path)
                                  .build();
        fFmpeg.compress(new FFmpegCallBack() {//callback回调在主线程运行
        @Override
        public void onSuccess(String outPath) {
                  Log.i(TAG, outPath);
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFinish() {
            ViewUtils.dismissProgress();
        }
    }); }
```
 ####视频压缩也可以单独配置LocalMediaConfig
 ```
    LocalMediaConfig mediaConfig = new LocalMediaConfig.Buidler()
                     .setVideoPath(inputPath)
                     .captureThumbnailsTime(1)
                     .doH264Compress(compressMode)
                     .setFramerate(0)
                     .setScale(1)
                     .build();

    FFmpeg fFmpeg = new FFmpeg.Builder()
                    .setMediaConfig(mediaConfig)
                    .build();
```
####视频录制
```
    FFmpeg fFmpeg = new FFmpeg.Builder()
                          .bind(this)
                          .setRequestCode(RECORD_CODE)//录制请求码
                          .setMaxRecordTime(60_000)//录制最长时间
                          .build();
    fFmpeg.recordr();
    ```
####同样也可以自己配置MediaRecorderConfig
    MediaRecorderConfig recorderConfig = new MediaRecorderConfig.Buidler()
                    .smallVideoWidth(320)
                    .smallVideoHeight(480)
                    .recordTimeMax(60_000)
                    .maxFrameRate(20)
                    .videoBitrate(580000)
                    .captureThumbnailsTime(1)
                    .build();

####视频剪切
```
    private void cut(String path){
        FFmpeg fFmpeg = new FFmpeg.Builder()
                                  .bind(this)
                                  .setRequestCode(CUT_CODE)
                                  .setMaxDuration(10*1000)
                                  .setInputPath(path)
                                  .build();
        fFmpeg.cut();
    }
```
####视频剪切、视频录制返回视频路径
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == RECORD_CODE) {
                if (data != null) {
                    Log.i(TAG, data.getStringExtra(Constants.RESULT));//视频录制路径
                }
          }
          else if (requestCode == CUT_CODE) {
                if (data != null) {
                    Log.i(TAG, data.getStringExtra(Constants.RESULT));//视频剪切路径
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    ```

Edit By [MaHua](http://mahua.jser.me)
