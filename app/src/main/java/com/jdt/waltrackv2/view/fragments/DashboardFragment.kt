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
import com.jdt.waltrackv2.databinding.DashboardBalanceDisplayBinding
import com.jdt.waltrackv2.databinding.DashboardIncomeDisplayBinding
import com.jdt.waltrackv2.databinding.DashboardExpsenseDisplayBinding
import com.jdt.waltrackv2.databinding.FragmentDashboardBinding
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.utils.RenderElementHandler
class DashboardFragment : Fragment() {


    private var dataLoadingListener : OnDataLoading? = null

    //dashboard binding
    private lateinit var binding: FragmentDashboardBinding

    //data to be displayed at the top:
    private lateinit var dataBalance: DashboardBalanceDisplayBinding
    private  lateinit var dataExpense: DashboardExpsenseDisplayBinding
    private lateinit var dataIncome: DashboardIncomeDisplayBinding

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataLoadingListener?.onDataLoadingStarted()

        //simulate data retrieval
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("DashboardFragment", "Handler delayed code execution")
            activity?.let {
                val inflater = LayoutInflater.from(requireContext())
                //inflating data
                dataBalance =
                    DashboardBalanceDisplayBinding.inflate(inflater, binding.balanceDisplay, true)
                dataExpense = DashboardExpsenseDisplayBinding.inflate(inflater, binding.expenseDisplay, true)
                dataIncome = DashboardIncomeDisplayBinding.inflate(inflater, binding.incomeDisplay, true)
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