package com.example.find_integer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var etNumberInput: EditText
    private lateinit var rgNumberType: RadioGroup
    private lateinit var lvResults: ListView
    private lateinit var tvNoResult: TextView

    private val numberList: ArrayList<Int> by lazy { ArrayList() }
    private val adapter: ArrayAdapter<Int> by lazy {
        ArrayAdapter(this, android.R.layout.simple_list_item_1, numberList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNumberInput = findViewById(R.id.etNumberInput)
        rgNumberType = findViewById(R.id.rgNumberType)
        lvResults = findViewById(R.id.lvResults)
        tvNoResult = findViewById(R.id.tvNoResult)

        lvResults.adapter = adapter

        etNumberInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        rgNumberType.setOnCheckedChangeListener { _, _ -> updateList() }

        updateList()
    }

    private fun updateList() {
        val maxNumber = etNumberInput.text.toString().toIntOrNull()
        if (maxNumber == null) {
            clearResults()
            return
        }

        numberList.clear()
        val checkedId = rgNumberType.checkedRadioButtonId

        // Lọc danh sách số
        for (i in 0 until maxNumber) {
            val shouldAdd = when (checkedId) {
                R.id.rbAll -> true
                R.id.rbEven -> isEven(i)
                R.id.rbOdd -> isOdd(i)
                R.id.rbPrime -> isPrime(i)
                R.id.rbPerfect -> isPerfect(i)
                R.id.rbSquare -> isSquare(i)
                else -> false
            }
            if (shouldAdd) {
                numberList.add(i)
            }
        }


        if (numberList.isEmpty()) {
            lvResults.visibility = View.GONE
            tvNoResult.visibility = View.VISIBLE
        } else {
            lvResults.visibility = View.VISIBLE
            tvNoResult.visibility = View.GONE
        }

        adapter.notifyDataSetChanged()
    }

    private fun clearResults() {
        numberList.clear()
        adapter.notifyDataSetChanged()
        lvResults.visibility = View.VISIBLE
        tvNoResult.visibility = View.GONE
    }

    //  Các hàm kiểm tra số
    private fun isEven(n: Int): Boolean = n % 2 == 0

    private fun isOdd(n: Int): Boolean = n % 2 != 0

    private fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        for (i in 2..sqrt(n.toDouble()).toInt()) {
            if (n % i == 0) return false
        }
        return true
    }

    private fun isPerfect(n: Int): Boolean {
        if (n < 1) return false
        // Lọc và tính tổng các ước số thực sự
        val sum = (1 until n).filter { n % it == 0 }.sum()
        return sum == n
    }

    private fun isSquare(n: Int): Boolean {
        if (n < 0) return false
        val sqrt = sqrt(n.toDouble()).toInt()
        return sqrt * sqrt == n
    }
}
