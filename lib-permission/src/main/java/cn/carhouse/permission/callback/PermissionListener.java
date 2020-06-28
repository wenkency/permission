package cn.carhouse.permission.callback;

import java.io.Serializable;

/**
 * 申请权限成功的回调
 */

public interface PermissionListener{
    /**
     * 成功
     */
    void onSucceed();

    /**
     * 用户选择了不再提示
     *
     * @param isCue true:用户选择了不再提示
     */
    void onFailed(boolean isCue);

    /**
     * 最后都调用的
     */
    void onAfter();
}


