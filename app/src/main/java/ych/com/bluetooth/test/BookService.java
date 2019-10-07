package ych.com.bluetooth.test;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

//接口方法和其参数上的注解指明了请求需要如何处理.
// 每个方法都必须有一个 HTTP 注解，用于提供请求类型(GET、POST、PUT、DELETE 和 HEAD)和对应的 URL (可以在 URL 中指定查询参数。)。

public interface BookService {

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST ("ultrasound/ultrasound01")
    Call<ResponseBody>  getBook(@Body RequestBody ultrasound);   // 请求体味RequestBody 类型
    @GET("ultrasound/{id}")
    Call<ResponseBody>  getUltrasound(@Path("id") long id);





    //@GET("ultrasound/{sUltra}/")   //通过使用注解来描述具体的 HTTP 请求,HTTP请求支持   URL 参数替换和指定查询参数、对象转换成请求体、文件上传。
                                        //是否可利用上述 请求支持类型  来传送数据？？？
    //Call<ResponseBody> getBook(@Path("sUltra") String sUltra);

   /* Call<ResponseBody> getBook();*/
    //定义了一个方法，每个 BookService 中的方法都可以向远程 web 服务器发出同步或异步的 HTTP 请求
                                    // 可见，是不是可以把数据放在请求中一起发出去？？？
                                    //Call是HTTP请求的意思，<>中的ResponseBody是什么意思？？？
                                    //答： ResponseBody是用来装获得的数据的。默认情况下，Retrofit 只能将 HTTP body 反序列化为 OkHttp 的 ResponseBody 类型，并且只能接受 @Body 的请求体类型。可以添加转换器（ConverterFactory）来支持其他类型。发送字符串数据用不到转换器
                                    //  getBook后面的括号里面填的参数   是需要替换到注解中的url中的值（@Path）、查询参数（@Query）。@Path和Query的区别：url中没有？的时候用Path

/*

    @POST("users/new")
    Call<User> createUser(@Body User user);

       对于@POST请求    方法参数中可以使用 @Body注释 将对象转换成请求体；还可以通过在 Retrofit 实例上指定的转换器（ConverterFactory）进行方法参数中对象的转换，如果没有添加转换器，则只能使用 RequestBody。
    */

/*

    @FormUrlEncoded
    @POST("user/edit")
    Call<User> updateUser(@Field("first_name") String first, @Field("last_name") String last);

    当方法上出现 @FormUrlEncoded 时，发送格式编码的数据。每个键值对都用 @Field 注释，在其后面是具体的名称和提供值的对象。
    */
}
