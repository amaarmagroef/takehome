package com.cermati.test.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cermati.test.R
import com.cermati.test.databinding.ItemListUserBinding
import com.cermati.test.model.UserGitModel
import com.cermati.test.utils.BindableViewHolder
import kotlin.coroutines.coroutineContext


/**
 * Created by siapaSAYA on 7/18/2020
 */

class UserAdapter() : RecyclerView.Adapter<UserAdapter.ItemViewHolder>() {

    private var data = mutableListOf<UserGitModel>()

    fun update(data: List<UserGitModel>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clear() {
        this.data.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_list_user,
            parent,
            false)
        return ItemViewHolder(view)

    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val model = data[position]
        holder.bind(model)

    }

    class ItemViewHolder(view: View) :
        BindableViewHolder<ItemListUserBinding, UserGitModel>(view) {
        override fun bind(viewModel: UserGitModel) {
            binding?.model =viewModel
            binding!!.username.text = viewModel.login
            ViewCompat.setTransitionName(binding!!.imageAvatar, viewModel.avatarUrl)
        }
    }


}