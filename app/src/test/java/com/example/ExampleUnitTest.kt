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
    assertEquals("★★★দোয়া কবুল হওয়ার সময় ও দোয়া★★★", section.titleBn)
    assertTrue(section.items.size >= 30)
    assertTrue(section.items.any { it.titleBn.contains("স্থান, ক্ষেত্র ও বরকতময় সময়") })
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

  @Test
  fun nightAmolSleepDuaData_configuredCorrectly() {
    val section = com.example.data.datasource.NightAmolSleepDuaData.section
    assertEquals("sleep_duas", section.id)
    assertEquals("★★★রাতের আমলঃ ঘুমানোর দোয়া সমুহ★★★", section.titleBn)
    assertEquals(33, section.items.size)
    assertTrue(section.items.any { it.titleBn.contains("সূরা আল-মুলক") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা আস-সাজদা") })
    assertTrue(section.items.any { it.titleBn.contains("আয়াতুল কুরসী") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা আল-বাকারার শেষ দুই আয়াত") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা আল-ইখলাস") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা আল-কাফিরুন") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা আল-ইসরা-র শেষ আয়াত ও সূরা আয-যুমার") })
    assertTrue(section.items.any { it.titleBn.contains("ঘুমানোর মূল দোয়া — ১") })
    assertTrue(section.items.any { it.titleBn.contains("ঘুমানোর মূল দোয়া — ২") })
    assertTrue(section.items.any { it.titleBn.contains("ঘুমানোর ব্যাপক দোয়া — ৪") })
    assertTrue(section.items.any { it.titleBn.contains("ঘুমানোর দোয়া — ৬") })
    assertTrue(section.items.any { it.titleBn.contains("তাসবীহ") })
    assertTrue(section.items.any { it.titleBn.contains("শরীরের সুরক্ষার পূর্ণাঙ্গ দোয়া ও বিছানা ঝাড়া") })
    assertTrue(section.items.any { it.titleBn.contains("ঘুম আসার দোয়া বা রাতে ঘুম না আসলে") })
    assertTrue(section.items.any { it.titleBn.contains("দুঃস্বপ্ন দেখলে করণীয় ৫টি মাসনূন সুন্নাত আমল") })
    assertTrue(section.items.any { it.titleBn.contains("১০টি গুরুত্বপূর্ণ সুন্নাত আমল ও আদব") })
    assertTrue(section.items.any { it.titleBn.contains("১৩টি ধাপের পূর্ণাঙ্গ রুটিন") })
    assertTrue(section.items.any { it.titleBn.contains("ঘুম থেকে ওঠার দোয়া — ১") })
    assertTrue(section.items.any { it.titleBn.contains("ঘুম থেকে ওঠার দোয়া — ২") })
    assertTrue(section.items.any { it.titleBn.contains("ছোট বাচ্চাদের ঘুমানোর দোয়া") })
    assertTrue(section.items.any { it.titleBn.contains("সর্বাধিক জিজ্ঞাসিত প্রশ্ন (FAQ)") })
  }

  @Test
  fun fridaySpecialDuaAmolData_configuredCorrectly() {
    val section = com.example.data.datasource.FridaySpecialDuaAmolData.section
    assertEquals("friday_special_duas", section.id)
    assertEquals("★★★শুক্রবারের বিশেষ দোয়া ও আমল★★★", section.titleBn)
    assertEquals(45, section.items.size)
    assertTrue(section.items.any { it.titleBn.contains("কুরআনে জুম্মার দিনের নির্দেশনা") })
    assertTrue(section.items.any { it.titleBn.contains("জুমার দিনের অপরিসীম মর্যাদা") })
    assertTrue(section.items.any { it.titleBn.contains("গোসল") })
    assertTrue(section.items.any { it.titleBn.contains("মসজিদে যাওয়া") || it.titleBn.contains("মসজিদে যাওয়ার") })
    assertTrue(section.items.any { it.titleBn.contains("সুগন্ধি") || it.titleBn.contains("পোশাক") })
    assertTrue(section.items.any { it.titleBn.contains("খুতবা চলাকালীন কঠোর আদব") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা আল-কাহাফ") })
    assertTrue(section.items.any { it.titleBn.contains("দরূদ") })
    assertTrue(section.items.any { it.titleBn.contains("সা'আতুল ইজাবাহ") || it.titleBn.contains("দো'আ কবুল") })
    assertTrue(section.items.any { it.titleBn.contains("সাইয়্যিদুল ইস্তিগফার") || it.titleBn.contains("সাইয়্যিদুল ইস্তিগফার") })
    assertTrue(section.items.any { it.titleBn.contains("সার্বিক কল্যাণ ও জাহান্নাম মুক্তির দো'আ") })
    assertTrue(section.items.any { it.titleBn.contains("জান্নাত") })
    assertTrue(section.items.any { it.titleBn.contains("হেদায়াত") || it.titleBn.contains("তাকওয়া") })
    assertTrue(section.items.any { it.titleBn.contains("উপকারী ইলম") || it.titleBn.contains("উপকারী জ্ঞান") })
    assertTrue(section.items.any { it.titleBn.contains("দুশ্চিন্তা") })
    assertTrue(section.items.any { it.titleBn.contains("ভিত্তিহীন") || it.titleBn.contains("সতর্কতা") })
    assertTrue(section.items.any { it.titleBn.contains("চেকলিস্ট") })
    assertTrue(section.items.any { it.titleBn.contains("FAQ") })
    // New additions (items 26-45)
    assertTrue(section.items.any { it.titleBn.contains("তাহিয়্যাতুল মসজিদ") })
    assertTrue(section.items.any { it.titleBn.contains("সালাতুত তাসবীহ") })
    assertTrue(section.items.any { it.titleBn.contains("সূরা আল-জুমু'আ") || it.titleBn.contains("সূরা আল-জুমু‘আ") })
    assertTrue(section.items.any { it.titleBn.contains("কালেমাতানে হাবিবাতানে") || it.titleBn.contains("মীযানের পাল্লায়") })
    assertTrue(section.items.any { it.titleBn.contains("লা হাওলা") || it.titleBn.contains("রত্নভাণ্ডার") })
    assertTrue(section.items.any { it.titleBn.contains("পিতা-মাতার") })
    assertTrue(section.items.any { it.titleBn.contains("শিরক") })

    // Verify presence in IslamicLifeData
    val registered = com.example.data.datasource.IslamicLifeData.sections.find { it.id == "friday_special_duas" }
    assertNotNull(registered)
    assertEquals(45, registered?.items?.size)
  }

  @Test
  fun asmaulHusnaSpecialData_configuredCorrectly() {
    val section = com.example.data.datasource.AsmaulHusnaSpecialData.section
    assertEquals("asmaul_husna_special", section.id)
    assertEquals("★★★আসমাউল হুসনা (আল্লাহর ৯৯টি পবিত্র নাম) বাংলা অর্থ সহ ফজিলত★★★", section.titleBn)
    assertEquals(16, section.items.size)
    assertTrue(section.items.any { it.titleBn.contains("কুরআনের সুস্পষ্ট নির্দেশ") })
    assertTrue(section.items.any { it.titleBn.contains("জান্নাতী সুসংবাদ") })
    assertTrue(section.items.any { it.titleBn.contains("৪টি অনন্য বিশুদ্ধ ফজিলত") })
    assertTrue(section.items.any { it.titleBn.contains("গুনাহ মাফ ও তওবা") })
    assertTrue(section.items.any { it.titleBn.contains("রিযিক") })
    assertTrue(section.items.any { it.titleBn.contains("রোগব্যাধি") || it.titleBn.contains("আরোগ্য") })
    assertTrue(section.items.any { it.titleBn.contains("জ্ঞান") || it.titleBn.contains("প্রজ্ঞা") })
    assertTrue(section.items.any { it.titleBn.contains("বিপদ") || it.titleBn.contains("নিরাপত্তা") })
    assertTrue(section.items.any { it.titleBn.contains("মুখস্থ") })
    assertTrue(section.items.any { it.titleBn.contains("FAQ") })
    assertTrue(section.items.any { it.titleBn.contains("১–১০") })
    assertTrue(section.items.any { it.titleBn.contains("১১–২০") })
    assertTrue(section.items.any { it.titleBn.contains("২১–৩৫") })
    assertTrue(section.items.any { it.titleBn.contains("৩৬–৫০") })
    assertTrue(section.items.any { it.titleBn.contains("৫১–৭০") })
    assertTrue(section.items.any { it.titleBn.contains("৭১–৯৯") })

    // Verify registration in IslamicLifeData
    val registered = com.example.data.datasource.IslamicLifeData.sections.find { it.id == "asmaul_husna_special" }
    assertNotNull(registered)
    assertEquals(16, registered?.items?.size)
  }
}

