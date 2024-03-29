package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.annotation.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

/**
 * Collection of iso-639 codes.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Specification(value = "https://www.loc.gov/standards/iso639-2/php/code_list.php", date = "14-NOV-2023")
public enum Iso639 implements Predicate<String> {

    Abkhazian("ABK", "ABK", "AB"),
    Achinese("ACE", "ACE", null),
    Acoli("ACH", "ACH", null),
    Adangme("ADA", "ADA", null),
    Adyghe("ADY", "ADY", null),
    Afar("AAR", "AAR", "AA"),
    AfroAsiatic("AFA", "AFA", null),
    Afrihili("AFH", "AFH", null),
    Afrikaans("AFR", "AFR", "AF"),
    Ainu("AIN", "AIN", null),
    Akan("AKA", "AKA", "AK"),
    Akkadian("AKK", "AKK", null),
    Albanian("ALB", "SQI", "SQ"),
    Aleut("ALE", "ALE", null),
    Algonquian("ALG", "ALG", null),
    Altaic("TUT", "TUT", null),
    Amharic("AMH", "AMH", "AM"),
    AncientGreek("GRC", "GRC", null),
    Angika("ANP", "ANP", null),
    Apache("APA", "APA", null),
    Arabic("ARA", "ARA", "AR"),
    Aragonese("ARG", "ARG", "AN"),
    Arapaho("ARP", "ARP", null),
    Arawak("ARW", "ARW", null),
    Armenian("ARM", "HYE", "HY"),
    Aromanian("RUP", "RUP", null),
    Artificial("ART", "ART", null),
    Assamese("ASM", "ASM", "AS"),
    Asturian("AST", "AST", null),
    Athapascan("ATH", "ATH", null),
    Australian("AUS", "AUS", null),
    Austronesian("MAP", "MAP", null),
    Avaric("AVA", "AVA", "AV"),
    Avestan("AVE", "AVE", "AE"),
    Awadhi("AWA", "AWA", null),
    Aymara("AYM", "AYM", "AY"),
    Azerbaijani("AZE", "AZE", "AZ"),
    Balinese("BAN", "BAN", null),
    Baltic("BAT", "BAT", null),
    Baluchi("BAL", "BAL", null),
    Bambara("BAM", "BAM", "BM"),
    Bamileke("BAI", "BAI", null),
    Banda("BAD", "BAD", null),
    Bantu("BNT", "BNT", null),
    Basa("BAS", "BAS", null),
    Bashkir("BAK", "BAK", "BA"),
    Basque("BAQ", "EUS", "EU"),
    Batak("BTK", "BTK", null),
    Beja("BEJ", "BEJ", null),
    Belarusian("BEL", "BEL", "BE"),
    Bemba("BEM", "BEM", null),
    Bengali("BEN", "BEN", "BN"),
    Berber("BER", "BER", null),
    Bhojpuri("BHO", "BHO", null),
    Bihari("BIH", "BIH", "BH"),
    Bikol("BIK", "BIK", null),
    Bini("BIN", "BIN", null),
    Bislama("BIS", "BIS", "BI"),
    Blin("BYN", "BYN", null),
    Blissymbols("ZBL", "ZBL", null),
    BokmalNorwegian("NOB", "NOB", "NB"),
    Bosnian("BOS", "BOS", "BS"),
    Braj("BRA", "BRA", null),
    Breton("BRE", "BRE", "BR"),
    Buginese("BUG", "BUG", null),
    Bulgarian("BUL", "BUL", "BG"),
    Buriat("BUA", "BUA", null),
    Burmese("BUR", "MYA", "MY"),
    Caddo("CAD", "CAD", null),
    Catalan("CAT", "CAT", "CA"),
    Caucasian("CAU", "CAU", null),
    Cebuano("CEB", "CEB", null),
    Celtic("CEL", "CEL", null),
    CentralAmericanIndian("CAI", "CAI", null),
    CentralKhmer("KHM", "KHM", "KM"),
    Chagatai("CHG", "CHG", null),
    Chamic("CMC", "CMC", null),
    Chamorro("CHA", "CHA", "CH"),
    Chechen("CHE", "CHE", "CE"),
    Cherokee("CHR", "CHR", null),
    Cheyenne("CHY", "CHY", null),
    Chibcha("CHB", "CHB", null),
    Chichewa("NYA", "NYA", "NY"),
    Chinese("CHI", "ZHO", "ZH"),
    ChinookJargon("CHN", "CHN", null),
    Chipewyan("CHP", "CHP", null),
    Choctaw("CHO", "CHO", null),
    ChurchSlavic("CHU", "CHU", "CU"),
    Chuukese("CHK", "CHK", null),
    Chuvash("CHV", "CHV", "CV"),
    ClassicalNewari("NWC", "NWC", null),
    ClassicalSyriac("SYC", "SYC", null),
    Coptic("COP", "COP", null),
    Cornish("COR", "COR", "KW"),
    Corsican("COS", "COS", "CO"),
    Cree("CRE", "CRE", "CR"),
    Creek("MUS", "MUS", null),
    CreoleFrench("CPF", "CPF", null),
    CreoleEnglish("CPE", "CPE", null),
    CreolePortuguese("CPP", "CPP", null),
    CreolesPidgins("CRP", "CRP", null),
    CrimeanTatar("CRH", "CRH", null),
    Croatian("HRV", "HRV", "HR"),
    Cushitic("CUS", "CUS", null),
    Czech("CZE", "CES", "CS"),
    Dakota("DAK", "DAK", null),
    Danish("DAN", "DAN", "DA"),
    Dargwa("DAR", "DAR", null),
    Delaware("DEL", "DEL", null),
    Dinka("DIN", "DIN", null),
    Divehi("DIV", "DIV", "DV"),
    Dogri("DOI", "DOI", null),
    Dogrib("DGR", "DGR", null),
    Dravidian("DRA", "DRA", null),
    Duala("DUA", "DUA", null),
    Dutch("DUT", "NLD", "NL"),
    Dyula("DYU", "DYU", null),
    Dzongkha("DZO", "DZO", "DZ"),
    EasternFrisian("FRS", "FRS", null),
    Efik("EFI", "EFI", null),
    Egyptian("EGY", "EGY", null),
    Ekajuk("EKA", "EKA", null),
    Elamite("ELX", "ELX", null),
    English("ENG", "ENG", "EN"),
    Erzya("MYV", "MYV", null),
    Esperanto("EPO", "EPO", "EO"),
    Estonian("EST", "EST", "ET"),
    Ewe("EWE", "EWE", "EE"),
    Ewondo("EWO", "EWO", null),
    Fang("FAN", "FAN", null),
    Fanti("FAT", "FAT", null),
    Faroese("FAO", "FAO", "FO"),
    Fijian("FIJ", "FIJ", "FJ"),
    Filipino("FIL", "FIL", null),
    FinnoUgrian("FIU", "FIU", null),
    Fon("FON", "FON", null),
    French("FRE", "FRA", "FR"),
    Friulian("FUR", "FUR", null),
    Fulah("FUL", "FUL", "FF"),
    Ga("GAA", "GAA", null),
    Gaelic("GLA", "GLA", "GD"),
    GalibiCarib("CAR", "CAR", null),
    Galician("GLG", "GLG", "GL"),
    Ganda("LUG", "LUG", "LG"),
    Gayo("GAY", "GAY", null),
    Gbaya("GBA", "GBA", null),
    Geez("GEZ", "GEZ", null),
    Georgian("GEO", "KAT", "KA"),
    German("GER", "DEU", "DE"),
    Germanic("GEM", "GEM", null),
    Gilbertese("GIL", "GIL", null),
    Gondi("GON", "GON", null),
    Gorontalo("GOR", "GOR", null),
    Gothic("GOT", "GOT", null),
    Grebo("GRB", "GRB", null),
    Guarani("GRN", "GRN", "GN"),
    Gujarati("GUJ", "GUJ", "GU"),
    GwichIn("GWI", "GWI", null),
    Haida("HAI", "HAI", null),
    Haitian("HAT", "HAT", "HT"),
    Hausa("HAU", "HAU", "HA"),
    Hawaiian("HAW", "HAW", null),
    Hebrew("HEB", "HEB", "HE"),
    Herero("HER", "HER", "HZ"),
    Hiligaynon("HIL", "HIL", null),
    Himachali("HIM", "HIM", null),
    Hindi("HIN", "HIN", "HI"),
    HiriMotu("HMO", "HMO", "HO"),
    Hittite("HIT", "HIT", null),
    Hmong("HMN", "HMN", null),
    Hungarian("HUN", "HUN", "HU"),
    Hupa("HUP", "HUP", null),
    Iban("IBA", "IBA", null),
    Icelandic("ICE", "ISL", "IS"),
    Ido("IDO", "IDO", "IO"),
    Igbo("IBO", "IBO", "IG"),
    Ijo("IJO", "IJO", null),
    InariSami("SMN", "SMN", null),
    Indic("INC", "INC", null),
    IndoEuropean("INE", "INE", null),
    Indonesian("IND", "IND", "ID"),
    Ingush("INH", "INH", null),
    Interlingua("INA", "INA", "IA"),
    Interlingue("ILE", "ILE", "IE"),
    Inuktitut("IKU", "IKU", "IU"),
    Inupiaq("IPK", "IPK", "IK"),
    Iloko("ILO", "ILO", null),
    Iranian("IRA", "IRA", null),
    Irish("GLE", "GLE", "GA"),
    Iroquoian("IRO", "IRO", null),
    Italian("ITA", "ITA", "IT"),
    JudeoArabic("JRB", "JRB", null),
    JudeoPersian("JPR", "JPR", null),
    Japanese("JPN", "JPN", "JA"),
    Javanese("JAV", "JAV", "JV"),
    Kabardian("KBD", "KBD", null),
    Kabyle("KAB", "KAB", null),
    Kachin("KAC", "KAC", null),
    Kalaallisut("KAL", "KAL", "KL"),
    Kalmyk("XAL", "XAL", null),
    Kamba("KAM", "KAM", null),
    Kannada("KAN", "KAN", "KN"),
    Kanuri("KAU", "KAU", "KR"),
    Kashubian("CSB", "CSB", null),
    KarachayBalkar("KRC", "KRC", null),
    KaraKalpak("KAA", "KAA", null),
    Karelian("KRL", "KRL", null),
    Kawi("KAW", "KAW", null),
    Kashmiri("KAS", "KAS", "KS"),
    Karen("KAR", "KAR", null),
    Kazakh("KAZ", "KAZ", "KK"),
    Khasi("KHA", "KHA", null),
    Khoisan("KHI", "KHI", null),
    Khotanese("KHO", "KHO", null),
    Kikuyu("KIK", "KIK", "KI"),
    Kimbundu("KMB", "KMB", null),
    Kinyarwanda("KIN", "KIN", "RW"),
    Kirghiz("KIR", "KIR", "KY"),
    Klingon("TLH", "TLH", null),
    Kongo("KON", "KON", "KG"),
    Konkani("KOK", "KOK", null),
    Korean("KOR", "KOR", "KO"),
    Kosraean("KOS", "KOS", null),
    Kpelle("KPE", "KPE", null),
    Kru("KRO", "KRO", null),
    Kuanyama("KUA", "KUA", "KJ"),
    Kumyk("KUM", "KUM", null),
    Kurdish("KUR", "KUR", "KU"),
    Kurukh("KRU", "KRU", null),
    Kutenai("KUT", "KUT", null),
    Ladino("LAD", "LAD", null),
    Lahnda("LAH", "LAH", null),
    Lamba("LAM", "LAM", null),
    LandDayak("DAY", "DAY", null),
    Lao("LAO", "LAO", "LO"),
    Latin("LAT", "LAT", "LA"),
    Latvian("LAV", "LAV", "LV"),
    Lezghian("LEZ", "LEZ", null),
    Limburgan("LIM", "LIM", "LI"),
    Lingala("LIN", "LIN", "LN"),
    Lithuanian("LIT", "LIT", "LT"),
    Lojban("JBO", "JBO", null),
    LowerSorbian("DSB", "DSB", null),
    LowGerman("NDS", "NDS", null),
    Lozi("LOZ", "LOZ", null),
    Luiseno("LUI", "LUI", null),
    LubaKatanga("LUB", "LUB", "LU"),
    LubaLulua("LUA", "LUA", null),
    LuleSami("SMJ", "SMJ", null),
    Lunda("LUN", "LUN", null),
    Luo("LUO", "LUO", null),
    Lushai("LUS", "LUS", null),
    Luxembourgish("LTZ", "LTZ", "LB"),
    Mapudungun("ARN", "ARN", null),
    Mari("CHM", "CHM", null),
    MiddleDutch("DUM", "DUM", null),
    ModernGreek("GRE", "ELL", "EL"),
    Macedonian("MAC", "MKD", "MK"),
    Madurese("MAD", "MAD", null),
    Magahi("MAG", "MAG", null),
    Manx("GLV", "GLV", "GV"),
    MiddleHightGerman("GMH", "GMH", null),
    Mirandese("MWL", "MWL", null),
    Marwari("MWR", "MWR", null),
    Mayan("MYN", "MYN", null),
    Mongo("LOL", "LOL", null),
    Marshallese("MAH", "MAH", "MH"),
    Maithili("MAI", "MAI", null),
    Makasar("MAK", "MAK", null),
    Malayalam("MAL", "MAL", "ML"),
    Mandingo("MAN", "MAN", null),
    Maori("MAO", "MRI", "MI"),
    Marathi("MAR", "MAR", "MR"),
    Masai("MAS", "MAS", null),
    Malay("MAY", "MSA", "MS"),
    Moksha("MDF", "MDF", null),
    Mandar("MDR", "MDR", null),
    Mende("MEN", "MEN", null),
    MiddleIrish("MGA", "MGA", null),
    MiKmaq("MIC", "MIC", null),
    Minangkabau("MIN", "MIN", null),
    MonKhmer("MKH", "MKH", null),
    Malagasy("MLG", "MLG", "MG"),
    Maltese("MLT", "MLT", "MT"),
    Manchu("MNC", "MNC", null),
    Manipuri("MNI", "MNI", null),
    Manobo("MNO", "MNO", null),
    Mohawk("MOH", "MOH", null),
    Mongolian("MON", "MON", "MN"),
    Mossi("MOS", "MOS", null),
    Multiple("MUL", "MUL", null),
    Munda("MUN", "MUN", null),
    MiddleEnglish("ENM", "ENM", null),
    MiddleFrench("FRM", "FRM", null),
    Montenegrin("CNR", "CNR", null),
    Nahuatl("NAH", "NAH", null),
    NorthAmericanIndian("NAI", "NAI", null),
    Neapolitan("NAP", "NAP", null),
    Nauru("NAU", "NAU", "NA"),
    Navajo("NAV", "NAV", "NV"),
    SouthNdebele("NBL", "NBL", "NR"),
    NorthNdebele("NDE", "NDE", "ND"),
    Ndonga("NDO", "NDO", "NG"),
    Nepali("NEP", "NEP", "NE"),
    NepalBhasa("NEW", "NEW", null),
    Nias("NIA", "NIA", null),
    NigerKordofanian("NIC", "NIC", null),
    Niuean("NIU", "NIU", null),
    NorwegianNynorsk("NNO", "NNO", "NN"),
    Nogai("NOG", "NOG", null),
    Norwegian("NOR", "NOR", "NO"),
    NKo("NQO", "NQO", null),
    Nubian("NUB", "NUB", null),
    Nyamwezi("NYM", "NYM", null),
    NorthernSami("SME", "SME", "SE"),
    NorthernFrisian("FRR", "FRR", null),
    NiloSaharan("SSA", "SSA", null),
    Nyankole("NYN", "NYN", null),
    Nyoro("NYO", "NYO", null),
    Nzima("NZI", "NZI", null),
    OldEnglish("ANG", "ANG", null),
    OfficialAramaic("ARC", "ARC", null),
    OldFrench("FRO", "FRO", null),
    Occitan("OCI", "OCI", "OC"),
    Ojibwa("OJI", "OJI", "OJ"),
    Oriya("ORI", "ORI", "OR"),
    Oromo("ORM", "ORM", "OM"),
    OldNorse("NON", "NON", null),
    OldHighGerman("GOH", "GOH", null),
    OldIrish("SGA", "SGA", null),
    Osage("OSA", "OSA", null),
    Ossetian("OSS", "OSS", "OS"),
    OttomanTurkish("OTA", "OTA", null),
    Otomian("OTO", "OTO", null),
    OldPersian("PEO", "PEO", null),
    OldProvencal("PRO", "PRO", null),
    Persian("PER", "FAS", "FA"),
    Philippine("PHI", "PHI", null),
    Pedi("NSO", "NSO", null),
    Phoenician("PHN", "PHN", null),
    Pali("PLI", "PLI", "PI"),
    Polish("POL", "POL", "PL"),
    Pohnpeian("PON", "PON", null),
    Portuguese("POR", "POR", "PT"),
    Prakrit("PRA", "PRA", null),
    Pushto("PUS", "PUS", "PS"),
    Papuan("PAA", "PAA", null),
    Pangasinan("PAG", "PAG", null),
    Pahlavi("PAL", "PAL", null),
    Pampanga("PAM", "PAM", null),
    Panjabi("PAN", "PAN", "PA"),
    Papiamento("PAP", "PAP", null),
    Palauan("PAU", "PAU", null),
    Quechua("QUE", "QUE", "QU"),
    Rajasthani("RAJ", "RAJ", null),
    Rapanui("RAP", "RAP", null),
    Rarotongan("RAR", "RAR", null),
    Romance("ROA", "ROA", null),
    Romansh("ROH", "ROH", "RM"),
    Romany("ROM", "ROM", null),
    Romanian("RUM", "RON", "RO"),
    Rundi("RUN", "RUN", "RN"),
    Russian("RUS", "RUS", "RU"),
    Syriac("SYR", "SYR", null),
    Sorbian("WEN", "WEN", null),
    SouthernAltai("ALT", "ALT", null),
    Siksika("BLA", "BLA", null),
    Slave("DEN", "DEN", null),
    SichuanYi("III", "III", "II"),
    Sardinian("SRD", "SRD", "SC"),
    SrananTongo("SRN", "SRN", null),
    Swati("SSW", "SSW", "SS"),
    SwissGerman("GSW", "GSW", null),
    Sandawe("SAD", "SAD", null),
    Sango("SAG", "SAG", "SG"),
    Sami("SMI", "SMI", null),
    SouthAmericanIndian("SAI", "SAI", null),
    Salishan("SAL", "SAL", null),
    SamaritanAramaic("SAM", "SAM", null),
    Sanskrit("SAN", "SAN", "SA"),
    Sasak("SAS", "SAS", null),
    Santali("SAT", "SAT", null),
    Sicilian("SCN", "SCN", null),
    Scots("SCO", "SCO", null),
    Selkup("SEL", "SEL", null),
    Semitic("SEM", "SEM", null),
    Sukuma("SUK", "SUK", null),
    Sundanese("SUN", "SUN", "SU"),
    Susu("SUS", "SUS", null),
    SignLanguage("SGN", "SGN", null),
    Shan("SHN", "SHN", null),
    Sidamo("SID", "SID", null),
    Sinhala("SIN", "SIN", "SI"),
    Siouan("SIO", "SIO", null),
    SinoTibetan("SIT", "SIT", null),
    Slavic("SLA", "SLA", null),
    Slovak("SLO", "SLK", "SK"),
    Slovenian("SLV", "SLV", "SL"),
    SouthernSami("SMA", "SMA", null),
    Samoan("SMO", "SMO", "SM"),
    SkoltSami("SMS", "SMS", null),
    Shona("SNA", "SNA", "SN"),
    Sindhi("SND", "SND", "SD"),
    Soninke("SNK", "SNK", null),
    Sogdian("SOG", "SOG", null),
    Somali("SOM", "SOM", "SO"),
    Songhai("SON", "SON", null),
    Sotho("SOT", "SOT", "ST"),
    Spanish("SPA", "SPA", "ES"),
    Sumerian("SUX", "SUX", null),
    Swahili("SWA", "SWA", "SW"),
    Swedish("SWE", "SWE", "SV"),
    Serbian("SRP", "SRP", "SR"),
    Serer("SRR", "SRR", null),
    StandardMoroccanTamazight("ZGH", "ZGH", null),
    Tlingit("TLI", "TLI", null),
    Tamashek("TMH", "TMH", null),
    TongaNyasa("TOG", "TOG", null),
    TongaIslands("TON", "TON", "TO"),
    TokPisin("TPI", "TPI", null),
    Tahitian("TAH", "TAH", "TY"),
    Tai("TAI", "TAI", null),
    Tamil("TAM", "TAM", "TA"),
    Tatar("TAT", "TAT", "TT"),
    Telugu("TEL", "TEL", "TE"),
    Timne("TEM", "TEM", null),
    Tereno("TER", "TER", null),
    Tetum("TET", "TET", null),
    Tajik("TGK", "TGK", "TG"),
    Tagalog("TGL", "TGL", "TL"),
    Thai("THA", "THA", "TH"),
    Tibetan("TIB", "BOD", "BO"),
    Tigre("TIG", "TIG", null),
    Tigrinya("TIR", "TIR", "TI"),
    Tiv("TIV", "TIV", null),
    Tokelau("TKL", "TKL", null),
    Tsimshian("TSI", "TSI", null),
    Tswana("TSN", "TSN", "TN"),
    Tsonga("TSO", "TSO", "TS"),
    Turkmen("TUK", "TUK", "TK"),
    Tumbuka("TUM", "TUM", null),
    Tupi("TUP", "TUP", null),
    Turkish("TUR", "TUR", "TR"),
    Tuvalu("TVL", "TVL", null),
    Twi("TWI", "TWI", "TW"),
    Tuvinian("TYV", "TYV", null),
    Udmurt("UDM", "UDM", null),
    Ugaritic("UGA", "UGA", null),
    Uighur("UIG", "UIG", "UG"),
    Ukrainian("UKR", "UKR", "UK"),
    Umbundu("UMB", "UMB", null),
    UpperSorbian("HSB", "HSB", null),
    Urdu("URD", "URD", "UR"),
    Uzbek("UZB", "UZB", "UZ"),
    Vai("VAI", "VAI", null),
    Venda("VEN", "VEN", "VE"),
    Vietnamese("VIE", "VIE", "VI"),
    Volapük("VOL", "VOL", "VO"),
    Votic("VOT", "VOT", null),
    Wakashan("WAK", "WAK", null),
    Wolaitta("WAL", "WAL", null),
    Walloon("WLN", "WLN", "WA"),
    Wolof("WOL", "WOL", "WO"),
    WesternFrisian("FRY", "FRY", "FY"),
    Waray("WAR", "WAR", null),
    Washo("WAS", "WAS", null),
    Welsh("WEL", "CYM", "CY"),
    Xhosa("XHO", "XHO", "XH"),
    Yao("YAO", "YAO", null),
    Yapese("YAP", "YAP", null),
    Yiddish("YID", "YID", "YI"),
    Yakut("SAH", "SAH", null),
    Yoruba("YOR", "YOR", "YO"),
    Yupik("YPK", "YPK", null),
    Zapotec("ZAP", "ZAP", null),
    Zhuang("ZHA", "ZHA", "ZA"),
    Zande("ZND", "ZND", null),
    Zenaga("ZEN", "ZEN", null),
    Zulu("ZUL", "ZUL", "ZU"),
    Zuni("ZUN", "ZUN", null),
    Zaza("ZZA", "ZZA", null);

    private String bibliography;
    private String terminology;
    private String alpha2;

    Iso639(String bib, String term, String al2) {
        bibliography = bib;
        terminology  = term;
        alpha2       = al2;
        LocalData.bibliography.put(bib, this);
        LocalData.terminology.put(term, this);
        LocalData.alpha2.put(al2, this);
        LocalData.alpha3.put(bib, this);
        LocalData.alpha3.put(term, this);
    }

    @NotBlank
    public String getBibliography() {
        return bibliography;
    }

    @NotBlank
    public String getTerminology() {
        return terminology;
    }

    public String getAlpha2() {
        return alpha2;
    }

    @Override
    public boolean test(String code) {
        var result = false;
        if (code != null) {
            result = bibliography.equalsIgnoreCase(code) || terminology.equalsIgnoreCase(code);
            if ((!result) && (alpha2 != null)) {
                result = alpha2.equalsIgnoreCase(code);
            }
        }
        return result;
    }

    /**
     * Returns the iso value for a bibliography alpha-3 code.
     *
     * @param bibliography
     *            The bibliography alpha-3 code which value shall be searched for (bibliography).
     * @return The iso value.
     */
    @NotNull
    public static Optional<Iso639> findByBibliography(String bibliography) {
        return findBy(LocalData.bibliography, bibliography);
    }

    /**
     * Returns the iso value for a terminology alpha-3 code.
     *
     * @param terminology
     *            The terminology alpha-3 code which value shall be searched for (terminology).
     * @return The iso value.
     */
    @NotNull
    public static Optional<Iso639> findByTerminology(String terminology) {
        return findBy(LocalData.terminology, terminology);
    }

    /**
     * Returns the iso value for a specific alpha-3 code.
     *
     * @param alpha3
     *            The alpha-3 code which value shall be searched for (either bibliography of
     *            terminology).
     * @return The iso value.
     */
    @NotNull
    public static Optional<Iso639> findByAlpha3(String alpha3) {
        return findBy(LocalData.alpha3, alpha3);
    }

    /**
     * Returns the iso value for a specific alpha-2 code.
     *
     * @param alpha2
     *            The alpha-2 code which value shall be searched for.
     * @return The iso value.
     */
    @NotNull
    public static Optional<Iso639> findByAlpha2(String alpha2) {
        return findBy(LocalData.alpha2, alpha2);
    }

    @NotNull
    private static Optional<Iso639> findBy(@NotNull Map<String, Iso639> map, String key) {
        if (key != null) {
            return Optional.ofNullable(map.get(key));
        }
        return Optional.empty();
    }

    private static class LocalData {

        private static Map<String, Iso639> alpha2       = new HashMap<>();

        private static Map<String, Iso639> alpha3       = new HashMap<>();

        private static Map<String, Iso639> terminology  = new HashMap<>();

        private static Map<String, Iso639> bibliography = new HashMap<>();

    } /* ENDCLASS */

} /* ENDENUM */
