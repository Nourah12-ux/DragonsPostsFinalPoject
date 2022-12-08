package com.example.dragonsposts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dragonsposts.databinding.ActivityShowPostsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowPosts : AppCompatActivity() {

    lateinit var binding: ActivityShowPostsBinding
    lateinit var comments: List<String>
    var username: String? = null
    lateinit var post: PostsItem
    private var  postId = 0
    lateinit var  rvAdapter: CommentAdapter


    private val apiInterface by lazy { APIClient().getClient()?.create(APIInterface::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getIntExtra("idPost", 0)
        username = intent.getStringExtra("name")

        comments = listOf()

        getPost(postId)
        adapter(comments)


        //button like
        binding.apply {
            var nightMode = intent.getBooleanExtra("nightMode",false)
            if (!nightMode){
                clShow.setBackgroundColor(resources.getColor(R.color.background))
            } else{
                clShow.setBackgroundResource(R.drawable.bg)
            }

            addLikeIv.setOnClickListener {

                if (username != null && username != "") {
                    if (post.likes.contains(username.toString().trim())) {
                        Toast.makeText(this@ShowPosts, "You have already liked this post", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        var like = post.likes + "," + "$username"
                        updatePost(PostsItem(post.comments, post.id, like, post.text, post.title, username!!), true)
                        likeNumberTv.text = sizeString(like).toString()
                    }
                } else {
                    Toast.makeText(this@ShowPosts, "You must be logged in to like posts", Toast.LENGTH_LONG).show()
                }
                rvAdapter?.notifyDataSetChanged()
            }


            addCommentBtn.setOnClickListener {

                if (etViewPostComment.text.isNotEmpty()) {
                    if (username != null && username !="" ) {
                        var comment = post.comments + "," + "${etViewPostComment.text}"
                        updatePost(PostsItem(comment, post.id, username!!, post.title, post.likes, post.text), false)
                        commentsTv.text = sizeString(comment).toString()
                        rvAdapter?.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@ShowPosts, "You must be logged in to like posts", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@ShowPosts, "The comment field must not be empty", Toast.LENGTH_LONG).show()
                    rvAdapter?.notifyDataSetChanged()
                }

            }

        }
    }

    //function to get the post
    private fun getPost(postId: Int) {
        apiInterface?.getPost(postId)!!.enqueue(object : Callback<PostsItem> {

            override fun onResponse(call: Call<PostsItem>, response: Response<PostsItem>) {

                binding.apply {
                    post = response.body()!!
                    postTitle.text = post.title
                    tvContent.text = post.text
                    postUser.text = post.user
                    commentsTv.text = sizeString(post.comments).toString()
                    likeNumberTv.text = sizeString(post.likes).toString()
                    comments = post.comments.split(",")
                    rvAdapter.update(comments)



                }
            }
            override fun onFailure(call: Call<PostsItem>, t: Throwable)
            {
                Log.d("MAIN", "unable to get the data")
            }
        })
    }


    //send the data of post to the server
    private fun updatePost(post: PostsItem, state: Boolean) {

        if (apiInterface != null) {
            apiInterface?.updatePost(post.id, post)!!.enqueue(object : Callback<PostsItem> {

                override fun onResponse(call: Call<PostsItem>, response: Response<PostsItem>) {
                    Log.d("Update Post", "onResponse: Successfully")
                }
                override fun onFailure(call: Call<PostsItem>, t: Throwable) {
                    Toast.makeText(this@ShowPosts, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            })

            if (state)
            {
                Toast.makeText(this, "You liked this post", Toast.LENGTH_LONG).show()
            }
            else
            {
                binding.etViewPostComment.text.clear()
                Toast.makeText(this, "Comment added", Toast.LENGTH_LONG).show()
            }
            getPost(post.id)
        }
    }


    fun sizeString(string: String): Int {
        if (string.isNotEmpty()) {

            return string.trim().split(",").filterNot { it == "" }.size
        }
        return 0
    }

    private fun adapter(comments : List<String>) {
        rvAdapter = CommentAdapter(comments)
        binding.rvComments.adapter = rvAdapter
        binding.rvComments.layoutManager = LinearLayoutManager(this@ShowPosts)
    }


}