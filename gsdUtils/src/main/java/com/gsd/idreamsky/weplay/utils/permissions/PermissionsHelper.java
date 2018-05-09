package com.gsd.idreamsky.weplay.utils.permissions;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class PermissionsHelper{

	public static final String TAG = "PermissionsHelper";

	private volatile static PermissionsHelper permissionsHelper;
	private PermissionsHelper(){
	}

	public static PermissionsHelper getPermissionsHelper(){
		if (permissionsHelper == null) {
			synchronized(PermissionsHelper.class){
				if (permissionsHelper == null) {
					permissionsHelper = new PermissionsHelper();
				}
			}
		}
		return permissionsHelper;
	}

	private static Context mContext;
	private String[] mPermissions;
	private onPermissionsResultLister mLister;
	private PermissionsUtils mPermissionsUtils;

	private boolean flag; // 通过标志位来区分不同的回调
	private boolean isonRestart = false;//通过开关来打开app设置授权界面的回调


	/**
	 * 请求权限
	 * @param activity
	 *        上下文
	 * @param permissions
	 *        权限列表
	 * @param prl
	 *        授权回调
	 */
	@TargetApi(Build.VERSION_CODES.M)
	public void onPermissionsAuthorization(Activity activity , final String[] permissions , onPermissionsResultLister prl){

		mContext = activity.getApplicationContext();
		mPermissions = permissions;
		flag = true;
		mLister = prl;
		mPermissionsUtils = new PermissionsUtils(mContext);
		if (mPermissionsUtils.PermissionLacked(permissions)) {
			Log.d(TAG, "权限没有在AndroidManifest中注册");
			Bundle bundle = new Bundle();
			for (int i = 0; i < permissions.length; i++) {
				bundle.putInt(permissions[i], 1);
			}
			mLister.onPermissionsResult(bundle);
			return;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//在安卓6.0或以上的机子运行
			if (mContext.getApplicationInfo().targetSdkVersion > 22) {//app包编译在22以上
				Log.d(TAG, "安卓系统为6.0或以上,且app包编译版本为22以上");
				RequestPermissionsByM(activity);

			}else {
				Log.d(TAG, "安卓系统为6.0或以上,app包编译版本为22或以下");
				Bundle bundle = new Bundle();
				for (int i = 0; i < permissions.length; i++) {
					bundle.putInt(permissions[i], 0);
				}
				mLister.onPermissionsResult(bundle);

			}

		}else {
			Log.d(TAG, "安卓系统为6.0以下");
			Bundle bundle = new Bundle();
			for (int i = 0; i < permissions.length; i++) {
				bundle.putInt(permissions[i], 0);
			}
			mLister.onPermissionsResult(bundle);
		}

	}

	@SuppressLint("NewApi")
	public void RequestPermissionsByLOLLIPOP_MR1(){

		if (mPermissionsUtils.checkPermission(mPermissions)) { // 后台关闭权限后，这里获取授权失败，需引导授权
			Log.d(TAG, "后台关闭权限");

			StringBuilder sb = new StringBuilder();
			sb.append("\n");
			//检查缺失什么权限
			for (String permission : mPermissions) {
				if (mPermissionsUtils.checkPermission(permission)) { //未授权
					Log.d(TAG, "当前权限缺失" + permission);
					sb.append(PermissionsConfig.getMatchPermission(permission));
				}
			}

			String message = mContext.getString(ResourseId.getStringId(mContext, mContext.getPackageName(),
					"DGC_Permissons_Permissions_Toast1")) + sb.toString() + mContext.getString(ResourseId.getStringId(mContext, mContext.getPackageName(),
							"DGC_Permissons_Permissions_Toast2"));
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


			Bundle bundle = new Bundle();
			for (int i = 0; i < mPermissions.length; i++) {
				int op = PermissionsConfig.getPermissionsOps(mPermissions[i]);
				bundle.putInt(mPermissions[i], mPermissionsUtils.GetPermissionAuthorization(mContext, op));
			}
			mLister.onPermissionsResult(bundle);

			return;

		}else {

			Bundle bundle = new Bundle();
			for (int i = 0; i < mPermissions.length; i++) {
				int op = PermissionsConfig.getPermissionsOps(mPermissions[i]);
				bundle.putInt(mPermissions[i], mPermissionsUtils.GetPermissionAuthorization(mContext, op));
			}
			mLister.onPermissionsResult(bundle);

		}

	}

	@TargetApi(Build.VERSION_CODES.M)
	public void RequestPermissionsByM(Activity activity){

		if (mPermissionsUtils.PermissionsLacked(mPermissions)) { //缺少权限，直接请求，不需要管是否弹出请求

//			StringBuilder sb = new StringBuilder();
			//检查缺失什么权限
//			for (String permission : mPermissions) {
//				if (mPermissionsUtils.PermissionIsDeclined(permission)) { //未授权
//					sb.append(PermissionsConfig.getMatchPermission(permission));
//				}
//			}

			mPermissionsUtils.Authorization(activity,mPermissionsUtils.REQUEST_CODE, mPermissions);
//			String message = mBeforeStr + "\n\n"+ sb.toString() ;
//			PermissionHintDialog dialog = new PermissionHintDialog(mContext,
//					message,
//					mContext.getString(ResourseId.getStringId(mContext, mContext.getPackageName(),"DGC_Permissons_Cancel")),
//					mContext.getString(ResourseId.getStringId(mContext, mContext.getPackageName(),"DGC_Permissons_Authorize")),
//					new PermissionHintDialog.MyDialogListener() {
//
//						@Override
//						public void onClick() {
//							Bundle bundle = new Bundle();
//							for (int i = 0; i < mPermissions.length; i++) {
//								bundle.putInt(mPermissions[i], mPermissionsUtils.GetPermissionAuthorization(mPermissions[i]));
//							}
//							mLister.onPermissionsResult(bundle);
//						}
//					},
//					new PermissionHintDialog.MyDialogListener() {
//
//								@Override
//								public void onClick() {
//									mPermissionsUtils.Authorization(mPermissionsUtils.REQUEST_CODE, mPermissions);
//								}
//							});
//			dialog.show();



		}else {

			//未启动应用之前已经赋予权限了，给回调成功
			Bundle bundle = new Bundle();
			for (int i = 0; i < mPermissions.length; i++) {
				bundle.putInt(mPermissions[i], mPermissionsUtils.GetPermissionAuthorization(mPermissions[i]));
			}
			mLister.onPermissionsResult(bundle);
		}

	}


	/**
	 * 返回权限请求的结果(6.0才会走这里)
	 * @param activity
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@TargetApi(Build.VERSION_CODES.M)
	public void onRequestPermissionsResult(Activity activity,int requestCode, final String[] permissions, int[] grantResults){

    	if (mPermissionsUtils != null && requestCode == mPermissionsUtils.REQUEST_CODE) {

    		if (flag) {//必须权限回调
    	        if (grantResults.length < 1) {
    	            return;
    	        }

    	        boolean tag = false; //用于标识授权后判断是否已全部授权
//    	        StringBuilder sb = new StringBuilder();
    	        for (int i = 0; i < grantResults.length; i++) {//遍历授权结果
					if (grantResults[i] ==  PackageManager.PERMISSION_DENIED) {
						Log.d(TAG, "仍未授权权限： " + permissions[i]);
						tag = true;
//						sb.append(PermissionsConfig.getMatchPermission(permissions[i]));

						if (!mPermissionsUtils.PermissionIsExplanation(activity,permissions[i])) {
							Log.d(TAG, "且是永久拒绝的权限： "+permissions[i]);
						}
					}
				}

    	        if (tag) { //仍有未授权权限

//    				String message = mLaterStr + "\n\n" + sb.toString() ;

//    		    	new PermissionHintDialog(mContext,
//    						message,
//    						mContext.getString(ResourseId.getStringId(mContext, mContext.getPackageName(),"DGC_Permissons_Cancel")),
//    						mContext.getString(ResourseId.getStringId(mContext, mContext.getPackageName(),"DGC_Permissons_Permissions_Settings")),
//    						new PermissionHintDialog.MyDialogListener() {
//								@Override
//								public void onClick() {
									Bundle bundle = new Bundle();
		    						for (int i = 0; i < permissions.length; i++) {
		    							bundle.putInt(permissions[i], mPermissionsUtils.GetPermissionAuthorization(permissions[i]));
		    						}
		    						mLister.onPermissionsResult(bundle);
//								}
//    						},
//    						new PermissionHintDialog.MyDialogListener() {
//								@Override
//								public void onClicks() {
//									isonRestart = true;
//		    						startAppSettings();
//								}
//							}).show();

				}else {//全部授权成功

					Log.d(TAG, "授权成功： ");
					Bundle bundle = new Bundle();
	        		for (int i = 0; i < permissions.length; i++) {
	        			bundle.putInt(permissions[i], grantResults[i]);
	    			}
	        		mLister.onPermissionsResult(bundle);
				}

			}else {

				//回调非必须权限的结果
				Bundle bundle = new Bundle();
        		for (int i = 0; i < permissions.length; i++) {
        			bundle.putInt(permissions[i], mPermissionsUtils.GetPermissionAuthorization(mPermissions[i]));
    			}
        		mLister.onPermissionsResult(bundle);
			}
		}
    }

    /**
     * 启动应用的设置页面
     */
    private static void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        mContext.startActivity(intent);
    }

	/**
	 * 在这里检测从设置界面回到app时的权限结果并回调
	 */
	public void onRestart() {

		Log.d(TAG, "onRestart");
		if (isonRestart) {
			Bundle bundle = new Bundle();
			for (int i = 0; i < mPermissions.length; i++) {
				bundle.putInt(mPermissions[i], mPermissionsUtils.GetPermissionAuthorization(mPermissions[i]));
			}
			mLister.onPermissionsResult(bundle);
			isonRestart = false;
		}
	}

	public static Context getContext(){
		return mContext;
	}


}
