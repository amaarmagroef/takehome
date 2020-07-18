package com.cermati.test.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cermati.test.model.ListUser
import com.cermati.test.repo.ApiClient
import com.cermati.test.repo.ApiRoute
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext


/**
 * Created by siapaSAYA on 7/18/2020
 */

class UserViewModel() : ViewModel(), CoroutineScope {
    private var disposables : Disposable? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    var queryKeyword = ""

    var mCurrentPage = 1

    var mTotalPage = 1

    val baseUrl ="https://api.github.com/"
    private var mApiRoute : ApiRoute = ApiClient.GetClient(baseUrl)!!.create(ApiRoute::class.java)

    val _errorcode = MutableLiveData<Int?>()
    val errorcode: LiveData<Int?>
        get() = _errorcode

    private val _users = MutableLiveData<ListUser?>()
    val users: LiveData<ListUser?>
        get() = _users

    init {

    }


    fun getData(q : String){
        if(q=="")
            return
        queryKeyword = q
        val dataObservable: Observable<ListUser?> = setObservable(queryKeyword, 1)
        disposables?.dispose()
        /*disposables = dataObservable.subscribe ({
            _users.value = it
        },{
            _users.value = null
        })*/
        launch {
            disposables = dataObservable.subscribe({
                _errorcode.value = 200
                _users.value = it
                mTotalPage= it?.totalCount!!/30
            },{
                if(it.cause.toString().contains("403")){
                    _errorcode.value = 403
                } else if (it is HttpException) {
                    val codeError : Int? = (it).response()?.code()
                    Log.d("limit", codeError.toString())
                    _errorcode.value = codeError
                } else _errorcode.value = 500
            },{

            })
        }

    }

    fun getNextData(q : String) {
        if(q=="")
            return
        if(mCurrentPage==mTotalPage)
            return
        mCurrentPage++

        queryKeyword = q
        val dataObservable: Observable<ListUser?> = setObservable(queryKeyword, mCurrentPage)
        disposables?.dispose()
        /*disposables = dataObservable.subscribe ({
            _users.value = it
        },{
            _users.value = null
        })*/
        launch {
            disposables = dataObservable.subscribe({
                _errorcode.value = 200
                _users.value = it
                mTotalPage= it?.totalCount!!
            },{
                if(it.cause.toString().contains("403")){
                    _errorcode.value = 403
                } else if (it is HttpException) {
                    val codeError : Int? = (it).response()?.code()
                    Log.d("limit", codeError.toString())
                    _errorcode.value = codeError
                } else _errorcode.value = 500
            },{

            })
        }
    }

    private fun setObservable(q: String, page:Int): Observable<ListUser?> {
        return mApiRoute.getUsers(q, mCurrentPage)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}