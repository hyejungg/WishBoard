package com.hyeeyoung.wishboard.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RemoteLib {
    private static RemoteLib remote_lib;

    public static RemoteLib getInstance() {
        if (remote_lib == null) {
            remote_lib = new RemoteLib();
        }
        return remote_lib;
    }

    /**
     * 커넥션 여부 리턴
     *
     * @param context
     * @return
     */
    public boolean isConnected(Context context) {
        try {
            // 시스템으로부터 커넥션 서비스를 얻어온다.
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 커넥션 서비스로부터 네트워크 상태를 받아오고
            NetworkInfo info = manager.getActiveNetworkInfo();

            if (info != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
