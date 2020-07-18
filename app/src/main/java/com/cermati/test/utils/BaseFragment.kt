package com.cermati.test.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


/**
 * Created by siapaSAYA on 7/18/2020
 */

abstract class BaseFragment : Fragment(), CoroutineScope {

    protected val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    abstract fun layoutResId(): Int

    @MenuRes
    protected open fun toolbarMenuResId(): Int? = null

    protected open val navController by lazy { findNavController() }

    protected open fun transitionId(): Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (transitionId() != -1) {
            sharedElementEnterTransition = TransitionInflater.from(context)
                .inflateTransition(transitionId())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(toolbarMenuResId() != null)
        return inflater.inflate(layoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}