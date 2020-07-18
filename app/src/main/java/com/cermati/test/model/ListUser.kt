package com.cermati.test.model

import com.google.gson.annotations.SerializedName


/**
 * Created by siapaSAYA on 7/18/2020
 */

class ListUser {
    @SerializedName("total_count")
    var totalCount: Int? = null

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean? = null

    @SerializedName("items")
    var items: List<UserGitModel>? = null
}