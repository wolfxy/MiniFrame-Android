package org.mini.frame.toolkit;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuquancheng on 16/10/8.
 */
public class MiniURIWrapper {
    private String uri ;
    private String scheme;
    private String path;
    private Map<String, String> param = new HashMap<>(0);

    public MiniURIWrapper(String uri) {
        if (uri == null) {
            return;
        }
        this.uri = uri;
        int index = uri.indexOf(":");
        int start = 0;
        if (index != -1) {
            scheme = uri.substring(0,index);
            start = index + 3;
        }
        index = this.uri.indexOf("?");
        if (index != -1) {
            this.path = this.uri.substring(start,index);
            String query = this.uri.substring(index+1);
            try {
                query = URLDecoder.decode(query, "utf-8");
            }
            catch (Exception e) {

            }
            String[] kvs = query.split("&");
            if (kvs != null && kvs.length > 0) {
                for (String kv : kvs) {
                    String[] item = kv.split("=");
                    if (item != null && item.length == 2) {
                        param.put(item[0].trim(), item[1].trim());
                    }
                }
            }
        }
        else {
            this.path = this.uri.substring(start);
        }
    }


    public String getPath() {
        return path;
    }

    public String getScheme() {
        return scheme;
    }

    public String getParameter(String key) {
        return param.get(key);
    }

}
