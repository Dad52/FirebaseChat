package com.ands.firebasechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ands.firebasechat.activities.ChatsActivity
import com.ands.firebasechat.data.models.Users
import com.ands.firebasechat.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater).also { setContentView(it.root) }

        auth = Firebase.auth

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "error api", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignIn.setOnClickListener() {
            signInWithGoogle()
        }
        checkAuthState()
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    private fun signInWithGoogle() {
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener() {
            if(it.isSuccessful) {
                checkAuthState()
            }
            else Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener() {
            Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAuthState() {
        if (auth.currentUser != null) {
            createNewUserInDatabase()
            val i = Intent(this, ChatsActivity::class.java)
            startActivity(i)
        }
    }

    private fun createNewUserInDatabase() {
        val database = Firebase.database
        val usersReference = database.getReference("users")
        usersReference.child(auth.currentUser?.uid ?: "error").setValue(Users(
                uid = auth.currentUser?.uid,
                name = auth.currentUser?.displayName,
                photoUrl = auth.currentUser?.photoUrl.toString(),
                email = auth.currentUser?.email,
                phoneNumber = auth.currentUser?.phoneNumber
        ))
    }

}