package com.example.admin.testvk

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.admin.testvk.adapter.GroupsListAdapter
import com.example.admin.testvk.common.EndlessRecyclerViewScrollListener
import com.example.admin.testvk.model.GroupModel
import com.example.admin.testvk.presenter.GroupsPresenter
import com.example.admin.testvk.presenter.GroupsPresenterView
import com.vk.sdk.api.model.VKApiUserFull
import kotlinx.android.synthetic.main.fragment_friends_detail.*

class FriendsDetailFragment : MvpAppCompatFragment(), GroupsPresenterView {

    @InjectPresenter
    lateinit var mPresenter: GroupsPresenter

    private lateinit var mAdapter: GroupsListAdapter
    private lateinit var user: VKApiUserFull
    private lateinit var mScrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments.containsKey(ARG_ITEM)) {
            user = arguments.getParcelable<VKApiUserFull>(ARG_ITEM)
        }
        mAdapter = GroupsListAdapter(LayoutInflater.from(context))
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_friends_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        mScrollListener = object: EndlessRecyclerViewScrollListener(linearLayoutManager, 10) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                mPresenter.getData(user.id.toString(), page)
            }
        }

        recyclerView.addOnScrollListener(mScrollListener)
        recyclerView.adapter = mAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        mPresenter.getData(user.id.toString())
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        val ARG_ITEM = "item"
    }

    override fun showGroups(data: ArrayList<GroupModel>) {
        mAdapter.addAll(data)
        noDataLabel.visibility = if (mAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun showError(msg: String) {
        (activity as BaseActivity).showError(activity.getString(R.string.errordialog_title), msg)
    }
}
