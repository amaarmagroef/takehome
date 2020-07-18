package com.cermati.test.repo

import com.cermati.test.model.ListUser
import io.reactivex.rxjava3.core.Maybe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.rxjava3.core.Observable


/**
 * Created by siapaSAYA on 7/18/2020
 */
 
interface ApiRoute {
    @GET("search/users")
    fun getUsers(@Query("q") keyword: String?, @Query("page") page : Int): Maybe<ListUser>
}