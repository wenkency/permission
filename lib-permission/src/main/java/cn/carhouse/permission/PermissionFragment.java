package cn.carhouse.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

import cn.carhouse.permission.callback.PermissionListener;


/**
 * 申请权限的Fragment
 */

@SuppressLint("ValidFragment")
public class PermissionFragment extends Fragment {
    private static final String TAG = PermissionFragment.class.getSimpleName();
    /**
     * 拒绝权限后是否显示设置的Dialog
     */
    private boolean isShowSetting = true;

    private Activity mActivity;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Map<Integer, PermissionInfo> listeners = new HashMap<>(2);
    private static final int REQUEST_CODE = 111;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * 显示Toast
     */
    private void showToast(String refusePermission, String text) {
        if (!TextUtils.isEmpty(refusePermission)) {
            text = refusePermission + text;
        }
        Toast.makeText(mActivity, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示设置的Dialog
     */
    private void showDialog(String msg) {
        if (!isShowSetting) {
            return;
        }
        String text = "请在设置中开启权限，以正常使用应用功能。";
        if (!TextUtils.isEmpty(msg)) {
            text = "请在设置中开启" + msg + "权限，以正常使用应用功能。";
        }
        new AlertDialog.Builder(mActivity)
                .setTitle("权限申请")
                .setMessage(text)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 打开应用设置页面
                        SettingUtil.openSetting(mActivity);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    /**
     * 申请权限
     */
    @SuppressLint("NewApi")
    public void requestPermissions(PermissionListener permissionListener, String[] permissions) {
        // 所有的权限都同意了
        if (PermissionUtil.hasSelfPermissions(mActivity, permissions)) {
            // 执行成功的方法
            onSucceed(permissionListener);
        } else {
            PermissionInfo info = new PermissionInfo(permissionListener, permissions);
            listeners.put(info.requestCode, info);
            // 申请权限
            requestPermissions(permissions, info.requestCode);
        }
    }

    /**
     * 授权回调
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 申请权限，没有回调的
        if (requestCode == REQUEST_CODE) {
            return;
        }
        // 请求码不对的
        PermissionInfo info = listeners.get(requestCode);
        if (info == null) {
            return;
        }
        // 所有权限都同意
        if (PermissionUtil.hasSelfPermissions(mActivity, info.permissions)) {
            // 执行成功的方法
            onSucceed(info.permissionListener);
        } else {
            // 找到第一个不同意的权限
            String refusePermission = PermissionUtil.getRefusePermission(mActivity, info.permissions);
            // 权限被拒绝并且选中不再提示
            if (!PermissionUtil.shouldShowRequestPermissionRationale(mActivity, info.permissions)) {
                showToast(refusePermission, "权限被拒绝");
                // 显示Dialog，让用户去设置打开
                if (isShowSetting) {
                    showDialog(refusePermission);
                }
                onFailed(info.permissionListener, true);
            } else {
                // 权限被拒绝
                showToast(refusePermission, "权限被拒绝");
                onFailed(info.permissionListener, false);
            }
        }

    }

    /**
     * 成功回调
     */
    public void onSucceed(final PermissionListener permissionListener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                permissionListener.onSucceed();
                permissionListener.onAfter();
            }
        });
    }

    public void onFailed(final PermissionListener permissionListener, final boolean isCue) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                permissionListener.onFailed(isCue);
                permissionListener.onAfter();
            }
        });
    }


    @Override
    public void onDestroy() {
        mActivity = null;
        super.onDestroy();
    }


    public void setShowSetting(boolean isShowSetting) {
        this.isShowSetting = isShowSetting;
    }


    /**
     * 获取PermissionsFragment
     */
    public static PermissionFragment getPermissionsFragment(Activity activity) {
        PermissionFragment permissionsFragment = findPermissionsFragment(activity);
        if (permissionsFragment == null) {
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
     * 查找PermissionsFragment
     */
    public static PermissionFragment findPermissionsFragment(Activity activity) {
        return (PermissionFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }
}
