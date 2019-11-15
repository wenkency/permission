package cn.carhouse.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请权限的帮助工具类
 */

class PermissionUtils {
    /**
     * 获取用户未授权的权限列表
     */
    public static List<String> getDeniedPermissions(Activity activity, String... permissions) {
        List<String> list = new ArrayList<>();
        if (permissions == null) {
            return list;
        }
        for (String p : permissions) {
            int permission = ContextCompat.checkSelfPermission(activity, p);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                list.add(p);
            }

        }
        return list;
    }


    /**
     * 判断版本是不是大于6.0
     */
    public static boolean isUpAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


}
