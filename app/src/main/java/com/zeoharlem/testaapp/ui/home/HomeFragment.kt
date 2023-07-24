package com.zeoharlem.testaapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeoharlem.testaapp.MainActivity
import com.zeoharlem.testaapp.R
import com.zeoharlem.testaapp.adapter.CategoryAdapter
import com.zeoharlem.testaapp.adapter.FrequentlyBookedAdapter
import com.zeoharlem.testaapp.databinding.FragmentHomeBinding
import com.zeoharlem.testaapp.ui.test.TestViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categoryAdapter = CategoryAdapter()
    private val frequentlyBookedAdapter = FrequentlyBookedAdapter()
    private val viewModel by viewModels<HomeViewModel>()
    private val testViewModel by viewModels<TestViewModel>()

    private var menuItemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.categories()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        observeCategoryResult()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBarTitle("FirstTesta")
        with(binding) {
            categoriesRecyclerView.adapter = categoryAdapter
            frequetlyRecyclerView.adapter = frequentlyBookedAdapter
            val horizontalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val horizontalLayoutManager2 = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            categoriesRecyclerView.layoutManager = horizontalLayoutManager
            frequetlyRecyclerView.layoutManager = horizontalLayoutManager2
        }
        init()
    }

    private fun init() {
        prepareMenuAction()
        testViewModel.getCartMenuItems().observe(viewLifecycleOwner) {
            menuItemCount = it.size
        }
    }

    private fun observeCategoryResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryStateFlow.collectLatest {
                    categoryAdapter.submitList(it.addresses)
                    frequentlyBookedAdapter.submitList(it.addresses)
                }
            }
        }
    }

    private fun prepareMenuAction() {
        val menuHost: MenuHost = requireActivity()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}