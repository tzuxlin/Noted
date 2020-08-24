package com.connie.noted.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.connie.noted.MainActivity
import com.connie.noted.R
import com.connie.noted.util.Logger
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private var callbackManager: CallbackManager? = null

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Logger.d("LoginActivity, onNewIntent ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.d("LoginActivity, onCreate ")


        setContentView(R.layout.activity_login)


        auth = FirebaseAuth.getInstance()

        facebook_login_button.setOnClickListener {
            //First step
            facebookLogin()
        }

        //printHashKey()
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onStart() {
        super.onStart()

        auth?.currentUser?.let {
            UserManager.userEmail = it.email
            moveMainPage(it)
            Logger.d("UserManager.userEmail = ${UserManager.userEmail}")

        }

    }

    private fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    //Second step
                    handleFacebookAccessToken(result?.accessToken)

                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                    Logger.d("Facebook Exception: $error")

                }

            })
    }

    fun handleFacebookAccessToken(token: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    //Third step
                    //Login

                    task.result?.let { result ->
                        moveMainPage(result.user)

                        val userId = result.additionalUserInfo?.profile?.getValue("id")


                        UserManager.userEmail = result.user?.email ?: ""
                        UserManager.user.value?.name = result.user?.displayName ?: ""
                        UserManager.user.value?.id = result.user!!.uid
                        UserManager.user.value?.email = result.user?.email ?: ""
                        UserManager.user.value?.image =
                            "https://graph.facebook.com/$userId/picture?height=500"

                        UserManager.justLogin = true


                    }


                } else {
                    //Show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
