package com.gsd.idreamsky.weplay.utils.permissions;

import android.content.Context;

public class ResourseId {

	public static int getId(Context context,String packageName, String name) {
		return getResourseIdByName(context,packageName, "id", name);
	}

	public static int getStyleByName(Context context,String packageName, String name) {
		return getResourseIdByName(context,packageName, "style", name);
	}

    public static int getStringId(Context context,String packageName, String name) {
        return getResourseIdByName(context,packageName, "string", name);
    }

    public static int getStringArrayId(Context context,String packageName, String name) {
        return getResourseIdByName(context,packageName, "array", name);
    }

    public static int getDrawableId(Context context,String packageName, String name) {
        return getResourseIdByName(context,packageName, "drawable", name);
    }

    public static int getLayoutId(Context context,String packageName, String name) {
        return getResourseIdByName(context,packageName, "layout", name);
    }

    public static int getResourseIdByName(Context context,String packageName, String className, String name) {
        int id = 0;
        try {
			id=context.getResources().getIdentifier(name, className, packageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return id;
    }
}
