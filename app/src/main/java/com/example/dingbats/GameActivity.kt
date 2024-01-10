package com.example.dingbats

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.iterator
import androidx.core.view.marginLeft
import androidx.navigation.fragment.findNavController
import com.example.dingbats.databinding.FragmentGameBinding


class GameActivity : Fragment() {
    private var _binding: FragmentGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var letterIndex = 0;

    private var letterTextViews : MutableList<TextView> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameBinding .inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        for (row in binding.keyboardRowLayout.children) {
            for (button in (row as ConstraintLayout).children) {
                if (button is Button) {
                    button.setOnClickListener {
                        if (letterIndex < letterTextViews.size ) {
                            val textView  = letterTextViews[letterIndex]
                            textView.text = button.text;
                            letterIndex++;
                        }
                    }
                }
            }
        }

        val answer = getString(R.string.question_1)
        generateDashes(answer)

        binding.backspaceBtn.setOnClickListener {

                letterTextViews[letterIndex].text = "";
                if (letterIndex > 0) {
                letterIndex--
            }
        }

        binding.clearBtn.setOnClickListener {


                for (textViewIndex in 0..letterIndex) {
                    letterTextViews[textViewIndex].setText("")
                }
            if (letterIndex > 0) {
                letterIndex = 0;

            }
        }

        super.onViewCreated(view, savedInstanceState)

    }

    private fun generateDashes(answer : String) {

        val words = answer.split(' ');
        val row = LinearLayout(context)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER_HORIZONTAL
        binding.dashRows.addView(row)
        row.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        for (wordIndex in 0 until words.size) {
            val word = words[wordIndex]
            val wordLayout = LinearLayout(context)
            if (wordIndex != 0) {
                wordLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                (wordLayout.layoutParams as MarginLayoutParams).leftMargin = 70

            }
            for (charIndex in 0 until word.length) {
                val letterTextView = TextView(context)
                val layoutParams : ViewGroup.LayoutParams = MarginLayoutParams(55, ViewGroup.LayoutParams.WRAP_CONTENT)
                letterTextView.gravity = Gravity.CENTER

                letterTextView.layoutParams = layoutParams

                if (charIndex != 0) {
                    (letterTextView.layoutParams as MarginLayoutParams).leftMargin = 9
                }
                letterTextView.setTextSize(30F)
                letterTextView.setTextColor(resources.getColor(R.color.black))
                letterTextView.background = context?.let { AppCompatResources.getDrawable(it, R.drawable.line) }
                letterTextViews.add(letterTextView)
                wordLayout.addView(letterTextView)
            }
            row.addView(wordLayout)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}