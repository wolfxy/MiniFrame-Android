package org.mini.frame.toolkit;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import org.mini.frame.log.MiniLogger;
import org.mini.frame.toolkit.file.MiniFileManager;
import org.mini.frame.tools.cache.MiniCache;
import org.mini.frame.view.MiniUIDialog;
import org.mini.frame.view.MiniUIDownloadDialog;

import java.io.File;

public abstract class MiniAppUpgradeKit {

    private Activity activity;
    private Handler handler = new Handler();
    public MiniAppUpgradeKit(Activity activity) {
        this.activity = activity;
    }

    public void checkAppNewVersion()
    {
        Long lastCheckUpdateTime = MiniCache.get("Last_Check_Update_Time", Long.class);
        if (lastCheckUpdateTime == null) {
            lastCheckUpdateTime = 0L;
        }
        final Long currentTime = System.currentTimeMillis();
        long interval = 2*3600*1000;
        //long interval = 200;
        if (currentTime - lastCheckUpdateTime < interval) {
            return;
        }
        processCheckVersion(currentTime);
    }

    protected abstract void processCheckVersion(final Long currentTime);


    protected void setLastCheckTime(Long time) {
        MiniCache.set("Last_Check_Update_Time", time);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void processVersionData(final String address, String desc) {
        MiniUIDialog dialog = new MiniUIDialog(activity);
        dialog.setTitle("升级提示")
                .setMessage(desc)
                .setOkButtonTitle("升级")
                .setCancelButtonTitle("取消")
                .setCallback(new MiniUIDialog.MiniDialogCallback() {
                    @Override
                    public void onOkButtonClicked() {
                        handler.post(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void run() {
                                upgrade(address);
                            }
                        });
                    }

                    @Override
                    public void onCancelButtonClicked() {

                    }
                });
        dialog.show();
    }

    MiniDownloadTask downloadTask = null;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void upgrade(String address)
    {
        final File file = new File(MiniFileManager.cacheSdPath(activity), MiniDownloadTask.getFileNameByUrl(address));
        if (false && file.exists()) {
            MiniSystem.installApk(activity, file.getAbsolutePath());
        }
        else {
            final MiniUIDownloadDialog downloadDialog = new MiniUIDownloadDialog(activity);
            downloadDialog.setTitle("正在下载文件")
                    .setMessage("")
                    .setOkButtonTitle("取消")
                    .setCallback(new MiniUIDialog.MiniDialogCallback() {
                        @Override
                        public void onOkButtonClicked() {
                            if (downloadTask != null) {
                                downloadTask.cancel(true);
                            }
                        }

                        @Override
                        public void onCancelButtonClicked() {

                        }
                    });
            downloadTask = new MiniDownloadTask(address, file.getAbsolutePath(), new MiniDownloadTask.DownloadTaskListener() {
                @Override
                public void downloadProgress(long currentNumber, long totalNumber) {
                    MiniLogger.get().d("Downloading progress " + currentNumber + '/' + totalNumber);
                    final float progress = (currentNumber / (float) totalNumber);
                    handler.post(new Runnable() {
                        public void run() {
                            downloadDialog.setProgress(progress);
                        }
                    });
                }

                @Override
                public void done() {
                    MiniLogger.get().d("Downloading finished ...");
                    MiniSystem.installApk(activity, file.getAbsolutePath());
                }

                @Override
                public void error(Exception e) {
                    MiniLogger.get().e(e);
                }
            });
            downloadTask.start();
            downloadDialog.show();
        }
    }
}
