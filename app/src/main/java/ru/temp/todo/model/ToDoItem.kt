package ru.temp.todo.model

import android.os.Parcel
import android.os.Parcelable

data class ToDoItem(var date: String? = null, var done: String? = null, var itemText: String? = null, var key: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(done)
        parcel.writeString(itemText)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ToDoItem> {
        override fun createFromParcel(parcel: Parcel): ToDoItem {
            return ToDoItem(parcel)
        }

        override fun newArray(size: Int): Array<ToDoItem?> {
            return arrayOfNulls(size)
        }
    }
}
