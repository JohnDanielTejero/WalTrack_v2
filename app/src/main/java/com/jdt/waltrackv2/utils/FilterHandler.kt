package com.jdt.waltrackv2.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.adapters.setupDropdownAdapter
import com.jdt.waltrackv2.data.model.WalletTable
import com.jdt.waltrackv2.databinding.FilterLayoutBinding
import com.jdt.waltrackv2.databinding.FilterLayoutWalletVerBinding
import java.util.Calendar
import java.util.Locale

//TODO: Add Wallet Item as Parameter
class FilterHandler(private val filterLayoutBinding: FilterLayoutBinding, context: Context, cb : (()->Unit)? = null, private val wallets: List<WalletTable>?= null) {
    private var isFilterToggled: Boolean = false
    init {
        filterLayoutBinding.filterButton.setOnClickListener {
            Log.d("FilterHandler", "filter is clicked")
            isFilterToggled = !isFilterToggled
            updateFilterView(
                filterLayoutBinding.filterDisplay,
                filterLayoutBinding.filterButton,
                isFilterToggled)
        }

        val allAndWallets = arrayOf("All") + (wallets!!.map { it.walletName }.toTypedArray())
        setupDropdown(filterLayoutBinding.filterWalletDropdown, context, allAndWallets) { _, _, _, _ -> cb?.invoke()}
        setupDropdown(filterLayoutBinding.filterYearDropdown, context, populateYear().toTypedArray()){ s, _, _, _ ->
            cb?.invoke()
            val yearText = s.toString()
            if (yearText != "All") {
                filterLayoutBinding.filterMonthDropdownContainer.isEnabled = true
                filterLayoutBinding.filterDayDropdown.setText("All")
                filterLayoutBinding.filterMonthDropdown.setText("All")
                setupDropdown(filterLayoutBinding.filterMonthDropdown, context,
                    populateMonth(yearText).toTypedArray()
                ){s, _, _, _ ->
                    cb?.invoke()
                    val monthText = s.toString()
                    if(monthText != "All"){
                        filterLayoutBinding.filterDayDropdownContainer.isEnabled = true
                        filterLayoutBinding.filterDayDropdown.setText("All")
                        setupDropdown(filterLayoutBinding.filterDayDropdown, context,
                            populateDays(yearText, monthText).toTypedArray()
                        ){_,_,_,_ ->
                            cb?.invoke()
                        }
                    }else{
                        filterLayoutBinding.filterDayDropdownContainer.isEnabled = false
                        filterLayoutBinding.filterDayDropdown.setText("All")
                    }

                }
            } else {
                filterLayoutBinding.filterMonthDropdownContainer.isEnabled = false
                filterLayoutBinding.filterMonthDropdown.setText("All")
                filterLayoutBinding.filterDayDropdownContainer.isEnabled = false
                filterLayoutBinding.filterDayDropdown.setText("All")
            }
        }
    }
}

class FilterHandlerWalletVer(private val filterLayoutBinding: FilterLayoutWalletVerBinding, context: Context, cb: (()-> Unit)? = null){
    private var isFilterToggled: Boolean = false
    init {
        filterLayoutBinding.filterButton.setOnClickListener {
            Log.d("FilterHandler", "filter is clicked")
            isFilterToggled = !isFilterToggled
            updateFilterView(
                filterLayoutBinding.filterDisplay,
                filterLayoutBinding.filterButton,
                isFilterToggled)
        }
        setupDropdown(filterLayoutBinding.filterYearDropdown, context, populateYear().toTypedArray()){ s, _, _, _ ->
            cb?.invoke()
            val yearText = s.toString()
            if (yearText != "All") {
                filterLayoutBinding.filterMonthDropdownContainer.isEnabled = true
                filterLayoutBinding.filterDayDropdown.setText("All")
                filterLayoutBinding.filterMonthDropdown.setText("All")
                setupDropdown(filterLayoutBinding.filterMonthDropdown, context,
                    populateMonth(yearText).toTypedArray()
                ){s, _, _, _ ->
                    cb?.invoke()
                    val monthText = s.toString()
                    if(monthText != "All"){
                        filterLayoutBinding.filterDayDropdownContainer.isEnabled = true
                        filterLayoutBinding.filterDayDropdown.setText("All")
                        setupDropdown(filterLayoutBinding.filterDayDropdown, context,
                            populateDays(yearText, monthText).toTypedArray()
                        ){_,_,_,_ ->
                            cb?.invoke()
                        }
                    }else{
                        filterLayoutBinding.filterDayDropdownContainer.isEnabled = false
                        filterLayoutBinding.filterDayDropdown.setText("All")
                    }

                }
            } else {
                filterLayoutBinding.filterMonthDropdownContainer.isEnabled = false
                filterLayoutBinding.filterMonthDropdown.setText("All")
                filterLayoutBinding.filterDayDropdownContainer.isEnabled = false
                filterLayoutBinding.filterDayDropdown.setText("All")
            }
        }
    }
}

fun setupDropdown(dropdown: AutoCompleteTextView,
                   context: Context, items: Array<String>, onTextChangeListener: ((CharSequence?, Int, Int, Int) -> Unit)? = null) {

    dropdown.setupDropdownAdapter(
        context,
        R.layout.dropdown_item,
        items
    )

    dropdown.addTextChangedListener(object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //no implementation
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChangeListener?.let { it(s, start, before, count) }
        }

        override fun afterTextChanged(s: Editable?) {
           //no implementation
        }

    })
}

fun updateFilterView(container: ConstraintLayout, button: MaterialButton, isToggled: Boolean) {
    if (isToggled) {
        container.visibility = View.VISIBLE
        button.setBackgroundResource(R.drawable.filter_bg)
        button.icon = ContextCompat.getDrawable(button.context, R.drawable.ic_filter_hide)
    } else {
        container.visibility = View.GONE
        button.setBackgroundResource(R.drawable.filter_bg)
        button.icon = ContextCompat.getDrawable(button.context, R.drawable.ic_filter_show)
    }
}
fun populateYear(): List<String> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return mutableListOf("All").apply {
        addAll((1970..currentYear).map { it.toString() })
    }.sortedDescending()
}

fun populateMonth(selectedYear: String): MutableList<String> {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, selectedYear.toInt())

    val months = mutableListOf("All")

    for (i in 0 until 12) {
        calendar.set(Calendar.MONTH, i)
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) ?: ""
        months.add(monthName)
    }

    return months
}

fun getMonthNumber(monthName: String): Int {
    val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    return months.indexOfFirst { it.equals(monthName, ignoreCase = true) }
}

fun populateDays(selectedYear: String, selectedMonth: String): MutableList<String> {
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

    return days
}

