package com.example.ui.screens.tools.zakat

import androidx.compose.ui.graphics.Color

/**
 * Fiqh School methodology options for Zakat calculation.
 */
enum class FiqhSchool(
    val titleBn: String,
    val subtitleBn: String,
    val descriptionBn: String
) {
    HANAFI(
        titleBn = "হানাফী মাযহাব (Hanafi)",
        subtitleBn = "বাংলাদেশে সর্বাধিক প্রচলিত ও ঐতিহাসিক পদ্ধতি",
        descriptionBn = "হানাফী ফিকহ অনুযায়ী ব্যবহৃত ও অব্যবহৃত সকল স্বর্ণ-রৌপ্য অলংকারে যাকাত ফরজ। নগদ ও মিশ্র সম্পদের ক্ষেত্রে রৌপ্য নিসাব গ্রহণ করা অধিকতর সতর্কতা ও দরিদ্রবান্ধব হিসেবে গণ্য করা হয়।"
    ),
    SHAFI_I(
        titleBn = "শাফেয়ী মাযহাব (Shafi'i)",
        subtitleBn = "স্বাভাবিক ব্যক্তিগত ব্যবহারের অলংকার যাকাতমুক্ত",
        descriptionBn = "শাফেয়ী ফিকহ অনুযায়ী নারীর স্বাভাবিক ব্যক্তিগত ব্যবহারের অলংকার যাকাতমুক্ত। তবে সঞ্চিত স্বর্ণ-রৌপ্য, বার বা অতিরিক্ত মাত্রাতিরিক্ত অলংকারে যাকাত প্রযোজ্য।"
    ),
    MALIKI(
        titleBn = "মালেকী মাযহাব (Maliki)",
        subtitleBn = "ব্যক্তিগত ব্যবহারের অলংকার ও ব্যবসার চলতি মূলধন কেন্দ্রিক দৃষ্টিভঙ্গি",
        descriptionBn = "মালেকী ফিকহ অনুযায়ী ব্যক্তিগত ব্যবহারের অলংকার সাধারণত যাকাতমুক্ত। ব্যবসায়িক ক্ষেত্রে মজুদ পণ্যের বাজারমূল্য ও বিক্রয়যোগ্য উদ্বৃত্তের ওপর গুরুত্ব দেওয়া হয়।"
    ),
    HANBALI(
        titleBn = "হাম্বলী মাযহাব (Hanbali)",
        subtitleBn = "অনুমোদিত ব্যবহারের অলংকার ছাড় ও ঋণের সুনির্দিষ্ট সমন্বয়",
        descriptionBn = "হাম্বলী ফিকহ অনুযায়ী বৈধ ব্যক্তিগত ব্যবহারের অলংকারে যাকাত নেই। ঋণের ক্ষেত্রেও চলতি যাকাতযোগ্য সম্পদের সাথে সুনির্দিষ্ট সামঞ্জস্য বজায় রাখা হয়।"
    ),
    COMBINED_JURISTIC(
        titleBn = "আন্তর্জাতিক ইসলামিক ফিকহ একাডেমি (OIC / AAOIFI)",
        subtitleBn = "সমকালীন অর্থনৈতিক ব্যবস্থা ও আধুনিক ফিনান্স সমন্বয়",
        descriptionBn = "আন্তর্জাতিক ফিকহ একাডেমি ও সমকালীন মুফতিদের সমন্বিত দৃষ্টিভঙ্গি: শেয়ারের দীর্ঘমেয়াদী বিনিয়োগের ক্ষেত্রে কোম্পানির চলতি সম্পদ ধরা, দীর্ঘমেয়াদী ঋণের কেবল চলতি বছরের কিস্তি বাদ দেওয়া এবং স্বর্ণ-রৌপ্যের ভারসাম্য রক্ষা।"
    )
}

/**
 * Standard basis for Nisab evaluation.
 */
enum class NisabStandard(
    val titleBn: String,
    val weightGrams: Double,
    val weightTolaBn: String,
    val noteBn: String
) {
    SILVER(
        titleBn = "রৌপ্য নিসাব (Silver Nisab - সতর্কতামূলক ও দরিদ্রবান্ধব)",
        weightGrams = 612.36,
        weightTolaBn = "৫২.৫ তোলা",
        noteBn = "ধ্রুপদী ফিকহে নগদ অর্থ ও বাণিজ্যিক সম্পদের ক্ষেত্রে রৌপ্যের নিসাবকে দরিদ্রদের জন্য অধিক কল্যাণকর (অনফাউল লিল-ফুক্বারা) গণ্য করা হয়েছে। অধিকাংশ ওলামায়ে কেরামের মতে নগদ টাকার ক্ষেত্রে রৌপ্য নিসাব প্রযোজ্য।"
    ),
    GOLD(
        titleBn = "স্বর্ণ নিসাব (Gold Nisab - আধুনিক ক্রয়ক্ষমতা ভিত্তি)",
        weightGrams = 87.48,
        weightTolaBn = "৭.৫ তোলা (২০ দিনার / মিসকাল)",
        noteBn = "বর্তমানে স্বর্ণ ও রৌপ্যের মূল্যের বিশাল ব্যবধানের কারণে কিছু সমকালীন আলেম স্বর্ণের নিসাবকে অধিক বাস্তবসম্মত ক্রয়ক্ষমতার পরিমাপক মনে করেন। শুধুমাত্র স্বর্ণের মালিকের ক্ষেত্রে এটি প্রযোজ্য।"
    )
}

