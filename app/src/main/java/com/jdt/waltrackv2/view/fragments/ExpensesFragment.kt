package com.jdt.waltrackv2.view.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.adapters.ExpenseAdapter
import com.jdt.waltrackv2.data.view_model.TransactionViewModel
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.databinding.AddItemLayoutBinding
import com.jdt.waltrackv2.databinding.BalanceShimmerPlaceholderBinding
import com.jdt.waltrackv2.databinding.ExpenseDataBinding
import com.jdt.waltrackv2.databinding.FilterLayoutBinding
import com.jdt.waltrackv2.databinding.FragmentExpensesBinding
import com.jdt.waltrackv2.databinding.PlaceholderTransactionsBinding
import com.jdt.waltrackv2.utils.ActionButtonHandler
import com.jdt.waltrackv2.utils.FilterHandler
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.view.TransactionAddActivity

class ExpensesFragment : Fragment() {


    private var dataLoadingListener: OnDataLoading? = null

    private lateinit var binding: FragmentExpensesBinding
    private lateinit var filterLayoutBinding: FilterLayoutBinding

    private lateinit var shimmerPlaceholderBinding : BalanceShimmerPlaceholderBinding
    private lateinit var expenseDisplayDataBinding: ExpenseDataBinding
    private lateinit var itemsPlaceholderBinding: PlaceholderTransactionsBinding
    private lateinit var addItemButton: AddItemLayoutBinding

    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var filterHandler: FilterHandler
    private  lateinit var actionButtonHandler: ActionButtonHandler
    private lateinit var activityResult: ActivityResultLauncher<Intent>

    private lateinit var walletViewModel: WalletViewModel
    private lateinit var transactionViewModel: TransactionViewModel
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

        activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //loadData(inflater)
            }
        }

        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]

        itemsPlaceholderBinding = PlaceholderTransactionsBinding.inflate(inflater, binding.expenseListingContainer, true)
        binding.expenseListing.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataLoadingListener?.onDataLoadingStarted()

        binding.expenseListing.visibility = View.GONE
        itemsPlaceholderBinding.root.visibility = View.VISIBLE

        expenseAdapter = ExpenseAdapter(requireContext(), transactionViewModel, activityResult)

        //simulate data retrieval
        Handler(Looper.getMainLooper()).postDelayed({
            val inflater = LayoutInflater.from(requireContext())
            loadData(inflater)
            expenseDisplayDataBinding = ExpenseDataBinding.inflate(inflater, binding.expenseBalanceDisplay, true)
            dataLoadingListener?.onDataLoadingFinished()
        }, 1500)
    }

    private fun loadData(inflater: LayoutInflater) {

        binding.expenseListing.layoutManager = LinearLayoutManager(requireContext())
        binding.expenseListing.adapter = expenseAdapter
        transactionViewModel.getAllTransactions("Expense").observe(viewLifecycleOwner) { transactionsData ->

            if (!::filterLayoutBinding.isInitialized) {
                filterLayoutBinding = FilterLayoutBinding.inflate(inflater, binding.filterOption, true)
                walletViewModel.wallets.observe(viewLifecycleOwner){
                    filterHandler = FilterHandler(filterLayoutBinding, requireContext(), {
                        shimmerPlaceholderBinding.root.visibility = View.VISIBLE
                        itemsPlaceholderBinding.root.visibility = View.VISIBLE
                        binding.expenseListing.visibility = View.GONE

                        val walletFilter = filterLayoutBinding.filterWalletDropdown.text.toString()
                        val yearFilter = filterLayoutBinding.filterYearDropdown.text.toString()
                        val monthFilter = filterLayoutBinding.filterMonthDropdown.text.toString()
                        val dayFilter = filterLayoutBinding.filterDayDropdown.text.toString()

                        val wallet = if (walletFilter != "All") walletFilter else null
                        val year = if (yearFilter != "All") yearFilter.toInt() else null
                        val month = if (monthFilter != "All") monthFilter else null
                        val day = if (dayFilter != "All") dayFilter.toInt() else null

                        if(wallet == null && year == null  && month == null && day == null){
                            loadData(inflater)
                        }else{
                            if (wallet != null) {
                                walletViewModel.getWalletByName(wallet)
                                    .observe(viewLifecycleOwner) { walletData ->
                                        val selectedWallet = walletData?.walletId
                                        if (selectedWallet != null) {
                                            transactionViewModel.getFilteredTransactions(
                                                "Expense",
                                                selectedWallet,
                                                year,
                                                month,
                                                day
                                            )?.observe(viewLifecycleOwner) { filteredTransaction ->
                                                expenseAdapter.setData(filteredTransaction)
                                            }
                                        } else {
                                            transactionViewModel.getFilteredTransactions(
                                                "Expense",
                                                null,
                                                year,
                                                month,
                                                day
                                            )?.observe(viewLifecycleOwner){ filteredTransaction ->
                                                expenseAdapter.setData(filteredTransaction)
                                            }
                                        }
                                    }
                            }
                            shimmerPlaceholderBinding.root.visibility = View.GONE
                            binding.expenseListing.visibility = View.VISIBLE
                            itemsPlaceholderBinding.root.visibility = View.GONE
                        }
                    }, it)
                }
            }

            if(!::addItemButton.isInitialized){
                addItemButton = AddItemLayoutBinding.inflate(inflater, binding.addItemContainer, true)
                actionButtonHandler = ActionButtonHandler(addItemButton.root,
                    Intent(requireContext(), TransactionAddActivity::class.java),
                    activityResult)
            }

            expenseAdapter.setData(transactionsData)
            binding.expenseListing.visibility = View.VISIBLE
            shimmerPlaceholderBinding.root.visibility = View.GONE
            itemsPlaceholderBinding.root.visibility = View.GONE
        }
        dataLoadingListener?.onDataLoadingFinished()
    }
}