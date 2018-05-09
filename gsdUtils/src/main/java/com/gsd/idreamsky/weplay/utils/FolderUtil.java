package com.gsd.idreamsky.weplay.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


//管理程序过程中使用的所有的文件夹 选项 包括检查SD卡是否可用 SD卡位置 等
public class FolderUtil {

    private static final String TAG = "FolderUtil";
    private String sdPath;
    private String folderPicCache;
    private static final String pathDiv = File.separator; // 这么写标准点

    // _ + time 的格式
    public static String getPhotoName() {
        String strTime = System.currentTimeMillis() + "";
        return "_" + strTime;
    }

    public static File getCacheDir(Context context) {
        if (context == null) {
            return null;
        }
        if (isExternalStorageWritable()) {
            return context.getExternalCacheDir();
        }
        return context.getFilesDir();
    }

    public FolderUtil(Context curContext) {
    }

    private static FolderUtil _instance;

    public static FolderUtil getInstance(Context curContext) {
        if (_instance == null) {
            _instance = new FolderUtil(curContext);
        }
        return _instance;
    }

    public String getSdPath(Context context) {
        if (TextUtils.isEmpty(sdPath)) {
            initSdPath(context);
        }
        return sdPath;
    }

    private void initSdPath(Context context) {
        sdPath = getSDCardPath();
        if (sdPath != null) {
            bClearAll = false;
        } else {
            if (context != null) {
                sdPath = context.getApplicationInfo().dataDir;
            }
            bClearAll = true;
        }
    }

    public String getFolderPicCache(Context context) {
        if (TextUtils.isEmpty(folderPicCache)) {
            initFolderPicCache(context);
        }
        return folderPicCache;
    }

    private void initFolderPicCache(Context context) {
        folderPicCache = getSdPath(context) + "/AMerchant/picCache/";
        checkOrCreateFolder(folderPicCache);
    }

    private boolean bClearAll = false; // 当使用手机内存时，程序退出 清空所有数据 除Save文件夹

    private String getSDCardPath() {
        if (!isSDCardAvailable()) {
            return null;
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static boolean isSDCardAvailable() {
        return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
    }

    /**
     * 创建临时文件
     *
     * @param type 文件类型
     */
    public static File getTempFile(Context contxt, FileType type) {
        try {
            File file = File.createTempFile(type.toString(), null, getCacheDir(contxt));
            file.deleteOnExit();
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 通过Uri获取文件在本地存储的真实路径
     */
    //public static String getRealPathFromUri(Activity activity, Uri uri) {
    //    String[] proj = { MediaStore.Images.Media.DATA };
    //    Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
    //    if (cursor != null) {
    //        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    //        cursor.moveToFirst();
    //        return cursor.getString(column_index);
    //    } else {
    //        return uri.getPath();
    //    }
    //}

    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri);
        if (sdkVersion < 19) return getRealPathFromUri_Api11To18(context, uri);
        else return getRealPathFromUri_AboveApi19(context, uri);
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    public static boolean checkOrCreateFolder(String folderPath) {
        try {
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.mkdirs();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除文件或目录
     */
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * 删除文件,如果路径为空或文件不存在则返回false
     */
    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) return false;

        File file = new File(filePath);
        if (!file.exists()) return false;

        return file.delete();
    }

    /**
     * 判断缓存文件是否存在
     */
    public static boolean isCacheFileExist(Context context, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }

        String path = getCacheFilePath(context, fileName);
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File file = new File(path);
        return file.exists();
    }

    /**
     * 获取缓存文件地址
     *
     * @param fileName 文件名
     * @return 缓存文件的绝对路径
     */
    public static String getCacheFilePath(Context context, String fileName) {
        File cacheDir = getCacheDir(context);
        if (cacheDir == null) {
            return null;
        }
        return cacheDir.getAbsolutePath() + pathDiv + fileName;
    }

    /**
     * 判断外部存储是否可用---这个方法的名字有问题
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        Log.e(TAG, "ExternalStorage not mounted");
        return false;
    }

    /**
     * 写对象到缓存文件夹的文件中
     *
     * @param obj 对象
     * @param filename 文件名（不含文件夹）
     */
    public static boolean writeObjectToCacheFile(Context context, Object obj, String filename) {
        String path = getCacheFilePath(context, filename);
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return writeObjectToFile(obj, path);
    }

    /**
     * 写对象到文件 copy
     *
     * @param obj 对象
     * @param path 文件名
     */
    public static boolean writeObjectToFile(Object obj, String path) {
        File file = new File(path);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file, false); // 直接覆写
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            LogUtil.i(TAG, "write object success!");
            return true;
        } catch (Exception e) {
            LogUtil.e(TAG, "write object failed!", e);
            return false;
        }
    }

    /**
     * 写对象到缓存文件夹的文件中
     *
     * @param filename 文件名（不含文件夹）
     */
    public static Object readObjectFromCacheFile(Context context, String filename) {
        String path = getCacheFilePath(context ,filename);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return readObjectFromFile(path);
    }

    /**
     * 读取文件中的对象到内存
     */
    public static Object readObjectFromFile(String path) {
        Object temp = null;
        File file = new File(path);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 字节数组输出流转File
     */
    public static File byteArray2File(ByteArrayOutputStream baos, String fileName) {
        File zipFile = new File(fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(zipFile);
            fos.write(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fos);
            CloseUtil.close(baos);
        }
        return zipFile;
    }

    ///**
    // * 将数据存储为文件
    // *
    // * @param data 数据
    // */
    //public static void createFile(byte[] data,String filename){
    //    String path = getCacheFilePath(context ,filename);
    //    if (TextUtils.isEmpty(path)) {
    //        return ;
    //    }
    //    File f = new File(path);
    //    try{
    //        if (f.createNewFile()){
    //            FileOutputStream fos = new FileOutputStream(f);
    //            fos.write(data);
    //            fos.flush();
    //            fos.close();
    //        }
    //    }catch (IOException e){
    //        Log.e(TAG,"create file error" + e);
    //    }
    //}

    public enum FileType {
        IMG,
        AUDIO,
        VIDEO,
        FILE,
    }
}
