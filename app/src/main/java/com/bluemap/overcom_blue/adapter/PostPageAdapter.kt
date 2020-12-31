package com.bluemap.overcom_blue.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.ItemPostBinding
import com.bluemap.overcom_blue.model.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostPageAdapter(val context: Context,
                      val postItemClick: (Post) -> Unit)
    : PagedListAdapter<Post,PostPageAdapter.ViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(
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

    inner class ViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: Post) {
            binding.model = model
            binding.root.setOnClickListener{
                postItemClick(model)
            }
            if(model.like!! == 1){
                binding.root.like_image_view.setColorFilter(ContextCompat.getColor(context, R.color.deepBlue), android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }
    }
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Post>(){
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id==newItem.id


            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem==newItem
        }
    }
}