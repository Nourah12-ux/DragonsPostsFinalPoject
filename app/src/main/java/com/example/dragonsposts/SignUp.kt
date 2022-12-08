package com.example.dragonsposts

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dragonsposts.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var username: String
    private lateinit var userEmail : String
    private var userPassword = ""
    private var confirmPassword = ""
    private  var userAbout = ""
    lateinit var time :String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        time = LocalDateTime.now().toString()

        binding.apply {

            signUpBtn.setOnClickListener(){

                username = userNameEt.text.toString()
                userEmail = emailEt.text.toString()
                userPassword = passwordEt.text.toString()
                confirmPassword = confirmPasswordEditText.text.toString()
                userAbout = userAboutEt.text.toString()

                if (username.isNotEmpty()&&userPassword.isNotEmpty()&&confirmPassword.isNotEmpty()&&userEmail.isNotEmpty()){

                if (username.length < 65 && userEmail.length < 65 && userPassword.length < 65){

                      if(userPassword == confirmPassword) {
                        if (userPassword.length<8)
                              Toast.makeText(this@SignUp, "The password must more than 7", Toast.LENGTH_LONG).show()
                         else if (userEmail.contains("@"))

                             checkUsernameAndEmail(username,userEmail)

                          else { Toast.makeText(this@SignUp, "Please enter correct email", Toast.LENGTH_LONG).show() }
                      }
                      else
                      { Toast.makeText(this@SignUp, "Please enter the same password", Toast.LENGTH_LONG).show() }
                }
                else
                { Toast.makeText(this@SignUp, "Please check length of all filed", Toast.LENGTH_LONG).show() }
            }
                else { Toast.makeText(this@SignUp, "Please fill all filed", Toast.LENGTH_LONG).show() }

            }
        }
    }


    private fun signUp() {

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

            apiInterface?.addUser(UsersItem(userAbout,time,userEmail,0,"",username,"",userPassword,""))?.enqueue(object : Callback<UsersItem> {

                override fun onResponse(call: Call<UsersItem>, response: Response<UsersItem>) {

                    recreate()
                    Toast.makeText(this@SignUp, "You create account correctly, you can sign in now", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUp, SignIn::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<UsersItem>, t: Throwable) {
                    Toast.makeText(this@SignUp, "Something wrong please try aging", Toast.LENGTH_SHORT).show()

                }
            })
    }
    private fun checkUsernameAndEmail(username :String, email: String){

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.getALLUsers()?.enqueue(object :Callback<Users>{

            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                var users = response.body()!!
                var usernames : ArrayList<String> = arrayListOf()
                var emails : ArrayList<String> = arrayListOf()
                for (user in users) {
                  usernames.add(user.username)
                    emails.add(user.email)
                }
                if (usernames.contains(username)||emails.contains(email)) {
                    Toast.makeText(this@SignUp, "The username or email is used before, try again", Toast.LENGTH_SHORT).show()
                } else{
                    signUp()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(this@SignUp, "Something wrong please try other", Toast.LENGTH_SHORT).show()
            }
        })
    }

 }


