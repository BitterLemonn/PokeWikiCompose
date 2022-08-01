package com.poke.pokewikicompose.data.repository

import com.poke.pokewikicompose.data.bean.PokemonDetailBean
import com.poke.pokewikicompose.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.data.bean.UserBean
import com.poke.pokewikicompose.utils.ResponseData
import com.poke.pokewikicompose.utils.SERVER_URL
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * 用于连接后台API的接口类，使用create函数获取实例调用对接方法
 */
interface ServerApi {

    @POST("login")
    @FormUrlEncoded
    suspend fun getAuth(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseData<UserBean>

    @POST("reg")
    @FormUrlEncoded
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseData<UserBean>

    @POST("selectAllPokeIntro")
    @FormUrlEncoded
    suspend fun getAllWithPage(
        @Field("page") page: Int
    ): ResponseData<ArrayList<PokemonSearchBean>>

    @Streaming
    @GET
    suspend fun downloadFromUrl(@Url url: String): Response<ResponseBody>

    @POST("getPokemonDetails")
    @FormUrlEncoded
    suspend fun getPokemonDetail(
        @Field("poke_id") poke_id: Int,
        @Field("userId") user_id: String
    ): ResponseData<PokemonDetailBean>

    @POST("addUserStar")
    @FormUrlEncoded
    suspend fun like(
        @Field("userId") user_id: String,
        @Field("pokemon_id") poke_id: Int
    ): ResponseData<Any>

    @POST("cancelUserStar")
    @FormUrlEncoded
    suspend fun unlike(
        @Field("userId") user_id: String,
        @Field("pokemon_id") poke_id: Int
    ): ResponseData<Any>

    @POST("searchPokeByName")
    @FormUrlEncoded
    suspend fun getPokemonByName(
        @Field("pokemon_keyword") pokemon_keyword: String
    ): ResponseData<ArrayList<PokemonSearchBean>>

    @POST("searchByGeneration")
    @FormUrlEncoded
    suspend fun getPokemonByGen(
        @Field("generation") pokemon_keyword: String
    ): ResponseData<ArrayList<PokemonSearchBean>>

    @POST("searchByType")
    @FormUrlEncoded
    suspend fun getPokemonByType(
        @Field("type") pokemon_keyword: String
    ): ResponseData<ArrayList<PokemonSearchBean>>

    @POST("updateUsername")
    @FormUrlEncoded
    suspend fun updateUsername(
        @Field("newusername") username: String,
        @Field("userId") userId: String,
        @Field("token") token: String
    ): ResponseData<UserBean>

    @POST("updatePassword")
    @FormUrlEncoded
    suspend fun updatePassword(
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String,
        @Field("userId") userId: String,
        @Field("token") token: String
    ): ResponseData<UserBean>

    @POST("changeProfilePicture")
    suspend fun updateUserIcon(
        @Body multipartBody: MultipartBody
    ): ResponseData<String>

    @POST("getUserFavorite")
    @FormUrlEncoded
    suspend fun getMyCollection(
        @Field("userId") userId: String
    ): ResponseData<ArrayList<PokemonSearchBean>>

    companion object {
        /**
         * 获取接口实例用于调用对接方法
         * @return ServerApi
         */
        fun create(): ServerApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor())
                .build()
            return Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ServerApi::class.java)
        }
    }
}