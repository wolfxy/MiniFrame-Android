package org.mini.frame.tools;

/**
 * Created by Wuquancheng on 2018/10/26.
 */

public class AndroidVersion {

    public static int compare(String version1, String version2) {
        if (version1 != null && version2 != null && version1.equals(version2)) {
            return 0;
        }
        String[] version1Elements = version1.split("\\.");
        String[] version2Elements = version2.split("\\.");
        for (int index = 0; index < version1Elements.length && index < version2Elements.length; index++) {
            int ele1 = Integer.parseInt(version1Elements[index]);
            int ele2 = Integer.parseInt(version2Elements[index]);
            int sub = ele1 - ele2;
            if (sub > 0) {
                return 1;
            }
            else if (sub < 0) {
                return -1;
            }
        }
        if (version2Elements.length > version1Elements.length) {
            return -1;
        }
        else {
            return 1;
        }
    }
}
