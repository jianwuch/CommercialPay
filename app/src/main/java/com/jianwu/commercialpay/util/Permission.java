package com.jianwu.commercialpay.util;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.jianwu.commercialpay.service.NotificationCaptureByAccessibility;

public class Permission {

    private static final String TAG = Permission.class.getSimpleName();

    /**
     * 检测辅助功能是否开启<br>
     *
     * @return boolean
     */
    public static <T extends AccessibilityService> boolean isAccessibilitySettingsOn(
            Context mContext, Class<T> acbsClassString) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        final String service = mContext.getPackageName()
                + "/"
                + acbsClassString.getCanonicalName();
        Log.i(TAG, "service:" + service);
        try {
            accessibilityEnabled =
                    Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                            android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG,
                    "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter =
                new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue =
                    Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            // com.z.buildingaccessibilityservices/com.z.buildingaccessibilityservices.TestService
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: "
                            + accessibilityService
                            + " "
                            + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG,
                                "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }
}
