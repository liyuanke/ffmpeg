package com.cb.ffmpegtest.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.cb.ffmpeg.FFmpeg;
import com.cb.ffmpeg.common.Constants;
import com.cb.ffmpeg.common.DeviceUtils;
import com.cb.ffmpeg.common.JianXiCamera;
import com.cb.ffmpeg.common.UriUtils;
import com.cb.ffmpeg.common.ViewUtils;
import com.cb.ffmpeg.jniinterface.FFmpegCallBack;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int PERMISSION_REQUEST_CODE = 0x001;
    private final int CHOOSE_CODE = 0x000520;
    private final int RECORD_CODE = 0x000521;
    private final int CUT_CODE = 0x000522;
    private final int CHOOSE_CUT_CODE = 0x000523;
    private RadioGroup rg_aspiration;
    private Button btnFunc;
    private final String TAG = getClass().getSimpleName();
    private int type = 0;
    private String[] funcs = {"开始录制", "开始压缩", "开始剪切"};
    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSmallVideo();
        initView();
        initEvent();
        permissionCheck();
    }

    private void initEvent() {
        rg_aspiration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_recorder:
                        type = 0;
                        break;
                    case R.id.rb_local:
                        type = 1;
                        break;
                    case R.id.rb_cut:
                        type = 2;
                        break;
                }
                btnFunc.setText(funcs[type]);
            }
        });

    }

    private void initView() {
        rg_aspiration = (RadioGroup) findViewById(R.id.rg_aspiration);
        btnFunc = findViewById(R.id.bt_start);
        btnFunc.setOnClickListener(this);
        notifyFileSystemChanged(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/");
    }

    private void notifyFileSystemChanged(String path) {
        if (path == null)
            return;
        final File f = new File(path);
        if (Build.VERSION.SDK_INT >= 19) { //添加此判断，判断SDK版本是不是4.4或者高于4.4
            String[] paths = new String[]{path};
            MediaScannerConnection.scanFile(this, paths, null, null);
        } else {
            final Intent intent;
            if (f.isDirectory()) {
                intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
                Log.v("TAG", "directory changed, send broadcast:" + intent.toString());
            } else {
                intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(new File(path)));
                Log.v("TAG", "file changed, send broadcast:" + intent.toString());
            }
            sendBroadcast(intent);
        }
    }

    /**
     * 选择本地视频，为了方便我采取了系统的API，所以也许在一些定制机上会取不到视频地址，
     * 所以选择手机里视频的代码根据自己业务写为妙。
     *
     * @param v
     */
    public void choose(View v) {
        Intent it = new Intent(Intent.ACTION_GET_CONTENT,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        it.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        startActivityForResult(it, CHOOSE_CODE);
    }

    /**
     * 视频剪切
     *
     * @param v
     */
    public void cut(View v) {

        Intent it = new Intent(Intent.ACTION_GET_CONTENT,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        it.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        startActivityForResult(it, CHOOSE_CUT_CODE);

    }

    /**
     * 录像
     *
     * @param c
     */
    public void recorder(View c) {
        FFmpeg fFmpeg = new FFmpeg.Builder()
                .bind(this)
                .setRequestCode(RECORD_CODE)
                .setMaxRecordTime(10_000)
                .build();
        fFmpeg.recordr();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (Manifest.permission.CAMERA.equals(permissions[i])) {
                        // setSupportCameraSize();
                    } else if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {

                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_CODE) {
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    String path = UriUtils.getPath(this, uri);
                    compress(path);
                    Log.i(TAG, path);
                }
            } else if (requestCode == RECORD_CODE) {
                if (data != null) {
                    ViewUtils.toast(MainActivity.this, "录制成功,路径:" + data.getStringExtra(Constants.RESULT));
                    Log.i(TAG, data.getStringExtra(Constants.RESULT));
                }
            } else if (requestCode == CHOOSE_CUT_CODE) {
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    String path = UriUtils.getPath(this, uri);
                    cut(path);
                    Log.i(TAG, path);
                }
            } else if (requestCode == CUT_CODE) {
                if (data != null) {
                    Log.i(TAG, data.getStringExtra(Constants.RESULT));
                    ViewUtils.toast(MainActivity.this, "剪切成功,路径:" +data.getStringExtra(Constants.RESULT));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 视频压缩
     *
     * @param path
     */
    private void compress(String path) {
        ViewUtils.showProgress(this, "", "正在压缩", 0);
        FFmpeg fFmpeg = new FFmpeg.Builder().setInputPath(path).build();
        fFmpeg.compress(new FFmpegCallBack() {
            @Override
            public void onSuccess(String outPath) {
                ViewUtils.toast(MainActivity.this, "压缩成功,路径:" + outPath);
                Log.i(TAG, outPath);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onFinish() {
                ViewUtils.dismissProgress();
            }
        });


    }

    /**
     * 视频剪切
     */
    private void cut(String path) {
        FFmpeg fFmpeg = new FFmpeg.Builder().bind(this).setRequestCode(CUT_CODE).setMaxDuration(10 * 1000).setInputPath(path).build();
        fFmpeg.cut();
    }


    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean permissionState = true;
            for (String permission : permissionManifest) {
                if (ContextCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionState = false;
                }
            }
            if (!permissionState) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * 初始化
     */
    public static void initSmallVideo() {
        // 设置拍摄视频缓存路径
        File dcim = Environment
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

    @Override
    public void onClick(View v) {
        switch (type) {
            case 0:
                recorder(v);
                break;
            case 1:
                choose(v);
                break;
            case 2:
                cut(v);
                break;
            default:
        }
    }
}
