package com.abdok.atmosphere.Utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.SystemClock
import android.util.Log
import com.abdok.atmosphere.BroadcastReceivers.AlarmReceiver
import com.abdok.atmosphere.Data.Models.ForecastResponse
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.abs

fun ForecastResponse.getDaysForecast(): Map<Int, List<ForecastResponse.Item0>> {
    val forecastMap = mutableMapOf<Int, MutableList<ForecastResponse.Item0>>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    for (item in list) {
        val dateKey = dateFormat.format(Date(item.dt * 1000L)).replace("-", "").toInt()
        if (forecastMap[dateKey] == null) {
            forecastMap[dateKey] = mutableListOf()
        }
        forecastMap[dateKey]?.add(item)
    }
    return forecastMap.mapValues { it.value.take(8) }
}


@SuppressLint("ScheduleExactAlarm")
fun Context.setAlarm(seconds: Int , id: Int) {
    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AlarmReceiver::class.java)
        .putExtra(Constants.ALARM_ID,id)
    val pendingIntent = PendingIntent.getBroadcast(
        this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    val triggerTime = SystemClock.elapsedRealtime() + (seconds * 1000)
    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)

}

fun Context.cancelAlarm(id: Int) {
    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AlarmReceiver::class.java)
        .putExtra(Constants.ALARM_ID, id)

    val pendingIntent = PendingIntent.getBroadcast(
        this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.cancel(pendingIntent)
}

fun String.convertArabicToEnglish(): String {
    val arabicNumbers = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    val englishNumbers = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    return this.map { char ->
        val index = arabicNumbers.indexOf(char)
        if (index != -1) englishNumbers[index] else char
    }.joinToString("")
}

fun String.convertNumbersBasedOnLanguage(): String {
    val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    return when (Locale.getDefault().language) {
        "ar" -> this.map {
            if (it.isDigit()) arabicDigits[it.digitToInt()] else it
        }.joinToString("")
        else -> this.map {
            val index = arabicDigits.indexOf(it)
            if (index != -1) englishDigits[index] else it
        }.joinToString("")
    }
}

fun String.durationFromNowInSeconds(): Int {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalTime.parse(this.convertArabicToEnglish(), formatter)
    val now = LocalTime.now()

    return abs(Duration.between(now, time).seconds).toInt()
}

fun String.translateWeatherCondition(): String {
    val map = mapOf(
        "clear sky" to mapOf("ar" to "سماء صافية", "es" to "Cielo despejado"),
        "few clouds" to mapOf("ar" to "سحب قليلة", "es" to "Pocas nubes"),
        "scattered clouds" to mapOf("ar" to "سحب متناثرة", "es" to "Nubes dispersas"),
        "broken clouds" to mapOf("ar" to "سحب متقطعة", "es" to "Nubes rotas"),
        "shower rain" to mapOf("ar" to "مطر غزير", "es" to "Lluvia intensa"),
        "rain" to mapOf("ar" to "مطر", "es" to "Lluvia"),
        "thunderstorm" to mapOf("ar" to "عاصفة رعدية", "es" to "Tormenta"),
        "snow" to mapOf("ar" to "ثلج", "es" to "Nieve"),
        "mist" to mapOf("ar" to "ضباب", "es" to "Niebla"),
        "light rain" to mapOf("ar" to "مطر خفيف", "es" to "Lluvia ligera"),
        "moderate rain" to mapOf("ar" to "مطر معتدل", "es" to "Lluvia moderada"),
        "heavy intensity rain" to mapOf("ar" to "مطر غزير", "es" to "Lluvia de intensidad fuerte"),
        "very heavy rain" to mapOf("ar" to "مطر شديد جدًا", "es" to "Lluvia muy intensa"),
        "extreme rain" to mapOf("ar" to "مطر شديد", "es" to "Lluvia extrema"),
        "freezing rain" to mapOf("ar" to "مطر متجمد", "es" to "Lluvia helada"),
        "light snow" to mapOf("ar" to "ثلج خفيف", "es" to "Nieve ligera"),
        "heavy snow" to mapOf("ar" to "ثلج كثيف", "es" to "Nieve intensa"),
        "sleet" to mapOf("ar" to "مطر ثلجي", "es" to "Aguanieve"),
        "shower sleet" to mapOf("ar" to "زخات مطر ثلجي", "es" to "Chubascos de aguanieve"),
        "light rain and snow" to mapOf("ar" to "مطر خفيف وثلج", "es" to "Lluvia ligera y nieve"),
        "rain and snow" to mapOf("ar" to "مطر وثلج", "es" to "Lluvia y nieve"),
        "light shower snow" to mapOf("ar" to "زخات ثلج خفيفة", "es" to "Chubascos de nieve ligera"),
        "heavy shower snow" to mapOf("ar" to "زخات ثلج كثيفة", "es" to "Chubascos de nieve intensa"),
        "fog" to mapOf("ar" to "ضباب كثيف", "es" to "Niebla"),
        "haze" to mapOf("ar" to "ضباب خفيف", "es" to "Bruma"),
        "dust" to mapOf("ar" to "غبار", "es" to "Polvo"),
        "sand" to mapOf("ar" to "رمال", "es" to "Arena"),
        "volcanic ash" to mapOf("ar" to "رماد بركاني", "es" to "Ceniza volcánica"),
        "squalls" to mapOf("ar" to "عواصف", "es" to "Chubascos"),
        "tornado" to mapOf("ar" to "إعصار", "es" to "Tornado")
    )

    val language = Locale.getDefault().language
    return map[this]?.get(language) ?: this
}

