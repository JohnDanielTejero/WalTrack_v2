package com.jdt.waltrackv2.view.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdt.waltrackv2.adapters.IncomeAdapter
import com.jdt.waltrackv2.data.model.TransactionTable
import com.jdt.waltrackv2.data.view_model.TransactionViewModel
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.databinding.AddItemLayoutBinding
import com.jdt.waltrackv2.databinding.BalanceShimmerPlaceholderBinding
import com.jdt.waltrackv2.databinding.FilterLayoutBinding
import com.jdt.waltrackv2.databinding.FragmentIncomeBinding
import com.jdt.waltrackv2.databinding.IncomeDataBinding
import com.jdt.waltrackv2.databinding.PlaceholderTransactionsBinding
import com.jdt.waltrackv2.utils.ActionButtonHandler
import com.jdt.waltrackv2.utils.FilterHandler
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.view.TransactionAddActivity

class IncomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var dataLoadingListener: OnDataLoading

    private lateinit var binding: FragmentIncomeBinding
    private lateinit var filterLayoutBinding: FilterLayoutBinding

    private lateinit var itemsPlaceholderBinding: PlaceholderTransactionsBinding
    private lateinit var placeholderBinding: BalanceShimmerPlaceholderBinding
    private lateinit var incomeDataBinding: IncomeDataBinding
    private lateinit var addItemButton: AddItemLayoutBinding

    private lateinit var incomeAdapter: IncomeAdapter
    private lateinit var filterHandler: FilterHandler
    private lateinit var actionButtonHandler: ActionButtonHandler
    private lateinit var activityResult: ActivityResultLauncher<Intent>

    private lateinit var walletViewModel: WalletViewModel
    private lateinit var transactionViewModel: TransactionViewModel
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
        binding = FragmentIncomeBinding.inflate(inflater, container, false)
        placeholderBinding = BalanceShimmerPlaceholderBinding.inflate(inflater, binding.incomeBalanceDisplay, true)

        activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadData(inflater)
            }
        }

        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]

        itemsPlaceholderBinding = PlaceholderTransactionsBinding.inflate(inflater, binding.incomeListingContainer, true)
        binding.incomeListing.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataLoadingListener.onDataLoadingStarted()

        binding.incomeListing.visibility = View.GONE
        itemsPlaceholderBinding.root.visibility = View.VISIBLE

        incomeAdapter = IncomeAdapter(requireContext(), activityResult, walletViewModel, viewLifecycleOwner)

        Handler(Looper.getMainLooper()).postDelayed({
            val inflater = LayoutInflater.from(requireContext())
            loadData(inflater)
            incomeDataBinding = IncomeDataBinding.inflate(inflater, binding.incomeBalanceDisplay, true)
            incomeDataBinding.root.visibility = View.GONE
            dataLoadingListener?.onDataLoadingFinished()

            dataLoadingListener.onDataLoadingFinished()
        }, 1500)
    }

    private fun loadData(inflater: LayoutInflater) {

        binding.incomeListing.layoutManager = LinearLayoutManager(requireContext())
        binding.incomeListing.adapter = incomeAdapter
        transactionViewModel.getAllTransactions("Income", null).observe(viewLifecycleOwner) { transactionsData ->
            calcTotalIncome(transactionsData)
            if (!::filterLayoutBinding.isInitialized) {
                filterLayoutBinding = FilterLayoutBinding.inflate(inflater, binding.filterOption, true)
                walletViewModel.wallets.observe(viewLifecycleOwner){
                    filterHandler = FilterHandler(filterLayoutBinding, requireContext(), {
                        placeholderBinding.root.visibility = View.VISIBLE
                        itemsPlaceholderBinding.root.visibility = View.VISIBLE
                        binding.incomeListing.visibility = View.GONE

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
                                                "Income",
                                                selectedWallet,
                                                year,
                                                month,
                                                day
                                            ).observe(viewLifecycleOwner) { filteredTransaction ->
                                                incomeAdapter.setData(filteredTransaction)
                                                calcTotalIncome(filteredTransaction)
                                            }
                                        }
                                    }
                            }else {
                                transactionViewModel.getFilteredTransactions(
                                    "Expense",
                                    null,
                                    year,
                                    month,
                                    day
                                ).observe(viewLifecycleOwner){ filteredTransaction ->
                                    incomeAdapter.setData(filteredTransaction)
                                    calcTotalIncome(filteredTransaction)
                                }
                            }

                            binding.incomeListing.visibility = View.VISIBLE
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

            incomeAdapter.setData(transactionsData)
            binding.incomeListing.visibility = View.VISIBLE
            placeholderBinding.root.visibility = View.GONE
            itemsPlaceholderBinding.root.visibility = View.GONE
        }
        dataLoadingListener?.onDataLoadingFinished()
    }

    private fun calcTotalIncome(transactions: List<TransactionTable>){
        val totalExpenseAmount = transactions
            .sumOf { it.transactionAmount }
        incomeDataBinding.incomeTotalBalanceDisplay.text = "Php ${totalExpenseAmount.toString()}"
        placeholderBinding.root.visibility = View.GONE
        incomeDataBinding.root.visibility = View.VISIBLE
    }
}