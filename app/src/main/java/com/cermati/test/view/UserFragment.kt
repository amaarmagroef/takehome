package com.cermati.test.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cermati.test.R
import com.cermati.test.utils.BaseFragment
import com.cermati.test.utils.observe
import com.cermati.test.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * Created by siapaSAYA on 7/18/2020
 */

class UserFragment : BaseFragment() {
    var sTemp = ""
    var isNext = false

    override fun layoutResId(): Int = R.layout.fragment_search

    private var adapterUser : UserAdapter = UserAdapter()

    private val viewModel by lazy { ViewModelProvider(this).get(UserViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edittext_search.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s==sTemp)
                    isNext = false
                sTemp = s.toString()
                if(s!!.isNotEmpty()){
                    viewModel.getData(s.toString())
                } else adapterUser.clear()
            }

        })

        configureRecyclerView()
        bindableViewModel()


        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeRefresh.setOnRefreshListener {
            val query = edittext_search.text.toString()
            isNext = false
            if (query.isEmpty()){
                showRefreshIndicator(false)
                return@setOnRefreshListener
            }
            viewModel.getData(query)
        }
    }

    private fun showRefreshIndicator(show: Boolean) {
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeRefresh.post { swipeRefresh.isRefreshing = show }
    }

    private fun bindableViewModel(){
        observe(viewModel.users){
            if (view==null)return@observe
            showLoading(false)
            it?.let{
                if (it!=null) {
                    if(!isNext)
                        adapterUser.clear()
                    adapterUser.update(it.items!!)
                }
                else {
                    adapterUser.clear()
                    showMessage("Data Tidak Ditemukan")
                }
            }
        }

        observe(viewModel.errorcode){
            showLoading(false)
            showRefreshIndicator(false)
            when (it) {
                500 -> showMessage("Terjadi kesalahan koneksi")
                403 -> {
                    val snackbar = Snackbar.make(container,"limit request API Github", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK") {

                        }
                    snackbar.show()
                }
                200 -> {

                }
                422 -> {
                    showMessage("Permintaan tidak dapat dilanjutkan : $it")
                }
                else -> {
                    showMessage("Terjadi kesalahan, Error code : $it")
                }
            }
        }

    }

    fun showLoading(show : Boolean){
        if(show)
            progress_circular.visibility= View.VISIBLE
        else progress_circular.visibility = View.GONE
    }

    fun showMessage(message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    private fun configureRecyclerView(){
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            adapter = this@UserFragment.adapterUser
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
            addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val totalItemCount = mLayoutManager.itemCount
                    val lastVisibleItem = mLayoutManager.findLastVisibleItemPosition()
                    if ( /*mPresenter.loadMore() &&*/ totalItemCount > 0 && totalItemCount <= lastVisibleItem + 2 && viewModel.mCurrentPage < viewModel.mTotalPage) {
                        viewModel.getNextData(edittext_search.text.toString())
                        isNext=true
                        showLoading(true)
                    }

                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                }
            })
        }
        adapterUser.clear()
    }

}