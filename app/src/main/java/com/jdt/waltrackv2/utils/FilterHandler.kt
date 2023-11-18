package com.jdt.waltrackv2.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.adapters.setupDropdownAdapter
import com.jdt.waltrackv2.databinding.FilterLayoutBinding
import com.jdt.waltrackv2.databinding.FilterLayoutWalletVerBinding
import java.util.Calendar
import java.util.Locale

class FilterHandler(private val filterLayoutBinding: FilterLayoutBinding, context: Context) {
    private var isFilterToggled: Boolean = false

    init {
        filterLayoutBinding.filterButton.setOnClickListener {
            Log.d("FilterHandler", "filter is clicked")
            isFilterToggled = !isFilterToggled
            updateFilter(context)

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val yearList = mutableListOf("All").apply {
                addAll((1970..currentYear).map { it.toString() })
            }.sortedDescending()

            filterLayoutBinding.filterYearDropdown.setupDropdownAdapter(
                context,
                R.layout.dropdown_item,
                yearList.toTypedArray())

            filterLayoutBinding.filterYearDropdown.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Not needed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Not needed
                }

                override fun afterTextChanged(s: Editable?) {
                    val yearText = s.toString()
                    if(yearText != "All"){
                        filterLayoutBinding.filterMonthDropdownContainer.isEnabled = true
                        filterLayoutBinding.filterDayDropdown.setText("All")
                        filterLayoutBinding.filterMonthDropdown.setText("All")
                        populateMonths(yearText, context)
                    }else{
                        filterLayoutBinding.filterMonthDropdownContainer.isEnabled = false
                        filterLayoutBinding.filterMonthDropdown.setText("All")
                        filterLayoutBinding.filterDayDropdownContainer.isEnabled = false
                        filterLayoutBinding.filterDayDropdown.setText("All")

                    }

                }
            })
        }
    }

    private fun populateMonths(selectedYear: String, context: Context) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, selectedYear.toInt())

        val months = mutableListOf("All")

        for (i in 0 until 12) {
            calendar.set(Calendar.MONTH, i)
            val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) ?: ""
            months.add(monthName)
        }

        // Use the months array to set up the dropdown adapter for the month dropdown
        filterLayoutBinding.filterMonthDropdown.setupDropdownAdapter(
            context,
            R.layout.dropdown_item,
            months.toTypedArray())
        filterLayoutBinding.filterMonthDropdown.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                val monthText = s.toString()
                if(monthText != "All"){
                    filterLayoutBinding.filterDayDropdownContainer.isEnabled = true
                    filterLayoutBinding.filterDayDropdown.setText("All")
                    populateDays(selectedYear, monthText, context)
                }else{
                    filterLayoutBinding.filterDayDropdownContainer.isEnabled = false
                    filterLayoutBinding.filterDayDropdown.setText("All")
                }

            }
        })
    }
    private fun populateDays(selectedYear: String, selectedMonth: String, context: Context) {
        val year = selectedYear.toInt()
        val month = getMonthNumber(selectedMonth)

        // Calculate the number of days for the selected year and month
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1) // Set the calendar to the selected year and month
        val numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Create an array to hold the days
        val days = mutableListOf("All") // Add "All" as the first item
        for (i in 1..numDaysInMonth) {
            days.add(i.toString())
        }

        // Use the days array to set up the dropdown adapter for the day dropdown
        filterLayoutBinding.filterDayDropdown.setupDropdownAdapter(
            context,
            R.layout.dropdown_item,
            days.toTypedArray())
    }

    // Helper function to get the month number from its short name
    private fun getMonthNumber(monthName: String): Int {
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        return months.indexOfFirst { it.equals(monthName, ignoreCase = true) }
    }

    private fun updateFilter(context: Context) {
        if (isFilterToggled) {
            filterLayoutBinding.filterDisplay.visibility = View.VISIBLE
            filterLayoutBinding.filterButton.setBackgroundResource(R.drawable.filter_bg)
            filterLayoutBinding.filterButton.icon = ContextCompat.getDrawable(filterLayoutBinding.filterButton.context, R.drawable.ic_filter_hide)
        } else {
            filterLayoutBinding.filterDisplay.visibility = View.GONE
            filterLayoutBinding.filterButton.setBackgroundResource(R.drawable.filter_bg)
            filterLayoutBinding.filterButton.icon = ContextCompat.getDrawable(filterLayoutBinding.filterButton.context, R.drawable.ic_filter_show)
        }
    }

}

