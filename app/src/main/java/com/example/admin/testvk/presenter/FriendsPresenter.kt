package com.example.admin.testvk.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiUserFull
import com.vk.sdk.api.model.VKUsersArray

@InjectViewState
class FriendsPresenter : MvpPresenter<FriendsPresenterView>() {

    fun clearCache() {
        tempData.clear()
    }

    val tempData = ArrayList<VKApiUserFull>()
    private var totalItemsFromApi = 0
    private var lastPageCached = 0
    fun getData(userId: String, page: Int = 0, count: Int = 20) {
        val params = VKParameters.from(VKApiConst.USER_ID, userId,
                VKApiConst.FIELDS, "photo_max_orig, photo_200, universities, city",
                VKApiConst.OFFSET, (page * count).toString(),
                VKApiConst.COUNT, count.toString())
        val request = VKApi.friends().get(params)
        request.executeWithListener(object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                lastPageCached = page
                totalItemsFromApi = response?.json?.getJSONObject("response")?.getInt("count") ?: 0
                (response?.parsedModel as VKUsersArray).forEach {
                    tempData.add(it)
                }
                viewState.showFriends(tempData)
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                var message = "Unknown error"
                if (error?.apiError != null) {
                    message = error.apiError.errorMessage
                } else if (error?.errorMessage != null) {
                    message = error.errorMessage
                }
                viewState.showError(message)
            }
        })
    }

    fun filterData(query: String): List<VKApiUserFull?> {
        val queryLc = query.toLowerCase()

        val filtered = ArrayList<VKApiUserFull?>()
        for (model in tempData) {
            val firstNameContains = model.first_name?.toLowerCase()?.contains(queryLc) ?: false
            val lastNameContains = model.last_name?.toLowerCase()?.contains(queryLc) ?: false
            val cityContains = model.city?.title?.toLowerCase()?.contains(queryLc) ?: false

            var universityContains = false
            model.universities?.forEach {
                universityContains = it.name?.toLowerCase()?.contains(queryLc) ?: false
                if (universityContains) return@forEach

            }

            if (firstNameContains || lastNameContains || cityContains || universityContains) {
                filtered.add(model)
            }
        }
        return filtered
    }
}

interface FriendsPresenterView : MvpView {
    fun showFriends(data: ArrayList<VKApiUserFull>)
    fun showError(msg: String)
}