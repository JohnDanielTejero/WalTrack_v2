package com.jdt.waltrackv2.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdt.waltrackv2.databinding.AddItemLayoutBinding
import com.jdt.waltrackv2.databinding.BalanceShimmerPlaceholderBinding
import com.jdt.waltrackv2.databinding.FilterLayoutBinding
import com.jdt.waltrackv2.databinding.FragmentIncomeBinding
import com.jdt.waltrackv2.databinding.IncomeDataBinding
import com.jdt.waltrackv2.utils.AddItemHandler
import com.jdt.waltrackv2.utils.FilterHandler
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.utils.RenderElementHandler
import com.jdt.waltrackv2.view.TransactionAddActivity

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IncomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
    private lateinit var addItemHandler: AddItemHandler
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDataLoading){
            dataLoadingListener = context
        }else{
            throw RuntimeException("$context must implement DataLoadingListener")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
        RenderElementHandler.initPlaceholders {
            binding.incomeBalanceDisplay.addView(placeholderBinding.root)
        }

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

            addItemHandler = AddItemHandler(
                addItemButton.root,
                Intent(requireContext(), TransactionAddActivity::class.java),
                requireActivity()
            )
            filterHandler = FilterHandler(filterLayoutBinding, requireContext())
            dataLoadingListener.onDataLoadingFinished()
        }, 3000)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IncomeFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IncomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}