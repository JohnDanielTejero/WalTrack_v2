package com.jdt.waltrackv2.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.databinding.AddItemLayoutBinding
import com.jdt.waltrackv2.databinding.BalanceShimmerPlaceholderBinding
import com.jdt.waltrackv2.databinding.FilterLayoutWalletVerBinding
import com.jdt.waltrackv2.databinding.FragmentWalletBinding
import com.jdt.waltrackv2.utils.AddItemHandler
import com.jdt.waltrackv2.utils.FilterHandler
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.view.AddWalletActivity

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WalletFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentWalletBinding
    private lateinit var layoutWalletVerBinding: FilterLayoutWalletVerBinding
    private lateinit var addItemLayoutBinding: AddItemLayoutBinding

    private lateinit var addItemHandler: AddItemHandler
    private lateinit var filterHandler: FilterHandler
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
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        layoutWalletVerBinding = FilterLayoutWalletVerBinding.inflate(inflater, binding.filterOption, true)
        addItemLayoutBinding = AddItemLayoutBinding.inflate(inflater, binding.addItemContainer, true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterHandler = FilterHandler(layoutWalletVerBinding.filterButton, layoutWalletVerBinding.filterDisplay, requireContext())
        addItemHandler = AddItemHandler(addItemLayoutBinding.root,
            Intent(requireContext(), AddWalletActivity::class.java),
            requireActivity())
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WalletFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalletFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}