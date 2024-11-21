package com.example.myonlinecoffeeshop

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val name: String,
    val price: Double,
    val category: String,
    val imageResId: Int,
    var quantity: Int = 0  // Add quantity field (default to 0)
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()  // Read quantity from the Parcel
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeString(category)
        parcel.writeInt(imageResId)
        parcel.writeInt(quantity)  // Write quantity to the Parcel
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
