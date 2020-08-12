package org.mini.frame.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by huangqihua on 15/9/25.
 * <p/>
 * 旋转图片
 */
public class MiniRotateImageUtil {

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）：
     *
     * @param srcPath
     * @return
     */
    public static byte[] getImageBytes(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap;// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 设置固定分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 600f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        } else if (w == h) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        int degree = readPictureDegree(srcPath);
        Bitmap newBitmap = rotatingImageView(degree, bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (newBitmap != null) {
            newBitmap.recycle();
            System.gc();
        }
        return baos.toByteArray();// 压缩好比例大小后再进行质量压缩
    }


    /**
     * 获取图片旋转的角度
     *
     * @param path 图片的路径
     * @return 返回旋转的角度值
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle 图片旋转的角度
     *
     * @param bitmap
     */
    public static Bitmap rotatingImageView(int angle, Bitmap bitmap) {

        if (angle == 0)
            return bitmap;

        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = null;
        if (bitmap != null) {
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return resizedBitmap;
    }

//    /**
//     * 压缩图片
//     * @param options
//     * @param reqWidth
//     * @param reqHeight
//     * @return
//     */
//    //计算图片的缩放值
//    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//            final int heightRatio = Math.round((float) height/ (float) reqHeight);
//            final int widthRatio = Math.round((float) width / (float) reqWidth);
//            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//        }
//        return inSampleSize;
//    }

//    // 根据路径获得图片并压缩，返回bitmap用于显示
//    public static Bitmap getSmallBitmap(String filePath) {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, 480, 800);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//
//        return BitmapFactory.decodeFile(filePath, options);
//    }

//    //把bitmap转换成String
//    public static byte[] bitmapToString(String filePath) {
//
//        Bitmap bm = getSmallBitmap(filePath);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int degree = readPictureDegree(filePath);
//        Bitmap newBitmap = rotatingImageView(degree, bm);
//        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        if (baos.toByteArray().length / 1024 > 200){  //如果图片大小大于200KB，再次进行压缩
//            baos.reset();
//            newBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
//        }else {
//            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        }
//        newBitmap.recycle();
//        byte[] b = baos.toByteArray();
//        return b;
//    }



    /**
     * 压缩图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;               //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
        BitmapFactory.decodeFile(filePath, opts);
        // 调用上面定义的方法计算inSampleSize值
        opts.inSampleSize = calculateInSampleSize(opts, 480, 800);
        // 使用获取到的inSampleSize值再次解析图片
        opts.inJustDecodeBounds = false;             //这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inInputShareable = true;
        opts.inPurgeable = true;
        try {
            return BitmapFactory.decodeFile(filePath, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    //把bitmap转换成byte[]
    public static byte[] bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int degree = readPictureDegree(filePath);
        Bitmap newBitmap = rotatingImageView(degree, bm);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 200) {  //如果图片大小大于200KB，再次进行压缩
            baos.reset();
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        } else {
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        if (!newBitmap.isRecycled()) {
            newBitmap.recycle();
            System.gc();
        }
        byte[] b = baos.toByteArray();
        return b;
    }


    @SuppressWarnings("unused")
    public static Bitmap copressImage(String imgPath) {
        Bitmap bmap;
        File picture = new File(imgPath);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        //下面这个设置是将图片边界不可调节变为可调节
        bitmapFactoryOptions.inJustDecodeBounds = true;
        bitmapFactoryOptions.inSampleSize = 5;
        int outWidth = bitmapFactoryOptions.outWidth;
        int outHeight = bitmapFactoryOptions.outHeight;

        float imagew = 150;
        float imageh = 150;
        int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight
                / imageh);
        int xRatio = (int) Math
                .ceil(bitmapFactoryOptions.outWidth / imagew);
        if (yRatio > 1 || xRatio > 1) {
            if (yRatio > xRatio) {
                bitmapFactoryOptions.inSampleSize = yRatio;
            } else {
                bitmapFactoryOptions.inSampleSize = xRatio;
            }

        }
        bitmapFactoryOptions.inJustDecodeBounds = false;
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        if (bmap != null) {
            return bmap;
        }
        return null;
    }


    /**
     * 保持长宽比缩小Bitmap
     *
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {

        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        // no need to resize
        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }

        int width = originWidth;
        int height = originHeight;

        // 若图片过宽, 则保持长宽比缩放图片
        if (originWidth > maxWidth) {
            width = maxWidth;

            double i = originWidth * 1.0 / maxWidth;
            height = (int) Math.floor(originHeight / i);
            if (height != 0) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            }
        }

        // 若图片过长, 则从上端截取
        if (height > maxHeight) {
            height = maxHeight;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        }


        return bitmap;
    }


}
