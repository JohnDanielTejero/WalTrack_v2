package com.jdt.waltrackv2.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdt.waltrackv2.databinding.BalanceShimmerPlaceholderBinding
import com.jdt.waltrackv2.databinding.DataBalanceDisplayDataBinding
import com.jdt.waltrackv2.databinding.DataExpenseDisplayDataBinding
import com.jdt.waltrackv2.databinding.DataIncomeDisplayDataBinding
import com.jdt.waltrackv2.databinding.FragmentDashboardBinding
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.utils.RenderElementHandler

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var dataLoadingListener : OnDataLoading? = null

    //dashboard binding
    private lateinit var binding: FragmentDashboardBinding

    //data to be displayed at the top:
    private lateinit var dataBalance: DataBalanceDisplayDataBinding
    private  lateinit var dataExpense: DataExpenseDisplayDataBinding
    private lateinit var dataIncome: DataIncomeDisplayDataBinding

    //shimmering to be created
    private lateinit var balancePlaceholder: BalanceShimmerPlaceholderBinding
    private lateinit var expensePlaceholder: BalanceShimmerPlaceholderBinding
    private lateinit var incomePlaceholder: BalanceShimmerPlaceholderBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDataLoading) {
            dataLoadingListener = context
        } else {
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
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        //placeholder
        balancePlaceholder =
            BalanceShimmerPlaceholderBinding.inflate(inflater, binding.balanceDisplay, true)
        expensePlaceholder = BalanceShimmerPlaceholderBinding.inflate(inflater, binding.expenseDisplay, true)
        incomePlaceholder = BalanceShimmerPlaceholderBinding.inflate(inflater, binding.incomeDisplay, true)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment dashboard.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataLoadingListener?.onDataLoadingStarted()

        RenderElementHandler.initPlaceholders {
            Log.d("DashboardFragment", "Adding shimmer")
            binding.balanceDisplay.addView(balancePlaceholder.root)
            binding.incomeDisplay.addView(incomePlaceholder.root)
            binding.expenseDisplay.addView(expensePlaceholder.root)
        }

        //simulate data retrieval
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("DashboardFragment", "Handler delayed code execution")
            activity?.let {
                val inflater = LayoutInflater.from(requireContext())
                //inflating data
                dataBalance =
                    DataBalanceDisplayDataBinding.inflate(inflater, binding.balanceDisplay, true)
                dataExpense = DataExpenseDisplayDataBinding.inflate(inflater, binding.expenseDisplay, true)
                dataIncome = DataIncomeDisplayDataBinding.inflate(inflater, binding.incomeDisplay, true)
                Log.d("DashboardFragment", "Stopped shimmer, adding actual data")

                // Stop shimmer animation
                RenderElementHandler.removePlaceholders {
                    binding.balanceDisplay.removeView(balancePlaceholder.root)
                    binding.incomeDisplay.removeView(incomePlaceholder.root)
                    binding.expenseDisplay.removeView(expensePlaceholder.root)
                }

                //adding data
                dataLoadingListener?.onDataLoadingFinished()
            }


        }, 3000) // Adjust the delay as need
    }

}