class FilterHandlerWalletVer(private val filterLayoutBinding: FilterLayoutWalletVerBinding, context: Context){
    private var isFilterToggled: Boolean = false

    init {
        filterLayoutBinding.filterButton.setOnClickListener {
            Log.d("FilterHandler", "filter is clicked")
            isFilterToggled = !isFilterToggled
            updateFilter(context)

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val yearList = mutableListOf("All").apply {
                addAll((1970..currentYear).map { it.toString() })
            }.sortedDescending()

            filterLayoutBinding.filterYearDropdown.setupDropdownAdapter(
                context,
                R.layout.dropdown_item,
                yearList.toTypedArray()
            )

            filterLayoutBinding.filterYearDropdown.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Not needed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Not needed
                }

                override fun afterTextChanged(s: Editable?) {
                    val yearText = s.toString()
                    if (yearText != "All") {
                        filterLayoutBinding.filterMonthDropdownContainer.isEnabled = true
                        filterLayoutBinding.filterDayDropdown.setText("All")
                        filterLayoutBinding.filterMonthDropdown.setText("All")
                        populateMonths(yearText, context)
                    } else {
                        filterLayoutBinding.filterMonthDropdownContainer.isEnabled = false
                        filterLayoutBinding.filterMonthDropdown.setText("All")
                        filterLayoutBinding.filterDayDropdownContainer.isEnabled = false
                        filterLayoutBinding.filterDayDropdown.setText("All")

                    }

                }
            })

        }

    }
    private fun populateMonths(selectedYear: String, context: Context) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, selectedYear.toInt())

        val months = mutableListOf("All")

        for (i in 0 until 12) {
            calendar.set(Calendar.MONTH, i)
            val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) ?: ""
            months.add(monthName)
        }

        // Use the months array to set up the dropdown adapter for the month dropdown
        filterLayoutBinding.filterMonthDropdown.setupDropdownAdapter(
            context,
            R.layout.dropdown_item,
            months.toTypedArray())
        filterLayoutBinding.filterMonthDropdown.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                val monthText = s.toString()
                if(monthText != "All"){
                    filterLayoutBinding.filterDayDropdownContainer.isEnabled = true
                    filterLayoutBinding.filterDayDropdown.setText("All")
                    populateDays(selectedYear, monthText, context)
                }else{
                    filterLayoutBinding.filterDayDropdownContainer.isEnabled = false
                    filterLayoutBinding.filterDayDropdown.setText("All")
                }

            }
        })
    }
    private fun populateDays(selectedYear: String, selectedMonth: String, context: Context) {
        val year = selectedYear.toInt()
        val month = getMonthNumber(selectedMonth)

        // Calculate the number of days for the selected year and month
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1) // Set the calendar to the selected year and month
        val numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Create an array to hold the days
        val days = mutableListOf("All") // Add "All" as the first item
        for (i in 1..numDaysInMonth) {
            days.add(i.toString())
        }

        // Use the days array to set up the dropdown adapter for the day dropdown
        filterLayoutBinding.filterDayDropdown.setupDropdownAdapter(
            context,
            R.layout.dropdown_item,
            days.toTypedArray())
    }

    // Helper function to get the month number from its short name
    private fun getMonthNumber(monthName: String): Int {
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        return months.indexOfFirst { it.equals(monthName, ignoreCase = true) }
    }
    private fun updateFilter(context: Context) {
        if (isFilterToggled) {
            filterLayoutBinding.filterDisplay.visibility = View.VISIBLE
            filterLayoutBinding.filterButton.setBackgroundResource(R.drawable.filter_bg)
            filterLayoutBinding.filterButton.icon = ContextCompat.getDrawable(filterLayoutBinding.filterButton.context, R.drawable.ic_filter_hide)
        } else {
            filterLayoutBinding.filterDisplay.visibility = View.GONE
            filterLayoutBinding.filterButton.setBackgroundResource(R.drawable.filter_bg)
            filterLayoutBinding.filterButton.icon = ContextCompat.getDrawable(filterLayoutBinding.filterButton.context, R.drawable.ic_filter_show)
        }
    }
}
