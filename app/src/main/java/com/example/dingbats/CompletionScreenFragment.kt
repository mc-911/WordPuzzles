package com.example.dingbats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dingbats.databinding.FragmentCompletionScreenBinding

class CompletionScreenFragment : Fragment() {

    private var _binding: FragmentCompletionScreenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val args : CompletionScreenFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCompletionScreenBinding .inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.levelNumberTextView.text = (args.phraseIndex + 1).toString()
        if (args.phraseIndex + 1 < resources.getStringArray(R.array.phrases).size  ) {
            binding.nextButton.setOnClickListener {
                findNavController().navigate(        CompletionScreenFragmentDirections.actionCompletionScreenFragmentToGameFragment(args.phraseIndex + 1)
                )
            }
        } else {
            binding.nextButton.text = "Coming Soon"
        }

        binding.menuButton.setOnClickListener {
            findNavController().navigate(CompletionScreenFragmentDirections.actionCompletionScreenFragmentToPuzzleMenuFragment())
        }

    }
}