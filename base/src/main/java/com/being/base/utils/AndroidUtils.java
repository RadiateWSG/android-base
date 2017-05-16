package com.being.base.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * 和Android系统相关的工具类
 * Created by zhangpeng on 16/1/11.
 */
public class AndroidUtils {
    public static void showKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isKeyboardShowed(View view) {
        if (view == null) {
            return false;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputManager.isActive(view);
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void openAlbum(Activity activity, File file, boolean crop, int ox, int oy, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);//ACTION_GET_CONTENT ACTION_PICK
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (file != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                "image/*");
        if (crop) {
            cropIntent(intent, 1, 1, ox, oy);
        }
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openCamera(Activity activity, File file, boolean crop, int ox, int oy, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (crop) {
            cropIntent(intent, 1, 1, ox, oy);
        }
        activity.startActivityForResult(intent, requestCode); //启动系统拍照页面
    }

    public static void openAlbum(Fragment fragment, File file, boolean crop, int ox, int oy, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);//ACTION_GET_CONTENT ACTION_PICK
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (file != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                "image/*");
        if (crop) {
            cropIntent(intent, 1, 1, ox, oy);
        }
        intent.putExtra("return-data", false);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void openCamera(Fragment fragment, File file, boolean crop, int ox, int oy, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (crop) {
            cropIntent(intent, 1, 1, ox, oy);
        }
        fragment.startActivityForResult(intent, requestCode); //启动系统拍照页面
    }

    public static void mediaScan(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static void recordVideo(Activity activity, File file, int quality, int durationSecond, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (Build.MANUFACTURER.contains("LG")) {
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 30 * 1024 * 1024);
        } else {
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationSecond);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    private static void cropIntent(Intent intent, int ax, int ay, int ox, int oy) {
        intent.putExtra("crop", "true");
        if (ax > 0) {
            intent.putExtra("aspectX", ax);
        }
        if (ay > 0) {
            intent.putExtra("aspectY", ay);
        }
        if (ox > 0) {
            intent.putExtra("outputX", ox);
        }
        if (oy > 0) {
            intent.putExtra("outputY", oy);
        }
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (uri.getScheme().contains("file")) {
            return uri.getPath();
        }
        Uri contentUri = uri;

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            if (Build.VERSION.SDK_INT > 19) {
                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(contentUri);
                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                cursor = context.getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, sel, new String[]{id}, null);
            } else {
                cursor = context.getContentResolver().query(contentUri,
                        projection, null, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            cursor = context.getContentResolver().query(contentUri,
                    projection, null, null, null);
        }

        if (cursor == null) {
            return null;
        }

        String path = null;
        try {
            int column_index = cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static void sendSms(Context context, String content) {
        try {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("sms_body", content);
            context.startActivity(smsIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendEmail(Context context, String content) {
        try {
            Intent data = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
            data.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openMarket(Context context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * 解析选择图片或者拍照后的resulto
     *
     * @param context
     * @param selFile     设置的文件
     * @param requestCode
     * @param resultCode
     * @param data
     * @param camera      请求拍照或者录像的requestCode
     * @param album       请求相册的requestCode
     * @return
     */
    @Nullable
    public static String parseActivityMediaResult(Context context, File selFile, int requestCode,
                                                int resultCode, Intent data, int camera,
                                                int album) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return selFile.getPath();
        }
        Uri photoUri = null;
        if (requestCode == camera) {
            if (data != null && data.getAction() != null) {//用line等第三方camera可能action为0
                photoUri = Uri.parse(data.getAction());
            } else {
                photoUri = Uri.fromFile(selFile);
            }
        } else if (requestCode == album) {
            if (data != null) {
                photoUri = data.getData();
            } else {
                photoUri = Uri.fromFile(selFile);
            }
        }
        String pathFromURI = getRealPathFromURI(context, photoUri);
        if (pathFromURI != null) {
            return pathFromURI;
        }
        return selFile.getPath();
    }

    /**
     * 启用系统播放器播放网络流媒体视频
     * @param context
     * @param url
     */
    public static void openVideoPlayer(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "video/mp4");
        context.startActivity(intent);
    }

    /**
     * 使用系统播放器播放本地视频文件
     * @param context
     * @param file
     */
    public static void openVideoPlayer(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "video/mp4");
        context.startActivity(intent);
    }

    /**
     * 获取当前进程名
     * @return
     */
    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取该pid对应的进程名
     * @param cxt 上下文环境
     * @param pid 进程pid
     * @return
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return getProcessName();
    }

    /**
     * Returns true if the device is in Doze/Idle mode. Should be called before checking the network connection because
     * the ConnectionManager may report the device is connected when it isn't during Idle mode.
     */
    @TargetApi(23)
    private static boolean isDozing(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            final String myPackageName = context.getPackageName();
            return !powerManager.isIgnoringBatteryOptimizations(myPackageName) && powerManager.isDeviceIdleMode();
        } else {
            return false;
        }
    }
}
