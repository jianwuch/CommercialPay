package com.gsd.idreamsky.weplay.utils.permissions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限的基础类
 * @author bzai.xiao
 */
public class PermissionsUtils {

	public final int REQUEST_CODE = 0x1450; // 授权请求码(可以自定义)

	private final Context mContext;

	public PermissionsUtils(Context context){
		this.mContext = context;
	}

    /**
     * 1.1、判断权限是否已注册,缺失
     * @param permissions
     * @return
     */
    public boolean PermissionLacked(String... permissions){

    	 for (String permission : permissions) {

			if (PermissionLacked(permission)) {
				return true;
			}
		 }

    	 return false;

    }

    /**
     * 判断权限没有在AndroidManifest中注册，没有注册就返回true。
     * 默认存在为false
     * @param permission
     * @return
     */
    public boolean PermissionLacked(String permission){

    	try {

    		List<String> permissonsList = new ArrayList<String>();
			PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_PERMISSIONS);
			if (packageInfo.requestedPermissions != null) {
				for (String mPermission : packageInfo.requestedPermissions) {
					permissonsList.add(mPermission);
				}
				if (permissonsList.contains(permission)) {
					return false;
				}else {
					return true;
				}

			}else {
				return true;
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	return false;
    }

    /**
     * 1.2、判断是否缺少权限
     * @param permissions
     * @return
     */
    public boolean PermissionsLacked(String... permissions) {
        for (String permission : permissions) {
            if (PermissionIsDeclined(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 1.3、请求权限
     * @param requestCode
     * @param permissions
     */
    public void Authorization(Activity activity,int requestCode, String[] permissions){
    	ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 针对6.0系统、编译版本低于23的时候，检测是否缺失权限
     * 缺失时，返回true
     * @param permissions
     * @return
     */
    public boolean checkPermission(String... permissions){

    	for (String permission : permissions) {

    		if (checkPermission(permission)) {
				return true;
			}
    	}

    	return false;
    }

    /**
     * 针对6.0系统、编译版本低于23的时候，检测是否缺失权限
     * 缺失时，返回true
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission){

    	int op = PermissionsConfig.getPermissionsOps(permission);
    	int result = GetPermissionAuthorization(mContext, op);

    	if (result == 1) {
			return true;
		}
    	return false;
    }

    /**
     * 返回授权结果
     * @param permission
     * @return 0 为允许，1为拒绝
     */
    public int GetPermissionAuthorization(String permission){

    	if (PermissionIsGranted(permission)) {
			return 0;
		}else {
			return 1;//本来6.0返回的是-1.为了统一回调，设置为1
		}
    }

    /**
     * 返回授权结果
     * @param context
     * @param op
     * @return 0 为允许 ，1 为拒绝
     */
	@SuppressLint("NewApi")
	public int GetPermissionAuthorization(Context context, int op) {
	    final int version = Build.VERSION.SDK_INT;
	    if (version >= 19) {
	        try {
	            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

	            Method dispatchMethod = AppOpsManager.class.getMethod("checkOp",
	                    new Class[] { int.class, int.class,String.class });

	            int mode = (Integer) dispatchMethod.invoke(
	                    appOpsManager,
	                    new Object[] { op, Binder.getCallingUid(),context.getPackageName() });
	            return mode;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return -1;
	}

    //权限已授权
    public boolean PermissionIsGranted(String permission){
    	return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
    }

    //权限未授权
    public boolean PermissionIsDeclined(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
    }

    //解释为什么需要权限
    public boolean PermissionIsExplanation(Activity activity,String permission){

    	// 如果应用之前请求过该权限，但拒绝了，这是返回true,但是用户在拒绝的同时，在系统的对话框中选择了 “不再提示”，则返回false,当再次请求时都会拒绝
    	return ActivityCompat.shouldShowRequestPermissionRationale(activity,permission);
    }

    //永久未授权,如果是永久拒绝，则再次请求时，则不会弹出请求框，这是只能引导用户手动开启
    public boolean PermissionPermanentlyDenied(Activity activity,String permission){
    	return PermissionIsDeclined(permission) && !PermissionIsExplanation(activity,permission);
    }
}
