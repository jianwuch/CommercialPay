package com.gsd.idreamsky.weplay.utils.permissions;

import android.os.Bundle;


/**
 * 权限的回调接口
 * @author bzai.xiao
 */
public interface onPermissionsResultLister{
	

	/**
	 * 权限的请求回调
	 * @param bundle
	 */
	public void onPermissionsResult(Bundle bundle);

}
