package com.zeoharlem.testaapp.ui.login

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.zeoharlem.testaapp.R
import com.zeoharlem.testaapp.databinding.FragmentLoginBinding
import com.zeoharlem.testaapp.extensions.prettyGson
import com.zeoharlem.testaapp.models.LoginRequest
import com.zeoharlem.testaapp.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val viewModel by activityViewModels<AuthViewModel>()

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

        checkUserLoggedIn()

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
                viewModel.loginUser(
                    LoginRequest(
                        email = emailField.text.toString(),
                        password = passwordField.text.toString()
                    )
                )
            }

            signupLinkLabel.setOnClickListener {
                findNavController().navigate(R.id.registerFragment)
            }
        }
        observeStateFlow()
        //googleSignInEvent()
    }

    private fun observeStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.loginStateFlow.collectLatest {
                    it.data?.let { response ->
                        println("collectLatest: ${prettyGson(response)}")
                        findNavController().navigate(R.id.navigation_home)
                    }
                }
            }
        }

    }

    private fun checkUserLoggedIn() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (viewModel.cacheData.userIsLoggedIn()) {
                findNavController().navigate(R.id.navigation_home)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}