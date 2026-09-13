package com.example

import com.example.data.model.BanglaFont
import com.example.data.model.BanglaFontWeight
import com.example.util.CalendarMonthProvider
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun banglaFonts_haveStandardWebFonts() {
    val fonts = BanglaFont.entries
    assertEquals(10, fonts.size)
    assertTrue(fonts.any { it.googleFontName == "Anek Bangla" })
    assertTrue(fonts.any { it.googleFontName == "Noto Sans Bengali" })
    assertTrue(fonts.any { it.googleFontName == "Tiro Bangla" })
    assertTrue(fonts.any { it.googleFontName == "Hind Siliguri" })
    assertTrue(fonts.any { it.id == "system_default" })
  }

  @Test
  fun banglaFontWeight_mapsCorrectly() {
    assertEquals(BanglaFontWeight.THIN, BanglaFontWeight.fromValue(100))
    assertEquals(BanglaFontWeight.NORMAL, BanglaFontWeight.fromValue(400))
    assertEquals(BanglaFontWeight.BOLD, BanglaFontWeight.fromValue(700))
    assertEquals(BanglaFontWeight.BLACK, BanglaFontWeight.fromValue(900))
  }

  @Test
  fun gregorianMonth_september2026_calculatedCorrectly() {
    val testCal = java.util.Calendar.getInstance().apply {
      set(java.util.Calendar.YEAR, 2026)
      set(java.util.Calendar.MONTH, 8)
      set(java.util.Calendar.DAY_OF_MONTH, 4)
    }
    val sep = CalendarMonthProvider.getGregorianMonth(8, 2026, testCal)
    assertEquals("September", sep.monthName)
    assertEquals(2026, sep.year)
    assertTrue(sep.seasonTitle.contains("Autumn"))
    val today = sep.days.filterNotNull().find { it.isToday }
    assertNotNull(today)
    assertEquals(4, today?.dayNumber)
  }

  @Test
  fun bengaliMonth_bhadra1433_calculatedCorrectly() {
    val testCal = java.util.Calendar.getInstance().apply {
      set(java.util.Calendar.YEAR, 2026)
      set(java.util.Calendar.MONTH, 8)
      set(java.util.Calendar.DAY_OF_MONTH, 4)
    }
    val bhadra = CalendarMonthProvider.getBengaliMonth(4, 1433, testCal)
    assertEquals("ভাদ্র", bhadra.monthNameBn)
    assertTrue(bhadra.seasonTitle.contains("শরৎকাল"))
    val today = bhadra.days.filterNotNull().find { it.isToday }
    assertNotNull(today)
    assertEquals("২০", today?.dayNumberBn)
    assertEquals(4, today?.gregorianDayNumber) // 4 September
  }

  @Test
  fun hijriMonth_rabiAlAwwal1448_calculatedCorrectly() {
    val testCal = java.util.Calendar.getInstance().apply {
      set(java.util.Calendar.YEAR, 2026)
      set(java.util.Calendar.MONTH, 8)
      set(java.util.Calendar.DAY_OF_MONTH, 4)
    }
    val rabi = CalendarMonthProvider.getHijriMonth(2, 1448, testCal)
    assertEquals("Rabi' al-Awwal", rabi.monthNameEn)
    assertTrue(rabi.eventTitle.contains("Rabi' al-Awwal"))
    val today = rabi.days.filterNotNull().find { it.isToday }
    assertNotNull(today)
  }

  @Test
  fun duaAcceptanceTimes_dataConfiguredCorrectly() {
    val section = com.example.data.datasource.DuaAcceptanceTimesData.section
    assertEquals("dua_acceptance_times", section.id)
    assertEquals("★দোয়া কবুল হওয়ার সময় ও দোয়া★", section.titleBn)
    assertTrue(section.items.size >= 30)
    assertTrue(section.items.any { it.titleBn.contains("২৯টি স্থান, ক্ষেত্র ও সময়") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা ফাতিহা ও সূরা বাকারার শেষ দুই আয়াত") })
  }

  @Test
  fun fajrAmolData_configuredCorrectly() {
    val section = com.example.data.datasource.FajrAmolData.section
    assertEquals("fajr_between_and_after", section.id)
    assertEquals("★★★ফযর নামাজের মাঝে ও পরের আমল সমুহ★★★", section.titleBn)
    assertEquals(17, section.items.size)
    assertTrue(section.items.any { it.titleBn.contains("ফজরের সুন্নত ও ফরজের") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা আল ইমরান ২৬-২৭") })
    assertTrue(section.items.any { it.titleBn.contains("ধনী হওয়ার আমল") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা হাশরের শেষ তিন আয়াত") })
    assertTrue(section.items.any { it.titleBn.contains("উপকারী জ্ঞান") })
    assertTrue(section.items.any { it.titleBn.contains("রিজিক বৃদ্ধির পরীক্ষিত আমল") })
  }
}

