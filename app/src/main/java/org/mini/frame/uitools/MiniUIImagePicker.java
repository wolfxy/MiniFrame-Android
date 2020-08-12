package org.mini.frame.uitools;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.mini.frame.BuildConfig;

import org.mini.frame.log.MiniLogger;
import org.mini.frame.toolkit.RequestPermissionsCallback;
import org.mini.frame.view.MiniRotateImageUtil;
import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.activity.MiniUIActivityResultHandler;
import org.mini.frame.view.MiniUIActionSheet;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static android.app.Activity.RESULT_OK;
import static org.mini.frame.uitools.MiniUITools.toast;

/**
 * Created by Wuquancheng on 2018/8/19.
 */

public class MiniUIImagePicker {

    public interface MiniUIImagePickerMonitor {
        void onSelectedImage(Bitmap image, File file);
    }

    MiniUIImagePickerMonitor pickerMonitor;
    MiniUIActivity activity;

    private String mTempImageFileName;
    private String mTempImageFilePath;

    private boolean crop = false;

    public static int PICKER_FROM_CAMERA = 0;
    public static int PICKER_FROM_PHOTO = 1;
    private int method = -1;

    public void selectImage(MiniUIActivity activity, MiniUIImagePickerMonitor listener) {
        this.activity = activity;
        this.pickerMonitor = listener;
        if (method == -1) {
            MiniUIActionSheet actionSheet = new MiniUIActionSheet(activity);
            actionSheet.addText("相机").addText("图片库");
            actionSheet.showInActivity(activity, new MiniUIActionSheet.MiniUIActionSheetCallback() {
                @Override
                public void onItemClicked(int index) {
                    if (index == 0) {
                        selectImageFromCamera();
                    } else if (index == 1) {
                        selectImageFromPhoto();
                    }
                }
            });
        }
        else if (method == PICKER_FROM_CAMERA) {
            selectImageFromCamera();
        }
        else if (method == PICKER_FROM_PHOTO) {
            selectImageFromPhoto();
        }
    }

    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    private void executeSelectImageFromCamera()
    {
        mTempImageFileName = System.currentTimeMillis() + ".png";
        File path = new File(activity.getExternalCacheDir(), "Pictures");
        if (!path.exists()) {
            path.mkdirs();
        }
        mTempImageFilePath = path.getAbsolutePath();
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File out = new File(mTempImageFilePath, mTempImageFileName);
            final Uri uri;
            if (Build.VERSION.SDK_INT>=24) {
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                String fileprovider = activity.getApplicationContext().getPackageName() + ".FileProvider";
                uri = FileProvider.getUriForFile(activity.getApplicationContext(), fileprovider, out);
            }
            else {
                uri = Uri.fromFile(out);
            }
            // 指定调用相机拍照后照片的储存路径
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(cameraIntent, new MiniUIActivityResultHandler() {
                @Override
                public void handleResult(int resultCode, Object data, Intent intent) {
                    if (resultCode == RESULT_OK) {
                        try {
                            if (uri != null) {
                                processCameraImage(uri);
                            } else {
                                toast("获取图片失败，请重试！");
                            }
                        } catch (Exception e) {
                            toast(e.toString());
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            toast("请确认已经插入SD卡");
        }
    }

    private void selectImageFromCamera() {
        this.activity.requestCameraPermission(new RequestPermissionsCallback() {
            @Override
            public void onRequestPermissionsResult(boolean granted) {
                if(granted) {
                    activity.requestStoragePermission(new RequestPermissionsCallback() {
                        @Override
                        public void onRequestPermissionsResult(boolean granted) {
                            if (granted) {
                                executeSelectImageFromCamera();
                            }
                            else {
                                activity.toastMessage("获取相机授权失败");
                            }
                        }
                    });
                }
                else {
                    activity.toastMessage("获取相机授权失败");
                }
            }
        });

    }

    public void processCameraImage(Uri uri) {
        if (crop) {
            cropImage(uri);
        }
        else {
            File out = new File(mTempImageFilePath, mTempImageFileName);
            if (out.exists()) {
                //解决三星手机拍照旋转问题
                String FilePath = out.getAbsolutePath();
                byte[] imageBytes = MiniRotateImageUtil.getImageBytes(FilePath);
                Bitmap rotateBitmap = Bytes2Bitmap(imageBytes);

                if (rotateBitmap != null)
                    doSelectedImage(rotateBitmap, out);
                else {
                    toast("获取图片失败，请重试！");
                }
            }
        }

    }

    public void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, new MiniUIActivityResultHandler() {
            @Override
            public void handleResult(int resultCode, Object data, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    if (photo != null) {
                        if (pickerMonitor != null) {
                             pickerMonitor.onSelectedImage(photo, null);
                         }
                        return;
                    }
                }
            }
        });
    }



    public void doSelectedImage(Bitmap image, File file) {
        if (pickerMonitor != null) {
            pickerMonitor.onSelectedImage(image, file);
        }
    }

    protected String filePathForUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        return cursor.getString(columnIndex);
    }

    private void addPhotoIntentExternalInfo(Intent intent) {
        if (crop) {
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);//是否把剪切后的图片通过data返回
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());//图片的输出格式
        intent.putExtra("noFaceDetection", true);
    }

    private void selectImageFromPhoto() {
        this.activity.requestStoragePermission(new RequestPermissionsCallback() {
            @Override
            public void onRequestPermissionsResult(boolean granted) {
                if (granted) {
                    doSelectImageFromPhoto();
                }
                else {
                    activity.toastMessage("获取相册授权权失败");
                }
            }
        });
    }
    private void doSelectImageFromPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        addPhotoIntentExternalInfo(intent);
        activity.startActivityForResult(intent, new MiniUIActivityResultHandler() {
            @Override
            public void handleResult(int resultCode, Object data, Intent intent) {
                if (resultCode == RESULT_OK) {
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        if (photo != null) {
                            doSelectedImage(photo, null);
                            return;
                        }
                    }
                    Uri uri = intent.getData();
                    if (uri != null) {
                        if (uri.toString().contains("content:")) {
                                String path = filePathForUri(uri);
                                if (path != null) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(path);

                                    doSelectedImage(bitmap, null);
                                } else {
                                    ContentResolver cr = activity.getContentResolver();
                                    try {
                                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                                        if (bitmap != null) {
                                            doSelectedImage(bitmap, null);
                                        }
                                    } catch (Exception e) {
                                        MiniLogger.get().e(e);
                                    }
                                }
                            }
                        } else {
                            try {
                                File file = new File(new URI(uri.toString()));
                                String fileAbsolutePath = file.getAbsolutePath();
                                Bitmap bitmap = BitmapFactory.decodeFile(fileAbsolutePath);
                                doSelectedImage(bitmap, file);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        });
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }
}
