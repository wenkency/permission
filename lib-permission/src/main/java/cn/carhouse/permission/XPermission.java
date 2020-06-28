package cn.carhouse.permission;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.util.List;

import cn.carhouse.permission.callback.PermissionLifecycleCallbacks;
import cn.carhouse.permission.callback.PermissionListener;
import cn.carhouse.permission.callback.PermissionListenerAdapter;


/**
 * 6.0权限申请的帮助类
 */

public class XPermission {

    private PermissionFragment mPermissionFragment;// 申请权限的中间Fragment
    private String[] mPermissions;
    private Activity mActivity;
    // 拒绝权限后是否显示设置的Dialog，默认不显示设置
    private boolean isShowSetting = false;
    // 权限拒绝弹提示吐司，默认弹
    private boolean isShowToast = true;

    public XPermission(Activity activity) {
        this.mActivity = activity;
        this.mPermissionFragment = PermissionFragment.getPermissionsFragment(activity);
        PermissionLifecycleCallbacks.register(activity);
    }

    public static XPermission with(Activity activity) {
        return new XPermission(activity);
    }


    /**
     * 要申请的权限
     */
    public XPermission permissions(String... permissions) {
        mPermissions = permissions;
        return this;
    }

    /**
     * 拒绝权限后是否显示设置的Dialog
     */
    public XPermission showSetting(boolean isShowSetting) {
        this.isShowSetting = isShowSetting;
        return this;
    }

    /**
     * 拒绝权限后是否显示弹吐司
     */
    public XPermission showToast(boolean isShowToast) {
        this.isShowToast = isShowToast;
        return this;
    }

    /**
     * 请求申请权限
     */
    @SuppressLint("NewApi")
    public void request(PermissionListener permissionListener) {
        // 如果是空，就默认回调
        if (permissionListener == null) {
            permissionListener = new PermissionListenerAdapter();
        }
        // 1.判断一下是不是6.0以上的系统
        if (!PermissionUtil.isUpAndroidM()) {
            // 执行成功的方法
            mPermissionFragment.onSucceed(permissionListener);
            return;
        }
        // 2.获取未申请的权限列表
        List<String> deniedPermissions = PermissionUtil.getDeniedPermissions(mActivity, mPermissions);
        if (deniedPermissions.size() > 0) {
            // 设置吐司和打开设置页面
            mPermissionFragment.setShowSetting(isShowSetting);
            mPermissionFragment.setShowToast(isShowToast);

            // 3.要去申请权限
            mPermissionFragment.requestPermissions(permissionListener,
                    deniedPermissions.toArray(new String[deniedPermissions.size()]));
        } else {
            // 执行成功的方法
            mPermissionFragment.onSucceed(permissionListener);
        }
    }

    public void request() {
        request(null);
    }


}
