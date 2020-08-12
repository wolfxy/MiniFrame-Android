package org.mini.frame.toolkit;

import android.net.TrafficStats;

import org.mini.frame.toolkit.manager.MiniAppManager;

/**
 * Created by huangqihua on 15/12/15.
 */
public class MiniTrafficUtil {



    //总流量值
    public static long getUidTotal(){
        int appUid = MiniAppManager.getAppUid();
        long uidTxBytes = getUidTxBytes(appUid);
        long uidRxBytes = getUidRxBytes(appUid);
        return uidRxBytes + uidTxBytes;
    }


    public static long getUidRxBytes(int uid) {  //得到本应用接受的字节数
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getUidRxBytes(uid) / 1024);
    }

    public static long getUidTxBytes(int uid) {  //得到本应用发送的字节数
        return TrafficStats.getUidTxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getUidTxBytes(uid) / 1024);
    }


}
