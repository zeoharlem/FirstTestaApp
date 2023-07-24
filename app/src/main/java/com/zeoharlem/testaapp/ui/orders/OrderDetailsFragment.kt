package com.zeoharlem.testaapp.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.zeoharlem.testaapp.MainActivity
import com.zeoharlem.testaapp.R
import com.zeoharlem.testaapp.databinding.FragmentOrderDetailsBinding
import com.zeoharlem.testaapp.ui.test.TestViewModel

class OrderDetailsFragment : Fragment() {
    private var _binding: FragmentOrderDetailsBinding? = null
    val binding get() = _binding!!

    private var menuItemCount = 0

    private val viewModel by activityViewModels<TestViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as MainActivity).setSupportActionBarTitle("Order Test")
        _binding = FragmentOrderDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        prepareMenuAction()
        viewModel.getCartMenuItems().observe(viewLifecycleOwner) {
            menuItemCount = it.size
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(requireContext(), "OnResume Called", Toast.LENGTH_SHORT).show()
    }

    private fun prepareMenuAction() {
        val menuHost: MenuHost = requireActivity()
        viewModel.getCartMenuItems().observe(viewLifecycleOwner) {

            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.order_checkout_menu, menu)
                    val badgeLayout = menu.findItem(R.id.order_checkout_item).actionView
                    badgeLayout?.let {
                        it.setOnClickListener {
                            findNavController().navigate(R.id.testListSelectedFragment)
                        }
                        it.findViewById<TextView>(R.id.actionbar_notification_tv)?.let { txtView ->
                            txtView.isVisible = menuItemCount > 0
                            txtView.text = menuItemCount.toString()
                        }
                    }
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.search_bar -> {
                            // performTaskAction()
                            true
                        }

                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}