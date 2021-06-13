package com.hyeeyoung.wishboard.remote;

import com.hyeeyoung.wishboard.model.CartItem;
import com.hyeeyoung.wishboard.model.UserItem;
import com.hyeeyoung.wishboard.model.WishItem;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IRemoteService {

    //@brief : 네트워크 설정
    String BASE_URL = "http://ec2-13-125-227-20.ap-northeast-2.compute.amazonaws.com/"; // @brief : IP 주소 적기 13.125.227.20
    String IMAGE_URL = "https://wishboardbucket.s3.ap-northeast-2.amazonaws.com/wishboard/";

    /**
     * @brief NewItemFragment : 서버에 아이템 등록
     */

    /*
    @brief : POST 방식, BASE_URL/item 호출
     */

    // @brief : 아이템 정보 저장 요청
    @POST("/item")
    Call<ResponseBody> insertItemInfo(@Body WishItem wish_item);

    // @brief : 회원 정보 관련 요청
    @POST("/user/signup")
    Call<ResponseBody> signUpUser(@Body UserItem user_item);
    @POST("/user/signin")
    Call<UserItem> signInUser(@Body UserItem user_item);

    // @brief : 장바구니 관련 요청
    @POST("/basket")
    Call<CartItem> insertCartInfo(@Body CartItem cart_item);
    @GET("/basket/{user_id}")
    Call<ArrayList<CartItem>> selectCartInfo(@Path("user_id") String user_id);

    /* @see : delete의 경우 body로 숨겨서 전달할 경우, HTTP 어노테이션을 사용하여 전달
     *        retrofit에서 제공하는 @DELETE 어노테이션을 이용할 경우, @Path로 전달 가능
     *        참고 사이트 : https://square.github.io/retrofit/2.x/retrofit/retrofit2/http/HTTP.html
     */
    @HTTP(method = "DELETE", path = "/basket", hasBody = true)
    Call<CartItem> deleteCartInfo(@Body CartItem cart_item);

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

    // @brief : 홈화면에서 로그인한 사용자의 아이템 정보를 요청
    @GET("/item/detail/{item_id}")
    Call<WishItem> selectItemDetails(@Path("item_id") String item_id);
}
