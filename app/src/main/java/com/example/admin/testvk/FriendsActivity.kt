package com.example.admin.testvk

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import com.example.admin.testvk.adapter.FriendsListAdapter
import com.example.admin.testvk.common.TestApp
import com.example.admin.testvk.presenter.FriendsPresenterView
import com.vk.sdk.api.model.VKApiUserFull
import kotlinx.android.synthetic.main.activity_friends.*
import kotlinx.android.synthetic.main.friends_list.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.admin.testvk.common.EndlessRecyclerViewScrollListener
import com.example.admin.testvk.presenter.FriendsPresenter


class FriendsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener, FriendsPresenterView {

    @InjectPresenter
    lateinit var mPresenter: FriendsPresenter

    private var mTwoPane:Boolean = false
    private lateinit var mAdapter: FriendsListAdapter
    private var userId = ""
    private lateinit var mScrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        userId = savedInstanceState?.getString(TestApp.USERIDKEY) ?: (intent?.extras?.getString(TestApp.USERIDKEY) ?: "empty id")

        refreshLayout.setOnRefreshListener(this)
        mAdapter = FriendsListAdapter(LayoutInflater.from(this), object : FriendsListAdapter.OnHolderClickClistener {
            override fun onClick(item: VKApiUserFull?) {
                if (mTwoPane) {
                    val arguments = Bundle()
                    arguments.putParcelable(FriendsDetailFragment.ARG_ITEM, item)
                    val fragment = FriendsDetailFragment()
                    fragment.arguments = arguments
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.friends_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(this@FriendsActivity, FriendsDetailActivity::class.java)
                    intent.putExtra(FriendsDetailFragment.ARG_ITEM, item)
                    startActivity(intent)
                }
            }
        })
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        mScrollListener = object: EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                mPresenter.getData(userId, page)
            }
        }
        recyclerView.addOnScrollListener(mScrollListener)
        recyclerView.adapter = mAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        searchView.setOnQueryTextListener(this)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (findViewById<View>(R.id.friends_detail_container) != null)
        {
            mTwoPane = true
        }
        mPresenter.getData(userId = userId)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(TestApp.USERIDKEY, userId)
    }

    override fun onRefresh() {
        refreshLayout.isRefreshing = true
        mPresenter.clearCache()
        mAdapter.removeAll()
        mPresenter.getData(userId = userId)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            val filteredModelList = mPresenter.filterData(newText)
            mAdapter.replaceAll(filteredModelList)
            recyclerView.scrollToPosition(0)
            return true
        }
        return false
    }

    override fun showFriends(data: ArrayList<VKApiUserFull>) {
        val searchedText = searchView?.query?.toString()
        if (searchedText != null) {
            val filteredModelList = mPresenter.filterData(searchedText)
            mAdapter.replaceAll(filteredModelList)
        } else {
            mAdapter.replaceAll(data)
        }
        refreshLayout.isRefreshing = false
    }

    override fun showError(msg: String) {
        showError(getString(R.string.errordialog_title), msg)
    }
}
