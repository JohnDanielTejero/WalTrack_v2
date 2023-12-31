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
import androidx.recyclerview.widget.RecyclerView
import com.jdt.waltrackv2.adapters.WalletAdapter
import com.jdt.waltrackv2.data.view_model.TransactionViewModel
import com.jdt.waltrackv2.data.view_model.WalletViewModel
import com.jdt.waltrackv2.databinding.AddItemLayoutBinding
import com.jdt.waltrackv2.databinding.FilterLayoutWalletVerBinding
import com.jdt.waltrackv2.databinding.FragmentWalletBinding
import com.jdt.waltrackv2.databinding.PlaceholderTransactionsBinding
import com.jdt.waltrackv2.utils.ActionButtonHandler
import com.jdt.waltrackv2.utils.FilterHandlerWalletVer
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.view.AddWalletActivity

class WalletFragment : Fragment() {

    private lateinit var binding : FragmentWalletBinding
    private lateinit var layoutWalletVerBinding: FilterLayoutWalletVerBinding
    private lateinit var addItemLayoutBinding: AddItemLayoutBinding
    private lateinit var itemsPlaceholderBinding: PlaceholderTransactionsBinding

    private var dataLoadingListener : OnDataLoading? = null
    private lateinit var actionButtonHandler: ActionButtonHandler
    private lateinit var filterHandler: FilterHandlerWalletVer

    private lateinit var walletListing: RecyclerView
    private lateinit var walletAdapter: WalletAdapter
    private lateinit var handleWalletEvents: ActivityResultLauncher<Intent>
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
        dataLoadingListener?.onDataLoadingStarted()
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        handleWalletEvents = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadData(inflater)
            }
        }
        itemsPlaceholderBinding = PlaceholderTransactionsBinding.inflate(inflater, binding.walletListingContainer, true)
        binding.walletListing.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        val inflater = LayoutInflater.from(requireContext())

        walletAdapter = WalletAdapter(requireContext(), walletViewModel, transactionViewModel, handleWalletEvents, viewLifecycleOwner)
        itemsPlaceholderBinding.root.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            loadData(inflater)
        }, 1500)
    }

    private fun loadData(inflater: LayoutInflater) {

        walletListing = binding.walletListing
        walletListing.layoutManager = LinearLayoutManager(requireContext())
        walletListing.adapter = walletAdapter
        walletViewModel.wallets.observe(viewLifecycleOwner) { walletData ->

            if (!::layoutWalletVerBinding.isInitialized) {
                layoutWalletVerBinding = FilterLayoutWalletVerBinding.inflate(inflater, binding.filterOption, true)
                filterHandler = FilterHandlerWalletVer(layoutWalletVerBinding, requireContext()){

                    itemsPlaceholderBinding.root.visibility = View.VISIBLE
                    binding.walletListing.visibility = View.GONE

                    val yearFilter = layoutWalletVerBinding.filterYearDropdown.text.toString()
                    val monthFilter = layoutWalletVerBinding.filterMonthDropdown.text.toString()
                    val dayFilter = layoutWalletVerBinding.filterDayDropdown.text.toString()
                    val year = if (yearFilter != "All") yearFilter.toInt() else null
                    val month = if (monthFilter != "All") monthFilter else null
                    val day = if (dayFilter != "All") dayFilter.toInt() else null

                    if(year == null  && month == null && day == null){
                        loadData(inflater)
                    }else{
                        walletViewModel.getFilteredWallets(year, month, day)?.observe(viewLifecycleOwner) { filteredWalletData ->
                            walletAdapter.setData(filteredWalletData)
                            binding.walletListing.visibility = View.VISIBLE
                            itemsPlaceholderBinding.root.visibility = View.GONE
                        }
                    }
                }
            }

            if(!::addItemLayoutBinding.isInitialized){
                addItemLayoutBinding = AddItemLayoutBinding.inflate(inflater, binding.addItemContainer, true)
                actionButtonHandler = ActionButtonHandler(addItemLayoutBinding.root,
                    Intent(requireContext(), AddWalletActivity::class.java),
                    handleWalletEvents)
            }

            walletAdapter.setData(walletData)
            binding.walletListing.visibility = View.VISIBLE
            itemsPlaceholderBinding.root.visibility = View.GONE
        }
        dataLoadingListener?.onDataLoadingFinished()
    }
}