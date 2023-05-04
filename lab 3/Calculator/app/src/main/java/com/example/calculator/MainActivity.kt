package com.example.calculator

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.calculator.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    lateinit var binding: ActivityMainBinding

    private val numsAndOps = mutableListOf<String>()
    private var isNewNum = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onOpsClick(view: View) {
        if (view is Button) {
            numsAndOps.add(binding.numberDisplay.text.toString().replace(",", "."))
            numsAndOps.add(view.text.toString())
            isNewNum = true
        }
    }

    fun zeroDivisionAlert(view: View){
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(getString(R.string.zero_division_alert_title))
            setMessage(getString(R.string.zero_division_alert_msg))
            setPositiveButton("OK", null)
            show()
        }
    }

    fun onNumberClick(view: View) {
        if (view is Button) {
            val currNum = binding.numberDisplay.text.toString()
            if (isNewNum) {
                if (numsAndOps.size > 0 && view.text == "0" && numsAndOps.last() == "/") {
                    zeroDivisionAlert(view)
                } else {
                    binding.numberDisplay.text = view.text
                    isNewNum = false
                }
            } else {
                if (currNum == "0") {
                    binding.numberDisplay.text = view.text
                } else {
                    binding.numberDisplay.text = currNum.plus(view.text)
                }
            }

        }
    }

    fun onFuzzyClick(view: View) {
        if (view is Button) {
            val currNum = binding.numberDisplay.text.toString()
            if (!currNum.contains(',')) {
                binding.numberDisplay.text = currNum.plus(",")
            }
        }
    }

    fun onClearAllClick(view: View) {
        if (view is Button) {
            binding.numberDisplay.text = "0"
            numsAndOps.clear()
        }
    }

    fun onChangeSignClick(view: View) {
        if (view is Button) {
            val currNum = binding.numberDisplay.text
            if (currNum != "0") {
                val sign = currNum.first()
                if (sign == '-') {
                    binding.numberDisplay.text = currNum.drop(1)
                } else {
                    binding.numberDisplay.text = "-".plus(currNum)
                }
            }
        }
    }

    fun onRemoveClick(view: View) {
        if (view is Button) {
            val removedLastNum = binding.numberDisplay.text.dropLast(1)
            if (removedLastNum == "")
                binding.numberDisplay.text = "0"
            else
                binding.numberDisplay.text = removedLastNum
        }
    }

    fun onCalcResultClick(view: View) {
        if (view is Button) {
            if (numsAndOps.last() in listOf("-", "+", "X", "/")) {
                numsAndOps.add(binding.numberDisplay.text.toString().replace(",", "."))
            }
            var resOfMulAndDiv = mutableListOf<String>()

            var currRes = 0.0
            var isOp = false
            for(i in 0 until  numsAndOps.size - 1  step 2) {
                if (numsAndOps[i + 1] == "X") {
                    if (!isOp) {
                        currRes = numsAndOps[i].toDouble() * numsAndOps[i + 2].toDouble()
                        isOp = true
                    } else {
                        currRes *= numsAndOps[i + 2].toDouble()
                    }
                } else if (numsAndOps[i + 1] == "/") {
                    if (!isOp) {
                        currRes = numsAndOps[i].toDouble() / numsAndOps[i + 2].toDouble()
                        isOp = true
                    } else {
                        currRes /= numsAndOps[i + 2].toDouble()
                    }
                } else {
                    if (isOp) {
                        resOfMulAndDiv.add(currRes.toString())
                        resOfMulAndDiv.add(numsAndOps[i + 1])
                        isOp = true
                    } else {
                        resOfMulAndDiv.add(numsAndOps[i])
                        resOfMulAndDiv.add(numsAndOps[i + 1])
                    }
                }
            }

            if (isOp) {
                resOfMulAndDiv.add(currRes.toString())
            } else {
                resOfMulAndDiv.add(numsAndOps.last())
            }

            var res = 0.0
            var isBegin = true
            for(i in 0 until  resOfMulAndDiv.size - 1 step 2) {
                if (isBegin) {
                    res = resOfMulAndDiv[i].toDouble()
                    isBegin = false
                }
                if (resOfMulAndDiv[i + 1] == "+") {
                    res += resOfMulAndDiv[i + 2].toDouble()
                } else if (resOfMulAndDiv[i + 1] == "-") {
                    res -= resOfMulAndDiv[i + 2].toDouble()
                }
            }

            if (resOfMulAndDiv.size == 1)
                res = resOfMulAndDiv.first().toDouble()

            val format = DecimalFormat("#.########")
            binding.numberDisplay.text = format.format(res).replace(".", ",")
            numsAndOps.clear()
        }
    }

}
