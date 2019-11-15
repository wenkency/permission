package cn.carhouse.permission;

/**
 * 申请权限成功的回调
 */

public interface PermissionListener {
    void onSucceed();

    /**
     * 用户选择了不再提示
     */
    void onFailed(boolean isCue);

    /**
     * 最后都调用的
     */
    void onAfter();
}


