package com.example.dingbats

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.util.TypedValue

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController

import com.example.dingbats.databinding.FragmentPuzzleMenuBinding


class PuzzleMenuFragment : Fragment() {
    private var _binding: FragmentPuzzleMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPuzzleMenuBinding .inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val phrases = resources.getStringArray(R.array.phrases)
        populatePuzzles(phrases.asList())
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref != null) {
            var gameCompleted = true;
            var index = 0;
            while (index < phrases.size && gameCompleted) {
                gameCompleted = sharedPref.getBoolean("question_${index}_completed", false)
                index++
            }
            if (gameCompleted) {
                binding.gameCompletedMessage.text = "Thank you for playing"
            }
        }

    }


    private fun populatePuzzles(phrases: List<String>) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        var previousButtonId : Int? = null
        var topId = binding.phrasePuzzlesConstraintLayout.id
        for (i in phrases.indices) {
            val puzzleButton = Button(context)
            puzzleButton.text = (i + 1).toString()
            puzzleButton.id = View.generateViewId()
            puzzleButton.background = ResourcesCompat.getDrawable(requireContext().resources, R.drawable.rounded_corner,
                requireContext().theme
            )
            if (sharedPref != null && sharedPref.getBoolean("question_${i}_completed", false)) {
                puzzleButton.background.mutate().setTint(resources.getColor(R.color.green))
            }
            puzzleButton.setPadding(0, 0, 0,0)
            puzzleButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)

            var layoutParams = ConstraintLayout.LayoutParams(100, 100)

            if (i % 6 == 0 && previousButtonId != null) {
                topId = previousButtonId
                previousButtonId = null
            }

            if (topId == binding.phrasePuzzlesConstraintLayout.id) {
                layoutParams.topToTop = topId
            } else {
                layoutParams.topMargin = 10;
                layoutParams.topToBottom = topId
            }


            if (previousButtonId == null) {
                layoutParams.startToStart = binding.phrasePuzzlesConstraintLayout.id
            } else {
                layoutParams.startToEnd = previousButtonId
                layoutParams.leftMargin = 8;
            }
            puzzleButton.layoutParams = layoutParams
            previousButtonId = puzzleButton.id

            puzzleButton.setOnClickListener {
                findNavController().navigate(PuzzleMenuFragmentDirections.actionPuzzleMenuFragmentToGameFragment(i))
            }
            binding.phrasePuzzlesConstraintLayout.addView(puzzleButton)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}