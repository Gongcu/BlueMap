package com.bluemap.overcom_blue.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.ItemCenterBinding
import com.bluemap.overcom_blue.databinding.ItemNoticeBinding
import com.bluemap.overcom_blue.databinding.ItemPostBinding
import com.bluemap.overcom_blue.model.Center
import com.bluemap.overcom_blue.model.Post
import kotlinx.android.synthetic.main.item_post.view.*

class CenterPageAdapter(val context: Context,
                        val centerItemClick: (Center) -> Unit)
    : PagedListAdapter<Center,CenterPageAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCenterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if(item!=null) {
            holder.bind(item)
            holder.itemView.tag=position
        }
    }


    override fun getItemViewType(position: Int): Int {
        return getItem(position).hashCode()
    }

    inner class ViewHolder(val binding: ItemCenterBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init{
            binding.root.setOnClickListener(this)
        }

        fun bind(model: Center) {
            binding.model = model
        }

        override fun onClick(v: View?) {
            centerItemClick(getItem(adapterPosition)!!)
        }
    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Center>(){
            override fun areItemsTheSame(oldItem: Center, newItem: Center): Boolean = oldItem.id==newItem.id

            override fun areContentsTheSame(oldItem: Center, newItem: Center): Boolean = oldItem==newItem
        }
    }
}