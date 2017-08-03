package com.example.admin.testvk.adapter

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.example.admin.testvk.R
import com.example.admin.testvk.common.GlideApp
import com.vk.sdk.api.model.VKApiUserFull
import kotlinx.android.synthetic.main.card_friend.view.*

class FriendsListAdapter(val inflater: LayoutInflater, val listener: OnHolderClickClistener) : RecyclerView.Adapter<FriendsListAdapter.ViewHolder>() {
    private val comparator: Comparator<VKApiUserFull> = Comparator { a, b ->
        val first = a.first_name + a.last_name
        val second = b.first_name + b.last_name
        first.compareTo(second)
    }

    private var dataSet: SortedList<VKApiUserFull> = SortedList<VKApiUserFull>(VKApiUserFull::class.java, object : SortedList.Callback<VKApiUserFull>() {
        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun compare(o1: VKApiUserFull?, o2: VKApiUserFull?): Int {
            return comparator.compare(o1, o2)
        }

        override fun areContentsTheSame(oldItem: VKApiUserFull?, newItem: VKApiUserFull?): Boolean {
            return oldItem?.equals(newItem) ?: false
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun areItemsTheSame(item1: VKApiUserFull?, item2: VKApiUserFull?): Boolean {
            return item1?.id == item2?.id
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
        }

    })

    fun removeAll() {
        dataSet.beginBatchedUpdates()
        dataSet.clear()
        dataSet.endBatchedUpdates()
    }

    fun replaceAll(models: List<VKApiUserFull?>) {
        dataSet.beginBatchedUpdates()


        (dataSet.size() - 1 downTo 0)
                .asSequence()
                .map { dataSet.get(it) }
                .filterNot { models.contains(it) }
                .forEach {
                    dataSet.remove(it)
                }
        dataSet.addAll(models)
        dataSet.endBatchedUpdates()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.card_friend, parent, false)
        val nouniversityTitle = parent?.context?.getString(R.string.nouniversity_title) ?: ""
        val nocityTitle = parent?.context?.getString(R.string.nocity_title) ?: ""
        return ViewHolder(view, nouniversityTitle, nocityTitle)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(dataSet.get(position))
        holder?.itemView?.setOnClickListener {
            listener.onClick(holder.mItem)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size()
    }

    class ViewHolder(view: View, val nouniversityTitle: String, val nocityTitle: String) : RecyclerView.ViewHolder(view) {

        var mItem: VKApiUserFull? = null

        fun bind(info: VKApiUserFull?) {
            mItem = info
            itemView.firstLastName.text = "${info?.first_name } ${info?.last_name}"
            itemView.city.text = info?.city?.title ?: nocityTitle
            if (info?.universities != null && info.universities.size > 0) {
                val builder = StringBuilder()

                for (i in 1..info.universities.size ) {
                    val name = info.universities[i-1].name
                    if (i == info.universities.size) {
                        builder.append("$i. ${name.replace("\n", "")}")
                    } else {
                        builder.append("$i. ${name.replace("\n", "")} \n")
                    }
                }
                /*info.universities?.forEach {
                    builder.append("$counter. ${it.name} \n")
                    counter++
                }*/
                val universities = builder.toString()
                itemView.universitet.text = universities //.substring(0, universities.lastIndex - 1)
            } else {
                itemView.universitet.text = nouniversityTitle
            }
            val photo = info?.photo_200
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

    interface OnHolderClickClistener {
        fun onClick(item: VKApiUserFull?)

    }

}