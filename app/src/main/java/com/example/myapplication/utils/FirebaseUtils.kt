package com.example.myapplication.utils

import com.google.firebase.database.DataSnapshot


fun <T> wrapSnapshotToClass(className: Class<T>, snap: DataSnapshot): T? {
    return snap.getValue(className)
}
