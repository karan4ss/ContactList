package com.example.contactlist;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface myApi {
    @FormUrlEncoded
    @POST("addGroups.php")
    Call<ModelGroupName> addDatta(@Field("group_name") String groupname);
}
