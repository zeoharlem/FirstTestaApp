package com.zeoharlem.testaapp.ui.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.GsonBuilder
import com.zeoharlem.testaapp.MainActivity
import com.zeoharlem.testaapp.R
import com.zeoharlem.testaapp.databinding.FragmentOrderDetailsBinding
import com.zeoharlem.testaapp.databinding.FragmentPaymentIntegrationBinding
import com.zeoharlem.testaapp.ui.test.TestViewModel


class PaymentIntegrationFragment : Fragment() {

    private val viewModel by activityViewModels<TestViewModel>()

    private var _binding: FragmentPaymentIntegrationBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as MainActivity).setSupportActionBarTitle("Payment")
        _binding = FragmentPaymentIntegrationBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun observeMenu() {
        viewModel.getCartMenuItems().observe(viewLifecycleOwner) {
            binding.paymentIntegration.text = GsonBuilder().setPrettyPrinting().create().toJson(it)
        }
    }

}