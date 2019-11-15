package cn.carhouse.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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

import cn.carhouse.permission.setting.SettingUtil;


/**
 * 申请权限的Fragment
 */

@SuppressLint("ValidFragment")
public class PermissionFragment extends Fragment {
    /**
     * 请求码
     */
    private static final int CODE = 66;
    /**
     * 回调
     */
    private PermissionListener mPermissionListener;

    private Activity mActivity;
    /**
     * 拒绝权限后是否显示设置的Dialog
     */
    private boolean isShowSetting = true;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * 设置成功回调的监听
     */
    public void setPermissionListener(PermissionListener permissionListener) {
        mPermissionListener = permissionListener;
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
                        SettingUtil.go2Setting(mActivity);
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
    public void requestPermissions(Activity activity, String[] permissions) {
        this.mActivity = activity;
        // 所有的权限都同意了
        if (PermissionUtil.hasSelfPermissions(activity, permissions)) {
            // 执行成功的方法
            onSucceed();
        } else {
            // 申请权限
            requestPermissions(permissions, CODE);
        }
    }

    /**
     * 授权回调
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 请求码不对就return
        if (requestCode != CODE) {
            return;
        }
        //所有权限都同意
        if (PermissionUtil.verifyPermissions(grantResults)) {
            // 执行成功的方法
            onSucceed();
        } else {
            // 找到第一个不同意的权限
            String refusePermission = PermissionUtil.getRefusePermission(mActivity, permissions);
            if (!PermissionUtil.shouldShowRequestPermissionRationale(mActivity, permissions)) {
                // 权限被拒绝并且选中不再提示
                if (permissions.length != grantResults.length) {
                    showToast(refusePermission, "权限被拒绝");
                    onFailed(false);
                    return;
                }
                // 显示Dialog，让用户去设置打开
                if (isShowSetting) {
                    showDialog(refusePermission);
                }
                onFailed(true);
            } else {
                //权限被取消
                showToast(refusePermission, "权限被拒绝");
                onFailed(false);
            }

        }

    }

    /**
     * 成功回调
     */
    public void onSucceed() {

        if (mPermissionListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPermissionListener.onSucceed();
                    mPermissionListener.onAfter();
                }
            });

        }
    }

    public void onFailed(final boolean isCue) {
        if (mPermissionListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPermissionListener.onFailed(isCue);
                    mPermissionListener.onAfter();
                }
            });
        }
    }


    @Override
    public void onDestroy() {
        mActivity = null;
        mPermissionListener = null;
        super.onDestroy();
    }


    public void setShowSetting(boolean isShowSetting) {
        this.isShowSetting = isShowSetting;
    }
}
