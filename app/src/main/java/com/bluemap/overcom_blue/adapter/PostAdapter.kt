package com.bluemap.overcom_blue.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.ItemPostBinding
import com.bluemap.overcom_blue.model.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(val context: Context,
                  var list: List<Post>,
                  val postItemClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    constructor(context: Context,postItemClick: (Post) -> Unit):this(context,ArrayList(),postItemClick)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag=position
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
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

    fun setList(list:ArrayList<Post>){
        this.list=list
        notifyDataSetChanged()
    }
}