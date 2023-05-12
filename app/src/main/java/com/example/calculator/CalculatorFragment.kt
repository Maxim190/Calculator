package com.example.calculator

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.calculator.databinding.FragmentCalculatorBinding

class CalculatorFragment : Fragment(R.layout.fragment_calculator) {

    private val binding by viewBinding(FragmentCalculatorBinding::bind)
    private val viewModel by viewModels<CalculatorViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtonsListeners(binding.buttonsContainer)
        initObservers()
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            binding.viewInput.text = it.inputValue
            binding.viewResult.text = it.expression
        }
    }

    private fun initButtonsListeners(container: ViewGroup) {
        container.children.forEach {
            if (it is ViewGroup) {
                initButtonsListeners(it)
            } else {
                it.setOnClickListener {
                    val symbol = if (it is ImageButton) {
                        Signs.CLEAR
                    } else {
                        (it as Button).text.first()
                    }
                    viewModel.buttonClicked(symbol)
                }
            }
        }
    }

}
