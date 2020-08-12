package org.mini.frame.tools;


import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SDCardUtils {
    public SDCardUtils() {
    }

    public static boolean isMounted() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getSDPath() {
        return isMounted()?Environment.getExternalStorageDirectory().getAbsolutePath():null;
    }

    public static boolean saveFileIntoSDCard(byte[] data, String path, String fileName) {
        if(isMounted()) {
            BufferedOutputStream bos = null;

            boolean var6;
            try {
                String filePath = getSDPath() + File.separator + path;
                File file = new File(filePath);
                if(!file.exists()) {
                    file.mkdirs();
                }

                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data, 0, data.length);
                bos.flush();
                var6 = true;
            } catch (Exception var16) {
                var16.printStackTrace();
                return false;
            } finally {
                if(bos != null) {
                    try {
                        bos.close();
                    } catch (IOException var15) {
                        var15.printStackTrace();
                    }
                }

            }

            return var6;
        } else {
            return false;
        }
    }

    public static byte[] getFileFromSDCard(String filePath) {
        if(isMounted()) {
            File file = new File(filePath);
            BufferedInputStream bis = null;
            ByteArrayOutputStream byteOutputStream = null;
            if(file.exists()) {
                try {
                    byteOutputStream = new ByteArrayOutputStream();
                    bis = new BufferedInputStream(new FileInputStream(file));
                    int len = 0;
                    byte[] buffer = new byte[8192];
                    while((len = bis.read(buffer)) != -1) {
                        byteOutputStream.write(buffer, 0, len);
                        byteOutputStream.flush();
                    }

                    byte[] var6 = byteOutputStream.toByteArray();
                    return var6;
                } catch (Exception var16) {
                    var16.printStackTrace();
                    return null;
                } finally {
                    if(bis != null) {
                        try {
                            bis.close();
                            byteOutputStream.close();
                        } catch (IOException var15) {
                            var15.printStackTrace();
                        }
                    }

                }
            }
        }

        return null;
    }

    public static int copy(String fromFile, String toFile) {
        File root = new File(fromFile);
        if(!root.exists()) {
            return -1;
        } else {
            File[] currentFiles = root.listFiles();
            File targetDir = new File(toFile);
            if(!targetDir.exists()) {
                targetDir.mkdirs();
            }

            for(int i = 0; i < currentFiles.length; ++i) {
                if(currentFiles[i].isDirectory()) {
                    copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");
                } else {
                    CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
                }
            }

            return 0;
        }
    }

    public static int CopySdcardFile(String fromFile, String toFile) {
        try {
            InputStream fromInputStream = new FileInputStream(fromFile);
            OutputStream toOutputStream = new FileOutputStream(toFile);
            byte[] bt = new byte[1024];

            int c;
            while((c = fromInputStream.read(bt)) > 0) {
                toOutputStream.write(bt, 0, c);
            }

            fromInputStream.close();
            toOutputStream.close();
            return 0;
        } catch (Exception var6) {
            return -1;
        }
    }
}
