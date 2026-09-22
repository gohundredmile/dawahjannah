package com.example.util

import android.hardware.GeomagneticField
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Astronomical and geodesic helper for calculating the exact direction and distance
 * to the Holy Kaaba in Makkah al-Mukarramah.
 */
object QiblaHelper {
    // Holy Kaaba, Masjid al-Haram, Makkah coordinates
    const val KAABA_LATITUDE = 21.422487
    const val KAABA_LONGITUDE = 39.826206
    const val KAABA_ALTITUDE_METERS = 277.0

    /**
     * Calculates the true forward azimuth (bearing) from user's location to the Holy Kaaba in degrees (0..360).
     * 0° = True North, 90° = East, 180° = South, 270° = West.
     */
    fun calculateQiblaBearing(userLat: Double, userLng: Double): Float {
        val userLatRad = Math.toRadians(userLat)
        val userLngRad = Math.toRadians(userLng)
        val kaabaLatRad = Math.toRadians(KAABA_LATITUDE)
        val kaabaLngRad = Math.toRadians(KAABA_LONGITUDE)

        val deltaLng = kaabaLngRad - userLngRad

        val y = sin(deltaLng) * cos(kaabaLatRad)
        val x = cos(userLatRad) * sin(kaabaLatRad) -
                sin(userLatRad) * cos(kaabaLatRad) * cos(deltaLng)

        val bearingRad = atan2(y, x)
        val bearingDeg = Math.toDegrees(bearingRad)

        return ((bearingDeg + 360.0) % 360.0).toFloat()
    }

    /**
     * Calculates the great-circle distance from user's location to the Holy Kaaba in kilometers.
     */
    fun calculateDistanceToKaabaKm(userLat: Double, userLng: Double): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(KAABA_LATITUDE - userLat)
        val dLng = Math.toRadians(KAABA_LONGITUDE - userLng)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(userLat)) * cos(Math.toRadians(KAABA_LATITUDE)) *
                sin(dLng / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusKm * c
    }

    /**
     * Calculates the magnetic declination in degrees for a given location and time.
     * Adding this to a magnetic heading converts it to True North heading.
     */
    fun getMagneticDeclination(userLat: Double, userLng: Double, altitudeMeters: Float = 0f): Float {
        return try {
            val geoField = GeomagneticField(
                userLat.toFloat(),
                userLng.toFloat(),
                altitudeMeters,
                System.currentTimeMillis()
            )
            geoField.declination
        } catch (_: Exception) {
            0f
        }
    }

    /**
     * Calculates the shortest signed relative angle from current device heading to Qibla bearing (-180..+180).
     * Positive = Turn Right (Clockwise)
     * Negative = Turn Left (Counter-clockwise)
     */
    fun calculateRelativeAngle(currentHeading: Float, qiblaBearing: Float): Float {
        var diff = (qiblaBearing - currentHeading) % 360f
        if (diff > 180f) diff -= 360f
        if (diff < -180f) diff += 360f
        return diff
    }

    /**
     * Returns Bengali compass direction name based on degrees.
     */
    fun getDirectionBn(degrees: Float): String {
        val deg = (degrees + 360f) % 360f
        return when {
            deg in 348.75f..360f || deg in 0f..<11.25f -> "উত্তর (N)"
            deg in 11.25f..<33.75f -> "উত্তর-উত্তর-পূর্ব (NNE)"
            deg in 33.75f..<56.25f -> "উত্তর-পূর্ব (NE)"
            deg in 56.25f..<78.75f -> "পূর্ব-উত্তর-পূর্ব (ENE)"
            deg in 78.75f..<101.25f -> "পূর্ব (E)"
            deg in 101.25f..<123.75f -> "পূর্ব-দক্ষিণ-পূর্ব (ESE)"
            deg in 123.75f..<146.25f -> "দক্ষিণ-পূর্ব (SE)"
            deg in 146.25f..<168.75f -> "দক্ষিণ-দক্ষিণ-পূর্ব (SSE)"
            deg in 168.75f..<191.25f -> "দক্ষিণ (S)"
            deg in 191.25f..<213.75f -> "দক্ষিণ-দক্ষিণ-পশ্চিম (SSW)"
            deg in 213.75f..<236.25f -> "দক্ষিণ-পশ্চিম (SW)"
            deg in 236.25f..<258.75f -> "পশ্চিম-দক্ষিণ-পশ্চিম (WSW)"
            deg in 258.75f..<281.25f -> "পশ্চিম (W)"
            deg in 281.25f..<303.75f -> "পশ্চিম-উত্তর-পশ্চিম (WNW)"
            deg in 303.75f..<326.25f -> "উত্তর-পশ্চিম (NW)"
            else -> "উত্তর-উত্তর-পশ্চিম (NNW)"
        }
    }

    /**
     * Converts English numbers to Bengali numerals string.
     */
    fun toBengaliDigits(number: Any): String {
        val bengaliDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        val str = number.toString()
        val sb = StringBuilder()
        for (ch in str) {
            if (ch in '0'..'9') {
                sb.append(bengaliDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    /**
     * Smooth angle interpolation handling 0/360 wrap-around without spinning backwards.
     */
    fun interpolateDegrees(from: Float, to: Float, factor: Float): Float {
        var diff = (to - from) % 360f
        if (diff > 180f) diff -= 360f
        if (diff < -180f) diff += 360f
        return (from + diff * factor + 360f) % 360f
    }
}
