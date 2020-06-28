package cn.carhouse.permision_sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import cn.carhouse.permission.Permission;
import cn.carhouse.permission.XPermission;
import cn.carhouse.permission.callback.PermissionListenerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void request(View view) {
        XPermission.with(this)
                .permissions(Permission.STORAGE)
                .isShowSetting(false) // 不显示设置Dialog，默认拒绝后显示的
                .request(new PermissionListenerAdapter() {
                    @Override
                    public void onSucceed() {
                        Log.e("TAG", "申请STORAGE成功");
                    }

                    @Override
                    public void onFailed(boolean isCue) {
                        super.onFailed(isCue);
                        Log.e("TAG", "Permission.STORAGE:" + isCue);
                    }
                });

        XPermission.with(this)
                .permissions(Permission.PHONE)
                .request(new PermissionListenerAdapter() {
                    @Override
                    public void onSucceed() {
                        Log.e("TAG", "申请PHONE成功");
                    }

                    @Override
                    public void onFailed(boolean isCue) {
                        Log.e("TAG", "Permission.PHONE:" + isCue);
                    }
                });

        XPermission.with(this)
                .permissions(Permission.CAMERA)
                .request(new PermissionListenerAdapter() {
                    @Override
                    public void onSucceed() {
                        Log.e("TAG", "申请CAMERA成功");
                    }

                    @Override
                    public void onFailed(boolean isCue) {
                        Log.e("TAG", "Permission.CAMERA:" + isCue);
                    }
                });

    }
}
