package cn.carhouse.permission;

import android.Manifest;
import android.os.Build;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 权限申请的封装
 */

public class Permission {
    public static final String[] CALENDAR;   // 读写日历。
    public static final String[] CAMERA;     // 相机。
    public static final String[] CONTACTS;   // 读写联系人。
    public static final String[] LOCATION;   // 读位置信息。
    public static final String[] MICROPHONE; // 使用麦克风。
    public static final String[] PHONE;      // 读电话状态、打电话、读写电话记录。
    public static final String[] SENSORS;    // 传感器。
    public static final String[] SMS;        // 读写短信、收发短信。
    public static final String[] STORAGE;    // 读写存储卡。
    public static final String[] STORAGE_11;    // 读写存储卡。

    /**
     * 是否是 Android 11 及以上版本
     */
    static boolean isAndroid11() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    /**
     * 是否是 Android 10 及以上版本
     */
    static boolean isAndroid10() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /**
     * 是否是 Android 9.0 及以上版本
     */
    static boolean isAndroid9() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    /**
     * 是否是 Android 8.0 及以上版本
     */
    static boolean isAndroid8() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 是否是 Android 7.0 及以上版本
     */
    static boolean isAndroid7() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * 是否是 Android 6.0 及以上版本
     */
    static boolean isAndroid6() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static String[] getStorage() {
        if (isAndroid11()) {
            return STORAGE_11;
        }
        return STORAGE;
    }

    static {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            CALENDAR = new String[]{};
            CAMERA = new String[]{};
            CONTACTS = new String[]{};
            LOCATION = new String[]{};
            MICROPHONE = new String[]{};
            PHONE = new String[]{};
            SENSORS = new String[]{};
            SMS = new String[]{};
            STORAGE = new String[]{};
            STORAGE_11 = new String[]{};

        } else {


            CAMERA = new String[]{
                    Manifest.permission.CAMERA};

            STORAGE = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};

            STORAGE_11 = new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE};

            CALENDAR = new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR};

            CONTACTS = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS};

            LOCATION = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};

            MICROPHONE = new String[]{
                    Manifest.permission.RECORD_AUDIO};

            PHONE = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE};

            SENSORS = new String[]{
                    Manifest.permission.BODY_SENSORS};

            SMS = new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS};


        }
    }

    public static String getMsg(String permission) {
        if (TextUtils.isEmpty(permission)) {
            return null;
        }
        List<String> ps = Arrays.asList(STORAGE);
        if (ps.contains(permission)) {
            return "使用存储卡";
        }
        ps = Arrays.asList(STORAGE_11);
        if (ps.contains(permission)) {
            return "使用存储卡";
        }
        ps = Arrays.asList(CAMERA);
        if (ps.contains(permission)) {
            return "使用相机";
        }
        ps = Arrays.asList(CONTACTS);
        if (ps.contains(permission)) {
            return "读写联系人";
        }
        ps = Arrays.asList(SMS);
        if (ps.contains(permission)) {
            return "读写短信息";
        }
        ps = Arrays.asList(CALENDAR);
        if (ps.contains(permission)) {
            return "使用日历";
        }

        ps = Arrays.asList(LOCATION);
        if (ps.contains(permission)) {
            return "获取位置信息";
        }
        ps = Arrays.asList(MICROPHONE);
        if (ps.contains(permission)) {
            return "使用麦克风";
        }
        ps = Arrays.asList(PHONE);
        if (ps.contains(permission)) {
            return "获取电话状态";
        }
        ps = Arrays.asList(SENSORS);
        if (ps.contains(permission)) {
            return "使用传感器";
        }
        return null;
    }


}
