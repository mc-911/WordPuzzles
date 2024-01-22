package com.example.dingbats

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dingbats.databinding.FragmentGameBinding


class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var letterIndex = 0;

    private lateinit var answer : String

    private var letterTextViews : MutableList<TextView> = mutableListOf()

    private val args : GameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameBinding .inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        for (row in binding.keyboardRowLayout.children) {
            for (button in (row as ConstraintLayout).children) {
                if (button is Button) {
                    button.setOnClickListener {
                        if (letterIndex < letterTextViews.size ) {
                            val textView  = letterTextViews[letterIndex]
                            textView.text = button.text;
                            print(answer.get(letterIndex))
                            if (button.text[0] == answer.get(letterIndex)) {
                                textView.setTextColor(resources.getColor(R.color.green))
                                textView.background.setTint(resources.getColor(R.color.green))
                            }
                            letterIndex++;
                        }
                        if (letterIndex == letterTextViews.size) {
                            var sentence = ""
                            for (i in letterTextViews) {
                                sentence += i.text
                            }
                            if (sentence == answer) {
                                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                                if (sharedPref != null) {
                                    with (sharedPref.edit()) {
                                        putBoolean("question_${args.phraseIndex}_completed", true)
                                        apply()
                                    }
                                }
                                findNavController().navigate(GameFragmentDirections.actionGameFragmentToCompletionScreenFragment(args.phraseIndex))
                            }
                        }
                    }
                }
            }
        }

        val icon = resources.obtainTypedArray(R.array.phraseDrawables).getDrawable(args.phraseIndex)
        icon?.applyTheme(requireContext().theme)
        binding.questionImage.setImageDrawable(icon)
        val answer = resources.getStringArray(R.array.phrases).get(args.phraseIndex)
        this.answer = answer.replace(" ", "").replace("\n", "").uppercase()

        generateDashes(answer)

        binding.backspaceBtn.setOnClickListener {
            val typedValue = TypedValue()


            requireContext().theme.resolveAttribute(android.R.attr.textColor, typedValue, true)

            if (letterIndex > 0) {
                letterIndex--
            }
                letterTextViews[letterIndex].text = "";
            letterTextViews[letterIndex].setTextColor(Color.valueOf(typedValue.data).toArgb())
            letterTextViews[letterIndex].backgroundTintList = null
        }

        binding.clearBtn.setOnClickListener {
                val typedValue = TypedValue()


                requireContext().theme.resolveAttribute(android.R.attr.textColor, typedValue, true)

                for (textViewIndex in 0 until letterIndex) {
                    letterTextViews[textViewIndex].setText("")
                    letterTextViews[textViewIndex].setTextColor(Color.valueOf(typedValue.data).toArgb())
                    letterTextViews[textViewIndex].backgroundTintList = null
                }
            if (letterIndex > 0) {
                letterIndex = 0;
            }
        }

        super.onViewCreated(view, savedInstanceState)

    }

    private fun generateDashes(answer : String) {

        val lines = answer.split('\n')

        for (line in lines) {
            val words = line.split(" ")
            val row = LinearLayout(context)
            row.orientation = LinearLayout.HORIZONTAL
            row.gravity = Gravity.CENTER_HORIZONTAL
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
                    val layoutParams : ViewGroup.LayoutParams = MarginLayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT)
                    letterTextView.gravity = Gravity.CENTER

                    letterTextView.layoutParams = layoutParams

                    if (charIndex != 0) {
                        (letterTextView.layoutParams as MarginLayoutParams).leftMargin = 9
                    }
                    letterTextView.setTextSize(30F)
                    letterTextView.background = context?.let { AppCompatResources.getDrawable(it, R.drawable.line) }
                    letterTextView.backgroundTintList = null

                    letterTextViews.add(letterTextView)
                    wordLayout.addView(letterTextView)
                }
                row.addView(wordLayout)
            }
            binding.dashRows.addView(row)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}