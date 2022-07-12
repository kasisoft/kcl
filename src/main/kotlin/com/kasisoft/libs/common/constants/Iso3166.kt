package com.kasisoft.libs.common.constants

import java.util.*
import java.util.function.*

/**
 * Collection of iso-3166 codes.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
enum class Iso3166(val alpha3 : String, val alpha2 : String, val numerical : Int) : Predicate<String?> {

    Afghanistan                          ("AFG", "AF",   4),
    Albania                              ("ALB", "AL",   8),
    Algeria                              ("DZA", "DZ",  12),
    AmericanSamoa                        ("ASM", "AS",  16),
    Andorra                              ("AND", "AD",  20),
    Angola                               ("AGO", "AO",  24),
    Anguilla                             ("AIA", "AI", 660),
    Antarctica                           ("ATA", "AQ",  10),
    AntiguaAndBarbuda                    ("ATG", "AG",  28),
    Argentina                            ("ARG", "AR",  32),
    Armenia                              ("ARM", "AM",  51),
    Aruba                                ("ABW", "AW", 533),
    Australia                            ("AUS", "AU",  36),
    Austria                              ("AUT", "AT",  40),
    Azerbaijan                           ("AZE", "AZ",  31),
    Bahamas                              ("BHS", "BS",  44),
    Bahrain                              ("BHR", "BH",  48),
    Bangladesh                           ("BGD", "BD",  50),
    Barbados                             ("BRB", "BB",  52),
    Belarus                              ("BLR", "BY", 112),
    Belgium                              ("BEL", "BE",  56),
    Belize                               ("BLZ", "BZ",  84),
    Benin                                ("BEN", "BJ", 204),
    Bermuda                              ("BMU", "BM",  60),
    Bhutan                               ("BTN", "BT",  64),
    Bolivia                              ("BOL", "BO",  68),
    BosniaHerzegovina                    ("BIH", "BA",  70),
    Botswana                             ("BWA", "BW",  72),
    BouvetIsland                         ("BVT", "BV",  74),
    Brazil                               ("BRA", "BR",  76),
    BritishIndianOceanTerritory          ("IOT", "IO",  86),
    BruneiDarussalam                     ("BRN", "BN",  96),
    Bulgaria                             ("BGR", "BG", 100),
    BurkinaFaso                          ("BFA", "BF", 854),
    Burundi                              ("BDI", "BI", 108),
    Cambodia                             ("KHM", "KH", 116),
    Cameroon                             ("CMR", "CM", 120),
    Canada                               ("CAN", "CA", 124),
    CapeVerde                            ("CPV", "CV", 132),
    CaymanIslands                        ("CYM", "KY", 136),
    CentralAfricanRepublic               ("CAF", "CF", 140),
    Chad                                 ("TCD", "TD", 148),
    Chile                                ("CHL", "CL", 152),
    China                                ("CHN", "CN", 156),
    ChristmasIsland                      ("CXR", "CX", 162),
    CocosIslands                         ("CCK", "CC", 166),
    Colombia                             ("COL", "CO", 170),
    Comoros                              ("COM", "KM", 174),
    RepublicCongo                        ("COG", "CG", 178),
    DemocraticRepublicCongo              ("COD", "CD", 180),
    CookIslands                          ("COK", "CK", 184),
    CostaRica                            ("CRI", "CR", 188),
    CoteDIvoire                          ("CIV", "CI", 384),
    Croatia                              ("HRV", "HR", 191),
    Cuba                                 ("CUB", "CU", 192),
    Cyprus                               ("CYP", "CY", 196),
    CzechRepublic                        ("CZE", "CZ", 203),
    Denmark                              ("DNK", "DK", 208),
    Djibouti                             ("DJI", "DJ", 262),
    Dominica                             ("DMA", "DM", 212),
    DominicanRepublic                    ("DOM", "DO", 214),
    Ecuador                              ("ECU", "EC", 218),
    Egypt                                ("EGY", "EG", 818),
    ElSalvador                           ("SLV", "SV", 222),
    EquatorialGuinea                     ("GNQ", "GQ", 226),
    Eritrea                              ("ERI", "ER", 232),
    Estonia                              ("EST", "EE", 233),
    Ethiopia                             ("ETH", "ET", 231),
    FalklandIslands                      ("FLK", "FK", 238),
    FaroeIslands                         ("FRO", "FO", 234),
    Fiji                                 ("FJI", "FJ", 242),
    Finland                              ("FIN", "FI", 246),
    France                               ("FRA", "FR", 250),
    FrenchGuiana                         ("GUF", "GF", 254),
    FrenchPolynesia                      ("PYF", "PF", 258),
    FrenchSouthernLands                  ("ATF", "TF", 260),
    Gabon                                ("GAB", "GA", 266),
    Gambia                               ("GMB", "GM", 270),
    Georgia                              ("GEO", "GE", 268),
    Germany                              ("DEU", "DE", 276),
    Ghana                                ("GHA", "GH", 288),
    Gibraltar                            ("GIB", "GI", 292),
    Greece                               ("GRC", "GR", 300),
    Greenland                            ("GRL", "GL", 304),
    Grenada                              ("GRD", "GD", 308),
    Guadeloupe                           ("GLP", "GP", 312),
    Guam                                 ("GUM", "GU", 316),
    Guatemala                            ("GTM", "GT", 320),
    Guernsey                             ("GGY", "GG", 831),
    Guinea                               ("GIN", "GN", 324),
    GuineaBissau                         ("GNB", "GW", 624),
    Guyana                               ("GUY", "GY", 328),
    Haiti                                ("HTI", "HT", 332),
    HeardAndMcDonaldIslands              ("HMD", "HM", 334),
    Honduras                             ("HND", "HN", 340),
    HongKong                             ("HKG", "HK", 344),
    Hungary                              ("HUN", "HU", 348),
    Iceland                              ("ISL", "IS", 352),
    India                                ("IND", "IN", 356),
    Indonesia                            ("IDN", "ID", 360),
    Iran                                 ("IRN", "IR", 364),
    Iraq                                 ("IRQ", "IQ", 368),
    Ireland                              ("IRL", "IE", 372),
    IsleOfMan                            ("IMN", "IM", 833),
    Israel                               ("ISR", "IL", 376),
    Italy                                ("ITA", "IT", 380),
    Jamaica                              ("JAM", "JM", 388),
    Japan                                ("JPN", "JP", 392),
    Jersey                               ("JEY", "JE", 832),
    Jordan                               ("JOR", "JO", 400),
    Kazakhstan                           ("KAZ", "KZ", 398),
    Kenya                                ("KEN", "KE", 404),
    Kiribati                             ("KIR", "KI", 296),
    NorthKorea                           ("PRK", "KP", 408),
    SouthKorea                           ("KOR", "KR", 410),
    Kuwait                               ("KWT", "KW", 414),
    Kyrgyzstan                           ("KGZ", "KG", 417),
    Laos                                 ("LAO", "LA", 418),
    Latvia                               ("LVA", "LV", 428),
    Lebanon                              ("LBN", "LB", 422),
    Lesotho                              ("LSO", "LS", 426),
    Liberia                              ("LBR", "LR", 430),
    Libya                                ("LBY", "LY", 434),
    Liechtenstein                        ("LIE", "LI", 438),
    Lithuania                            ("LTU", "LT", 440),
    Luxembourg                           ("LUX", "LU", 442),
    Macau                                ("MAC", "MO", 446),
    Macedonia                            ("MKD", "MK", 807),
    Madagascar                           ("MDG", "MG", 450),
    Malawi                               ("MWI", "MW", 454),
    Malaysia                             ("MYS", "MY", 458),
    Maldives                             ("MDV", "MV", 462),
    Mali                                 ("MLI", "ML", 466),
    Malta                                ("MLT", "MT", 470),
    MarshallIslands                      ("MHL", "MH", 584),
    Martinique                           ("MTQ", "MQ", 474),
    Mauritania                           ("MRT", "MR", 478),
    Mauritius                            ("MUS", "MU", 480),
    Mayotte                              ("MYT", "YT", 175),
    Mexico                               ("MEX", "MX", 484),
    Micronesia                           ("FSM", "FM", 583),
    Moldova                              ("MDA", "MD", 498),
    Monaco                               ("MCO", "MC", 492),
    Mongolia                             ("MNG", "MN", 496),
    Montenegro                           ("MNE", "ME", 499),
    Montserrat                           ("MSR", "MS", 500),
    Morocco                              ("MAR", "MA", 504),
    Mozambique                           ("MOZ", "MZ", 508),
    Myanmar                              ("MMR", "MM", 104),
    Namibia                              ("NAM", "NA", 516),
    Nauru                                ("NRU", "NR", 520),
    Nepal                                ("NPL", "NP", 524),
    Netherlands                          ("NLD", "NL", 528),
    NetherlandsAntilles                  ("ANT", "AN", 530),
    NewCaledonia                         ("NCL", "NC", 540),
    NewZealand                           ("NZL", "NZ", 554),
    Nicaragua                            ("NIC", "NI", 558),
    Niger                                ("NER", "NE", 562),
    Nigeria                              ("NGA", "NG", 566),
    Niue                                 ("NIU", "NU", 570),
    NorfolkIsland                        ("NFK", "NF", 574),
    NorthernMarianaIslands               ("MNP", "MP", 580),
    Norway                               ("NOR", "NO", 578),
    Oman                                 ("OMN", "OM", 512),
    Pakistan                             ("PAK", "PK", 586),
    Palau                                ("PLW", "PW", 585),
    Palestine                            ("PSE", "PS", 275),
    Panama                               ("PAN", "PA", 591),
    PapuaNewGuinea                       ("PNG", "PG", 598),
    Paraguay                             ("PRY", "PY", 600),
    Peru                                 ("PER", "PE", 604),
    Philippines                          ("PHL", "PH", 608),
    Pitcairn                             ("PCN", "PN", 612),
    Poland                               ("POL", "PL", 616),
    Portugal                             ("PRT", "PT", 620),
    PuertoRico                           ("PRI", "PR", 630),
    Qatar                                ("QAT", "QA", 634),
    Reunion                              ("REU", "RE", 638),
    Romania                              ("ROU", "RO", 642),
    RussianFederation                    ("RUS", "RU", 643),
    Rwanda                               ("RWA", "RW", 646),
    SaintBarthelemy                      ("BLM", "BL", 652),
    SaintHelena                          ("SHN", "SH", 654),
    SaintKittsAndNevis                   ("KNA", "KN", 659),
    SaintLucia                           ("LCA", "LC", 662),
    SaintMartin                          ("MAF", "MF", 663),
    SaintPierreAndMiquelon               ("SPM", "PM", 666),
    SaintVincentAndTheGrenadines         ("VCT", "VC", 670),
    Samoa                                ("WSM", "WS", 882),
    SanMarino                            ("SMR", "SM", 674),
    SaoTomeAndPrincipe                   ("STP", "ST", 678),
    SaudiArabia                          ("SAU", "SA", 682),
    Senegal                              ("SEN", "SN", 686),
    Serbia                               ("SRB", "RS", 688),
    Seychelles                           ("SYC", "SC", 690),
    SierraLeone                          ("SLE", "SL", 694),
    Singapore                            ("SGP", "SG", 702),
    Slovakia                             ("SVK", "SK", 703),
    Slovenia                             ("SVN", "SI", 705),
    SolomonIslands                       ("SLB", "SB",  90),
    Somalia                              ("SOM", "SO", 706),
    SouthAfrica                          ("ZAF", "ZA", 710),
    SouthGeorgiaAndSouthSandwichIslands  ("SGS", "GS", 239),
    Spain                                ("ESP", "ES", 724),
    SriLanka                             ("LKA", "LK", 144),
    Sudan                                ("SDN", "SD", 736),
    Suriname                             ("SUR", "SR", 740),
    SvalbardAndJanMayenIslands           ("SJM", "SJ", 744),
    Swaziland                            ("SWZ", "SZ", 748),
    Sweden                               ("SWE", "SE", 752),
    Switzerland                          ("CHE", "CH", 756),
    Syria                                ("SYR", "SY", 760),
    Taiwan                               ("TWN", "TW", 158),
    Tajikistan                           ("TJK", "TJ", 762),
    Tanzania                             ("TZA", "TZ", 834),
    Thailand                             ("THA", "TH", 764),
    TimorLeste                           ("TLS", "TL", 626),
    Togo                                 ("TGO", "TG", 768),
    Tokelau                              ("TKL", "TK", 772),
    Tonga                                ("TON", "TO", 776),
    TrinidadAndTobago                    ("TTO", "TT", 780),
    Tunisia                              ("TUN", "TN", 788),
    Turkey                               ("TUR", "TR", 792),
    Turkmenistan                         ("TKM", "TM", 795),
    TurksAndCaicosIslands                ("TCA", "TC", 796),
    Tuvalu                               ("TUV", "TV", 798),
    Uganda                               ("UGA", "UG", 800),
    Ukraine                              ("UKR", "UA", 804),
    UnitedArabEmirates                   ("ARE", "AE", 784),
    UnitedKingdom                        ("GBR", "GB", 826),
    UnitedStatesMinorOutlyingIslands     ("UMI", "UM", 581),
    UnitedStatesOfAmerica                ("USA", "US", 840),
    Uruguay                              ("URY", "UY", 858),
    Uzbekistan                           ("UZB", "UZ", 860),
    Vanuatu                              ("VUT", "VU", 548),
    VaticanCity                          ("VAT", "VA", 336),
    Venezuela                            ("VEN", "VE", 862),
    Vietnam                              ("VNM", "VN", 704),
    BritishVirginIslands                 ("VGB", "VG",  92),
    USVirginIslands                      ("VIR", "VI", 850),
    WallisAndFutunaIslands               ("WLF", "WF", 876),
    WesternSahara                        ("ESH", "EH", 732),
    Yemen                                ("YEM", "YE", 887),
    Zambia                               ("ZMB", "ZM", 894),
    Zimbabwe                             ("ZWE", "ZW", 716);

    init {
        synchronized(Iso3166Cache) {
            Iso3166Cache.valuebyalpha3.put(alpha3, this)
            Iso3166Cache.valuebyalpha2.put(alpha2, this)
            Iso3166Cache.valuebynum.put(numerical, this)
            Iso3166Cache.values.add(this)
        }
    }

    companion object {

        /**
         * Returns the iso value for a specific alpha-3 code.
         *
         * @param alpha3   The alpha-3 code which value shall be searched for.
         *
         * @return   The iso value.
         */
        @JvmStatic
        fun findByAlpha3(alpha3 : String?) = findBy(Iso3166Cache.valuebyalpha3, alpha3)

        /**
         * Returns the iso value for a specific alpha-2 code.
         *
         * @param alpha2   The alpha-2 code which value shall be searched for.
         *
         * @return   The iso value.
         */
        @JvmStatic
        fun findByAlpha2(alpha2 : String?) = findBy(Iso3166Cache.valuebyalpha2, alpha2)

        /**
         * Returns the iso value for a specific numerical code.
         *
         * @param numerical   The numerical code which value shall be searched for.
         *
         * @return   The iso value.
         */
        @JvmStatic
        fun findByNumerical(numerical : Int?) = findBy(Iso3166Cache.valuebynum, numerical)

        private fun <K> findBy(map : Map<K, Iso3166>, key : K?) : Iso3166? {
            if (key != null) {
              return map.get(key)
            }
            return null
        }

    } /* ENDOBJECT */

    override fun test(langcode : String?) : Boolean {
        var result = false
        if (langcode != null) {
          result = alpha2.equals(langcode, true) || alpha3.equals(langcode, true)
        }
        return result
    }

} /* ENDENUM */

private object Iso3166Cache {

    val valuebyalpha2 = mutableMapOf<String, Iso3166>()
    val valuebyalpha3 = mutableMapOf<String, Iso3166>()
    val valuebynum    = mutableMapOf<Int, Iso3166>()
    val values        = mutableListOf<Iso3166>()

} /* ENDOBJECT */
