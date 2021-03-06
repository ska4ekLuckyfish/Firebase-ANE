/*
 *  Copyright 2018 Tua Rua Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
@file:Suppress("FunctionName")

package com.tuarua.firebase.firestore.extensions

import com.adobe.fre.FREObject
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.tuarua.frekotlin.*

fun FirebaseFirestoreSettings(freObject: FREObject?): FirebaseFirestoreSettings? {
    val rv = freObject ?: return null
    val isSSLEnabled = Boolean(rv["isSSLEnabled"])
    val isPersistenceEnabled = Boolean(rv["isPersistenceEnabled"])
    val builder = FirebaseFirestoreSettings.Builder()
    if (isPersistenceEnabled != null) {
        builder.setPersistenceEnabled(isPersistenceEnabled)
    }
    if (isSSLEnabled != null) {
        builder.setSslEnabled(isSSLEnabled)
    }
    return builder.build()
}

fun FirebaseFirestoreSettings.toFREObject(): FREObject? {
    val ret = FREObject("com.tuarua.firebase.firestore.FirestoreSettings")
    ret["host"] = host.toFREObject()
    ret["isPersistenceEnabled"] = isPersistenceEnabled.toFREObject()
    ret["isSslEnabled"] = isSslEnabled.toFREObject()
    return ret
}