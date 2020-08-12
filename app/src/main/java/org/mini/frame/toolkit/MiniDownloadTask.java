package org.mini.frame.toolkit;

import android.os.AsyncTask;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gassion on 15/5/15.
 */
public class MiniDownloadTask extends AsyncTask<Void, Void, Boolean>  {

    public interface DownloadTaskListener {
        void downloadProgress(long currentNumber, long totalNumber);
        void done();
        void error(Exception e);
    }

    String urlStr;
    String filePath;
    DownloadTaskListener downloadFileListener;
    HttpURLConnection httpURLConnection;

    public MiniDownloadTask(String urlStr, String filePath, DownloadTaskListener downloadFileListener) {
        this.urlStr = urlStr;
        this.filePath = filePath;
        this.downloadFileListener = downloadFileListener;
    }

    public void start()
    {
        this.execute();
    }

    protected Boolean doInBackground(Void... voids) {
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.setRequestMethod("GET");
            int contentLength = httpURLConnection.getContentLength();

            // 创建文件，filePath为下载文件的本地路径
            File file = new File(filePath+".temp");
            if (file.exists()) {
                file.delete();
            }
            // 判断是否存在
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                // 创建该文件
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.setLength(contentLength);
            InputStream inputStream = httpURLConnection.getInputStream();
            int currentNumber = 0;
            byte[] buf = new byte[1024];
            int length;
            while ((length = inputStream.read(buf)) != -1) {
                randomAccessFile.write(buf, 0, length);
                currentNumber += length;
                if (!this.isCancelled()) {
                    downloadFileListener.downloadProgress(currentNumber, contentLength);
                }
            }
            inputStream.close();
            randomAccessFile.close();
            file.renameTo(new File(filePath));
            if (!this.isCancelled()) {
                downloadFileListener.done();
            }
            return null;
        } catch (Exception e) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            downloadFileListener.error(e);
            return false;
        }
        finally {
            closeConnection();
        }
    }

    protected void onCancelled() {
        closeConnection();
    }

    private void closeConnection() {
        try {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            httpURLConnection = null;
        }
        catch (Exception e) {

        }
    }

    public static String getFileNameByUrl(String downFileUrl) {
        if (downFileUrl != null) {
            int index = downFileUrl.lastIndexOf("/");
            String fileName = downFileUrl.substring(index);
            return fileName;
        }
        else {
            return null;
        }
    }
}
