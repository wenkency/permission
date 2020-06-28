package cn.carhouse.permission;

import cn.carhouse.permission.callback.PermissionListener;

/**
 * 请求权限信息
 */
class PermissionInfo {
    public int requestCode;
    public PermissionListener permissionListener;
    public String[] permissions;

    public PermissionInfo(PermissionListener permissionListener, String[] permissions) {
        this.requestCode = permissionListener.hashCode();
        this.permissionListener = permissionListener;
        this.permissions = permissions;
    }
}
