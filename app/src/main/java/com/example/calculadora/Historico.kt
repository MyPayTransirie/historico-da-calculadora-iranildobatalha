package com.example.calculadora

import android.os.Build
import android.os.Parcel
import android.os.Parcelable


class Historico(var historic: ArrayList<String>) : Parcelable {
    constructor(parcel: Parcel) : this(
        arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        }
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        arrayListOf<String>().apply {
            p0?.writeList(historic)
        }
    }

    companion object CREATOR : Parcelable.Creator<Historico> {
        override fun createFromParcel(parcel: Parcel): Historico {
            return Historico(parcel)
        }

        override fun newArray(size: Int): Array<Historico?> {
            return arrayOfNulls(size)
        }
    }

}