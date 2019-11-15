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


implementation 'com.github.wenkency:permistion:1.0.0'

```

### 使用方式
```
 XPermission.with(this)
                .permissions(Permission.STORAGE)
                .request(new PermissionListenerAdapter() {
                    @Override
                    public void onSucceed() {
                        Toast.makeText(MainActivity.this, "申请成功", Toast.LENGTH_SHORT).show();
                    }
                });
```

### 运行结果

<img src="screenshot/image.jpg" width="360px"/>