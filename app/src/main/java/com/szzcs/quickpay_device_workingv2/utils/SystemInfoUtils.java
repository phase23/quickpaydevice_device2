package com.szzcs.quickpay_device_workingv2.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yyzz on 2018/12/10.
 */
public class SystemInfoUtils {


    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.DISPLAY;
        //return android.os.Build.VERSION.RELEASE;

    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取SN
     *
     * @return
     */
    public static String getSn(Context ctx) {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");

        } catch (Exception ignored) {

        }
        return serial;
    }


    /**
     * 统一使用这个获取IMEI IMEI2 MEID
     *
     * @param ctx
     * @return
     */
    @SuppressLint({"MissingPermission", "NewApi"})
    public static Map getImeiAndMeid(Context ctx) {
        Map<String, String> map = new HashMap<>();
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            map.put("imei1", telephonyManager.getImei(0));
            map.put("imei2", telephonyManager.getImei(1));
            int phoneType = telephonyManager.getPhoneType();
            if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                map.put("meid", telephonyManager.getDeviceId(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String getMac() {
        if (Build.VERSION.SDK_INT > 28) {
            return getMacFromHardware();
        }
        String macSerial = "";
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static String getMacFromHardware() {
        try {
            ArrayList<NetworkInterface> all =
                    Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equals("wlan0"))
                    continue;
                byte macBytes[] = nif.getHardwareAddress();
                if (macBytes == null) return "";
                StringBuilder res1 = new StringBuilder();
                for (Byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (!TextUtils.isEmpty(res1)) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
