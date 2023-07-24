package com.zeoharlem.testaapp.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeoharlem.testaapp.MainActivity
import com.zeoharlem.testaapp.adapter.TestCartListAdapter
import com.zeoharlem.testaapp.databinding.FragmentTestListSelectedBinding
import com.zeoharlem.testaapp.ui.test.TestViewModel


class TestListSelectedFragment : Fragment() {
    private var _binding: FragmentTestListSelectedBinding? = null
    val binding get() = _binding!!

    private val viewModel by activityViewModels<TestViewModel>()
    private val testCartListAdapter = TestCartListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity() as MainActivity).setSupportActionBarTitle("Test Selected")
        _binding = FragmentTestListSelectedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setRecyclerView()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.getCartMenuItems().observe(viewLifecycleOwner) {
            testCartListAdapter.submitList(it)
        }
    }

    private fun setRecyclerView() {
        with(binding) {
            testListRecyclerView.adapter = testCartListAdapter.apply {
                val orientation = LinearLayoutManager(requireContext())
                testListRecyclerView.layoutManager = orientation
                val dividerItemDecoration = DividerItemDecoration(requireContext(), orientation.orientation)
                testListRecyclerView.addItemDecoration(dividerItemDecoration)
                this.removeItemCheckEvent = {
                    viewModel.removeItem(it)
                }
            }
        }
    }
}