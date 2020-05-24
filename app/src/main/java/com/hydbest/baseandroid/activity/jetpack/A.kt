package com.hydbest.baseandroid.activity.jetpack

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import java.io.OutputStream


class A private constructor() {

    var index:Int?=null

    constructor(parcel: Parcel) : this() {
        index = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    companion object{

        object CREATOR : Parcelable.Creator<A> {
            override fun createFromParcel(parcel: Parcel): A {
                return A(parcel)
            }

            override fun newArray(size: Int): Array<A?> {
                return arrayOfNulls(size)
            }
        }

        @Volatile
        private var  INSTANCE:A?= null

        fun getInstance():A{
            INSTANCE ?: synchronized(A::class.java){
                 INSTANCE ?: A().apply {
                    INSTANCE = this
                }

            }
            return INSTANCE!!
        }

        fun main(args:Array<String>?):String{
            val instance1 = getInstance()
            val instance2 :A =  getInstance()

            instance1.index = 10
            instance2.index = 20
            return instance1.index.toString() + "   :   " +instance2.index.toString()
        }

    }


    @VersionedParcelize
    data class B constructor(val index:Int):Parcelable {

        constructor(parcel: Parcel) : this(parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(index)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<B> {
            override fun createFromParcel(parcel: Parcel): B {
                return B(parcel)
            }

            override fun newArray(size: Int): Array<B?> {
                return arrayOfNulls(size)
            }
        }
    }
}