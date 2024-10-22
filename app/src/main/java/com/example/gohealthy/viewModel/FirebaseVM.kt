package com.example.gohealthy.viewModel

import android.content.Context

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

    // LiveData to expose errors
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    suspend fun signIn(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            status = true
            getUser(email)
        } catch (e: Exception) {
            status = false
            _errorMessage.value = e.message
        }
    }

    suspend fun updateUserData(newUser: User, context: Context) {
        _user.value = newUser
        var updatedUser= mapOf(
            "name" to newUser.name,
            "gender" to newUser.gender,
            "weight" to newUser.weight,
            "height" to newUser.height,
            "email" to newUser.email,
            "password" to newUser.password,
            "age" to newUser.age
        )
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                DB.collection("users").document(currentUser.uid).set(updatedUser).await()
            } else {
                _errorMessage.value = "User is not logged in"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Something went wrong: ${e.message}"
        }
    }

     suspend fun getUser(email: String) {
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
            } else {
                _errorMessage.value = "User not found"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Failed to retrieve user: ${e.message}"
        }
    }

    suspend fun signUp(newUser: User) {
        try {
            auth.createUserWithEmailAndPassword(newUser.email, newUser.password).await()

            val userId = auth.currentUser?.uid ?: throw Exception("User ID is null")

            status = true
            _user.value = newUser

            DB.collection("users").document(userId).set(newUser).await()
            getUser(newUser.email)


        } catch (e: Exception) {
            status = false
            _errorMessage.value = "Sign-up error: ${e.message}"
        }
    }
}
