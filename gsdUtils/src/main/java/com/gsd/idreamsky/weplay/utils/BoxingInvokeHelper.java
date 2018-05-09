package com.gsd.idreamsky.weplay.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;
import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.gsd.utils.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by magical.zhang on 2017/3/13.
 * Description : Boxing 调用工具类
 */
public class BoxingInvokeHelper {

    private static final String TAG = BoxingInvokeHelper.class.getSimpleName();
    public static final int REQUEST_CODE = 1024;
    private static BoxingInvokeHelper INSTANCE = new BoxingInvokeHelper();

    private int mMaxCount;
    private int mAlreadySelectedCount;

    private BoxingInvokeHelper() {
    }

    public static BoxingInvokeHelper getInstance() {
        return INSTANCE;
    }

    private BoxingInvokeHelper setMaxCount(int count) {
        if (count > 0) {
            mMaxCount = count;
        }
        return this;
    }

    /**
     * 清除配置
     */
    public BoxingInvokeHelper clear() {
        mMaxCount = 0;
        mAlreadySelectedCount = 0;
        return this;
    }

    /**
     * 移除图片时调用
     */
    public BoxingInvokeHelper remove() {
        if (mAlreadySelectedCount >= 1) {
            mAlreadySelectedCount--;
        }
        return this;
    }

    /**
     * 设置当前已选中图片个数
     *
     * @param count 当前已选中图片数量
     */
    public BoxingInvokeHelper setSelectedCount(int count) {

        if (count >= 0) {
            mAlreadySelectedCount = count;
        }
        return this;
    }

    /**
     * select multi image
     *
     * @param maxCount max available images
     */
    public void startActivityForResult(Object context, int maxCount) {

        if (null == context) {
            LogUtil.e(TAG, " context is null !");
            return;
        }

        if (maxCount > 0) {
            setMaxCount(maxCount);
        }

        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.MULTI_IMG);
        int limitCount = mMaxCount - mAlreadySelectedCount;
        if (limitCount > 0) {
            config.withMaxCount(limitCount);
        }

        if (context instanceof Activity) {
            Boxing.of(config)
                    .withIntent(((Activity) context), BoxingActivity.class)
                    .start(((Activity) context), REQUEST_CODE);
        } else if (context instanceof Fragment) {
            Boxing.of(config)
                    .withIntent(((Fragment) context).getActivity(), BoxingActivity.class)
                    .start(((Fragment) context), REQUEST_CODE);
        }
    }

    /**
     * select single image
     *
     * @param context Activity or Fragment
     */
    public void startActivityForResult(Object context) {

        if (null == context) {
            LogUtil.e(TAG, " context is null !");
            return;
        }

        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG);
        //一定要区分 防止 onActivityResult 不能正确回调
        if (context instanceof Activity) {
            Boxing.of(config)
                    .withIntent(((Activity) context), BoxingActivity.class)
                    .start(((Activity) context), REQUEST_CODE);
        } else if (context instanceof Fragment) {
            Boxing.of(config)
                    .withIntent(((Fragment) context).getActivity(), BoxingActivity.class)
                    .start(((Fragment) context), REQUEST_CODE);
        }
    }

    /**
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data 图像数据
     * @return 图像数据 Uri集合 注意判空
     */
    @Nullable
    public static List<Uri> onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            //拿到 选中的图片
            ArrayList<BaseMedia> medias = Boxing.getResult(data);
            if (ValidateUtil.isListEmpty(medias)) {
                return null;
            }
            if (requestCode == REQUEST_CODE) {

                List<Uri> imageUris = new ArrayList<>();
                for (BaseMedia media : medias) {
                    imageUris.add(Uri.parse(media.getPath()));
                }
                return imageUris;
            }
        }
        return null;
    }

    /**
     * 带剪裁功能的相册选择
     *
     * @param x 剪裁比例x
     * @param y 剪裁比例y
     */
    public static void getPicWithCameraAndCrop(Activity activity, float x, float y) {
        String cachePath = BoxingFileHelper.getCacheDir(activity);
        if (TextUtils.isEmpty(cachePath)) {
            Toast.makeText(activity, R.string.boxing_storage_deny, Toast.LENGTH_SHORT).show();
            return;
        }
        Uri destUri = new Uri.Builder().scheme("file")
                .appendPath(cachePath)
                .appendPath(String.format(Locale.US, "%s.jpg", System.currentTimeMillis()))
                .build();
        BoxingConfig singleCropImgConfig =
                new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG).withCropOption(
                        new BoxingCropOption(destUri).aspectRatio(x, y))
                        .needCamera(R.drawable.ic_boxing_camera_white);
        Boxing.of(singleCropImgConfig)
                .withIntent(activity, BoxingActivity.class)
                .start(activity, REQUEST_CODE);
    }

    public static void openCamera() {

        //CameraPickerHelper helper = new CameraPickerHelper(null);
        //helper.setPickCallback(new CameraPickerHelper.Callback() {
        //    @Override
        //    public void onFinish(@NonNull CameraPickerHelper helper) {
        //        helper.
        //    }
        //
        //    @Override
        //    public void onError(@NonNull CameraPickerHelper helper) {
        //
        //    }
        //});
    }
}
