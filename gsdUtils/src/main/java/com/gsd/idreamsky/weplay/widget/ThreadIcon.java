package com.gsd.idreamsky.weplay.widget;

import android.graphics.Color;
import com.gsd.idreamsky.weplay.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ThreadIcon {
    private static final String TAG = ThreadIcon.class.getSimpleName();
    public String name;
    public int fontColor;
    public int bgColor;
    public int type;
    public String img;

    public ThreadIcon(JSONObject jsonObject){
        if (jsonObject == null) {
            return;
        }
        name = jsonObject.optString("icon_name");
        if (jsonObject.optInt("is_solid") == 1){
            type = LabelTextView.SOLID;
        }else {
            type = LabelTextView.HOLLOW;
        }

        try {
            bgColor = Color.parseColor(jsonObject.optString("icon_background_color"));
            fontColor = Color.parseColor(jsonObject.optString("icon_font_color"));
        }catch (IllegalArgumentException e){
            LogUtil.e(TAG,"parseColor ",e);
            bgColor = Color.parseColor("#1995FE");
            fontColor = Color.parseColor("#ffffff");
        }

    }

    public static List<ThreadIcon> resolveJsonArray(JSONArray jsonArray){
        List<ThreadIcon> icons = new ArrayList<>();
        if (jsonArray == null){
            return icons;
        }

        for (int i=0;i<jsonArray.length();i++){
            icons.add(new ThreadIcon(jsonArray.optJSONObject(i)));
        }

        return icons;
    }

}
