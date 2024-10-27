package com.example.dicodingevent.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.preferences.SettingPreferences
import com.example.dicodingevent.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingPreferences = SettingPreferences(requireContext())
        val factory = ViewModelFactory(settingPreferences)
        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        // Setup Switch for theme
        binding.switchTheme.isChecked = viewModel.isDarkMode.value ?: false

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDarkMode(isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Observe changes in the dark mode setting
        viewModel.isDarkMode.observe(viewLifecycleOwner) { isDarkMode ->
            binding.switchTheme.isChecked = isDarkMode
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
