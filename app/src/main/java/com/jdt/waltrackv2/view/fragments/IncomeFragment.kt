package com.jdt.waltrackv2.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jdt.waltrackv2.databinding.AddItemLayoutBinding
import com.jdt.waltrackv2.databinding.BalanceShimmerPlaceholderBinding
import com.jdt.waltrackv2.databinding.FilterLayoutBinding
import com.jdt.waltrackv2.databinding.FragmentIncomeBinding
import com.jdt.waltrackv2.databinding.IncomeDataBinding
import com.jdt.waltrackv2.utils.ActionButtonHandler
import com.jdt.waltrackv2.utils.FilterHandler
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.utils.RenderElementHandler

class IncomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var dataLoadingListener: OnDataLoading

    private lateinit var binding: FragmentIncomeBinding
    private lateinit var filterLayoutBinding: FilterLayoutBinding
    private lateinit var placeholderBinding: BalanceShimmerPlaceholderBinding
    private lateinit var incomeDataBinding: IncomeDataBinding
    private lateinit var addItemButton: AddItemLayoutBinding

    private lateinit var filterHandler: FilterHandler
    private lateinit var actionButtonHandler: ActionButtonHandler
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDataLoading){
            dataLoadingListener = context
        }else{
            throw RuntimeException("$context must implement DataLoadingListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentIncomeBinding.inflate(inflater, container, false)
        placeholderBinding = BalanceShimmerPlaceholderBinding.inflate(inflater, binding.incomeBalanceDisplay, true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataLoadingListener.onDataLoadingStarted()
        //simulate data loading
        Handler(Looper.getMainLooper()).postDelayed({

            activity?.let {
                val inflater = LayoutInflater.from(requireContext())
                filterLayoutBinding = FilterLayoutBinding.inflate(inflater, binding.filterOption, true)
                incomeDataBinding = IncomeDataBinding.inflate(inflater, binding.incomeBalanceDisplay, true)
                addItemButton = AddItemLayoutBinding.inflate(inflater, binding.addItemContainer, true)
                RenderElementHandler.removePlaceholders {
                    //remove shimmer
                    binding.incomeBalanceDisplay.removeView(placeholderBinding.root)

                    //TODO: Add data
                }
            }

//            addItemHandler = AddItemHandler(
//                addItemButton.root,
//                Intent(requireContext(), TransactionAddActivity::class.java),
//                requireActivity(),
//                loadData(inflater)
//            )

            filterHandler = FilterHandler(filterLayoutBinding, requireContext()) {
                Toast.makeText(requireContext(), "hello world", Toast.LENGTH_SHORT).show()
            }
            dataLoadingListener.onDataLoadingFinished()
        }, 3000)
    }
}