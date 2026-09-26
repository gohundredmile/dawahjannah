package com.example.ui.screens.tools.zakat

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.max

/**
 * Robust, transparent, evidence-based calculation engine for Islamic Zakat.
 */
object ZakatCalculatorEngine {

    private val numberFormatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

    fun formatMoney(amount: Double, currency: CurrencyOption = CurrencyOption.BDT): String {
        return "${currency.symbol} ${numberFormatter.format(max(0.0, amount))}"
    }

    /**
     * Executes the comprehensive Zakat calculation according to the user's selected fiqh methodology.
     */
    fun calculate(form: ZakatFormState): ZakatCalculationSummary {
        val breakdown = mutableListOf<ZakatBreakdownItem>()
        val specialConditions = mutableListOf<String>()
        val notesAndDisclaimers = mutableListOf<String>()

        // 1. NISAB COMPUTATION
        // Classical: 87.48g of pure (24K) gold, or 612.36g of pure silver.
        // Base price per gram for 24K gold derived from 22K rate:
        val pureGoldPricePerGram = form.goldPricePerGram22KBdt * (24.0 / 22.0)
        val pureSilverPricePerGram = form.silverPricePerGramBdt

        val goldNisabValue = 87.48 * pureGoldPricePerGram
        val silverNisabValue = 612.36 * pureSilverPricePerGram

        val selectedNisabValue = when (form.nisabStandard) {
            NisabStandard.SILVER -> silverNisabValue
            NisabStandard.GOLD -> goldNisabValue
        }

        // 2. CASH & LIQUID MONETARY ASSETS
        var totalCash = 0.0

        if (form.cashInHand > 0) {
            totalCash += form.cashInHand
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "নগদ অর্থ",
                    titleBn = "হাতে ও ঘরে রক্ষিত নগদ টাকা",
                    grossAmount = form.cashInHand,
                    zakatableAmount = form.cashInHand,
                    explanationBn = "অবিলম্বে ব্যবহারযোগ্য উদ্বৃত্ত নগদ অর্থ সম্পূর্ণ যাকাতযোগ্য।",
                    fiqhRuleBn = "নগদ অর্থ স্বয়ংক্রিয়ভাবে যাকাতযোগ্য সম্পদ (মালে তিজারাহ সমতুল্য)।"
                )
            )
        }

        val totalBankDeposits = form.cashInBankCurrent + form.cashInBankSavings + form.cashInIslamicBank
        if (totalBankDeposits > 0) {
            totalCash += totalBankDeposits
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "ব্যাংক ব্যালেন্স",
                    titleBn = "ব্যাংক একাউন্ট ও সঞ্চয়ী ডিপোজিট (মূল হালাল জমা)",
                    grossAmount = totalBankDeposits,
                    zakatableAmount = totalBankDeposits,
                    explanationBn = "কারেন্ট, সেভিংস ও ইসলামিক ব্যাংকে রক্ষিত হালাল মূল জমা।",
                    fiqhRuleBn = "ব্যাংকে থাকা হালাল অর্থ এক বছর পূর্ণ হলে যাকাতের আওতাভুক্ত।"
                )
            )
        }

        if (form.foreignCurrencyEquivalent > 0) {
            totalCash += form.foreignCurrencyEquivalent
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "বৈদেশিক মুদ্রা",
                    titleBn = "বৈদেশিক মুদ্রা বা রেমিট্যান্স সঞ্চয়",
                    grossAmount = form.foreignCurrencyEquivalent,
                    zakatableAmount = form.foreignCurrencyEquivalent,
                    explanationBn = "বর্তমান বিনিময় হারে রূপান্তরিত স্থানীয় মুদ্রা মান।",
                    fiqhRuleBn = "মুদ্রার রূপান্তর ঘটে কিন্তু যাকাতযোগ্যতা অপরিবর্তিত থাকে।"
                )
            )
        }

        if (form.otherImmediatelyLiquidCash > 0) {
            totalCash += form.otherImmediatelyLiquidCash
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "অন্যান্য নগদ",
                    titleBn = "মোবাইল ব্যাংকিং (বিকাশ/নগদ) ও চেক ইত্যাদির ব্যালেন্স",
                    grossAmount = form.otherImmediatelyLiquidCash,
                    zakatableAmount = form.otherImmediatelyLiquidCash,
                    explanationBn = "যেকোনো সময়ে উত্তোলনযোগ্য তরল আর্থিক ব্যালেন্স।",
                    fiqhRuleBn = "নগদ অর্থের অন্তর্ভুক্ত।"
                )
            )
        }

        // ISOLATE RIBA / INTEREST (সুদ)
        if (form.bankInterestRibaAmount > 0) {
            specialConditions.add("ব্যাংক সুদ (${formatMoney(form.bankInterestRibaAmount, form.currency)}): সম্পূর্ণ হারাম হওয়ায় ব্যক্তিগত যাকাতযোগ্য সম্পদ থেকে বাদ রাখা হয়েছে।")
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "হারাম সুদ (আইসোলেটেড)",
                    titleBn = "ব্যাংক অর্জিত সুদ (হারাম - যাকাতমুক্ত কিন্তু দায়মুক্তি আবশ্যক)",
                    grossAmount = form.bankInterestRibaAmount,
                    zakatableAmount = 0.0,
                    explanationBn = "সুদের ওপর যাকাত হয় না; সওয়াবের নিয়ত ছাড়া সম্পূর্ণ অর্থ গরীব বা জনকল্যাণে বিলিয়ে দিতে হবে।",
                    fiqhRuleBn = "হারাম অর্থ যাকাতযোগ্য নয়; নিঃশর্ত দান করে দায়মুক্ত হতে হয়।"
                )
            )
        }

        // 3. GOLD & SILVER ASSETS
        var totalGoldSilver = 0.0

        if (form.goldJewelryWeightGrams > 0) {
            val goldPurityRatio = form.goldJewelryPurity.purityFactor
            val grossGoldJewelryVal = form.goldJewelryWeightGrams * form.goldPricePerGram22KBdt * (goldPurityRatio / (22.0 / 24.0))

            val zakatableGoldJewelryVal = when (form.jewelryMethodology) {
                JewelryMethodology.ALL_JEWELRY_ZAKATABLE -> grossGoldJewelryVal
                JewelryMethodology.EXEMPT_CUSTOMARY_PERSONAL_USE -> 0.0
            }

            totalGoldSilver += zakatableGoldJewelryVal
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "স্বর্ণালংকার",
                    titleBn = "ব্যক্তিগত ও ব্যবহৃত স্বর্ণালংকার (${form.goldJewelryWeightGrams} গ্রাম, ${form.goldJewelryPurity.titleBn})",
                    grossAmount = grossGoldJewelryVal,
                    zakatableAmount = zakatableGoldJewelryVal,
                    explanationBn = if (zakatableGoldJewelryVal > 0) {
                        "হানাফী মাযহাব ও সতর্কতামূলক অভিমত অনুযায়ী ব্যবহৃত অলংকারেও পূর্ণাঙ্গ যাকাত অন্তর্ভুক্ত করা হয়েছে।"
                    } else {
                        "শাফেয়ী, মালেকী ও হাম্বলী জমহুর অভিমত অনুযায়ী স্বাভাবিক ব্যক্তিগত ব্যবহারের অলংকার যাকাতমুক্ত রাখা হয়েছে।"
                    },
                    fiqhRuleBn = "ব্যবহারিক অলংকারে যাকাতের ক্ষেত্রে সাহাবা ও ফকীহগণের মধ্যে প্রামাণ্য মতপার্থক্য রয়েছে।"
                )
            )
            specialConditions.add("স্বর্ণালংকার গণনা: ${form.jewelryMethodology.titleBn}")
        }

        if (form.goldBarsCoinsWeightGrams > 0) {
            val purityRatio = form.goldBarsCoinsPurity.purityFactor
            val goldBarVal = form.goldBarsCoinsWeightGrams * pureGoldPricePerGram * purityRatio
            totalGoldSilver += goldBarVal
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "স্বর্ণ বার/কয়েন",
                    titleBn = "সঞ্চিত স্বর্ণের বার, কয়েন ও সঞ্চয়পত্র (${form.goldBarsCoinsWeightGrams} গ্রাম)",
                    grossAmount = goldBarVal,
                    zakatableAmount = goldBarVal,
                    explanationBn = "সঞ্চয় বা বিনিয়োগের উদ্দেশ্যে রক্ষিত খাঁটি স্বর্ণ সর্বসম্মতভাবে ১০০% যাকাতযোগ্য।",
                    fiqhRuleBn = "সকল মাযহাবেই সঞ্চিত অবিকৃত স্বর্ণের ওপর যাকাত ফরজ।"
                )
            )
        }

        if (form.silverJewelryWeightGrams > 0) {
            val silverJewelryVal = form.silverJewelryWeightGrams * form.silverPricePerGramBdt
            val zakatableSilverJewelryVal = when (form.jewelryMethodology) {
                JewelryMethodology.ALL_JEWELRY_ZAKATABLE -> silverJewelryVal
                JewelryMethodology.EXEMPT_CUSTOMARY_PERSONAL_USE -> 0.0
            }
            totalGoldSilver += zakatableSilverJewelryVal
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "রৌপ্যালংকার",
                    titleBn = "রৌপ্যের অলংকার (${form.silverJewelryWeightGrams} গ্রাম)",
                    grossAmount = silverJewelryVal,
                    zakatableAmount = zakatableSilverJewelryVal,
                    explanationBn = if (zakatableSilverJewelryVal > 0) "হানাফী পদ্ধতি অনুযায়ী হিসাবভুক্ত।" else "জমহুর পদ্ধতি অনুযায়ী ব্যবহারে ছাড়।",
                    fiqhRuleBn = "স্বর্ণের অলংকারের অনুরূপ নীতিমালা প্রযোজ্য।"
                )
            )
        }

        if (form.silverBarsCoinsWeightGrams > 0) {
            val silverBarVal = form.silverBarsCoinsWeightGrams * form.silverPricePerGramBdt
            totalGoldSilver += silverBarVal
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "রৌপ্য বার/কয়েন",
                    titleBn = "সঞ্চিত রৌপ্যের বার ও কয়েন (${form.silverBarsCoinsWeightGrams} গ্রাম)",
                    grossAmount = silverBarVal,
                    zakatableAmount = silverBarVal,
                    explanationBn = "সঞ্চিত রৌপ্য বার সর্বসম্মতভাবে যাকাতযোগ্য।",
                    fiqhRuleBn = "২০০ দিরহাম (৬১২.৩৬ গ্রাম) হলে ২.৫% যাকাত প্রযোজ্য।"
                )
            )
        }

        // 4. INVESTMENTS & SHARES
        var totalInvestments = 0.0

        if (form.sharesForTradingMarketValue > 0) {
            totalInvestments += form.sharesForTradingMarketValue
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "শেয়ার বিনিয়োগ",
                    titleBn = "স্বল্পমেয়াদী ট্রেডিং শেয়ার (ক্যাপিটাল গেইনের উদ্দেশ্যে)",
                    grossAmount = form.sharesForTradingMarketValue,
                    zakatableAmount = form.sharesForTradingMarketValue,
                    explanationBn = "বিক্রির উদ্দেশ্যে রাখা হওয়ায় বর্তমান বাজারমূল্যের ১০০% বাণিজ্যিক পণ্য হিসেবে যাকাতযোগ্য।",
                    fiqhRuleBn = "ট্রেডিং শেয়ার পণ্যদ্রব্য (উরূদুত তিজারাহ) হিসেবে গণ্য হয়।"
                )
            )
        }

        if (form.sharesForLongTermDividendsValue > 0) {
            // OIC Fiqh Academy recommendation: Proxy ~25-30% of market value represents net zakatable current assets
            val zakatableSharePortion = form.sharesForLongTermDividendsValue * 0.25
            totalInvestments += zakatableSharePortion
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "শেয়ার বিনিয়োগ",
                    titleBn = "দীর্ঘমেয়াদী ডিভিডেন্ড শেয়ার (২৫% প্রক্সি কারেন্ট অ্যাসেটস)",
                    grossAmount = form.sharesForLongTermDividendsValue,
                    zakatableAmount = zakatableSharePortion,
                    explanationBn = "দীর্ঘমেয়াদী অংশীদারিত্বের ক্ষেত্রে কারখানার জমি বা যন্ত্রে যাকাত নেই; আন্তর্জাতিক ফিকহ একাডেমি অনুযায়ী মোট মূল্যের ২৫% চলতি তরল সম্পদ হিসেবে যাকাতযোগ্য।",
                    fiqhRuleBn = "স্থায়ী অবকাঠামো বাদ দিয়ে কেবলমাত্র চলতি যাকাতযোগ্য অংশ হিসাব করা হয়।"
                )
            )
            specialConditions.add("দীর্ঘমেয়াদী শেয়ারে ২৫% ওআইসি ফিকহ একাডেমি প্রক্সি হিসাব প্রয়োগ করা হয়েছে।")
        }

        val otherInvestments = form.mutualFundsAndEtfsValue + form.sukukAndIslamicBondsValue + form.businessPartnershipLiquidShareValue
        if (otherInvestments > 0) {
            totalInvestments += otherInvestments
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "মিউচুয়াল ফান্ড ও সুকুক",
                    titleBn = "ইসলামিক বন্ড (সুকুক), মিউচুয়াল ফান্ড ও ব্যবসায়িক অংশীদারিত্ব",
                    grossAmount = otherInvestments,
                    zakatableAmount = otherInvestments,
                    explanationBn = "বিনিয়োগের তরল মূলধন ও লাভযোগ্য অংশ।",
                    fiqhRuleBn = "উদ্বৃত্ত লাভজনক বিনিয়োগ সম্পদের যাকাত প্রযোজ্য।"
                )
            )
        }

        // 5. BUSINESS ASSETS / TRADE GOODS (বাণিজ্যিক পণ্য)
        var totalBusinessGoods = 0.0

        val totalInventory = form.tradeInventoryWholesaleValue + form.rawMaterialsForSaleValue + form.finishedGoodsForSaleValue
        if (totalInventory > 0) {
            totalBusinessGoods += totalInventory
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "বাণিজ্যিক পণ্য",
                    titleBn = "দোকান বা কারখানার বিক্রয়যোগ্য পণ্যের পাইকারী বাজারমূল্য",
                    grossAmount = totalInventory,
                    zakatableAmount = totalInventory,
                    explanationBn = "বিক্রয়ের জন্য মজুদকৃত পণ্য ও কাঁচামাল। মূল্যায়নে খুচরা বিক্রয়মূল্য বা ভবিষ্যৎ সম্ভাব্য লাভ নয়, বরং বর্তমান পাইকারী বাজারমূল্য ধরা হয়েছে।",
                    fiqhRuleBn = "উরূদুত তিজারাহ (বাণিজ্যিক পণ্য) বর্তমান ন্যায্য মূল্যে হিসাবযোগ্য।"
                )
            )
        }

        if (form.businessCashAndBankBalance > 0) {
            totalBusinessGoods += form.businessCashAndBankBalance
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "বাণিজ্যিক নগদ",
                    titleBn = "ব্যবসার চলতি ক্যাশ ও ব্যাংক একাউন্ট",
                    grossAmount = form.businessCashAndBankBalance,
                    zakatableAmount = form.businessCashAndBankBalance,
                    explanationBn = "ব্যবসার পরিচালনা ও কাঁচামাল ক্রয়ের জন্য রক্ষিত নগদ উদ্বৃত্ত।",
                    fiqhRuleBn = "ব্যবসায়ের নগদ অর্থ সাধারণ নগদ অর্থের মতোই যাকাতযোগ্য।"
                )
            )
        }

        // 6. RECEIVABLES (পাওনা অর্থ)
        var totalReceivables = 0.0

        if (form.strongReceivablesLikelyToReceive > 0) {
            totalReceivables += form.strongReceivablesLikelyToReceive
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "আদায়যোগ্য পাওনা",
                    titleBn = "অন্যের কাছে পাওনা নিশ্চিত টাকা (Good Debt)",
                    grossAmount = form.strongReceivablesLikelyToReceive,
                    zakatableAmount = form.strongReceivablesLikelyToReceive,
                    explanationBn = "যে দেনাদার ঋণ স্বীকার করে এবং পরিশোধে সক্ষম, সেই পাওনা বর্তমান বছরেই হিসাবভুক্ত করতে হয়।",
                    fiqhRuleBn = "হযরত উসমান (রা.) ও হানাফী ফিকহ মতে নিশ্চিত পাওনা চলতি সম্পদে গণ্য।"
                )
            )
        }

        if (form.doubtfulReceivablesUnlikely > 0) {
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "সন্দেহজনক পাওনা",
                    titleBn = "অনদায়ী বা অনিশ্চিত পাওনা (Bad Debt - যাকাত স্থগিত)",
                    grossAmount = form.doubtfulReceivablesUnlikely,
                    zakatableAmount = 0.0,
                    explanationBn = "যে পাওনা ফিরে পাওয়া অনিশ্চিত, তা হস্তগত হওয়ার পূর্বে কোনো যাকাত নেই।",
                    fiqhRuleBn = "হযরত আলী (রা.)-এর ফতোয়া অনুযায়ী অনিশ্চিত পাওনা হস্তগত হওয়ার পর অতীতের এক বছরের যাকাত প্রযোজ্য।"
                )
            )
            specialConditions.add("অনিশ্চিত পাওনা (${formatMoney(form.doubtfulReceivablesUnlikely, form.currency)}): হস্তগত হওয়ার পূর্ব পর্যন্ত হিসাব থেকে বাদ রাখা হয়েছে।")
        }

        // 7. REAL ESTATE, PROPERTY & MODERN ASSETS
        var totalModernAssets = 0.0

        if (form.propertyHeldForResaleMarketValue > 0) {
            totalModernAssets += form.propertyHeldForResaleMarketValue
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "বিক্রির উদ্দেশ্যে জমি/প্লট",
                    titleBn = "পুনর্বিক্রয়ের উদ্দেশ্যে ক্রয়কৃত জমি, প্লট বা ফ্ল্যাট",
                    grossAmount = form.propertyHeldForResaleMarketValue,
                    zakatableAmount = form.propertyHeldForResaleMarketValue,
                    explanationBn = "ক্রয়ের শুরু থেকেই বিক্রির নিয়ত থাকলে তা বাণিজ্যিক পণ্যের মতো বর্তমান বাজারমূল্যে যাকাতযোগ্য।",
                    fiqhRuleBn = "তিজারতের নিয়তে ক্রয়কৃত স্থাবর সম্পত্তি পণ্য হিসেবে বিবেচিত।"
                )
            )
            specialConditions.add("পুনর্বিক্রয়ের উদ্দেশ্যে রাখা সম্পত্তি বাণিজ্যিক পণ্য হিসেবে যাকাতযোগ্য ধরা হয়েছে।")
        }

        if (form.netAccumulatedRentalIncomeSavings > 0) {
            totalModernAssets += form.netAccumulatedRentalIncomeSavings
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "ভাড়ার সঞ্চিত আয়",
                    titleBn = "ভাড়া দেওয়া বাড়ি বা ফ্ল্যাট থেকে সঞ্চিত নিট অর্থ",
                    grossAmount = form.netAccumulatedRentalIncomeSavings,
                    zakatableAmount = form.netAccumulatedRentalIncomeSavings,
                    explanationBn = "ভাড়া দেওয়া মূল ভবনের মূল্যে কোনো যাকাত নেই; কেবলমাত্র তা থেকে অর্জিত সঞ্চিত উদ্বৃত্ত ভাড়ার ওপর যাকাত ওয়াজিব।",
                    fiqhRuleBn = "ভাড়াকৃত স্থায়ী সম্পদে যাকাত নেই, ভাড়ার সঞ্চিত আয়ে যাকাত ফরজ।"
                )
            )
        }

        if (form.cryptocurrencyTradingValue > 0) {
            totalModernAssets += form.cryptocurrencyTradingValue
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "ক্রিপ্টো অ্যাসেট",
                    titleBn = "ট্রেডিং বা বিনিয়োগের জন্য রক্ষিত ক্রিপ্টোকারেন্সি",
                    grossAmount = form.cryptocurrencyTradingValue,
                    zakatableAmount = form.cryptocurrencyTradingValue,
                    explanationBn = "ডিজিটাল সম্পদ হিসেবে ট্রেডিং মূল্যে হিসাবভুক্ত।",
                    fiqhRuleBn = "সমকালীন ফিকহবিদদের মতে ক্রিপ্টো বিনিয়োগযোগ্য বাণিজ্যিক সম্পদ হিসেবে যাকাতযোগ্য।"
                )
            )
            specialConditions.add("ক্রিপ্টোকারেন্সি সম্পর্কিত সমকালীন আলেমদের ফতোয়া ও বৈধতার শর্ত প্রযোজ্য।")
        }

        if (form.accessibleWithdrawableProvidentFund > 0) {
            totalModernAssets += form.accessibleWithdrawableProvidentFund
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "উত্তোলনযোগ্য প্রভিডেন্ট ফান্ড",
                    titleBn = "স্বেচ্ছাধীন বা উত্তোলনযোগ্য প্রভিডেন্ট ফান্ড / গ্র্যাচুইটি",
                    grossAmount = form.accessibleWithdrawableProvidentFund,
                    zakatableAmount = form.accessibleWithdrawableProvidentFund,
                    explanationBn = "যে ফান্ড চাকরিজীবী চাইলে যেকোনো সময় তুলে নিতে পারে, তার ওপর যাকাত ওয়াজিব।",
                    fiqhRuleBn = "পূর্ণাঙ্গ মালিকানা ও হস্তগত করার ক্ষমতা থাকলে যাকাত প্রযোজ্য।"
                )
            )
        }

        if (form.nonAccessibleGovernmentProvidentFund > 0) {
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "বাধ্যতামূলক পিএফ (অনুপলব্ধ)",
                    titleBn = "সরকারি বা বাধ্যতামূলক প্রভিডেন্ট ফান্ড (যা উত্তোলনযোগ্য নয়)",
                    grossAmount = form.nonAccessibleGovernmentProvidentFund,
                    zakatableAmount = 0.0,
                    explanationBn = "চাকরিকালীন সময়ে এই ফান্ডের ওপর কর্মচারীর পূর্ণ দখল থাকে না। অবসর গ্রহণ বা হস্তগত হওয়ার পর অতীতের ১ বছরের হিসাব প্রযোজ্য হবে।",
                    fiqhRuleBn = "মালিকানা অসম্পূর্ণ ও নিয়ন্ত্রণহীন হওয়ায় চলতি যাকাত প্রযোজ্য নয়।"
                )
            )
            specialConditions.add("বাধ্যতামূলক প্রভিডেন্ট ফান্ড উত্তোলনযোগ্য না হওয়ায় চলতি হিসাব থেকে ছাড় দেওয়া হয়েছে।")
        }

        // 8. DEDUCTIBLE LIABILITIES & DEBTS (দেনা ও দায়)
        var totalLiabilities = 0.0

        val immediateDebts = form.immediateDueDebtsAndInstallments +
                form.unpaidUtilityBillsAndRentDue +
                form.unpaidEmployeeSalariesDue +
                form.immediateTradePayablesDue

        if (immediateDebts > 0) {
            totalLiabilities += immediateDebts
            breakdown.add(
                ZakatBreakdownItem(
                    categoryBn = "প্রদেয় দায় (তাত্ক্ষণিক)",
                    titleBn = "তাত্ক্ষণিক বকেয়া ঋণ, বিল, কর্মচারীর বেতন ও চলতি ঋণের কিস্তি",
                    grossAmount = immediateDebts,
                    zakatableAmount = immediateDebts,
                    explanationBn = "যাকাত দিবসে তাৎক্ষণিকভাবে পরিশোধযোগ্য দেনা বা আসন্ন মেয়াদী ঋণের কিস্তি।",
                    fiqhRuleBn = "সকল মাযহাবেই তাৎক্ষণিক দেনা বিদ্যমান সম্পদ থেকে বিয়োগযোগ্য।",
                    isDeduction = true
                )
            )
        }

        if (form.longTermTotalDebtOutstanding > 0) {
            when (form.debtDeductionMethodology) {
                DebtDeductionMethodology.IMMEDIATE_DUE_ONLY -> {
                    // Do NOT deduct the entire 20-year long-term debt; only immediate installment was deducted
                    breakdown.add(
                        ZakatBreakdownItem(
                            categoryBn = "দীর্ঘমেয়াদী ঋণ (ছাড়প্রাপ্ত নয়)",
                            titleBn = "মোট অবশিষ্ট দীর্ঘমেয়াদী ঋণ (${formatMoney(form.longTermTotalDebtOutstanding, form.currency)})",
                            grossAmount = form.longTermTotalDebtOutstanding,
                            zakatableAmount = 0.0,
                            explanationBn = "আন্তর্জাতিক ইসলামিক ফিকহ একাডেমি অনুযায়ী দীর্ঘমেয়াদী ঋণের কেবল চলতি বছরের কিস্তি বাদ যায়; পুরো ঋণ বাদ দিয়ে যাকাত ফাঁকি দেওয়া যাবে না।",
                            fiqhRuleBn = "দীর্ঘমেয়াদী ঋণ স্থায়ী সম্পদের বিপরীতে গৃহীত হওয়ায় তা চলতি তরল সম্পদ থেকে পুরোটা বাদ যায় না।",
                            isDeduction = true
                        )
                    )
                    notesAndDisclaimers.add("দীর্ঘমেয়াদী ঋণ: ওআইসি ফিকহ একাডেমির সিদ্ধান্ত অনুযায়ী মোট দীর্ঘমেয়াদী ঋণ বাদ না দিয়ে কেবল চলতি প্রদেয় কিস্তি বাদ দেওয়া হয়েছে।")
                }
                DebtDeductionMethodology.TOTAL_OUTSTANDING_DEBTS -> {
                    totalLiabilities += form.longTermTotalDebtOutstanding
                    breakdown.add(
                        ZakatBreakdownItem(
                            categoryBn = "দীর্ঘমেয়াদী ঋণ (সম্পূর্ণ বিয়োগ)",
                            titleBn = "মোট বকেয়া ঋণের সম্পূর্ণ অংশ (${formatMoney(form.longTermTotalDebtOutstanding, form.currency)})",
                            grossAmount = form.longTermTotalDebtOutstanding,
                            zakatableAmount = form.longTermTotalDebtOutstanding,
                            explanationBn = "ধ্রুপদী ফিকহের সাধারণ অভিমত অনুযায়ী বিদ্যমান সম্পদ থেকে পুরো ঋণ বাদ দেওয়া হয়েছে।",
                            fiqhRuleBn = "ঋণ যাকাতযোগ্য সম্পদের মালিকানাকে দুর্বল করে দেয়।",
                            isDeduction = true
                        )
                    )
                }
            }
        }

        // 9. FINAL TOTALS & NISAB COMPARISON
        val totalZakatableAssets = totalCash + totalGoldSilver + totalInvestments + totalBusinessGoods + totalReceivables + totalModernAssets
        val totalDeductibleLiabilities = totalLiabilities
        val netZakatableWealth = max(0.0, totalZakatableAssets - totalDeductibleLiabilities)

        val isNisabReached = netZakatableWealth >= selectedNisabValue
        val isZakatDue = isNisabReached && form.isHawlCompleted
        val zakatPayableAmount = if (isZakatDue) netZakatableWealth * 0.025 else 0.0

        // 10. FORMULA EXPLANATION
        val formulaString = buildString {
            append("মোট যাকাতযোগ্য সম্পদ: ${formatMoney(totalZakatableAssets, form.currency)}\n")
            append("বাদযোগ্য প্রদেয় দেনা: - ${formatMoney(totalDeductibleLiabilities, form.currency)}\n")
            append("────────────────────────────────────\n")
            append("নিট যাকাতযোগ্য সম্পদ: ${formatMoney(netZakatableWealth, form.currency)}\n")
            append("নিসাব সীমা (${form.nisabStandard.titleBn}): ${formatMoney(selectedNisabValue, form.currency)}\n")
            if (isZakatDue) {
                append("হাওল (এক চান্দ্র বছর): সম্পন্ন হয়েছে\n")
                append("যাকাতের শরীয়াহ হার: ২.৫% (১/৪০ অংশ)\n")
                append("প্রদেয় যাকাত: ${formatMoney(netZakatableWealth, form.currency)} × ২.৫% = ${formatMoney(zakatPayableAmount, form.currency)}")
            } else if (!isNisabReached) {
                append("ফলাফল: আপনার নিট সম্পদ নিসাব সীমা (${formatMoney(selectedNisabValue, form.currency)})-এ পৌঁছায়নি। তাই যাকাত ফরজ নয়।")
            } else {
                append("ফলাফল: নিসাব সীমা পূর্ণ হলেও এক চান্দ্র বছর (হাওল) অতিবাহিত না হওয়ায় যাকাত এখনো ওয়াজিব হয়নি।")
            }
        }

        // Add general disclaimers
        notesAndDisclaimers.add("ব্যক্তিগত বাসস্থান, দৈনন্দিন ব্যবহারের গাড়ি, পোশাক-পরিচ্ছদ, গৃহস্থালী আসবাবপত্র ও ব্যবসায়ের স্থায়ী যন্ত্রপাতি (Fixed Assets) শরীয়াহ অনুযায়ী যাকাতমুক্ত।")
        notesAndDisclaimers.add("যাকাত প্রদানের সময় মনে মনে বা মুখে যাকাতের সুনির্দিষ্ট নিয়ত থাকা অপরিহার্য। নিয়ত ছাড়া সাধারণ দান করলে তা যাকাত হিসেবে পরিগণিত হবে না।")
        notesAndDisclaimers.add("এই ক্যালকুলেটরটি আপনার প্রদত্ত তথ্য ও নির্বাচিত মাযহাবী নীতির ওপর ভিত্তি করে একটি অত্যন্ত প্রামাণ্য হিসাব প্রদান করে। জটিল কোনো যৌথ ব্যবসা বা উত্তরাধিকার সম্পত্তির ক্ষেত্রে বিশ্বস্ত মুফতির পরামর্শ নিন।")

        return ZakatCalculationSummary(
            calculationDateBn = "যাকাত বর্ষ: ${form.zakatDateDescriptionBn}",
            currency = form.currency,
            fiqhSchool = form.fiqhSchool,
            nisabStandard = form.nisabStandard,
            goldNisabValue = goldNisabValue,
            silverNisabValue = silverNisabValue,
            selectedNisabValue = selectedNisabValue,
            goldPricePerGram = form.goldPricePerGram22KBdt,
            silverPricePerGram = form.silverPricePerGramBdt,
            totalZakatableAssets = totalZakatableAssets,
            totalDeductibleLiabilities = totalDeductibleLiabilities,
            netZakatableWealth = netZakatableWealth,
            isNisabReached = isNisabReached,
            isZakatDue = isZakatDue,
            zakatRatePercent = 2.5,
            zakatPayableAmount = zakatPayableAmount,
            items = breakdown,
            formulaExplanationBn = formulaString,
            importantNotesAndDisclaimers = notesAndDisclaimers,
            detectedSpecialConditions = specialConditions
        )
    }
}
