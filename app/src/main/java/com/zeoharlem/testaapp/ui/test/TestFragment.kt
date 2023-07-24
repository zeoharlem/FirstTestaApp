package com.zeoharlem.testaapp.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.zeoharlem.testaapp.MainActivity
import com.zeoharlem.testaapp.R
import com.zeoharlem.testaapp.adapter.TestSearchAdapter
import com.zeoharlem.testaapp.data.CartRepository
import com.zeoharlem.testaapp.databinding.FragmentTestBinding
import com.zeoharlem.testaapp.models.CartMenu
import com.zeoharlem.testaapp.models.Tests
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [TestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestFragment : Fragment(), SearchView.OnQueryTextListener {

    private val testSearchAdapter = TestSearchAdapter()
    private val viewModel by activityViewModels<TestViewModel>()
    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    private var cartMenuList = MutableLiveData<CartMenu<Tests>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.testsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBarTitle("Select Test")
        prepareMenuAction()
        setRecyclerViewAction()
        observeTestResult()
        callbackImplAction()
    }

    private fun callbackImplAction() {
        testSearchAdapter.addItemCheckEvent = { test ->
            viewModel.addItem(test.dataType)
        }
        testSearchAdapter.removeItemCheckEvent = {
            viewModel.removeItem(it)
        }
    }

    private fun prepareMenuAction() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.search_test_menu, menu)
                val badgeLayout = menu.findItem(R.id.view_menu_list).actionView
                badgeLayout?.let {
                    it.setOnClickListener {
                        findNavController().navigate(R.id.testListSelectedFragment)
                    }

                    viewModel.getCartMenuItems().observe(viewLifecycleOwner) { liveCartMenu ->
                        it.findViewById<TextView>(R.id.actionbar_notification_tv)?.let { txtView ->
                            txtView.isVisible = liveCartMenu.isNotEmpty()
                            txtView.text = liveCartMenu.size.toString()
                        }
                    }
                }
                (menu.findItem(R.id.search_bar).actionView as SearchView).let {
                    it.setOnQueryTextListener(this@TestFragment)
                    it.queryHint = "Search Test"
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.search_bar -> {
                        // performTaskAction()
                        true
                    }
                    R.id.view_menu_list, R.id.cart_with_badge -> {
                        findNavController().navigate(R.id.testListSelectedFragment)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setRecyclerViewAction() {
        with(binding) {
            testRecyclerView.adapter = testSearchAdapter
            testRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun observeTestResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.testStateFlow.collectLatest {
                    testSearchAdapter.submitList(it.testsList)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}