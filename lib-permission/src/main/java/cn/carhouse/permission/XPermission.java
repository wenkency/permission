package cn.carhouse.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;


import java.util.List;


/**
 * 6.0权限申请的帮助类
 */

public class XPermission {
    private PermissionFragment mPermissionFragment;// 申请权限的中间Fragment
    private String[] mPermissions;
    private static final String TAG = "XPermission";
    private Activity mActivity;
    // 拒绝权限后是否显示设置的Dialog
    private boolean isShowSetting = true;

    public XPermission(Activity activity) {
        this.mActivity = activity;
        this.mPermissionFragment = getPermissionsFragment(activity);
        if (mActivity != null) {
            mActivity.getApplication().registerActivityLifecycleCallbacks(new LifecycleCallbacks() {
                @Override
                public void onActivityDestroyed(Activity activity) {
                    try {
                        if (mActivity != null && activity == mActivity) {
                            mActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
                            PermissionFragment fragment = findPermissionsFragment(mActivity);
                            if (fragment != null) {
                                FragmentManager fragmentManager = mActivity.getFragmentManager();
                                fragmentManager
                                        .beginTransaction()
                                        .remove(fragment)
                                        .commitAllowingStateLoss();
                                fragmentManager.executePendingTransactions();
                            }
                            mPermissionFragment = null;
                            mActivity = null;
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static XPermission with(Activity activity) {
        return new XPermission(activity);
    }

    private PermissionFragment getPermissionsFragment(Activity activity) {
        PermissionFragment permissionsFragment = findPermissionsFragment(activity);
        boolean isNewInstance = permissionsFragment == null;
        if (isNewInstance) {
            permissionsFragment = new PermissionFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(permissionsFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return permissionsFragment;
    }

    /**
     * 去找权限的Fragment
     */
    private PermissionFragment findPermissionsFragment(Activity activity) {
        return (PermissionFragment) activity.getFragmentManager().findFragmentByTag(TAG);
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
    public XPermission isShowSetting(boolean isShowSetting) {
        this.isShowSetting = isShowSetting;
        return this;
    }

    /**
     * 请求申请权限
     */
    @SuppressLint("NewApi")
    public void request(PermissionListener permissionListener) {
        mPermissionFragment.setPermissionListener(permissionListener);
        // 1.判断一下是不是6.0以上的系统
        if (!PermissionUtils.isUpAndroidM()) {
            // 执行成功的方法
            mPermissionFragment.onSucceed();
            return;
        }
        // 2.获取未申请的权限列表
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mActivity, mPermissions);
        if (deniedPermissions.size() > 0) {
            // 3.要去申请权限
            mPermissionFragment.setShowSetting(isShowSetting);
            mPermissionFragment.requestPermissions(mActivity, deniedPermissions.toArray(new String[deniedPermissions.size()]));
        } else {
            // 执行成功的方法
            mPermissionFragment.onSucceed();

        }
    }

    public void request() {
        request(null);
    }


}
