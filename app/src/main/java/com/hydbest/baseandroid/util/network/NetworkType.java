package com.hydbest.baseandroid.util.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.IntDef;

import static android.net.ConnectivityManager.TYPE_BLUETOOTH;
import static android.net.ConnectivityManager.TYPE_DUMMY;
import static android.net.ConnectivityManager.TYPE_ETHERNET;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_MOBILE_DUN;
import static android.net.ConnectivityManager.TYPE_MOBILE_HIPRI;
import static android.net.ConnectivityManager.TYPE_MOBILE_MMS;
import static android.net.ConnectivityManager.TYPE_MOBILE_SUPL;
import static android.net.ConnectivityManager.TYPE_VPN;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.ConnectivityManager.TYPE_WIMAX;

public class NetworkType {
    public static final int NETWORKTYPE_WIFI = 100;
    public static final int NETWORKTYPE_MOBILE = 101;
    public static final int NETWORKTYPE_OTHER = 102;
    public static final int NETWORKTYPE_UNKNOW = 103;

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER,ElementType.FIELD})
    @IntDef({NETWORKTYPE_WIFI,NETWORKTYPE_MOBILE,NETWORKTYPE_OTHER,NETWORKTYPE_UNKNOW})
    public @interface DefNetInt{
    }

    @DefNetInt
    private int netType = NETWORKTYPE_UNKNOW;
    private String desc;
    private int type;

    public static NetworkType build(int type) {
        NetworkType networkType = new NetworkType().setType(type);
        switch (type) {
            case TYPE_MOBILE:
                networkType.setDesc("TYPE_MOBILE").setNetType(NETWORKTYPE_MOBILE);
                break;
            case TYPE_MOBILE_MMS:
                networkType.setDesc("TYPE_MOBILE_MMS").setNetType(NETWORKTYPE_MOBILE);
                break;
            case TYPE_MOBILE_SUPL:
                networkType.setDesc("TYPE_MOBILE_SUPL").setNetType(NETWORKTYPE_MOBILE);
                break;
            case TYPE_MOBILE_DUN:
                networkType.setDesc("TYPE_MOBILE_DUN").setNetType(NETWORKTYPE_MOBILE);
                break;
            case TYPE_MOBILE_HIPRI:
                networkType.setDesc("TYPE_MOBILE_HIPRI").setNetType(NETWORKTYPE_MOBILE);
                break;
            case TYPE_WIFI:
                networkType.setDesc("TYPE_WIFI").setNetType(NETWORKTYPE_WIFI);
                break;
            case TYPE_WIMAX:
                networkType.setDesc("TYPE_WIMAX").setNetType(NETWORKTYPE_OTHER);
                break;
            case TYPE_BLUETOOTH:
                networkType.setDesc("TYPE_BLUETOOTH").setNetType(NETWORKTYPE_OTHER);
                break;
            case TYPE_DUMMY:
                networkType.setDesc("TYPE_DUMMY").setNetType(NETWORKTYPE_OTHER);
                break;
            case TYPE_ETHERNET:
                networkType.setDesc("TYPE_ETHERNET").setNetType(NETWORKTYPE_OTHER);
                break;
            case TYPE_VPN:
                networkType.setDesc("TYPE_VPN").setNetType(NETWORKTYPE_OTHER);
                break;
            default:
                networkType.setDesc("TYPE_UNKNOW").setNetType(NETWORKTYPE_UNKNOW);
                break;
        }
        return networkType;
    }

    public NetworkType setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    /**
     * @see android.net.ConnectivityManager
     * @param type
     * @return
     */
    public NetworkType setType(int type) {
        this.type = type;
        return this;
    }

    public NetworkType setNetType(@DefNetInt int netType) {
        this.netType = netType;
        return this;
    }

    public int getNetType() {
        return netType;
    }

    public String getDesc() {
        return desc;
    }

    public int getType() {
        return type;
    }
}
