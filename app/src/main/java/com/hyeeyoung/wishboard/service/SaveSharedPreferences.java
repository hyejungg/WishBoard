package com.hyeeyoung.wishboard.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {
    static final String PREF_USER_ID = "user_id";
    static final String PREF_USER_EMAIL = "email";
    static final String PREF_USER_CART = "isChecked_cartBtn";
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // @param : 사용자 정보 저장
    public static void setUserEmail(Context ctx, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, email);
        editor.commit();
    }
    // @param : 서버로부터 받은 User_id 저장
    public static void setUserId(Context ctx, String user_id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, user_id);
        editor.commit();
    }
    // @param : 메인화면에서 cart 버튼의 상태 저장
    public static void setCheckedCart(Context ctx, boolean isChecked) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_CART, isChecked);
        editor.commit();
    }

    // @param : 저장된 이메일 정보 가져오기
    public static String getUserEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    // @param : 저장된 User_id 가져오기
    public static String getUserId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }
    // @param : 저장된 cart 버튼의 상태 가져오기
    public static boolean getCheckedCart(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_CART, false);
    }

    // @param : 로그아웃, 앱 내에서 로그아웃 누를 경우에 사용 예정
    public static void clearUser(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}