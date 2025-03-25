package com.abdok.atmosphere.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import com.abdok.atmosphere.Enums.Languages
import com.abdok.atmosphere.MainActivity
import com.abdok.atmosphere.View.Screens.CurvedNavBar
import java.util.Locale

object LanguageManager {
    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        val dm = context.resources.displayMetrics
        val conf = context.resources.configuration
        conf.locale = locale
        context.resources.updateConfiguration(conf, dm)

        val refresh = Intent(context, MainActivity::class.java)
        refresh.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(refresh)
        (context as Activity).finish()
    }

    fun convertToArabicNumbers(number: String): String {
        val arabicDigits = arrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return number.map { if (it.isDigit()) arabicDigits[it.digitToInt()] else it }.joinToString("")
    }

    fun formatNumberBasedOnLanguage(number: String): String {
        val language = Locale.getDefault().language
        return if (language == Languages.ARABIC.code) convertToArabicNumbers(number) else number
    }
    fun restartActivity(context: Context) {
        val intent = (context as? Activity)?.intent
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        (context as? Activity)?.finish()
        CurvedNavBar.activeIndex.value = 0
    }

/*    private fun applyLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }*/

}