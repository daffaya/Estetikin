package com.codegeniuses.estetikin.ui.setting

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.databinding.ActivitySettingsBinding
import com.codegeniuses.estetikin.ui.authentication.AuthActivity
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var tvUiSelected: TextView
    private val settingViewModel: SettingViewModel by viewModels()
    private lateinit var preferences: UserPreference
    private lateinit var tvSelectedPreference: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupAction()
        updateSelectedPreferences()
        binding.ivBackButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = getString(R.string.title_setting)
    }

    private fun setupAction() {
        preferences = UserPreference(this)

        val sharedPreferences =
            getSharedPreferences("settings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("language", "en") // Default to English if not found

        val languageTextResId = if (languageCode == "en") {
            R.string.english
        } else {
            R.string.bahasa_indonesia
        }

        binding.tvLanguageSelected.text = getString(languageTextResId)


        val language = binding.languageItem
        language.setOnClickListener {
            showLanguageBottomSheet()
        }

        val uiMode = binding.uiItem
        tvUiSelected = binding.tvUiSelected
        uiMode.setOnClickListener {
            showUiBottomSheet()
        }
        checkUiSelected()

        tvSelectedPreference = binding.tvPreferencesSelected
        binding.preferencesItem.setOnClickListener {
            showPreferencesBottomSheet()
        }

        binding.logoutItem.setOnClickListener {
            val pref = UserPreference(this)
            pref.clearPreferences()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showLanguageBottomSheet() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_bottom_language)

        val english = dialog.findViewById<TextView>(R.id.english)
        val indonesian = dialog.findViewById<TextView>(R.id.bahasa_indonesia)

        english.setOnClickListener {
            changeLanguage("en")
            binding.tvLanguageSelected
            dialog.dismiss()
            preferences.setLanguage("en")
            Toast.makeText(this, "English is Clicked", Toast.LENGTH_SHORT).show()
        }

        indonesian.setOnClickListener {
            changeLanguage("id")
            dialog.dismiss()
            preferences.setLanguage("id")
            Toast.makeText(this, "Bahasa Indonesia is Clicked", Toast.LENGTH_SHORT)
                .show()
        }

        dialog.show()
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.Bottom_Sheet_Animation
            setGravity(Gravity.BOTTOM)
        }
    }

    private fun changeLanguage(languageCode: String) {
        val sharedPreferences =
            getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language", languageCode)
        editor.apply()

        val locale = Locale(languageCode)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        // Update the text and background of btnLanguageSelected based on the selected language
        val languageTextResId = if (languageCode == "en") {
            R.string.english
        } else {
            R.string.bahasa_indonesia
        }
        binding.tvLanguageSelected.setText(languageTextResId)


        recreate()
    }

    private fun showUiBottomSheet() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_ui_mode)

        val light = dialog.findViewById<TextView>(R.id.light_mode)
        val dark = dialog.findViewById<TextView>(R.id.dark_mode)

        light.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            tvUiSelected.setText(R.string.light_theme)
            dialog.dismiss()
            preferences.setTheme("Light")
            Toast.makeText(this, "Light Mode is Clicked", Toast.LENGTH_SHORT).show()
        }

        dark.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            tvUiSelected.setText(R.string.dark_theme)
            dialog.dismiss()
            preferences.setTheme("Dark")
            Toast.makeText(this, "Dark Mode is Clicked", Toast.LENGTH_SHORT).show()
        }

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.Bottom_Sheet_Animation
            setGravity(Gravity.BOTTOM)
        }

        dialog.show()
    }

    private fun checkUiSelected() {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        val uiModeTextResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.string.dark_theme
        } else {
            R.string.light_theme
        }
        tvUiSelected.setText(uiModeTextResId)

        val uiModeIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_dark_mode
        } else {
            R.drawable.ic_light_mode
        }
        binding.ivUi.setImageResource(uiModeIconResId)


        val languageIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_language_light
        } else {
            R.drawable.ic_language
        }
        binding.ivLanguage.setImageResource(languageIconResId)

        val preferencesIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_preferences_light
        } else {
            R.drawable.ic_preferences
        }
        binding.ivPreferences.setImageResource(preferencesIconResId)

        val logoutIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_logout_light
        } else {
            R.drawable.ic_logout
        }
        binding.ivLogout.setImageResource(logoutIconResId)
    }

    private fun showPreferencesBottomSheet() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_preferences)

        val ios = dialog.findViewById<TextView>(R.id.ios)
        val android = dialog.findViewById<TextView>(R.id.android)
        val dslr = dialog.findViewById<TextView>(R.id.dslr)
        val video = dialog.findViewById<TextView>(R.id.video)

        ios.setOnClickListener {
            saveUserPreference("ios")
            tvSelectedPreference.text = getString(R.string.ios)
            dialog.dismiss()
            makePrefText("IOS")
            updateSelectedPreferences()
        }

        android.setOnClickListener {
            saveUserPreference("android")
            tvSelectedPreference.setText(R.string.android)
            dialog.dismiss()
            makePrefText("Android")
            updateSelectedPreferences()
        }

        dslr.setOnClickListener {
            saveUserPreference("dslr")
            tvSelectedPreference.setText(R.string.dslr)
            dialog.dismiss()
            makePrefText("DSLR")
            updateSelectedPreferences()
        }

        video.setOnClickListener {
            saveUserPreference("video")
            tvSelectedPreference.setText(R.string.video)
            dialog.dismiss()
            makePrefText("Video")
            updateSelectedPreferences()
        }


        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.Bottom_Sheet_Animation
            setGravity(Gravity.BOTTOM)
        }

        dialog.show()
    }

    private fun updateSelectedPreferences() {
        val selectedPreference = preferences.getUserPreference()
        tvSelectedPreference.text = selectedPreference?.let { getPreferenceText(it) }
    }

    private fun getPreferenceText(preference: String): String {
        return when (preference) {
            "ios" -> getString(R.string.ios)
            "android" -> getString(R.string.android)
            "dslr" -> getString(R.string.dslr)
            "video" -> getString(R.string.video)
            else -> ""
        }
    }
    private fun makePrefText(text: String) {
        Toast.makeText(this@SettingsActivity, "You choose $text as your preference", Toast.LENGTH_SHORT).show()
    }

    private fun saveUserPreference(data: String) {
        val pref = UserPreference(this)
        pref.saveUserPreference(data)
    }

}
