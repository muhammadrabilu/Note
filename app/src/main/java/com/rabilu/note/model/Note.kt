package com.rabilu.note.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: String? = null,
    val title: String? = null,
    val date: Long? = null,
    val note: String? = null,
    val uid: String? = null
) : Parcelable
