package org.mini.frame.toolkit.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import org.mini.frame.toolkit.MiniFramework;
import org.mini.frame.toolkit.MiniImageBitmapUtil;
import org.mini.frame.tools.MiniMd5;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Wuquancheng on 15/4/19.
 */
public class MiniFileManager {

    public static void clearCache(Context context) {
        if (context != null) {
            File file = new File(getCachePath(context));
            delete(file);
        }
    }

    public static String getCachePath(Context context) {
        String sdcardPath = getSdcardPath(context);
        String path = sdcardPath + File.separator + MiniFramework.appShortName;
        return path;
    }

    //删除所有的文件夹以及文件
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
//                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public static String getSdcardPath(Context context) {
        String sdcardPath;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            sdcardPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdcardPath = context.getCacheDir().getPath();
        }
        return sdcardPath;
    }

    public static String getAppFilePath(Context context, String directoryPath) {
        String sdcardPath = getApplicationFileRootPath(context);
        if (context != null) {
            String path = sdcardPath + File.separator + MiniFramework.appShortName + File.separator + directoryPath;
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
            return path;
        } else {
            return sdcardPath;
        }
    }

    public static String getApplicationFileRootPath(Context context)
    {
       return context.getExternalCacheDir().getAbsolutePath();
    }

    public static String getTempDirectory(Context context)
    {
        File file = new File(context.getExternalCacheDir().getAbsolutePath() + "/tmp");
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static void saveDownloadFile(byte[] data, String name) throws Exception {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(data, 0, data.length);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void rename(String src, String obj) {
        File file = new File(src);
        if (file.exists()) {
            File objFile = new File(obj);
            if (objFile.exists()) {
                objFile.delete();
            }
            file.renameTo(objFile);
        }
    }

    public static void createFile(String name) throws Exception {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    public static String existFileForUrl(Context context, String url) {
        String fname = fileForUrl(context, url);
        if (fname != null) {
            File file = new File(fname);
            if (file.exists()) {
                return fname;
            }
        }
        return null;
    }

    public static String existDirForUrl(Context context, String url) {
        String fname = fileForUrl(context, url);
        if (!TextUtils.isEmpty(fname)) {
            String dirPath = fname.substring(0, fname.lastIndexOf("."));
            File file = new File(dirPath);
            if (file.isDirectory()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    public static String fileForUrl(Context context, String url) {
        String fileName = MiniMd5.md5String(url);
        int index = url.lastIndexOf(".");
        if (index != -1) {
            String ext = url.substring(index + 1);
            fileName = fileName + "." + ext;
        }
        fileName = getAppFilePath(context, "cache/download") + File.separator + fileName;
        return fileName;
    }

    public static String cacheSdPath(Context context) {
        return getAppFilePath(context, "cache/download");
    }


    public static String saveBitmap(Context context, Bitmap bitmap) throws Exception {
        String fileName = getAppFilePath(context, "cache/bitmap") + File.separator + System.currentTimeMillis() + ".jpg";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            return fileName;
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static Bitmap getImage(String fileName) {
        return MiniImageBitmapUtil.getDiskBitmap(fileName);
    }


    public static String codeString(String fileName) throws Exception {
        File file = new File(fileName);
        if (file == null || !file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
        try {
            int p = (bin.read() << 8) + bin.read();
            String code;
            //其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                case 0x5c75:
                    code = "ANSI|ASCII";
                    break;
                default:
                    code = "GBK";
            }
            return code;
        } finally {
            if (bin != null) {
                bin.close();
            }
        }
    }

    public static void unZip(String srcFile, String folderPath) throws IOException {
        ZipFile zipFile = new ZipFile(new File(srcFile));
        Enumeration zList = zipFile.entries();
        ZipEntry ze;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                String dirstr = new String(folderPath.getBytes("8859_1"), "GB2312");
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zipFile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zipFile.close();
    }


    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        String lastDir = baseDir+"/";
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                lastDir += (dirs[i] + "/");
                File dir = new File(lastDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            File ret = new File(lastDir, dirs[dirs.length - 1]);
            return ret;
        } else {
            return new File(baseDir, absFileName);
        }
    }
}
