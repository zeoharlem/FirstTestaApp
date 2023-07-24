package com.zeoharlem.testaapp.ui.login

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.zeoharlem.testaapp.R
import com.zeoharlem.testaapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val REQ_ONE_TAP = 2
    private var showOneTapUI = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater)

        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(it.data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null && password != null -> {
                            credential.let { signInCredential: SignInCredential ->
                                val email = signInCredential.id
                                val displayName = signInCredential.displayName
                                val profileImage = signInCredential.profilePictureUri
                            }
                            //todo: perform backend operations and local cache
                            findNavController().navigate(R.id.navigation_home)
                        }

                        else -> {

                        }
                    }
                } catch (e: ApiException) {
                    Log.e("GoogleSignInError", e.localizedMessage)
                }
            }

        }

        return binding.root
    }

    private fun googleSignInEvent() {
        with(binding) {
            googleButton.setOnClickListener {
                oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(requireActivity()) { result ->
                    try {
                        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        activityResultLauncher.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("TAG", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                    .addOnFailureListener(requireActivity()) { e ->
                        Log.d("TAG", e.localizedMessage)
                    }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            loginButton.setOnClickListener {
                findNavController().navigate(R.id.navigation_home)
            }

            signupLinkLabel.setOnClickListener {
                findNavController().navigate(R.id.registerFragment)
            }
        }

        googleSignInEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}