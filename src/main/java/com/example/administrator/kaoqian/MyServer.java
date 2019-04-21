package com.example.administrator.kaoqian;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MyServer {
    public String URL="http://yun918.cn/study/public/";
    @POST("file_upload.php")
    @Multipart
    Observable<UpLoadBean>getCall(@Part("key")RequestBody requestBody, @Part MultipartBody.Part file);
}
