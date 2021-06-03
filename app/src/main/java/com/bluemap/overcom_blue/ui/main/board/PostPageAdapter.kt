package com.bluemap.overcom_blue.ui.main.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bluemap.overcom_blue.R
import com.bluemap.overcom_blue.databinding.ItemNoticeBinding
import com.bluemap.overcom_blue.databinding.ItemPostBinding
import com.bluemap.overcom_blue.model.Post
import com.bluemap.overcom_blue.user.UserManager
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.android.synthetic.main.item_post.view.*
import javax.inject.Inject

class PostPageAdapter @Inject constructor(
    @ActivityContext private val context: Context,
    private val navController: NavController
) : PagedListAdapter<Post, PostPageAdapter.ViewHolder>(diffUtil) {
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


    override fun getItemViewType(position: Int): Int {
        return getItem(position).hashCode()
    }

    inner class ViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init{
            binding.root.setOnClickListener(this)
        }

        fun bind(model: Post) {
            binding.model = model
            if(model.like!! == 1){
                binding.root.like_image_view.setColorFilter(ContextCompat.getColor(context, R.color.deepBlue), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                binding.root.like_image_view.setColorFilter(ContextCompat.getColor(context, R.color.deepGray), android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }

        override fun onClick(v: View?) {
            val post = getItem(adapterPosition)!!
            val directions = BoardFragmentDirections.actionCommunityFragmentToPostFragment(post.id!!)
            navController.navigate(directions)
            UserManager.accessPostId = post.id!!
        }
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Post>(){
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id==newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem==newItem
        }
    }
}