package cn.carhouse.permission;

import cn.carhouse.permission.callback.PermissionListener;

/**
 * 请求权限信息
 */
class PermissionInfo {
    public int requestCode;
    public PermissionListener permissionListener;
    public String[] permissions;
    // 拒绝权限后是否显示设置的Dialog，默认不显示设置
    public boolean isShowSetting = false;
    // 权限拒绝弹提示吐司，默认弹
    public boolean isShowToast = true;

    public PermissionInfo(PermissionListener permissionListener,
                          String[] permissions) {
        this.requestCode = permissionListener.hashCode();
        this.permissionListener = permissionListener;
        this.permissions = permissions;
    }
}
