package com.example.gohealthy.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gohealthy.UserData.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseVM : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private lateinit var DB: FirebaseFirestore
    private var _user: MutableLiveData<User> = MutableLiveData(User())
    val user: LiveData<User> get() = _user
    var status = false

    init {
        auth = FirebaseAuth.getInstance()
        DB = FirebaseFirestore.getInstance()
    }

    suspend fun signIn(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            status = true
            getUser(email)
        } catch (e: Exception) {
            status = false
        }
    }
   suspend fun updateUserData(newUser: User,context: Context) {
        _user.value = newUser
       try {
           DB.collection("users").document(auth.currentUser!!.uid).set(newUser).await()
       }
       catch (e:Exception){
           Toast.makeText(context,"Something went wrong ${e.message}",Toast.LENGTH_SHORT).show()
       }
    }
    private suspend fun getUser(email: String) {
        try {
            val querySnapshot = DB.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val document = querySnapshot.documents[0]
                _user.value = User(
                    document.getString("name") ?: "",
                    document.getString("gender") ?: "",
                    document.getDouble("weight")?.toFloat() ?: 0f,
                    document.getDouble("height")?.toFloat() ?: 0f,
                    document.getString("email") ?: "",
                    document.getString("password") ?: "",
                    document.getDouble("age")?.toFloat() ?: 0f
                )
            }
        } catch (e: Exception) {
            // Handle errors, log them if necessary
        }
    }

    suspend fun signUp(newUser: User) {
        try {
            auth.createUserWithEmailAndPassword(newUser.email, newUser.password).await()
            status = true
            _user.value = newUser
            DB.collection("users").document(auth.currentUser!!.uid).set(user).await()
        } catch (e: Exception) {
            status = false
        }
    }
}
