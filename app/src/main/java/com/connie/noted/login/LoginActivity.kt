package com.connie.noted.login

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.connie.noted.MainActivity
import com.connie.noted.NotedApplication
import com.connie.noted.R
import com.connie.noted.data.User
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.util.*


class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null

    //    var googleSignInClient : GoogleSignInClient? = null
//    var GOOGLE_LOGIN_CODE = 9001
    private var callbackManager: CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        auth = FirebaseAuth.getInstance()

//        google_sign_in_button.setOnClickListener {
//            //First step
//            googleLogin()
//        }
        facebook_login_button.setOnClickListener {
            //First step
            facebookLogin()
        }

//        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken("973705262050-a5cbp8np9f2n73v660m2cijuiqvoeoc0.apps.googleusercontent.com")
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this,gso)
        //printHashKey()
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    //    fun googleLogin(){
//        var signInIntent = googleSignInClient?.signInIntent
//        startActivityForResult(signInIntent,GOOGLE_LOGIN_CODE)
//    }
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

                    Log.e("Connie", "Facebook Error: $error")

                    moveMainPageWithMockData()


                    UserManager.user.value?.name = "Xuan Lin"
                    UserManager.user.value?.id = "3686017864745810"
                    UserManager.user.value?.email = "annul7tin@gmail.com"
                    UserManager.user.value?.image = "https://graph.facebook.com/3686017864745810/picture?height=500"

                    UserManager.justLogin = true

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


                        UserManager.user.value?.name = result.user?.displayName ?: ""
                        UserManager.user.value?.id = result.user!!.uid
                        UserManager.user.value?.email = result.user?.email ?: ""
                        UserManager.user.value?.image = "https://graph.facebook.com/$userId/picture?height=500"

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
        //        if(requestCode == GOOGLE_LOGIN_CODE){
//            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            if(result.isSuccess){
//                var account = result.signInAccount
//                //Second step
//                firebaseAuthWithGoogle(account)
//            }
//        }
//    }
//    fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
//        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
//        auth?.signInWithCredential(credential)
//            ?.addOnCompleteListener {
//                    task ->
//                if(task.isSuccessful){
//                    //Login
//                    moveMainPage(task.result?.user)
//                }else{
//                    //Show the error message
//                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
//                }
//            }
//    }
//    fun signinAndSignup(){
//        auth?.createUserWithEmailAndPassword(email_edittext.text.toString(),password_edittext.text.toString())
//            ?.addOnCompleteListener {
//                    task ->
//                if(task.isSuccessful){
//                    //Creating a user account
//                    moveMainPage(task.result?.user)
//                }else if(task.exception?.message.isNullOrEmpty()){
//                    //Show the error message
//                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
//                }else{
//                    //Login if you have account
//                    signinEmail()
//                }
//            }
    }

//    fun signinEmail(){
//        auth?.signInWithEmailAndPassword(email_edittext.text.toString(),password_edittext.text.toString())
//            ?.addOnCompleteListener {
//                    task ->
//                if(task.isSuccessful){
//                    //Login
//                    moveMainPage(task.result?.user)
//                }else{
//                    //Show the error message
//                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
//                }
//            }
//    }
    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun moveMainPageWithMockData() {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
    }
}
