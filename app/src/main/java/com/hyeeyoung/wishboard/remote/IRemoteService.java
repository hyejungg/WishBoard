package com.hyeeyoung.wishboard.remote;

import com.hyeeyoung.wishboard.model.CartItem;
import com.hyeeyoung.wishboard.model.FolderItem;
import com.hyeeyoung.wishboard.model.NotiItem;
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
import retrofit2.http.PUT;
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

    // @brief : 홈화면에서 로그인한 사용자의 아이템 정보를 요청
    @GET("/noti/{user_id}")
    Call<ArrayList<NotiItem>> selectNotiInfo(@Path("user_id") String user_id);


    // @brief : wishbaord 앱 회원가입 요청
    @POST("/user/signup")
    Call<ResponseBody> signUpUser(@Body UserItem user_item);

    // @brief : wishbaord 앱 로그인 요청
    @POST("/user/signin")
    Call<UserItem> signInUser(@Body UserItem user_item);

    // @brief : 장바구니 데이터 추가 요청
    @POST("/basket")
    Call<CartItem> insertCartInfo(@Body CartItem cart_item);

    // @brief : 장바구니 데이터 조회 요청
    @GET("/basket/{user_id}")
    Call<ArrayList<CartItem>> selectCartInfo(@Path("user_id") String user_id);

    // @brief : 장바구니 데이터 삭제 요청
    @HTTP(method = "DELETE", path = "/basket", hasBody = true)
    Call<ResponseBody> deleteCartInfo(@Body CartItem cart_item);
    /* @see : delete의 경우 보안을 위해 body로 숨겨서 전달할 때 HTTP 어노테이션을 사용하여 전달
     *        retrofit에서 제공하는 @DELETE 어노테이션을 이용할 경우, @Path로 전달 가능
     *        참고 사이트 : https://square.github.io/retrofit/2.x/retrofit/retrofit2/http/HTTP.html
     */
    // @brief : 장바구니 데이터 수정 요청
    @PUT("/basket")
    Call<ResponseBody> updateCartInfo(@Body ArrayList<CartItem> cartItem);


    // @brief : 장바구니 아이템 개수 요청
    @GET("/basket/cnt/{user_id}")
    Call<String> selectCartCnt(@Path("user_id") String user_id);

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

    // @brief : 아이템 상세조회에서 아이템 수정
    @PUT("/item/detail/{item_id}")
    Call<WishItem> updateItem(@Path("item_id") String item_id, @Body WishItem wish_item);

    // @brief : 아이템 상세조회에서 아이템 삭제
    @DELETE("/item/detail/{item_id}")
    Call<Void> deleteItem(@Path("item_id") String item_id);

    // @brief : 알림 정보 저장 요청
    @POST("/noti")
    Call<ResponseBody> insertItemNoti(@Body NotiItem noti_item);

    // @brief : 아이템 상세조회에서 상품 알림 정보 삭제 요청
    @DELETE("/noti/detail/{item_id}")
    Call<Void> deleteItemNoti(@Path("item_id") String item_id);

    // @brief : 아이템 상세조회에서 상품 알림 정보 수정 요청
    @PUT("/noti/detail/{item_id}")
    Call<ResponseBody> updateItemNoti(@Path("item_id") String item_id, @Body NotiItem noti_item);

    // @brief : 알림화면에서 사용자가 조회한 알림은 읽은 알림으로 수정 요청
    @PUT("/noti/{item_id}")
    Call<ResponseBody> updateNotiRead(@Path("item_id") String item_id);

    // @brief : 홈화면에서 로그인한 사용자의 폴더 정보를 요청
    @GET("/folder/{user_id}")
    Call<ArrayList<FolderItem>> selectFolderInfo(@Path("user_id") String user_id);

    // @brief : 폴더명을 추가 요청
    @POST("/folder")
    Call<ResponseBody> insertFolderInfo(@Body FolderItem folderItem);

    // @brief : 폴더명을 수정 요청
    @PUT("/folder")
    Call<ResponseBody> updateFolderInfo(@Body FolderItem folderItem);

    // @brief : 폴더 삭제 요청
    @HTTP(method = "DELETE", path = "/folder", hasBody = true)
    Call<ResponseBody> deleteFolderInfo(@Body FolderItem folderItem);

    // @brief : 수동등록 및 수정화면에서 보여질 폴더리스트 정보 요청
    @GET("/folder/list/{user_id}")
    Call<ArrayList<FolderItem>> selectFolderListInfo(@Path("user_id") String user_id);

    // @brief : 폴더화면 내 아이템 정보 요청
    @GET("/folder/item/{user_id}/{folder_id}")
    Call<ArrayList<WishItem>> selectFolderItemInfo(@Path("user_id") String user_id, @Path("folder_id") String folder_id);

    // @brief : 아이템 수정, 추가 시 폴더 이미지 변경 요청 (최신순으로 보이기 위함)
    @PUT("/folder/item/image")
    Call<ResponseBody> updateFolderImage(@Body FolderItem folderItem);
}
