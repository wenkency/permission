package cn.carhouse.permission.callback;

import android.app.Activity;
import android.app.FragmentManager;

import cn.carhouse.permission.PermissionFragment;

/**
 * 自动移除PermissionFragment，防止内存泄漏
 */
public class PermissionLifecycleCallbacks extends EmptyLifecycleCallbacks {
    private Activity activity;

    public static void register(Activity activity) {
        if (activity == null) {
            return;
        }
        new PermissionLifecycleCallbacks(activity);
    }

    private PermissionLifecycleCallbacks(Activity activity) {
        this.activity = activity;
        activity.getApplication().registerActivityLifecycleCallbacks(this);
    }


    @Override
    public void onActivityDestroyed(Activity activity) {
        if (this.activity != null && activity == this.activity) {
            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
            PermissionFragment fragment = PermissionFragment.findPermissionsFragment(activity);
            if (fragment != null) {
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .remove(fragment)
                        .commitAllowingStateLoss();
            }
            this.activity = null;
        }
    }
}
