package com.example.dragonsposts

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dragonsposts.databinding.ActivityAddPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPost : AppCompatActivity() {

    lateinit var binding: ActivityAddPostBinding
    private var username : String? = null
    private lateinit var title : String
    private lateinit var content : String
    private lateinit var post : PostsItem
    private lateinit var usernamePost : String

        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding = ActivityAddPostBinding.inflate(layoutInflater)
            setContentView(binding.root)


            val sharedPref=this.getSharedPreferences("com.example.dragonsposts.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
            username=sharedPref.getString("username",null)

                binding.apply {

                    var nightMode = intent.getBooleanExtra("nightMode",false)
                    if (!nightMode){
                        clAddPost.setBackgroundColor(resources.getColor(R.color.background))
                    } else{
                        clAddPost.setBackgroundResource(R.drawable.bg)
                    }


                if (username != null && username != ""){
                        nameEt.setText("$username")
                }

               nameEt.setText(username)
                //when button click check if user enter data
                submitBtn.setOnClickListener {
                    title = titleEt.text.toString()
                    content = addContentEt.text.toString()
                    usernamePost = if (nameEt.text.isEmpty()){
                        "Anonymous"
                    }else nameEt.text.toString()


                    if (title.isNotEmpty() && content.isNotEmpty()) {
                        post = PostsItem("", 0, "", content, title, usernamePost)
                        addPost ()
                    }
                    else
                        Toast.makeText(this@AddPost, "Please fill title and content", Toast.LENGTH_LONG).show()

                }
            }
        }
    private fun addPost(){

        var apiClint = APIClient().getClient()
        if (apiClint != null) {

            //connect to the Api when response
            var apiInterface = apiClint.create(APIInterface::class.java)
            apiInterface.addPost(post).enqueue(object: Callback<PostsItem> {

                override fun onResponse(call: Call<PostsItem>, response: Response<PostsItem>) {
                    var mainIntent = Intent(this@AddPost,MainActivity::class.java)
                    mainIntent.putExtra("name", username)
                    startActivity(mainIntent)
                }
                //if there is an issue when Connect to Api Show a message
                override fun onFailure(call: Call<PostsItem>, t: Throwable) {
                    Log.d("TAG","onFailure:${t.message}")
                }


            })
        }
    }

}