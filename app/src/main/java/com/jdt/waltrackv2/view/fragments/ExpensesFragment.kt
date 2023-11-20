package com.jdt.waltrackv2.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jdt.waltrackv2.databinding.AddItemLayoutBinding
import com.jdt.waltrackv2.databinding.BalanceShimmerPlaceholderBinding
import com.jdt.waltrackv2.databinding.ExpenseDataBinding
import com.jdt.waltrackv2.databinding.FilterLayoutBinding
import com.jdt.waltrackv2.databinding.FragmentExpensesBinding
import com.jdt.waltrackv2.utils.ActionButtonHandler
import com.jdt.waltrackv2.utils.FilterHandler
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.utils.RenderElementHandler

class ExpensesFragment : Fragment() {


    private var dataLoadingListener: OnDataLoading? = null

    private lateinit var binding: FragmentExpensesBinding
    private lateinit var filterLayoutBinding: FilterLayoutBinding
    private var isFilterToggled: Boolean = false

    private lateinit var shimmerPlaceholderBinding : BalanceShimmerPlaceholderBinding
    private lateinit var expenseDisplayDataBinding: ExpenseDataBinding
    private lateinit var addItemButton: AddItemLayoutBinding

    private lateinit var filterHandler: FilterHandler
    private  lateinit var actionButtonHandler: ActionButtonHandler

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

//            addItemHandler = AddItemHandler(
//                addItemButton.root,
//                Intent(requireContext(), TransactionAddActivity::class.java),
//                requireActivity(),
//                loadData(inflater)
//            )

            //TODO: Add data
            expenseDisplayDataBinding.expenseTotalBalanceDisplay.text = "Php 300.00"
            dataLoadingListener?.onDataLoadingFinished()
        }, 3000)
    }
}