package com.example.admin.testvk.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.example.admin.testvk.R
import com.example.admin.testvk.common.GlideApp
import com.example.admin.testvk.model.GroupModel
import kotlinx.android.synthetic.main.card_group.view.*

class GroupsListAdapter(val inflater: LayoutInflater) : RecyclerView.Adapter<GroupsListAdapter.ViewHolder>() {

    private var dataSet = ArrayList<GroupModel>()

    fun addAll(array: ArrayList<GroupModel>) {
        val curSize = itemCount
        dataSet.addAll(array)
        notifyItemRangeInserted(curSize, array.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.card_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.count()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mItem: GroupModel? = null

        fun bind(info: GroupModel?) {
            mItem = info
            itemView.groupName.text = info?.name
            val photo = info?.photoUrl
            if (photo != null) {
                GlideApp
                        .with(itemView.context)
                        .load(photo)
                        .centerCrop()
                        .placeholder(R.drawable.ic_avatar_stub)
                        .apply(RequestOptions.circleCropTransform())
                        .into(itemView.avatarView)
            }
        }
    }
}