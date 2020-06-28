# Permission
6.0权限申请类，用于权限申请。

### 引入

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


implementation 'com.github.wenkency:permission:1.1.0'

```

### 使用方式
```
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
```

### 运行结果

<img src="screenshot/image.jpg" width="360px"/>