/**
 * Gold purity levels with fine gold conversion ratio.
 */
enum class GoldPurity(
    val titleBn: String,
    val karat: Int,
    val purityFactor: Double
) {
    K24(titleBn = "২৪ ক্যারেট (৯৯.৯% খাঁটি স্বর্ণ)", karat = 24, purityFactor = 1.0),
    K22(titleBn = "২২ ক্যারেট (৯১.৬% খাঁটি স্বর্ণ - সর্বাধিক প্রচলিত)", karat = 22, purityFactor = 22.0 / 24.0),
    K21(titleBn = "২১ ক্যারেট (৮৭.৫% খাঁটি স্বর্ণ)", karat = 21, purityFactor = 21.0 / 24.0),
    K18(titleBn = "১৮ ক্যারেট (৭৫.০% খাঁটি স্বর্ণ)", karat = 18, purityFactor = 18.0 / 24.0)
}

/**
 * Scholarly methodology for personal-use gold & silver jewelry.
 */
enum class JewelryMethodology(
    val titleBn: String,
    val descriptionBn: String
) {
    ALL_JEWELRY_ZAKATABLE(
        titleBn = "ব্যক্তিগত ও ব্যবহৃত সকল অলংকারে যাকাত প্রযোজ্য (হানাফী মাযহাব ও সতর্কতামূলক অভিমত)",
        descriptionBn = "হযরত আবু হুরায়রা (রা.) ও আয়েশা (রা.) বর্ণিত সহীহ হাদিস এবং হানাফী ফিকহ অনুযায়ী স্বর্ণ ও রৌপ্য যেকোনো রূপেই থাকুক না কেন, নিসাব পরিমাণ হলে এবং এক বছর অতিবাহিত হলে তার ওপর যাকাত ওয়াজিব।"
    ),
    EXEMPT_CUSTOMARY_PERSONAL_USE(
        titleBn = "স্বাভাবিক ব্যক্তিগত ব্যবহারের অলংকার যাকাতমুক্ত (জমহুর আলেম: শাফেয়ী, মালেকী ও হাম্বলী)",
        descriptionBn = "জমহুর (অধিকাংশ) সাহাবা ও তাবেঈদের আমল অনুযায়ী নারীর স্বাভাবিক ও বৈধ ব্যক্তিগত ব্যবহারের অলংকার পরিধেয় পোশাক ও ব্যক্তিগত সামগ্রীর মতো বিবেচিত হওয়ায় এতে যাকাত ওয়াজিব নয়। তবে অপচয় বা জমানোর উদ্দেশ্যে রাখা অলংকারে যাকাত হবে।"
    )
}

/**
 * Methodology for calculating Zakat on Stocks and Investments.
 */
enum class InvestmentMethodology(
    val titleBn: String,
    val descriptionBn: String
) {
    TRADING_MARKET_VALUE(
        titleBn = "স্বল্পমেয়াদী ট্রেডিং বা ক্যাপিটাল গেইনের উদ্দেশ্যে ক্রয়কৃত শেয়ার",
        descriptionBn = "শেয়ার দ্রুত কেনাবেচা বা মুনাফা লাভের জন্য রাখা হলে তা বাণিজ্যিক পণ্য (উরুদুত তিজারাহ) হিসেবে গণ্য হয় এবং যাকাত দিবসে তার পূর্ণাঙ্গ ১০০% বাজারমূল্যের ওপর যাকাত ওয়াজিব।"
    ),
    LONG_TERM_LIQUID_ASSETS(
        titleBn = "দীর্ঘমেয়াদী বিনিয়োগ বা বাৎসরিক ডিভিডেন্ড আয়ের উদ্দেশ্য (প্রক্সি অনুপাত ~২৫%)",
        descriptionBn = "কোম্পানির দীর্ঘমেয়াদী অংশীদার হলে কারখানার জমি, দালান বা যন্ত্রপাতিতে যাকাত নেই; কেবল কোম্পানির নগদ অর্থ ও বিক্রয়যোগ্য মজুদের (চলতি সম্পদ) অংশে যাকাত ওয়াজিব। ব্যালেন্স শিট বিস্তারিত জানা না থাকলে ওআইসি ফিকহ একাডেমি অনুযায়ী মোট বাজারমূল্যের ২৫-৩০% যাকাতযোগ্য ধরা হয়।"
    )
}

