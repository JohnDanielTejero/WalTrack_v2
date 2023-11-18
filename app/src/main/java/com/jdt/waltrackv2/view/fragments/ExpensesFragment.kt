package com.jdt.waltrackv2.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.adapters.setupDropdownAdapter
import com.jdt.waltrackv2.databinding.AddItemLayoutBinding
import com.jdt.waltrackv2.databinding.BalanceShimmerPlaceholderBinding
import com.jdt.waltrackv2.databinding.ExpenseDataBinding
import com.jdt.waltrackv2.databinding.FilterLayoutBinding
import com.jdt.waltrackv2.databinding.FragmentExpensesBinding
import com.jdt.waltrackv2.utils.AddItemHandler
import com.jdt.waltrackv2.utils.FilterHandler
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.utils.RenderElementHandler
import com.jdt.waltrackv2.view.TransactionAddActivity
import java.util.Calendar


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpensesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpensesFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var dataLoadingListener: OnDataLoading? = null

    private lateinit var binding: FragmentExpensesBinding
    private lateinit var filterLayoutBinding: FilterLayoutBinding
    private var isFilterToggled: Boolean = false

    private lateinit var shimmerPlaceholderBinding : BalanceShimmerPlaceholderBinding
    private lateinit var expenseDisplayDataBinding: ExpenseDataBinding
    private lateinit var addItemButton: AddItemLayoutBinding

    private lateinit var filterHandler: FilterHandler
    private  lateinit var addItemHandler: AddItemHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDataLoading) {
            dataLoadingListener = context
        } else {
            throw RuntimeException("$context must implement DataLoadingListener")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpensesBinding.inflate(inflater, container, false)
        shimmerPlaceholderBinding = BalanceShimmerPlaceholderBinding.inflate(inflater, binding.expenseBalanceDisplay, true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataLoadingListener?.onDataLoadingStarted()
        RenderElementHandler.initPlaceholders {
            binding.expenseBalanceDisplay.addView(shimmerPlaceholderBinding.root)
        }
        //simulate data retrieval
        Handler(Looper.getMainLooper()).postDelayed({
            activity?.let {
                val inflater = LayoutInflater.from(requireContext())
                filterLayoutBinding = FilterLayoutBinding.inflate(inflater, binding.filterOption, true)
                expenseDisplayDataBinding = ExpenseDataBinding.inflate(inflater, binding.expenseBalanceDisplay, true)
                addItemButton = AddItemLayoutBinding.inflate(inflater, binding.addItemContainer, true)
                RenderElementHandler.removePlaceholders {
                    Log.d("RenderElement", "removing placeholder event")
                    //remove shimmer
                    binding.expenseBalanceDisplay.removeView(shimmerPlaceholderBinding.root)
                }
            }


            filterHandler = FilterHandler(filterLayoutBinding, requireContext())

            addItemHandler = AddItemHandler(addItemButton.root, Intent(requireContext(), TransactionAddActivity::class.java), requireActivity())

            //TODO: Add data
            expenseDisplayDataBinding.expenseTotalBalanceDisplay.text = "Php 300.00"
            dataLoadingListener?.onDataLoadingFinished()
        }, 3000)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExpensesFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpensesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}