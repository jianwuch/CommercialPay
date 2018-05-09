package com.gsd.idreamsky.weplay.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.gsd.idreamsky.weplay.thirdpart.glide.GlideApp;
import com.gsd.idreamsky.weplay.thirdpart.glide.ImageDownLoadCallBack;
import com.gsd.utils.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by magical.zhang on 2017/2/16.
 * Description : 统一加载图片 调用方式
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";
    private static volatile ImageLoader instance;

    private ImageLoader() {
    }

    public static ImageLoader getInstance() {
        if (null == instance) {
            synchronized (ImageLoader.class) {
                if (null == instance) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    /**
     * 限制高宽 按比例显示图片
     * 用于list中展示
     */
    public void loadLimit(String url, ImageView view) {
        GlideApp.with(view.getContext())
                .asBitmap()
                .load(url)
                //避免listview复用item导致的问题
                .override(DensityUtil.dp2px(LimitImageTarget.WIDE_IMG_MAX_WIDTH),
                        DensityUtil.dp2px(LimitImageTarget.WIDE_IMG_MAX_HEIGHT))
                .into(new LimitImageTarget(view));
    }

    public void loadRaw(int raw,ImageView view){
        String url = "android.resource://com.idreamsky.wandao/raw/"+raw;
        RequestOptions options =
                new RequestOptions().placeholder(R.drawable.wpk_default_icon_image);
        GlideApp.with(view.getContext())
                .load(url)
                .transition(withCrossFade())
                .apply(options)
                .into(view);
    }

    /**
     * 普通加载
     */
    public void loadNormal(String url, ImageView view) {
        //if (TextUtils.isEmpty(url)) {
        //    return;
        //}

        // TODO: 2018/4/26 这里暂时发现有问题，初次加载会显示背景图
        //RequestOptions options =
        //        new RequestOptions().placeholder(R.drawable.wpk_default_icon_image);
        GlideApp.with(view.getContext())
                .load(url)
                .transition(withCrossFade())
                //.apply(options)
                .into(view);
    }

    /**
     * 加载圆角矩形的图片
     * 此处glide有bug 需要将CenterCrop与RoundCrop一起使用才行
     * 具体看 https://github.com/bumptech/glide/issues/613
     */
    public void loadRoundBitmap(String url, ImageView view) {
        loadRoundBitmap(url, view, 10);
    }

    public void loadRoundBitmap(String url, ImageView view, int roundDp) {
        if (TextUtils.isEmpty(url) || null == view) {
            return;
        }

        MultiTransformation multi = new MultiTransformation(new CenterCrop(),
                new RoundedCornersTransformation(DensityUtil.dp2px(roundDp), 0));
        RequestOptions options = new RequestOptions().placeholder(R.drawable.wpk_default_icon_image)
                .transform(multi);

        Glide.with(view.getContext()).load(url).apply(options).into(view);
    }



    /**
     * 拿到圆形的 Bitmap
     *
     * @param target 添加占位符的话需要重写 onLoadStarted 方法
     */
    public void getCircleBitmap(Context context, String url,
                                SimpleTarget<Bitmap> target, int size) {

        MultiTransformation multi = new MultiTransformation(new CenterCrop(),
                new RoundedCornersTransformation(DensityUtil.dp2px(size/2), 0));
        RequestOptions options = new RequestOptions().placeholder(R.drawable.wpk_default_icon_image)
                .transform(multi).override(size,size);

        Glide.with(context).asBitmap().load(url).apply(options).into(target);
    }


    public void loadBlur(Context context, String url, SimpleTarget<Bitmap> target, int width,
            int height) {

        MultiTransformation multi =
                new MultiTransformation(new BlurTransformation(), new CenterCrop());

        RequestOptions options = new RequestOptions().placeholder(R.drawable.wpk_default_icon_image)
                .transform(multi);
        if (width > 0 && height > 0) {
            options.override(width, height);
        }

        GlideApp.with(context).asBitmap().load(url).apply(options).into(target);
    }

    public void loadBlur(String url, ImageView imageView) {

        //        调整模糊程度，缩小了8倍，再取附近25个像素的平均值来模糊
        MultiTransformation multi =
                new MultiTransformation(new BlurTransformation(25, 8), new CenterCrop());

        RequestOptions options = new RequestOptions().placeholder(R.drawable.wpk_default_icon_image)
                .transform(multi);

        GlideApp.with(imageView.getContext()).asBitmap().load(url).apply(options).into(imageView);
    }

    public void loadBlur(String url, ImageView imageView, RequestListener listener) {

        //        调整模糊程度，缩小了8倍，再取附近25个像素的平均值来模糊
        MultiTransformation multi =
                new MultiTransformation(new BlurTransformation(25, 8), new CenterCrop());

        RequestOptions options = new RequestOptions().placeholder(R.drawable.wpk_default_icon_image)
                .transform(multi);

        GlideApp.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(options)
                .listener(listener)
                .into(imageView);
    }

    /**
     * 拿到 Bitmap
     *
     * @param target 添加占位符的话需要重写 onLoadStarted 方法
     */
    public void getBitmap(Context context, String url, SimpleTarget<Bitmap> target) {

        Glide.with(context).asBitmap().load(url).into(target);
    }

    /**
     * Glide保存图片到本地
     */
    public void savePicture(final Context context, String url,
            final ImageDownLoadCallBack callBack) {

        RequestOptions options =
                new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

        GlideApp.with(context).asBitmap().apply(options).load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                saveImageToGallery(context, resource, callBack);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                callBack.onDownLoadFailed("下载失败");
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
                LogUtil.d(TAG, " download image start");
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                super.onLoadCleared(placeholder);
                LogUtil.d(TAG, " download image cancel");
            }
        });
    }

    public static void saveImageToGallery(Context context, Bitmap bmp,
            ImageDownLoadCallBack callBack) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "WanDao");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            callBack.onDownLoadFailed(e.getMessage());
            e.printStackTrace();
            return;
        } catch (IOException e) {
            callBack.onDownLoadFailed(e.getMessage());
            e.printStackTrace();
            return;
        }

        //通知保存成功
        callBack.onDownLoadSuccess(file);

        //这种方式会保存两次
        //// 其次把文件插入到系统图库
        //try {
        //    MediaStore.Images.Media.insertImage(context.getContentResolver(),
        //            file.getAbsolutePath(), fileName, null);
        //} catch (FileNotFoundException e) {
        //    e.printStackTrace();
        //}

        try {
            // 保存后要扫描一下文件，及时更新到系统目录（一定要加绝对路径，这样才能更新）
            MediaScannerConnection.scanFile(context, new String[] {
                    Environment.getExternalStorageDirectory()
                            + File.separator
                            + "WanDao"
                            + File.separator
                            + fileName
            }, null, null);
        } catch (Exception ex) {
            LogUtil.e(TAG, " notify system gallery failed");
            ex.printStackTrace();
        }

        //这种方式更新不成功
        //// 最后通知图库更新
        //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
        //        FileProvider7.getUriForFile(context, file)));
    }
}
