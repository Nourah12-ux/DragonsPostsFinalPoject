package com.example.dragonsposts

import androidx.recyclerview.widget.DiffUtil

class PostDiffutil:DiffUtil.ItemCallback<PostsItem>() {
    override fun areItemsTheSame(oldItem: PostsItem, newItem: PostsItem): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: PostsItem, newItem: PostsItem): Boolean {
        return when{

            oldItem.id==newItem.id ->true
            oldItem.comments==newItem.comments ->true
            oldItem.likes==newItem.likes -> true
            oldItem.text==newItem.text -> true
            oldItem.title==newItem.title -> true
            oldItem.user==newItem.user->true

            else -> {false}
        }
    }

}
