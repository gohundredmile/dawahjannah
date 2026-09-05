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
    assertEquals(5, fonts.size)
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
    val sep = CalendarMonthProvider.getGregorianMonth(8, 2026, 4)
    assertEquals("September", sep.monthName)
    assertEquals(2026, sep.year)
    assertTrue(sep.seasonTitle.contains("Autumn"))
    val today = sep.days.filterNotNull().find { it.isToday }
    assertNotNull(today)
    assertEquals(4, today?.dayNumber)
  }

  @Test
  fun bengaliMonth_bhadra1433_calculatedCorrectly() {
    val bhadra = CalendarMonthProvider.getBengaliMonth(4, 1433, 20)
    assertEquals("ভাদ্র", bhadra.monthNameBn)
    assertTrue(bhadra.seasonTitle.contains("শরৎকাল"))
    val today = bhadra.days.filterNotNull().find { it.isToday }
    assertNotNull(today)
    assertEquals("২০", today?.dayNumberBn)
    assertEquals(4, today?.gregorianDayNumber) // 4 September
  }

  @Test
  fun hijriMonth_rabiAlAwwal1448_calculatedCorrectly() {
    val rabi = CalendarMonthProvider.getHijriMonth(2, 1448, 22)
    assertEquals("Rabi' al-Awwal", rabi.monthNameEn)
    assertTrue(rabi.eventTitle.contains("Rabi' al-Awwal"))
    val today = rabi.days.filterNotNull().find { it.isToday }
    assertNotNull(today)
    assertEquals(22, today?.hijriDayEng)
    assertEquals("4 Sep", today?.gregorianSubDate)
  }
}

