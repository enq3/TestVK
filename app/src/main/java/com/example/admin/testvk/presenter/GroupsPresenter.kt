package com.example.admin.testvk.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.example.admin.testvk.model.GroupModel
import com.vk.sdk.api.*
import org.json.JSONObject

@InjectViewState
class GroupsPresenter : MvpPresenter<GroupsPresenterView>() {
    fun getData(userId: String, page: Int = 0, count: Int = 30) {
        val tempData = ArrayList<GroupModel>()
        val params = VKParameters.from(VKApiConst.USER_ID, userId, VKApiConst.EXTENDED, "1", VKApiConst.OFFSET, (page*count).toString(), VKApiConst.COUNT, count.toString())
        val request = VKApi.users().getSubscriptions(params)
        request?.executeWithListener(object: VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val groups = response?.json?.getJSONObject("response")?.optJSONArray("items")
                if (groups != null) {
                    (0..groups.length()-1)
                            .map { groups.get(it) as JSONObject }
                            .filter { it.getString("type") == "page" }
                            .mapTo(tempData) { GroupModel(name = it.getString("name"), photoUrl = it.getString("photo_200"), id = it.getInt("id")) }
                }
                viewState.showGroups(tempData)
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
}

interface GroupsPresenterView : MvpView {
    fun showGroups(data: ArrayList<GroupModel>)
    fun showError(msg: String)
}