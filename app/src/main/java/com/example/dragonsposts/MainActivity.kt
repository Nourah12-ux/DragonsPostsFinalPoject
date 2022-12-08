package com.example.dragonsposts


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dragonsposts.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private var userName : String? = null
    lateinit var posts: Posts
    lateinit var rvMain: PostAdapter
    lateinit var sharedPref:SharedPreferences
    var nightMode =false
    private var apiKey :String? = null
    private lateinit var binding: ActivityMainBinding
    private val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("name")
        apiKey = intent.getStringExtra("apiKey")


        binding.apply {

            if (userName != null) {
                userNameHello.text = "Hello $userName"
                signInBtn.visibility = View.GONE


                sharedPref = super.getSharedPreferences(
                    "com.example.dragonsposts.PREFERENCE_FILE_KEY",
                    Context.MODE_PRIVATE
                )
                sharedPref.edit().apply {
                    putString("username", userName)
                    putBoolean("nightMode", nightMode)
                    apply()
                }
            }
                else{
                signInBtn.visibility = View.VISIBLE
            }

            if (nightMode){
                clMain.setBackgroundColor(resources.getColor(R.color.background))
            } else{
                clMain.setBackgroundResource(R.drawable.bg)
            }


            signInBtn.setOnClickListener() {

                var intent = Intent(this@MainActivity, SignIn::class.java)
                intent.putExtra("nightMode",nightMode)
                startActivity(intent)
            }

            nightMoodBtn.setOnClickListener() {
                 if (!nightMode){
                    clMain.setBackgroundColor(resources.getColor(R.color.background))
                     nightMode =true

                } else{
                    clMain.setBackgroundResource(R.drawable.bg)
                     nightMode =false
                }

            }

            addPostBtn.setOnClickListener() {
                if (userName != null){
                var intent = Intent(this@MainActivity, AddPost::class.java)
                    intent.putExtra("nightMode",nightMode)
                startActivity(intent)
                }else
                    Toast.makeText(this@MainActivity, "SignIn first to add post", Toast.LENGTH_LONG).show()
            }


        }

        getPosts()
        adapter()

    }

    private fun getPosts() {
        apiInterface?.getAllPosts()!!.enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                posts = response.body()!!
                rvMain.submitList(posts)
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
                Log.d("MAIN", "unable to get the data")
            }
        })

    }


    private fun adapter() {
        rvMain = PostAdapter(this)
        binding.rvMain.adapter = rvMain
        binding.rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
    }



    fun userNameFun(): String {
        return if (userName != null && userName != "")
            userName.toString()
        else
            ""
    }


}
