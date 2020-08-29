package app.mblackman.orderfulfillment.data.network.etsy

import app.mblackman.orderfulfillment.data.common.Address
import app.mblackman.orderfulfillment.data.common.OrderStatus
import app.mblackman.orderfulfillment.data.network.NetworkOrder
import app.mblackman.orderfulfillment.data.network.NetworkProduct
import app.mblackman.orderfulfillment.data.network.NetworkProductSale
import app.mblackman.orderfulfillment.data.network.etsy.json.Listing
import app.mblackman.orderfulfillment.data.network.etsy.json.Receipt
import app.mblackman.orderfulfillment.data.network.etsy.json.Transaction
import java.time.LocalDate

fun Listing.toNetworkProduct(imageUrl: String? = null): NetworkProduct =
    NetworkProduct(
        this.id.toLong(),
        this.title,
        this.description,
        imageUrl,
        this.price.toDoubleOrNull()
    )

fun Receipt.toNetworkOrder(): NetworkOrder =
    NetworkOrder(
        this.id.toLong(),
        OrderStatus.Purchased,
        this.creationTime!!.toDate(),
        this.name ?: "Joe Exotic",
        this.buyerEmail!!,
        Address(
            this.name!!,
            this.addressFirstLine!!,
            this.addressSecondLine!!,
            this.addressCity!!,
            this.addressState!!,
            this.addressZip!!
        ),
        null
    )

fun Transaction.toNetworkProductSale(): NetworkProductSale =
    NetworkProductSale(
        this.id.toLong(),
        this.listingId.toLong(),
        this.quantity
    )

fun Float.toDate(): LocalDate =
    LocalDate.ofEpochDay(this.toLong())
