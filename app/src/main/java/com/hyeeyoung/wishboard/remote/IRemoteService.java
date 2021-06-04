package com.hyeeyoung.wishboard.remote;

import com.hyeeyoung.wishboard.model.WishItem;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IRemoteService {

    //@brief : 네트워크 설정
    String BASE_URL = ""; // @brief : IP 주소 적기

    /*@brief : 각 요청 URL
    String NEW_ITEM_URL = BASE_URL+"/item/";
    String HOME_URL = BASE_URL+"/home/";
    */

    /**
     * @brief NewItemFragment : 서버에 아이템 등록
     */

    /*
    @brief : POST 방식, BASE_URL/item 호출
     */

    @POST("/item/new")
    Call<ResponseBody> insertItemInfo(@Body WishItem wish_item);

    /*
    @brief : GET 방식, BASE_URL/home/{user_id} 호출
    Data Type의 JSON을 통신을 통해 받음
    @Path("user_id") String id : id 로 들어간 String 값을 {user_id}에 넘김
    @return WishItem 객체를 JSON 형태로 반환
     */

    @GET("/home/{user_id}")
    Call<WishItem> getData(@Path("user_id") String user_id);
}