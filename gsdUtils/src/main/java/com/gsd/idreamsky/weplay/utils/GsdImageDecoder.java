package com.gsd.idreamsky.weplay.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具类
 */
public class GsdImageDecoder {

    public static int CUSTOM_SERVER_IMG_MAX_SIZE = 512;
    public static int ALBUM_IMG_MAX_SIZE = 1024;
    public static int TOPIC_ADD_REPLAY_MAX_SIZE = 1024;


    public static File saveFile(Bitmap bm,String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path , fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }

    // 这个函数会对图片的大小进行判断
    static int calulateInSampleSize(BitmapFactory.Options opts, int limitSize) {
        int height = opts.outHeight;
        int width = opts.outWidth;
        int sampleSize = 1;
        while ((height / sampleSize) >= limitSize && (width / sampleSize) >= limitSize) {
            sampleSize *= 2;
        }
        LogUtil.d("cjw", "最大像素限制："
                + limitSize
                + "||原始大小宽/高："
                + width
                + "/"
                + height
                + "||最后的压缩比："
                + sampleSize);
        return sampleSize;
    }

    /**
     * 将图片转化成bitmap,根据大小显示压缩bitmap,且根据图片选择将bitmap旋转
     *
     * @param strImageSource 图片真实绝对路径地址
     * @param nLimitSize 限制图片的大小
     */
    public static Bitmap comPressFile2Bitmap(String strImageSource, int nLimitSize) {
        Bitmap rotatedNewbitmap = null;
        try {
            Log.v("ddrb", "strImageSource = " + strImageSource);
            BitmapFactory.Options opts = new BitmapFactory.Options();

            opts.inDither = true; // 避免图片抖动
            opts.inPurgeable = true; // 内存可以被回收
            opts.inJustDecodeBounds = true;//如果该 值设为true那么将不返回实际的bitmap，也不给其分配内存空间这样就避免内存溢出了
            BitmapFactory.decodeFile(strImageSource, opts);
            opts.inSampleSize = calulateInSampleSize(opts, nLimitSize);

            opts.inJustDecodeBounds = false;//是时候来获取bitmap了
            Bitmap bitmap = BitmapFactory.decodeFile(strImageSource, opts);//现在获取的就是压缩尺寸后的bitmap
            if (bitmap != null) {

                //把图片旋转为正的方向
                int degree = getExifOrientation(strImageSource);
                rotatedNewbitmap = rotaingImageView(degree, bitmap);
            } else {
                return null;
            }
        } catch (Exception e) {
            //if (StaticValue.DEBUG) {
            //    Log.d(StaticValue.EXCEPTIONTAG, e.toString());
            //}
        }

        return rotatedNewbitmap;
    }

    /**
     * 获取图片的角度
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle 选择角度
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {

        if (angle == 0) {
            return bitmap;
        }
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                        true);

        //matirx肯定不为null, resizedBitmap和bitmap不为同一对象
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return resizedBitmap;
    }

    public static Bitmap rotaingAndImageView(int angle, Bitmap bitmap, float scaleWidth) {

        if (angle == 0) {
            return bitmap;
        }
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        matrix.postScale(1/scaleWidth,1/scaleWidth);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                        true);

        //matirx肯定不为null, resizedBitmap和bitmap不为同一对象
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return resizedBitmap;
    }

    /**
     * 将本地路径下的图片 根据指定质量压缩率 压缩到缓存文件夹 + 当前系统时间.jpg的File中
     *
     * @param localPath 本地图片路径
     * @param ratio 指定的压缩率 默认为80
     * @return 经过质量压缩后的File文件
     */
    public static File compressPic2File(String localPath, int ratio) {
        Bitmap bitmap = comPressFile2Bitmap(localPath, ALBUM_IMG_MAX_SIZE);
        if (null == bitmap) {
            return null;
        }
        int compressRatio = 80;     //给个默认值
        if (ratio > 0 && ratio <= 100) {
            compressRatio = ratio;
        }
        FileOutputStream fos = null;
        File targetFile = null;
        try {
            String fileName = String.valueOf(System.currentTimeMillis()).concat(".jpg");
            //创建缓存文件夹下 指定文件名为fileName的文件 并最终返回给调用者
            String path = FolderUtil.getCacheFilePath(UtilsApplication.getContext(),fileName);
            if (TextUtils.isEmpty(path)) {
                return null;
            }
            targetFile = new File(path);
            //将压缩图片流写入targetFile中
            fos = new FileOutputStream(targetFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressRatio, fos);   //质量压缩
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fos);
            CloseUtil.recycle(bitmap);
        }
        return targetFile;
    }

    /**
     * 包括压缩尺寸，尺寸压缩是压缩效率最高的方法
     * @param localPath
     * @param ratio
     * @return
     *
     * 1.计算图片的缩放值
     * 2.通过BitmapFactory.decodeFile(filePath, options);压缩bitmap
     * 3.bitmap的增加压缩率
     * 4.bitmap生成文件
     */
    public static String compressPic2FileContainPix(String localPath,  int ratio) {
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;//如果该 值设为true那么将不返回实际的bitmap，也不给其分配内存空间这样就避免内存溢出了
        BitmapFactory.decodeFile(localPath, opts);
        opts.inSampleSize = calulateInSampleSize(opts, 1024);

        opts.inJustDecodeBounds = false;//是时候来获取bitmap了
        Bitmap bitmap = BitmapFactory.decodeFile(localPath, opts);//现在获取的就是压缩尺寸后的bitmap


        if (null == bitmap) {
            return null;
        }
        int compressRatio = 80;     //给个默认值
        if (ratio > 0 && ratio <= 100) {
            compressRatio = ratio;
        }
        FileOutputStream fos = null;
        File targetFile = null;
        try {
            String fileName = String.valueOf(System.currentTimeMillis()).concat(".jpg");
            //创建缓存文件夹下 指定文件名为fileName的文件 并最终返回给调用者
            String path = FolderUtil.getCacheFilePath(UtilsApplication.getContext(),fileName);
            if (TextUtils.isEmpty(path)) {
                return null;
            }
            targetFile = new File(path);
            //将压缩图片流写入targetFile中
            fos = new FileOutputStream(targetFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressRatio, fos);   //质量压缩
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fos);
            CloseUtil.recycle(bitmap);
        }
        return targetFile.getPath();
    }
}