/**
 * Methodology for Debt / Liability Deductions.
 */
enum class DebtDeductionMethodology(
    val titleBn: String,
    val descriptionBn: String
) {
    IMMEDIATE_DUE_ONLY(
        titleBn = "কেবলমাত্র তাৎক্ষণিক বকেয়া ও চলতি বছরের কিস্তি বাদ দেওয়া (সমকালীন ফিকহ একাডেমি অভিমত)",
        descriptionBn = "আন্তর্জাতিক ইসলামিক ফিকহ একাডেমি ও অধিকাংশ সমকালীন মুফতির মতে দীর্ঘমেয়াদী ঋণের (যেমন ২০ বছরের গৃহঋণ বা দীর্ঘমেয়াদী ঋণ) পুরো অর্থ বাদ দেওয়া যাবে না; কেবল যাকাত দিবসে তাৎক্ষণিক প্রদেয় বা আগামী এক বছরের কিস্তি বাদ যাবে।"
    ),
    TOTAL_OUTSTANDING_DEBTS(
        titleBn = "মোট প্রদেয় বকেয়া ঋণের সম্পূর্ণ অংশ বাদ দেওয়া (ধ্রুপদী সাধারণ অভিমত)",
        descriptionBn = "ধ্রুপদী ফিকহের কিছু সাধারণ মূলনীতি অনুযায়ী মানুষের ওপর অপরিশোধিত যে কোনো ঋণ তার বিদ্যমান সম্পদের ওপর অধিকার দাবি করে, ফলে নিট সঞ্চয় বের করতে পুরো ঋণ বাদ দেওয়া হয়।"
    )
}

/**
 * Receivable likelihood classification.
 */
enum class ReceivableLikelihood(
    val titleBn: String,
    val noteBn: String
) {
    STRONG(
        titleBn = "নিশ্চিত আদায়যোগ্য পাওনা (Strong / Good Debt)",
        noteBn = "যে দেনাদার সক্ষম এবং অস্বীকার করছে না, তার কাছে পাওনা অর্থ আপনার সক্রিয় সম্পদ। বর্তমান বছরের যাকাত হিসাবে অন্তর্ভুক্ত করা উচিত।"
    ),
    DOUBTFUL(
        titleBn = "সন্দেহজনক বা অনদায়ী পাওনা (Doubtful / Bad Debt)",
        noteBn = "যে পাওনা ফিরে পাওয়ার সম্ভাবনা ক্ষীণ বা দেনাদার দেউলিয়া/অস্বীকৃতি জানিয়েছে, তা যতদিন হস্তগত না হবে ততদিন তার ওপর যাকাত ওয়াজিব নয়। হস্তগত হলে কেবল অতীত এক বছরের যাকাত আদায়ের মত রয়েছে।"
    )
}

/**
 * Currency configuration.
 */
enum class CurrencyOption(
    val code: String,
    val symbol: String,
    val nameBn: String,
    val defaultExchangeRateToBdt: Double
) {
    BDT("BDT", "৳", "বাংলাদেশি টাকা (BDT)", 1.0),
    USD("USD", "$", "ইউএস ডলার (USD)", 121.50),
    EUR("EUR", "€", "ইউরো (EUR)", 131.20),
    GBP("GBP", "£", "ব্রিটিশ পাউন্ড (GBP)", 156.40),
    SAR("SAR", "﷼", "সৌদি রিয়াল (SAR)", 32.40),
    AED("AED", "د.إ", "ইউএই দিরহাম (AED)", 33.10),
    INR("INR", "₹", "ভারতীয় রুপি (INR)", 1.45),
    PKR("PKR", "₨", "পাকিস্তানি রুপি (PKR)", 0.44),
    MYR("MYR", "RM", "মালয়েশিয়ান রিঙ্গিত (MYR)", 27.50)
}

/**
 * Complete UI & Form state for the interactive Zakat calculator.
 */