fun String.getWeatherNotification(): String {
    val notifications = mapOf(
        "01d" to mapOf(
            "en" to "Clear sky during the day! Enjoy the sunshine. ☀️",
            "ar" to "سماء صافية خلال النهار! استمتع بأشعة الشمس. ☀️",
            "es" to "Cielo despejado durante el día! Disfruta del sol. ☀️"
        ),
        "01n" to mapOf(
            "en" to "Clear night sky! Perfect for stargazing. 🌙",
            "ar" to "سماء صافية في الليل! مثالية لمشاهدة النجوم. 🌙",
            "es" to "Cielo nocturno despejado! Perfecto para observar las estrellas. 🌙"
        ),
        "02d" to mapOf(
            "en" to "A few clouds in the sky, but still a nice day! ⛅",
            "ar" to "بعض السحب في السماء، لكن الجو لا يزال جميلاً! ⛅",
            "es" to "Algunas nubes en el cielo, pero aún es un buen día! ⛅"
        ),
        "02n" to mapOf(
            "en" to "Partly cloudy night! Enjoy the cool breeze. 🌌",
            "ar" to "ليلة غائمة جزئيًا! استمتع بالنسيم البارد. 🌌",
            "es" to "Noche parcialmente nublada! Disfruta de la brisa fresca. 🌌"
        ),
        "03d" to mapOf(
            "en" to "Scattered clouds today. ☁️",
            "ar" to "غيوم متفرقة اليوم. ☁️",
            "es" to "Nubes dispersas hoy. ☁️"
        ),
        "03n" to mapOf(
            "en" to "Scattered clouds at night. 🌥️",
            "ar" to "غيوم متفرقة في الليل. 🌥️",
            "es" to "Nubes dispersas por la noche. 🌥️"
        ),
        "04d" to mapOf(
            "en" to "Broken clouds covering the sky. 🌥️",
            "ar" to "غيوم متقطعة تغطي السماء. 🌥️",
            "es" to "Nubes rotas cubriendo el cielo. 🌥️"
        ),
        "04n" to mapOf(
            "en" to "Broken clouds tonight. Might feel chilly! 🌙",
            "ar" to "غيوم متقطعة الليلة. قد يكون الجو باردًا! 🌙",
            "es" to "Nubes rotas esta noche. ¡Podría hacer frío! 🌙"
        ),
        "09d" to mapOf(
            "en" to "Shower rain expected. Carry an umbrella! 🌧️",
            "ar" to "متوقع هطول أمطار غزيرة. احمل مظلة! 🌧️",
            "es" to "Se esperan lluvias. ¡Lleva un paraguas! 🌧️"
        ),
        "09n" to mapOf(
            "en" to "Shower rain at night. Stay warm! 🌧️",
            "ar" to "أمطار غزيرة في الليل. ابقَ دافئًا! 🌧️",
            "es" to "Lluvias nocturnas. ¡Mantente abrigado! 🌧️"
        ),
        "10d" to mapOf(
            "en" to "Rain expected during the day. Don't forget your raincoat! ☔",
            "ar" to "من المتوقع هطول أمطار خلال النهار. لا تنس معطف المطر! ☔",
            "es" to "Se espera lluvia durante el día. ¡No olvides tu impermeable! ☔"
        ),
        "10n" to mapOf(
            "en" to "Rainy night ahead. Stay dry! ☔",
            "ar" to "ليلة ماطرة قادمة. ابقَ جافًا! ☔",
            "es" to "Noche lluviosa por delante. ¡Mantente seco! ☔"
        ),
        "11d" to mapOf(
            "en" to "Thunderstorm alert! Be careful. ⛈️",
            "ar" to "تحذير من عاصفة رعدية! كن حذرًا. ⛈️",
            "es" to "¡Alerta de tormenta! Ten cuidado. ⛈️"
        ),
        "11n" to mapOf(
            "en" to "Thunderstorm at night. Avoid going outside! ⛈️",
            "ar" to "عاصفة رعدية في الليل. تجنب الخروج! ⛈️",
            "es" to "Tormenta nocturna. ¡Evita salir! ⛈️"
        ),
        "13d" to mapOf(
            "en" to "Snowfall expected! ❄️",
            "ar" to "من المتوقع تساقط الثلوج! ❄️",
            "es" to "¡Se espera nieve! ❄️"
        ),
        "13n" to mapOf(
            "en" to "Snowy night ahead. Stay warm! ❄️",
            "ar" to "ليلة ثلجية قادمة. ابقَ دافئًا! ❄️",
            "es" to "Noche nevada por delante. ¡Abrígate! ❄️"
        ),
        "50d" to mapOf(
            "en" to "Misty weather today. Drive carefully! 🌫️",
            "ar" to "الطقس ضبابي اليوم. قد بحذر! 🌫️",
            "es" to "Tiempo brumoso hoy. ¡Conduce con cuidado! 🌫️"
        ),
        "50n" to mapOf(
            "en" to "Foggy night ahead. Watch your step! 🌫️",
            "ar" to "ليلة ضبابية قادمة. انتبه لخطواتك! 🌫️",
            "es" to "Noche con niebla por delante. ¡Mira por dónde pisas! 🌫️"
        )
    )

    val language = Locale.getDefault().language
    return notifications[this]?.get(language) ?: "Weather update not available."
}






@SuppressLint("MissingPermission")
fun Context.getGpsLocation(onLocationReceived: (location: Location) -> Unit){
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    fusedLocationProviderClient.lastLocation
        .addOnSuccessListener {
            Log.i("TAG", "getGpsLocation: ")
            if (it != null) {
                Log.i("TAG", "getGpsLocation: 111 ${it.latitude} ${it.longitude}")
                onLocationReceived(it)
            }
        }.addOnFailureListener {
            Log.i("TAG", "getGpsLocation: 222 ${it.message}")
            it.printStackTrace()
        }
}
















