package com.jdt.waltrackv2.view.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdt.waltrackv2.adapters.TransactionAdapter
import com.jdt.waltrackv2.data.view_model.TransactionViewModel
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.databinding.BalanceShimmerPlaceholderBinding
import com.jdt.waltrackv2.databinding.DashboardBalanceDisplayBinding
import com.jdt.waltrackv2.databinding.DashboardIncomeDisplayBinding
import com.jdt.waltrackv2.databinding.DashboardExpsenseDisplayBinding
import com.jdt.waltrackv2.databinding.FragmentDashboardBinding
import com.jdt.waltrackv2.databinding.PlaceholderTransactionsBinding
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
    private lateinit var itemsPlaceholderBinding: PlaceholderTransactionsBinding

    //viewmodels
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var walletViewModel: WalletViewModel

    private lateinit var tAdapter: TransactionAdapter
    private lateinit var activityResult: ActivityResultLauncher<Intent>
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

        activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadData()
            }
        }

        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        //placeholders
        itemsPlaceholderBinding = PlaceholderTransactionsBinding.inflate(inflater, binding.transactionListingContainer, true)
        balancePlaceholder =
            BalanceShimmerPlaceholderBinding.inflate(inflater, binding.balanceDisplay, true)
        expensePlaceholder = BalanceShimmerPlaceholderBinding.inflate(inflater, binding.expenseDisplay, true)
        incomePlaceholder = BalanceShimmerPlaceholderBinding.inflate(inflater, binding.incomeDisplay, true)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataLoadingListener?.onDataLoadingStarted()
        tAdapter = TransactionAdapter(requireContext(), activityResult, walletViewModel, viewLifecycleOwner)

        Handler(Looper.getMainLooper()).postDelayed({

            loadData()

            dataLoadingListener?.onDataLoadingFinished()

        }, 1500)
    }

    private fun loadData(){

        val context = context ?: return  // Ensuring context is not null
        val inflater = LayoutInflater.from(context)
        if (isAdded && view != null) {
            binding.recentTransactions.layoutManager = LinearLayoutManager(context)
            binding.recentTransactions.adapter = tAdapter

            transactionViewModel.getAllTransactions(null, 5).observe(viewLifecycleOwner){transaction ->
                tAdapter.setData(transaction)
                itemsPlaceholderBinding.root.visibility = View.GONE

                binding.recentTransactions.visibility = View.VISIBLE

                transactionViewModel.getTotal("Expense", null).observe(viewLifecycleOwner){ expense ->

                    val expenseData = expense ?: 0.0

                    dataBalance = DashboardBalanceDisplayBinding.inflate(inflater, binding.balanceDisplay, true)
                    dataExpense = DashboardExpsenseDisplayBinding.inflate(inflater, binding.expenseDisplay, true)
                    dataIncome = DashboardIncomeDisplayBinding.inflate(inflater, binding.incomeDisplay, true)

                    transactionViewModel.getTotal("Income", null).observe(viewLifecycleOwner){income ->
                        val incomeData = income?: 0.0
                        dataExpense.totalExpenseDisplay.text = "-Php $expenseData"
                        dataIncome.totalIncomeDisplay.text = "+Php $incomeData"
                        dataBalance.totalBalanceDisplay.text = "Php ${incomeData - expenseData}"
                    }

                }

                binding.balanceDisplay.removeView(balancePlaceholder.root)
                binding.incomeDisplay.removeView(incomePlaceholder.root)
                binding.expenseDisplay.removeView(expensePlaceholder.root)
            }
        }
    }


}