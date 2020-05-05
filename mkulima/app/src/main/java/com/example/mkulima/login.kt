package com.example.mkulima

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.example.mkulima.databinding.LoginBinding

class login : Fragment() {
    private lateinit var binding: LoginBinding
    private val RC_SIGN_IN = 123


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login,
            container,
            false
        )

        binding.loginbtn.setOnClickListener {
            createSignInIntent()
        }

        return binding.root
    }

    private fun createSignInIntent() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder()
                .setDefaultNumber("+25456789")
                .build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                view?.findNavController()?.navigate(R.id.action_login_to_home)
                Log.i("chei","succesful")
            } else {

                Log.i("chei","not successful ${response?.error?.errorCode}")
            }
        }
    }

    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_signout]
    }

    private fun delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
            .delete(requireContext())
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_delete]
    }

    override fun onDestroy() {
        super.onDestroy()
        signOut()
        Log.i("chei","signed out")
    }
}

