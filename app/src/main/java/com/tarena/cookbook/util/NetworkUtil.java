package com.tarena.cookbook.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NetworkUtil {
    public static String KEY = "0f7600307ed770dc2a24ee927346c7a1";

    /**
     * 判断当前手机网络状态
     *
     * @param context
     * @return
     */
    public static boolean getNetState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * pn	数据返回起始下标，默认0
     * rn	数据返回条数，最大30，默认10
     */
    public static String getURL(int id, String menu, int pn, int rn) {
        String url;
        if (menu == null || menu.equals("")) {
            url = "http://apis.juhe.cn/cook/index?key=" + KEY + "&cid=" + id + "&pn=" + pn + "&rn=" + rn;
        } else {
            try {
                menu = URLEncoder.encode(menu, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url = "http://apis.juhe.cn/cook/query?key=" + KEY + "&menu=" + menu + "&pn=" + pn + "&rn=" + rn;
        }
        return url;
    }

    public static String getURL(int id, int pn) {
        String url = "http://apis.juhe.cn/cook/index?key=" + KEY + "&cid=" + id + "&pn=" + pn;
        return url;
    }

    public static String getURL(int id) {
        String url = "http://apis.juhe.cn/cook/index?key=" + KEY + "&cid=" + id;
        return url;
    }

}
