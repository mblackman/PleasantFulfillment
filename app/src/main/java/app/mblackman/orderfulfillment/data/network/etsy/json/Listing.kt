package app.mblackman.orderfulfillment.data.network.etsy.json

import com.squareup.moshi.Json

enum class WhoMade {
    I_MADE, COLLECTIVE, SOMEONE_ELSE
}

enum class WhenMade {
    MADE_TO_ORDER,
    YEAR_2020_2020,
    YEAR_2010_2019,
    YEAR_2001_2009,
    BEFORE_2001,
    YEAR_2000_2000,
    YEAR_1990S,
    YEAR_1980S,
    YEAR_1970S,
    YEAR_1960S,
    YEAR_1950S,
    YEAR_1940S,
    YEAR_1930S,
    YEAR_1920S,
    YEAR_1910S,
    YEAR_1900S,
    YEAR_1800S,
    YEAR_1700S,
    BEFORE_1700
}

enum class WeightUnit {
    OUNCE, POUND, GRAM, KILOGRAM
}

enum class DimensionUnit {
    INCH, FOOT, MILLIMETER, CENTIMETER, METER
}

enum class Recipient {
    MEN,
    WOMEN,
    UNISEX_ADULTS,
    TEEN_BOYS,
    TEEN_GIRLS,
    TEENS,
    BOYS,
    GIRLS,
    CHILDREN,
    BABY_BOYS,
    BABY_GIRLS,
    BABIES,
    BIRDS,
    CATS,
    DOGS,
    PETS,
    NOT_SPECIFIED
}

enum class Occasion {
    ANNIVERSARY,
    BAPTISM,
    BAR_OR_BAT_MITZVAH,
    BIRTHDAY,
    CANADA_DAY,
    CHINESE_NEW_YEAR,
    CINCO_DE_MAYO,
    CONFIRMATION,
    CHRISTMAS,
    DAY_OF_THE_DEAD,
    EASTER,
    EID,
    ENGAGEMENT,
    FATHERS_DAY,
    GET_WELL,
    GRADUATION,
    HALLOWEEN,
    HANUKKAH,
    HOUSEWARMING,
    KWANZAA,
    PROM,
    JULY_4TH,
    MOTHERS_DAY,
    NEW_BABY,
    NEW_YEARS,
    QUINCEANERA,
    RETIREMENT,
    ST_PATRICKS_DAY,
    SWEET_16,
    SYMPATHY,
    THANKSGIVING,
    VALENTINES,
    WEDDING
}

data class Listing(
    @Json(name = "listing_id") val id: Int,
    val state: String,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "category_id") val categoryId: Int,
    val title: String,
    val description: String,
    @Json(name = "creation_tsz") val creationTime: Float,
    @Json(name = "ending_tsz") val endingTime: Float,
    @Json(name = "original_creation_tsz") val originalCreationTime: Float,
    @Json(name = "last_modified_tsz") val lastModifiedTime: Float,
    val price: String,
    @Json(name = "currency_code") val currencyCode: String,
    val quantity: Int,
    val sku: List<String>,
    val tags: List<String>,
    @Json(name = "taxonomy_id") val taxonomyId: Int,
    @Json(name = "suggested_taxonomy_id") val suggestedTaxonomyId: Int,
    @Json(name = "taxonomy_path") val taxonomyPath: List<String>,
    val materials: List<String>,
    @Json(name = "shop_section_id") val shopSectionId: Int,
    @Json(name = "featured_rank") val featuredRank: Int, // TODO Check if this should be made into an enum.
    @Json(name = "state_tsz") val stateLastUpdatedTime: Float,
    val url: String,
    val views: Int,
    @Json(name = "num_favorers") val numFavorers: Int,
    @Json(name = "shipping_template_id") val shippingTemplateId: Int,
    @Json(name = "shipping_profile_id") val shippingProfileId: Int,
    @Json(name = "processing_min") val processingMin: Int,
    @Json(name = "processing_max") val processingMax: Int,
    @Json(name = "who_made") val whoMade: WhoMade,
    @Json(name = "is_supply") val isSupply: Boolean,
    @Json(name = "when_made") val whenMade: WhenMade,
    @Json(name = "item_weight") val itemWeight: Int,
    @Json(name = "item_weight_unit") val itemWeightUnit: WeightUnit,
    @Json(name = "item_length") val itemLength: Int,
    @Json(name = "item_width") val itemWidth: Int,
    @Json(name = "item_height") val itemHeight: Int,
    @Json(name = "item_dimensions_unit") val itemDimensionUnit: DimensionUnit,
    @Json(name = "is_private") val isPrivate: Boolean,
    val recipient: Recipient,
    val occasion: Occasion,
    val style: List<String>,
    @Json(name = "non_taxable") val isNonTaxable: Boolean,
    @Json(name = "is_customizable") val isCustomizable: Boolean,
    @Json(name = "is_digital") val isDigital: Boolean,
    @Json(name = "file_date") val fileDate: String,
    @Json(name = "can_write_inventory") val canWriteInventory: Boolean,
    @Json(name = "has_variations") val hasVariations: Boolean,
    @Json(name = "should_auto_renew") val shouldAutoRenew: Boolean,
    val language: String
)