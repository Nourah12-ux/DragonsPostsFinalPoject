package com.example.dragonsposts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dragonsposts.databinding.ActivitySignInBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var username : String
    private lateinit var userPassword : String
    private lateinit var users: ArrayList<UsersItem>
    private lateinit var apiKey : String
    private val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            users = arrayListOf()


            signInBtn.setOnClickListener() {
                username = userNameEt.text.toString()
                userPassword = passwordEt.text.toString()
                if (username.isNotEmpty()&&userPassword.isNotEmpty())
                checkUserExist()
                else
                    Toast.makeText(this@SignIn, "Please enter email and password", Toast.LENGTH_SHORT).show()

            }

            createAccount.setOnClickListener(){
                val intent = Intent(this@SignIn, SignUp::class.java)
                startActivity(intent)
            }

        }
    }
    private fun checkUserExist () {

        apiInterface?.getALLUsers()?.enqueue(object : Callback<Users> {

            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                var usersList = response.body()!!
                var state = true
                for (user in usersList) {
                    users.add(user)
                }
                for (user in usersList) {
                    if (user.username == username) {
                        signIn(this@SignIn)
                        state = true
                        break
                    } else
                        state = false
                }
                if (!state)
                    Toast.makeText(this@SignIn, "Something wrong please enter correct Username and Password", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(this@SignIn, "Something wrong ", Toast.LENGTH_LONG).show()
            }


        })
    }
        fun signIn(activity : Activity){

            apiInterface?.logIn(username , userPassword)?.enqueue(object  : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    var body = response.body()
                    if (body != null)
                    {
                        apiKey = body.toString()
                        Toast.makeText(this@SignIn, "Welcome $username ", Toast.LENGTH_LONG).show()
                        val signInIntent = Intent(activity, MainActivity::class.java)
                        signInIntent.putExtra("api",apiKey)
                        signInIntent.putExtra("name",username)
                        startActivity(signInIntent)
                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.message}")
                    Toast.makeText(this@SignIn, "Something wrong ", Toast.LENGTH_LONG).show()
                }


            })
        }

    }
