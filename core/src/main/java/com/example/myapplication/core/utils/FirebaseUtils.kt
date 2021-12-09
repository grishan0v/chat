package com.example.myapplication.core.utils

import com.google.firebase.database.DataSnapshot


fun <T> wrapSnapshotToClass(className: Class<T>, snap: DataSnapshot): T? {
    return snap.getValue(className)
}
