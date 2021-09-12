package com.hyeeyoung.wishboard.remote;

import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// @brief : 추후 사용
public class RemoteLib {
    static IRemoteService remote_service = ServiceGenerator.createService(IRemoteService.class);
    private static final String TAG = "RemoteLib";

    /**
     * @param user_id 사용자 아이디
     * @brief : 서버에서 장바구니 아이템 개수를 조회
     */
    public static String getCartItemCnt(String user_id) {
        final String[] array_cart = {"0"};
        Call<String> call = remote_service.selectCartCnt(user_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                array_cart[0] = response.body();
                if (response.isSuccessful()) {
                    Log.i(TAG, "Retrofit 통신 성공");
                    Log.i(TAG, "장바구니 아이템 개수 : " + array_cart[0] + "");
                } else {
                    // @brief : 통신에 실패한 경우
                    Log.e(TAG, "Retrofit 통신 실패");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // @brief : 통신 실패 시 callback
                Log.e(TAG, "서버 연결 실패");
                t.printStackTrace();
            }
        });

        return array_cart[0];
    }
}
