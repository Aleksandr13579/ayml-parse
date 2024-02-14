/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2023 Apryse Group NV
    Authors: Apryse Software.

    This program is offered under a commercial and under the AGPL license.
    For commercial licensing, contact us at https://itextpdf.com/sales.  For AGPL licensing, see below.

    AGPL licensing:
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.font.otf;

/**
 * Constants corresponding to language tags in the OTF specification.
 * Extracted from the specification, as published by Microsoft
 * <a href="https://docs.microsoft.com/en-us/typography/opentype/spec/languagetags">here</a>.
 * Note that tags in OTF always consist of exactly 4 bytes. Shorter
 * identifiers are padded with spaces as necessary.
 *
 * @author <a href="mailto:matthias.valvekens@itextpdf.com">Matthias Valvekens</a>
 */
public final class LanguageTags {
    public static final String ABAZA = "ABA ";
    public static final String ABKHAZIAN = "ABK ";
    public static final String ACHOLI = "ACH ";
    public static final String ACHI = "ACR ";
    public static final String ADYGHE = "ADY ";
    public static final String AFRIKAANS = "AFK ";
    public static final String AFAR = "AFR ";
    public static final String AGAW = "AGW ";
    public static final String AITON = "AIO ";
    public static final String AKAN = "AKA ";
    public static final String ALSATIAN = "ALS ";
    public static final String ALTAI = "ALT ";
    public static final String AMHARIC = "AMH ";
    public static final String ANGLO_SAXON = "ANG ";
    public static final String PHONETIC_AMERICANIST = "APPH";
    public static final String ARABIC = "ARA ";
    public static final String ARAGONESE = "ARG ";
    public static final String AARI = "ARI ";
    public static final String RAKHINE = "ARK ";
    public static final String ASSAMESE = "ASM ";
    public static final String ASTURIAN = "AST ";
    public static final String ATHAPASKAN = "ATH ";
    public static final String AVAR = "AVR ";
    public static final String AWADHI = "AWA ";
    public static final String AYMARA = "AYM ";
    public static final String TORKI = "AZB ";
    public static final String AZERBAIJANI = "AZE ";
    public static final String BADAGA = "BAD ";
    public static final String BANDA = "BAD0";
    public static final String BAGHELKHANDI = "BAG ";
    public static final String BALKAR = "BAL ";
    public static final String BALINESE = "BAN ";
    public static final String BAVARIAN = "BAR ";
    public static final String BAULE = "BAU ";
    public static final String BATAK_TOBA = "BBC ";
    public static final String BERBER = "BBR ";
    public static final String BENCH = "BCH ";
    public static final String BIBLE_CREE = "BCR ";
    public static final String BANDJALANG = "BDY ";
    public static final String BELARUSSIAN = "BEL ";
    public static final String BEMBA = "BEM ";
    public static final String BENGALI = "BEN ";
    public static final String HARYANVI = "BGC ";
    public static final String BAGRI = "BGQ ";
    public static final String BULGARIAN = "BGR ";
    public static final String BHILI = "BHI ";
    public static final String BHOJPURI = "BHO ";
    public static final String BIKOL = "BIK ";
    public static final String BILEN = "BIL ";
    public static final String BISLAMA = "BIS ";
    public static final String KANAUJI = "BJJ ";
    public static final String BLACKFOOT = "BKF ";
    public static final String BALUCHI = "BLI ";
    public static final String PAO_KAREN = "BLK ";
    public static final String BALANTE = "BLN ";
    public static final String BALTI = "BLT ";
    public static final String BAMBARA = "BMB ";
    public static final String BAMILEKE = "BML ";
    public static final String BOSNIAN = "BOS ";
    public static final String BISHNUPRIYA_MANIPURI = "BPY ";
    public static final String BRETON = "BRE ";
    public static final String BRAHUI = "BRH ";
    public static final String BRAJ_BHASHA = "BRI ";
    public static final String BURMESE = "BRM ";
    public static final String BODO = "BRX ";
    public static final String BASHKIR = "BSH ";
    public static final String BURUSHASKI = "BSK ";
    public static final String BETI = "BTI ";
    public static final String BATAK_SIMALUNGUN = "BTS ";
    public static final String BUGIS = "BUG ";
    public static final String MEDUMBA = "BYV ";
    public static final String KAQCHIKEL = "CAK ";
    public static final String CATALAN = "CAT ";
    public static final String ZAMBOANGA_CHAVACANO = "CBK ";
    public static final String CHINANTEC = "CCHN";
    public static final String CEBUANO = "CEB ";
    public static final String CHECHEN = "CHE ";
    public static final String CHAHA_GURAGE = "CHG ";
    public static final String CHATTISGARHI = "CHH ";
    public static final String CHICHEWA = "CHI ";
    public static final String CHUKCHI = "CHK ";
    public static final String CHUUKESE = "CHK0";
    public static final String CHOCTAW = "CHO ";
    public static final String CHIPEWYAN = "CHP ";
    public static final String CHEROKEE = "CHR ";
    public static final String CHAMORRO = "CHA ";
    public static final String CHUVASH = "CHU ";
    public static final String CHEYENNE = "CHY ";
    public static final String CHIGA = "CGG ";
    public static final String WESTERN_CHAM = "CJA ";
    public static final String EASTERN_CHAM = "CJM ";
    public static final String COMORIAN = "CMR ";
    public static final String COPTIC = "COP ";
    public static final String CORNISH = "COR ";
    public static final String CORSICAN = "COS ";
    public static final String CREOLES = "CPP ";
    public static final String CREE = "CRE ";
    public static final String CARRIER = "CRR ";
    public static final String CRIMEAN_TATAR = "CRT ";
    public static final String KASHUBIAN = "CSB ";
    public static final String CHURCH_SLAVONIC = "CSL ";
    public static final String CZECH = "CSY ";
    public static final String CHITTAGONIAN = "CTG ";
    public static final String SAN_BLAS_KUNA = "CUK ";
    public static final String DANISH = "DAN ";
    public static final String DARGWA = "DAR ";
    public static final String DAYI = "DAX ";
    public static final String WOODS_CREE = "DCR ";
    public static final String GERMAN = "DEU ";
    public static final String DOGRI = "DGO ";
    public static final String DOGRI2 = "DGR ";
    public static final String DHANGU = "DHG ";
    public static final String DHIVEHI = "DHV ";
    public static final String DIMLI = "DIQ ";
    public static final String DIVEHI = "DIV ";
    public static final String ZARMA = "DJR ";
    public static final String DJAMBARRPUYNGU = "DJR0";
    public static final String DANGME = "DNG ";
    public static final String DAN = "DNJ ";
    public static final String DINKA = "DNK ";
    public static final String DARI = "DRI ";
    public static final String DHUWAL = "DUJ ";
    public static final String DUNGAN = "DUN ";
    public static final String DZONGKHA = "DZN ";
    public static final String EBIRA = "EBI ";
    public static final String EASTERN_CREE = "ECR ";
    public static final String EDO = "EDO ";
    public static final String EFIK = "EFI ";
    public static final String GREEK = "ELL ";
    public static final String EASTERN_MANINKAKAN = "EMK ";
    public static final String ENGLISH = "ENG ";
    public static final String ERZYA = "ERZ ";
    public static final String SPANISH = "ESP ";
    public static final String CENTRAL_YUPIK = "ESU ";
    public static final String ESTONIAN = "ETI ";
    public static final String BASQUE = "EUQ ";
    public static final String EVENKI = "EVK ";
    public static final String EVEN = "EVN ";
    public static final String EWE = "EWE ";
    public static final String FRENCH_ANTILLEAN = "FAN ";
    public static final String FANG = "FAN0";
    public static final String PERSIAN = "FAR ";
    public static final String FANTI = "FAT ";
    public static final String FINNISH = "FIN ";
    public static final String FIJIAN = "FJI ";
    public static final String DUTCH_FLEMISH = "FLE ";
    public static final String FEFE = "FMP ";
    public static final String FOREST_NENETS = "FNE ";
    public static final String FON = "FON ";
    public static final String FAROESE = "FOS ";
    public static final String FRENCH = "FRA ";
    public static final String CAJUN_FRENCH = "FRC ";
    public static final String FRISIAN = "FRI ";
    public static final String FRIULIAN = "FRL ";
    public static final String ARPITAN = "FRP ";
    public static final String FUTA = "FTA ";
    public static final String FULAH = "FUL ";
    public static final String NIGERIAN_FULFULDE = "FUV ";
    public static final String GA = "GAD ";
    public static final String SCOTTISH_GAELIC = "GAE ";
    public static final String GAGAUZ = "GAG ";
    public static final String GALICIAN = "GAL ";
    public static final String GARSHUNI = "GAR ";
    public static final String GARHWALI = "GAW ";
    public static final String GEEZ = "GEZ ";
    public static final String GITHABUL = "GIH ";
    public static final String GILYAK = "GIL ";
    public static final String KIRIBATI_GILBERTESE = "GIL0";
    public static final String KPELLE_GUINEA = "GKP ";
    public static final String GILAKI = "GLK ";
    public static final String GUMUZ = "GMZ ";
    public static final String GUMATJ = "GNN ";
    public static final String GOGO = "GOG ";
    public static final String GONDI = "GON ";
    public static final String GREENLANDIC = "GRN ";
    public static final String GARO = "GRO ";
    public static final String GUARANI = "GUA ";
    public static final String WAYUU = "GUC ";
    public static final String GUPAPUYNGU = "GUF ";
    public static final String GUJARATI = "GUJ ";
    public static final String GUSII = "GUZ ";
    public static final String HAITIAN_CREOLE = "HAI ";
    public static final String HALAM_FALAM_CHIN = "HAL ";
    public static final String HARAUTI = "HAR ";
    public static final String HAUSA = "HAU ";
    public static final String HAWAIIAN = "HAW ";
    public static final String HAYA = "HAY ";
    public static final String HAZARAGI = "HAZ ";
    public static final String HAMMER_BANNA = "HBN ";
    public static final String HERERO = "HER ";
    public static final String HILIGAYNON = "HIL ";
    public static final String HINDI = "HIN ";
    public static final String HIGH_MARI = "HMA ";
    public static final String HMONG = "HMN ";
    public static final String HIRI_MOTU = "HMO ";
    public static final String HINDKO = "HND ";
    public static final String HO = "HO  ";
    public static final String HARARI = "HRI ";
    public static final String CROATIAN = "HRV ";
    public static final String HUNGARIAN = "HUN ";
    public static final String ARMENIAN = "HYE ";
    public static final String ARMENIAN_EAST = "HYE0";
    public static final String IBAN = "IBA ";
    public static final String IBIBIO = "IBB ";
    public static final String IGBO = "IBO ";
    public static final String IJO_LANGUAGES = "IJO ";
    public static final String IDO = "IDO ";
    public static final String INTERLINGUE = "ILE ";
    public static final String ILOKANO = "ILO ";
    public static final String INTERLINGUA = "INA ";
    public static final String INDONESIAN = "IND ";
    public static final String INGUSH = "ING ";
    public static final String INUKTITUT = "INU ";
    public static final String INUPIAT = "IPK ";
    public static final String PHONETIC_TRANSCRIPTION_IPA = "IPPH";
    public static final String IRISH = "IRI ";
    public static final String IRISH_TRADITIONAL = "IRT ";
    public static final String ICELANDIC = "ISL ";
    public static final String INARI_SAMI = "ISM ";
    public static final String ITALIAN = "ITA ";
    public static final String HEBREW = "IWR ";
    public static final String JAMAICAN_CREOLE = "JAM ";
    public static final String JAPANESE = "JAN ";
    public static final String JAVANESE = "JAV ";
    public static final String LOJBAN = "JBO ";
    public static final String KRYMCHAK = "JCT ";
    public static final String YIDDISH = "JII ";
    public static final String LADINO = "JUD ";
    public static final String JULA = "JUL ";
    public static final String KABARDIAN = "KAB ";
    public static final String KABYLE = "KAB0";
    public static final String KACHCHI = "KAC ";
    public static final String KALENJIN = "KAL ";
    public static final String KANNADA = "KAN ";
    public static final String KARACHAY = "KAR ";
    public static final String GEORGIAN = "KAT ";
    public static final String KAZAKH = "KAZ ";
    public static final String MAKONDE = "KDE ";
    public static final String KABUVERDIANU_CRIOULO = "KEA ";
    public static final String KEBENA = "KEB ";
    public static final String KEKCHI = "KEK ";
    public static final String KHUTSURI_GEORGIAN = "KGE ";
    public static final String KHAKASS = "KHA ";
    public static final String KHANTY_KAZIM = "KHK ";
    public static final String KHMER = "KHM ";
    public static final String KHANTY_SHURISHKAR = "KHS ";
    public static final String KHAMTI_SHAN = "KHT ";
    public static final String KHANTY_VAKHI = "KHV ";
    public static final String KHOWAR = "KHW ";
    public static final String KIKUYU = "KIK ";
    public static final String KIRGHIZ = "KIR ";
    public static final String KISII = "KIS ";
    public static final String KIRMANJKI = "KIU ";
    public static final String SOUTHERN_KIWAI = "KJD ";
    public static final String EASTERN_PWO_KAREN = "KJP ";
    public static final String BUMTHANGKHA = "KJZ ";
    public static final String KOKNI = "KKN ";
    public static final String KALMYK = "KLM ";
    public static final String KAMBA = "KMB ";
    public static final String KUMAONI = "KMN ";
    public static final String KOMO = "KMO ";
    public static final String KOMSO = "KMS ";
    public static final String KHORASANI_TURKIC = "KMZ ";
    public static final String KANURI = "KNR ";
    public static final String KODAGU = "KOD ";
    public static final String KOREAN_OLD_HANGUL = "KOH ";
    public static final String KONKANI = "KOK ";
    public static final String KIKONGO = "KON ";
    public static final String KOMI = "KOM ";
    public static final String KONGO = "KON0";
    public static final String KOMI_PERMYAK = "KOP ";
    public static final String KOREAN = "KOR ";
    public static final String KOSRAEAN = "KOS ";
    public static final String KOMI_ZYRIAN = "KOZ ";
    public static final String KPELLE = "KPL ";
    public static final String KRIO = "KRI ";
    public static final String KARAKALPAK = "KRK ";
    public static final String KARELIAN = "KRL ";
    public static final String KARAIM = "KRM ";
    public static final String KAREN = "KRN ";
    public static final String KOORETE = "KRT ";
    public static final String KASHMIRI = "KSH ";
    public static final String RIPUARIAN = "KSH0";
    public static final String KHASI = "KSI ";
    public static final String KILDIN_SAMI = "KSM ";
    public static final String SGAW_KAREN = "KSW ";
    public static final String KUANYAMA = "KUA ";
    public static final String KUI = "KUI ";
    public static final String KULVI = "KUL ";
    public static final String KUMYK = "KUM ";
    public static final String KURDISH = "KUR ";
    public static final String KURUKH = "KUU ";
    public static final String KUY = "KUY ";
    public static final String KORYAK = "KYK ";
    public static final String WESTERN_KAYAH = "KYU ";
    public static final String LADIN = "LAD ";
    public static final String LAHULI = "LAH ";
    public static final String LAK = "LAK ";
    public static final String LAMBANI = "LAM ";
    public static final String LAO = "LAO ";
    public static final String LATIN = "LAT ";
    public static final String LAZ = "LAZ ";
    public static final String L_CREE = "LCR ";
    public static final String LADAKHI = "LDK ";
    public static final String LEZGI = "LEZ ";
    public static final String LIGURIAN = "LIJ ";
    public static final String LIMBURGISH = "LIM ";
    public static final String LINGALA = "LIN ";
    public static final String LISU = "LIS ";
    public static final String LAMPUNG = "LJP ";
    public static final String LAKI = "LKI ";
    public static final String LOW_MARI = "LMA ";
    public static final String LIMBU = "LMB ";
    public static final String LOMBARD = "LMO ";
    public static final String LOMWE = "LMW ";
    public static final String LOMA = "LOM ";
    public static final String LURI = "LRC ";
    public static final String LOWER_SORBIAN = "LSB ";
    public static final String LULE_SAMI = "LSM ";
    public static final String LITHUANIAN = "LTH ";
    public static final String LUXEMBOURGISH = "LTZ ";
    public static final String LUBA_LULUA = "LUA ";
    public static final String LUBA_KATANGA = "LUB ";
    public static final String GANDA = "LUG ";
    public static final String LUYIA = "LUH ";
    public static final String LUO = "LUO ";
    public static final String LATVIAN = "LVI ";
    public static final String MADURA = "MAD ";
    public static final String MAGAHI = "MAG ";
    public static final String MARSHALLESE = "MAH ";
    public static final String MAJANG = "MAJ ";
    public static final String MAKHUWA = "MAK ";
    public static final String MALAYALAM = "MAL ";
    public static final String MAM = "MAM ";
    public static final String MANSI = "MAN ";
    public static final String MAPUDUNGUN = "MAP ";
    public static final String MARATHI = "MAR ";
    public static final String MARWARI = "MAW ";
    public static final String MBUNDU = "MBN ";
    public static final String MBO = "MBO ";
    public static final String MANCHU = "MCH ";
    public static final String MOOSE_CREE = "MCR ";
    public static final String MENDE = "MDE ";
    public static final String MANDAR = "MDR ";
    public static final String MEEN = "MEN ";
    public static final String MERU = "MER ";
    public static final String PATTANI_MALAY = "MFA ";
    public static final String MORISYEN = "MFE ";
    public static final String MINANGKABAU = "MIN ";
    public static final String MIZO = "MIZ ";
    public static final String MACEDONIAN = "MKD ";
    public static final String MAKASAR = "MKR ";
    public static final String KITUBA = "MKW ";
    public static final String MALE = "MLE ";
    public static final String MALAGASY = "MLG ";
    public static final String MALINKE = "MLN ";
    public static final String MALAYALAM_REFORMED = "MLR ";
    public static final String MALAY = "MLY ";
    public static final String MANDINKA = "MND ";
    public static final String MONGOLIAN = "MNG ";
    public static final String MANIPURI = "MNI ";
    public static final String MANINKA = "MNK ";
    public static final String MANX = "MNX ";
    public static final String MOHAWK = "MOH ";
    public static final String MOKSHA = "MOK ";
    public static final String MOLDAVIAN = "MOL ";
    public static final String MON = "MON ";
    public static final String MOROCCAN = "MOR ";
    public static final String MOSSI = "MOS ";
    public static final String MAORI = "MRI ";
    public static final String MAITHILI = "MTH ";
    public static final String MALTESE = "MTS ";
    public static final String MUNDARI = "MUN ";
    public static final String MUSCOGEE = "MUS ";
    public static final String MIRANDESE = "MWL ";
    public static final String HMONG_DAW = "MWW ";
    public static final String MAYAN = "MYN ";
    public static final String MAZANDERANI = "MZN ";
    public static final String NAGA_ASSAMESE = "NAG ";
    public static final String NAHUATL = "NAH ";
    public static final String NANAI = "NAN ";
    public static final String NEAPOLITAN = "NAP ";
    public static final String NASKAPI = "NAS ";
    public static final String NAURUAN = "NAU ";
    public static final String NAVAJO = "NAV ";
    public static final String N_CREE = "NCR ";
    public static final String NDEBELE = "NDB ";
    public static final String NDAU = "NDC ";
    public static final String NDONGA = "NDG ";
    public static final String LOW_SAXON = "NDS ";
    public static final String NEPALI = "NEP ";
    public static final String NEWARI = "NEW ";
    public static final String NGBAKA = "NGA ";
    public static final String NAGARI = "NGR ";
    public static final String NORWAY_HOUSE_CREE = "NHC ";
    public static final String NISI = "NIS ";
    public static final String NIUEAN = "NIU ";
    public static final String NYANKOLE = "NKL ";
    public static final String NKO = "NKO ";
    public static final String DUTCH = "NLD ";
    public static final String NIMADI = "NOE ";
    public static final String NOGAI = "NOG ";
    public static final String NORWEGIAN = "NOR ";
    public static final String NOVIAL = "NOV ";
    public static final String NORTHERN_SAMI = "NSM ";
    public static final String SOTHO_NORTHERN = "NSO ";
    public static final String NORTHERN_TAI = "NTA ";
    public static final String ESPERANTO = "NTO ";
    public static final String NYAMWEZI = "NYM ";
    public static final String NORWEGIAN_NYNORSK = "NYN ";
    public static final String MBEMBE_TIGON = "NZA ";
    public static final String OCCITAN = "OCI ";
    public static final String OJI_CREE = "OCR ";
    public static final String OJIBWAY = "OJB ";
    public static final String ODIA_ORIYA = "ORI ";
    public static final String OROMO = "ORO ";
    public static final String OSSETIAN = "OSS ";
    public static final String PALESTINIAN_ARAMAIC = "PAA ";
    public static final String PANGASINAN = "PAG ";
    public static final String PALI = "PAL ";
    public static final String PAMPANGAN = "PAM ";
    public static final String PUNJABI = "PAN ";
    public static final String PALPA = "PAP ";
    public static final String PAPIAMENTU = "PAP0";
    public static final String PASHTO = "PAS ";
    public static final String PALAUAN = "PAU ";
    public static final String BOUYEI = "PCC ";
    public static final String PICARD = "PCD ";
    public static final String PENNSYLVANIA_GERMAN = "PDC ";
    public static final String POLYTONIC_GREEK = "PGR ";
    public static final String PHAKE = "PHK ";
    public static final String NORFOLK = "PIH ";
    public static final String FILIPINO = "PIL ";
    public static final String PALAUNG = "PLG ";
    public static final String POLISH = "PLK ";
    public static final String PIEMONTESE = "PMS ";
    public static final String WESTERN_PANJABI = "PNB ";
    public static final String POCOMCHI = "POH ";
    public static final String POHNPEIAN = "PON ";
    public static final String PROVENCAL = "PRO ";
    public static final String PORTUGUESE = "PTG ";
    public static final String WESTERN_PWO_KAREN = "PWO ";
    public static final String CHIN = "QIN ";
    public static final String KICHE = "QUC ";
    public static final String QUECHUA_BOLIVIA = "QUH ";
    public static final String QUECHUA = "QUZ ";
    public static final String QUECHUA_ECUADOR = "QVI ";
    public static final String QUECHUA_PERU = "QWH ";
    public static final String RAJASTHANI = "RAJ ";
    public static final String RAROTONGAN = "RAR ";
    public static final String RUSSIAN_BURIAT = "RBU ";
    public static final String R_CREE = "RCR ";
    public static final String REJANG = "REJ ";
    public static final String RIANG = "RIA ";
    public static final String TARIFIT = "RIF ";
    public static final String RITARUNGO = "RIT ";
    public static final String ARAKWAL = "RKW ";
    public static final String ROMANSH = "RMS ";
    public static final String VLAX_ROMANI = "RMY ";
    public static final String ROMANIAN = "ROM ";
    public static final String ROMANY = "ROY ";
    public static final String RUSYN = "RSY ";
    public static final String ROTUMAN = "RTM ";
    public static final String KINYARWANDA = "RUA ";
    public static final String RUNDI = "RUN ";
    public static final String AROMANIAN = "RUP ";
    public static final String RUSSIAN = "RUS ";
    public static final String SADRI = "SAD ";
    public static final String SANSKRIT = "SAN ";
    public static final String SASAK = "SAS ";
    public static final String SANTALI = "SAT ";
    public static final String SAYISI = "SAY ";
    public static final String SICILIAN = "SCN ";
    public static final String SCOTS = "SCO ";
    public static final String SEKOTA = "SEK ";
    public static final String SELKUP = "SEL ";
    public static final String OLD_IRISH = "SGA ";
    public static final String SANGO = "SGO ";
    public static final String SAMOGITIAN = "SGS ";
    public static final String TACHELHIT = "SHI ";
    public static final String SHAN = "SHN ";
    public static final String SIBE = "SIB ";
    public static final String SIDAMO = "SID ";
    public static final String SILTE_GURAGE = "SIG ";
    public static final String SKOLT_SAMI = "SKS ";
    public static final String SLOVAK = "SKY ";
    public static final String NORTH_SLAVEY = "SCS ";
    public static final String SLAVEY = "SLA ";
    public static final String SLOVENIAN = "SLV ";
    public static final String SOMALI = "SML ";
    public static final String SAMOAN = "SMO ";
    public static final String SENA = "SNA ";
    public static final String SHONA = "SNA0";
    public static final String SINDHI = "SND ";
    public static final String SINHALA = "SNH ";
    public static final String SONINKE = "SNK ";
    public static final String SODO_GURAGE = "SOG ";
    public static final String SONGE = "SOP ";
    public static final String SOTHO_SOUTHERN = "SOT ";
    public static final String ALBANIAN = "SQI ";
    public static final String SERBIAN = "SRB ";
    public static final String SARDINIAN = "SRD ";
    public static final String SARAIKI = "SRK ";
    public static final String SERER = "SRR ";
    public static final String SOUTH_SLAVEY = "SSL ";
    public static final String SOUTHERN_SAMI = "SSM ";
    public static final String SATERLAND_FRISIAN = "STQ ";
    public static final String SUKUMA = "SUK ";
    public static final String SUNDANESE = "SUN ";
    public static final String SURI = "SUR ";
    public static final String SVAN = "SVA ";
    public static final String SWEDISH = "SVE ";
    public static final String SWADAYA_ARAMAIC = "SWA ";
    public static final String SWAHILI = "SWK ";
    public static final String SWATI = "SWZ ";
    public static final String SUTU = "SXT ";
    public static final String UPPER_SAXON = "SXU ";
    public static final String SYLHETI = "SYL ";
    public static final String SYRIAC = "SYR ";
    public static final String SYRIAC_ESTRANGELA = "SYRE";
    public static final String SYRIAC_WESTERN = "SYRJ";
    public static final String SYRIAC_EASTERN = "SYRN";
    public static final String SILESIAN = "SZL ";
    public static final String TABASARAN = "TAB ";
    public static final String TAJIKI = "TAJ ";
    public static final String TAMIL = "TAM ";
    public static final String TATAR = "TAT ";
    public static final String TH_CREE = "TCR ";
    public static final String DEHONG_DAI = "TDD ";
    public static final String TELUGU = "TEL ";
    public static final String TETUM = "TET ";
    public static final String TAGALOG = "TGL ";
    public static final String TONGAN = "TGN ";
    public static final String TIGRE = "TGR ";
    public static final String TIGRINYA = "TGY ";
    public static final String THAI = "THA ";
    public static final String TAHITIAN = "THT ";
    public static final String TIBETAN = "TIB ";
    public static final String TIV = "TIV ";
    public static final String TURKMEN = "TKM ";
    public static final String TAMASHEK = "TMH ";
    public static final String TEMNE = "TMN ";
    public static final String TSWANA = "TNA ";
    public static final String TUNDRA_NENETS = "TNE ";
    public static final String TONGA = "TNG ";
    public static final String TODO = "TOD ";
    public static final String TOMA = "TOD0";
    public static final String TOK_PISIN = "TPI ";
    public static final String TURKISH = "TRK ";
    public static final String TSONGA = "TSG ";
    public static final String TSHANGLA = "TSJ ";
    public static final String TUROYO_ARAMAIC = "TUA ";
    public static final String TULU = "TUM ";
    public static final String TUMBUKA = "TUL ";
    public static final String TUVIN = "TUV ";
    public static final String TUVALU = "TVL ";
    public static final String TWI = "TWI ";
    public static final String TAY = "TYZ ";
    public static final String TAMAZIGHT = "TZM ";
    public static final String TZOTZIL = "TZO ";
    public static final String UDMURT = "UDM ";
    public static final String UKRAINIAN = "UKR ";
    public static final String UMBUNDU = "UMB ";
    public static final String URDU = "URD ";
    public static final String UPPER_SORBIAN = "USB ";
    public static final String UYGHUR = "UYG ";
    public static final String UZBEK = "UZB ";
    public static final String VENETIAN = "VEC ";
    public static final String VENDA = "VEN ";
    public static final String VIETNAMESE = "VIT ";
    public static final String VOLAPUK = "VOL ";
    public static final String VORO = "VRO ";
    public static final String WA = "WA  ";
    public static final String WAGDI = "WAG ";
    public static final String WARAY_WARAY = "WAR ";
    public static final String WEST_CREE = "WCR ";
    public static final String WELSH = "WEL ";
    public static final String WALLOON = "WLN ";
    public static final String WOLOF = "WLF ";
    public static final String MEWATI = "WTM ";
    public static final String LU = "XBD ";
    public static final String KHENGKHA = "XKF ";
    public static final String XHOSA = "XHS ";
    public static final String MINJANGBAL = "XJB ";
    public static final String SOGA = "XOG ";
    public static final String KPELLE_LIBERIA = "XPE ";
    public static final String SAKHA = "YAK ";
    public static final String YAO = "YAO ";
    public static final String YAPESE = "YAP ";
    public static final String YORUBA = "YBA ";
    public static final String Y_CREE = "YCR ";
    public static final String YI_CLASSIC = "YIC ";
    public static final String YI_MODERN = "YIM ";
    public static final String ZEALANDIC = "ZEA ";
    public static final String STANDARD_MOROCCAN_TAMAZIGHT = "ZGH ";
    public static final String ZHUANG = "ZHA ";
    public static final String CHINESE_HONG_KONG = "ZHH ";
    public static final String CHINESE_PHONETIC = "ZHP ";
    public static final String CHINESE_SIMPLIFIED = "ZHS ";
    public static final String CHINESE_TRADITIONAL = "ZHT ";
    public static final String ZANDE = "ZND ";
    public static final String ZULU = "ZUL ";
    public static final String ZAZAKI = "ZZA ";

    private LanguageTags() {}
}
