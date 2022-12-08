package com.example.dragonsposts

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonsposts.databinding.OverviewCardBinding

class PostAdapter  (var activity: MainActivity ): ListAdapter<PostsItem, PostAdapter.ItemViewHolder>(PostDiffutil()){
    class ItemViewHolder (var binding: OverviewCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(OverviewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var post = getItem(position)

        holder.binding.apply {
            tvPostTitle.text = post.title

            var comments = post.comments.trim().split(",").filterNot{ it == ""}.size
            tvComments.text= "Comment : $comments"

            var likes = post.likes.trim().split(",").filterNot{ it == ""}.size
            tvLikes.text = "Like : $likes"

            tvOpenThread.setOnClickListener {
                var showPostIntent = Intent(activity, ShowPosts::class.java)
                var name = activity.userNameFun()
                showPostIntent.putExtra("name",name)
                showPostIntent.putExtra("idPost", post.id)
                activity.startActivity(showPostIntent)
            }
        }

    }


}