data class ZakatFormState(
    // Fiqh & Configuration
    val fiqhSchool: FiqhSchool = FiqhSchool.HANAFI,
    val nisabStandard: NisabStandard = NisabStandard.SILVER,
    val jewelryMethodology: JewelryMethodology = JewelryMethodology.ALL_JEWELRY_ZAKATABLE,
    val investmentMethodology: InvestmentMethodology = InvestmentMethodology.TRADING_MARKET_VALUE,
    val debtDeductionMethodology: DebtDeductionMethodology = DebtDeductionMethodology.IMMEDIATE_DUE_ONLY,
    val currency: CurrencyOption = CurrencyOption.BDT,

    // Market Prices (Defaults: Bangladesh standard market 2026)
    val goldPricePerGram22KBdt: Double = 11450.0, // ~৳1,33,500 per bhori / 11.664g
    val silverPricePerGramBdt: Double = 185.0,     // ~৳2,150 per bhori / 11.664g
    val isCustomPriceUsed: Boolean = false,
    val priceSourceDateBn: String = "সেপ্টেম্বর ২০২৬ (বাজুস / স্থানীয় বাজার প্রামাণ্য গড়)",

    // Hawl & Date
    val zakatDateDescriptionBn: String = "চলতি যাকাত বর্ষ (এক চান্দ্র বছর পূর্ণ হওয়া)",
    val isHawlCompleted: Boolean = true,

    // 1. CASH ASSETS
    val cashInHand: Double = 0.0,
    val cashInBankCurrent: Double = 0.0,
    val cashInBankSavings: Double = 0.0,
    val cashInIslamicBank: Double = 0.0,
    val foreignCurrencyEquivalent: Double = 0.0,
    val otherImmediatelyLiquidCash: Double = 0.0,
    val bankInterestRibaAmount: Double = 0.0, // Strictly isolated! Never counts as Zakatable personal wealth

    // 2. GOLD & SILVER
    val goldJewelryWeightGrams: Double = 0.0,
    val goldJewelryPurity: GoldPurity = GoldPurity.K22,
    val goldBarsCoinsWeightGrams: Double = 0.0,
    val goldBarsCoinsPurity: GoldPurity = GoldPurity.K24,
    val silverJewelryWeightGrams: Double = 0.0,
    val silverBarsCoinsWeightGrams: Double = 0.0,

    // 3. INVESTMENTS & SHARES
    val sharesForTradingMarketValue: Double = 0.0,
    val sharesForLongTermDividendsValue: Double = 0.0,
    val mutualFundsAndEtfsValue: Double = 0.0,
    val sukukAndIslamicBondsValue: Double = 0.0,
    val businessPartnershipLiquidShareValue: Double = 0.0,

    // 4. BUSINESS ASSETS / TRADE GOODS
    val tradeInventoryWholesaleValue: Double = 0.0,
    val rawMaterialsForSaleValue: Double = 0.0,
    val finishedGoodsForSaleValue: Double = 0.0,
    val businessCashAndBankBalance: Double = 0.0,

    // 5. RECEIVABLES (পাওনা অর্থ)
    val strongReceivablesLikelyToReceive: Double = 0.0,
    val doubtfulReceivablesUnlikely: Double = 0.0,

    // 6. REAL ESTATE, PROPERTY & MODERN ASSETS
    val propertyHeldForResaleMarketValue: Double = 0.0,
    val netAccumulatedRentalIncomeSavings: Double = 0.0,
    val cryptocurrencyTradingValue: Double = 0.0,
    val accessibleWithdrawableProvidentFund: Double = 0.0,
    val nonAccessibleGovernmentProvidentFund: Double = 0.0,

    // 7. LIABILITIES & DEBTS (দেনা ও দায়)
    val immediateDueDebtsAndInstallments: Double = 0.0,
    val unpaidUtilityBillsAndRentDue: Double = 0.0,
    val unpaidEmployeeSalariesDue: Double = 0.0,
    val immediateTradePayablesDue: Double = 0.0,
    val longTermTotalDebtOutstanding: Double = 0.0
)

/**
 * Itemized breakdown row for transparent auditability.
 */
data class ZakatBreakdownItem(
    val categoryBn: String,
    val titleBn: String,
    val grossAmount: Double,
    val zakatableAmount: Double,
    val explanationBn: String,
    val fiqhRuleBn: String,
    val isDeduction: Boolean = false
)

/**
 * Comprehensive calculation result.
 */
data class ZakatCalculationSummary(
    val calculationDateBn: String,
    val currency: CurrencyOption,
    val fiqhSchool: FiqhSchool,
    val nisabStandard: NisabStandard,
    val goldNisabValue: Double,
    val silverNisabValue: Double,
    val selectedNisabValue: Double,
    val goldPricePerGram: Double,
    val silverPricePerGram: Double,
    val totalZakatableAssets: Double,
    val totalDeductibleLiabilities: Double,
    val netZakatableWealth: Double,
    val isNisabReached: Boolean,
    val isZakatDue: Boolean,
    val zakatRatePercent: Double = 2.5,
    val zakatPayableAmount: Double,
    val items: List<ZakatBreakdownItem>,
    val formulaExplanationBn: String,
    val importantNotesAndDisclaimers: List<String>,
    val detectedSpecialConditions: List<String>
)
