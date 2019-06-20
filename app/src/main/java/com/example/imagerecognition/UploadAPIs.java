package com.example.imagerecognition;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadAPIs {
    @POST ("/service/buildings/recognize/ HTTP/1.1")
    @Headers({"Content-Type: multipart/form-data",
            "Content-Disposition: attachment;filename= 'filename.jpg' "})
    //Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part RequestBody requestBody);
    Call <ResponseBody> uploadBinaryFile(@Body RequestBody body);

}
