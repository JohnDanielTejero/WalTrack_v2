package com.jdt.waltrackv2.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.databinding.FragmentDashboardBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var binding: FragmentDashboardBinding;
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
    ): View? {

        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        binding.balanceDisplayPlaceholder.startShimmer()
        binding.incomeDisplayPlaceholder.startShimmer()
        binding.expenseDisplayPlaceholder.startShimmer()
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
        // TODO: Rename and change types and number of parameters
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
        Handler(Looper.getMainLooper()).postDelayed({
            // Stop shimmer animation after loading data
            stopShimmerAndShowData()
        }, 3000) // Adjust the delay as need
    }
    private fun stopShimmerAndShowData() {
        // Stop shimmer animation
        binding.balanceDisplayPlaceholder.stopShimmer()
        binding.balanceDisplayPlaceholder.visibility = View.GONE
        binding.balanceDisplayData.visibility = View.VISIBLE

        binding.incomeDisplayPlaceholder.stopShimmer()
        binding.incomeDisplayPlaceholder.visibility = View.GONE
        binding.incomeDisplayData.visibility = View.VISIBLE

        binding.expenseDisplayPlaceholder.stopShimmer()
        binding.expenseDisplayPlaceholder.visibility = View.GONE
        binding.expenseDisplayData.visibility = View.VISIBLE

    }
}