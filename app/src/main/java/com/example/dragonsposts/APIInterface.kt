package com.example.dragonsposts

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIInterface {

    @GET("users/")
    fun getALLUsers():Call<Users>

    @POST("users/")
    fun addUser(@Body user : UsersItem): Call<UsersItem>

    @GET("login/{username}/{password}")
    fun logIn(@Path("username") username: String, @Path("password") password: String): Call<String>

    @GET("users/{api_key}")
    fun getUser(@Path("api_key") api :String): Call<UsersItem>

    @GET("posts/")
    fun getAllPosts():Call<Posts>


    @GET("posts/{id}")
    fun getPost(@Path("id")id:Int): Call<PostsItem>

    @POST("posts/")
    fun addPost(@Body postData: PostsItem ): Call<PostsItem>

    @PUT("posts/{id}")
    fun updatePost(@Path("id") id: Int, @Body post: PostsItem ): Call<PostsItem>

}