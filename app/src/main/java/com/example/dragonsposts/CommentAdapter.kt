package com.example.dragonsposts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dragonsposts.databinding.CommentRowBinding

class CommentAdapter (private var comments: List<String>): RecyclerView.Adapter<CommentAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding:CommentRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(CommentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var commentRaw = comments[position]

        holder.binding.apply {
            tvComment.text = commentRaw
        }
    }

    override fun getItemCount() = comments.size

    fun update(comments: List<String>){
        this.comments = comments
        notifyDataSetChanged()
    }
}