package com.hyeeyoung.wishboard.remote;

import com.google.gson.JsonElement;
import com.hyeeyoung.wishboard.model.CartItem;
import com.hyeeyoung.wishboard.model.UserItem;
import com.hyeeyoung.wishboard.model.WishItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRemoteService {

    //@brief : 네트워크 설정
    String BASE_URL = "http://ec2-13-125-227-20.ap-northeast-2.compute.amazonaws.com/"; // @brief : IP 주소 적기 13.125.227.20
    String IMAGE_URL = "https://wishboardbucket.s3.ap-northeast-2.amazonaws.com/wishboard/";
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


    // @brief : 아이템 정보 저장 요청
    @POST("/item/new")
    Call<ResponseBody> insertItemInfo(@Body WishItem wish_item);

    // @brief : 회원 정보 관련 요청
    @POST("/user/signup")
    Call<ResponseBody> signUpUser(@Body UserItem user_item);
    @POST("/user/signin")
    Call<UserItem> signInUser(@Body UserItem user_item);

    // @brief : 장바구니 관련 요청
    @POST("/basket/add")
    Call<CartItem> insertCartInfo(@Body CartItem cart_item);
    @GET("/basket/{user_id}")
    Call<ArrayList<CartItem>> selectCartInfo(@Path("user_id") String user_id);
    @DELETE("/basket/delete/{user_id}/{item_id}")
    Call<CartItem> deleteCartInfo(@Path("user_id") String user_id, @Path("item_id") String item_id);

    /*
    @brief :
        @GET( EndPoint-자원위치(URI) )  ET 방식, BASE_URL/home/{user_id} 호출
        Data Type의 JSON을 통신을 통해 받음
        @Path("user_id") String id : id 로 들어간 String 값을 {user_id}에 넘김
        @return WishItem 객체를 JSON 형태로 반환
     */

    // @brief : 홈화면에서 로그인한 사용자의 아이템 정보를 요청
    @GET("/item/home/{user_id}")
    Call<ArrayList<WishItem>> selectItemInfo(@Path("user_id") String user_id); // @brief : <>안의 자료형은 JSON 데이터를 <>안의 자료형으로 받겠다는 의미.

}
