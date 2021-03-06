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
package com.tuarua.firebase.firestore

import com.adobe.fre.FREContext
import com.adobe.fre.FREObject
import com.tuarua.firebase.firestore.data.Order
import com.tuarua.firebase.firestore.data.Where
import com.tuarua.firebase.firestore.extensions.*
import com.tuarua.frekotlin.*
import java.util.*


@Suppress("unused", "UNUSED_PARAMETER", "UNCHECKED_CAST", "PrivatePropertyName")
class KotlinController : FreKotlinMainController {
    private val TRACE = "TRACE"
    private lateinit var firestoreController: FirestoreController

    fun createGUID(ctx: FREContext, argv: FREArgv): FREObject? {
        return UUID.randomUUID().toString().toFREObject()
    }

    fun init(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("init")
        val loggingEnabled = Boolean(argv[0]) == true
        val settings = FirebaseFirestoreSettings(argv[1])
        firestoreController = FirestoreController(context, loggingEnabled, settings)
        return true.toFREObject()
    }

    fun getFirestoreSettings(ctx: FREContext, argv: FREArgv): FREObject? {
        return firestoreController.getFirestoreSettings().toFREObject()
    }

    fun getCollectionParent(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("getCollectionParent")
        val path = String(argv[0]) ?: return FreConversionException("path")
        return firestoreController.getCollectionParent(path)?.toFREObject()
    }

    fun initCollectionReference(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("initCollectionReference")
        val path = String(argv[0]) ?: return FreConversionException("path")
        return firestoreController.initCollectionReference(path).toFREObject()
    }

    fun getDocumentParent(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("getDocumentParent")
        val path = String(argv[0]) ?: return FreConversionException("path")
        return firestoreController.getDocumentParent(path).toFREObject()
    }

    fun getDocuments(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 8 } ?: return FreArgException("getDocuments")
        val path = String(argv[0]) ?: return FreConversionException("path")
        val asId = String(argv[1]) ?: return FreConversionException("asId")
        try {
            val whereClauses = FREArray(argv[2])
            val orderClauses = FREArray(argv[3])
            val startAtClauses = FREArray(argv[4])
            val startAfterClauses = FREArray(argv[5])
            val endAtClauses = FREArray(argv[6])
            val endBeforeClauses = FREArray(argv[7])
            val limitTo = Int(argv[8]) ?: 10000
            val whereList = mutableListOf<Where>()
            val orderList = mutableListOf<Order>()

            for (fre in whereClauses) {
                if (fre == null) continue
                val fieldPath = String(fre["fieldPath"]) ?: continue
                val operator = String(fre["operator"]) ?: continue
                val value = fre["value"] ?: continue
                val freK = FreObjectKotlin(value).value ?: continue
                whereList.add(Where(fieldPath, operator, freK))
            }

            for (fre in orderClauses) {
                if (fre == null) continue
                val by = String(fre["by"]) ?: continue
                val descending = Boolean(fre["descending"]) ?: continue
                orderList.add(Order(by, descending))
            }

            val startAtList = mutableListOf<Any>()
            for (fre in startAtClauses) {
                val item = FreObjectKotlin(fre).value ?: continue
                startAtList.add(item)
            }

            val startAfterList = mutableListOf<Any>()
            for (fre in startAfterClauses) {
                val item = FreObjectKotlin(fre).value ?: continue
                startAfterList.add(item)
            }

            val endAtList = mutableListOf<Any>()
            for (fre in endAtClauses) {
                val item = FreObjectKotlin(fre).value ?: continue
                endAtList.add(item)
            }

            val endBeforeList = mutableListOf<Any>()
            for (fre in endBeforeClauses) {
                val item = FreObjectKotlin(fre).value ?: continue
                endBeforeList.add(item)
            }

            firestoreController.getDocuments(path, asId, whereList, orderList,
                    startAtList, startAfterList, endAtList, endBeforeList, limitTo)
        } catch (e: FreException) {
            return e.getError()
        } catch (e: Exception) {
            return FreException(e).getError()
        }
        return null
    }

    fun initDocumentReference(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("initDocumentReference")
        val path = String(argv[0]) ?: return FreConversionException("path")
        return firestoreController.initDocumentReference(path).toFREObject()
    }

    fun getDocumentReference(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 1 } ?: return FreArgException("getDocumentReference")
        val path = String(argv[0]) ?: return FreConversionException("path")
        val asId = String(argv[1]) ?: return FreConversionException("asId")
        firestoreController.getDocumentReference(path, asId)
        return null
    }

    fun addSnapshotListenerDocument(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 1 } ?: return FreArgException("addSnapshotListenerDocument")
        val path = String(argv[0]) ?: return FreConversionException("path")
        val eventId = String(argv[1]) ?: return FreConversionException("eventId")
        val asId = String(argv[2]) ?: return FreConversionException("asId")
        firestoreController.addSnapshotListenerDocument(path, eventId, asId)
        return null
    }

    fun removeSnapshotListener(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("removeSnapshotListener")
        val asId = String(argv[0]) ?: return FreConversionException("asId")
        firestoreController.removeSnapshotListener(asId)
        return null
    }

    fun setDocumentReference(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 3 } ?: return FreArgException("setDocumentReference")
        val path = String(argv[0]) ?: return FreConversionException("path")
        val eventId = String(argv[1])
        val documentData: Map<String, Any> = Map(argv[2])
                ?: return FreConversionException("documentData")
        val merge = Boolean(argv[3]) ?: return FreConversionException("merge")
        firestoreController.setDocumentReference(path, eventId, documentData, merge)
        return null
    }

    fun updateDocumentReference(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 2 } ?: return FreArgException("updateDocumentReference")
        val path = String(argv[0]) ?: return FreConversionException("path")
        val eventId = String(argv[1])
        val documentData: Map<String, Any> = Map(argv[2])
                ?: return FreConversionException("documentData")
        firestoreController.updateDocumentReference(path, eventId, documentData)
        return null
    }

    fun deleteDocumentReference(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 1 } ?: return FreArgException("deleteDocumentReference")
        val path = String(argv[0]) ?: return FreConversionException("path")
        val eventId = String(argv[1])
        firestoreController.deleteDocumentReference(path, eventId)
        return null
    }

    fun documentWithAutoId(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("documentWithAutoId")
        val path = String(argv[0]) ?: return FreConversionException("path")
        return firestoreController.documentWithAutoId(path).toFREObject()
    }

    /**************** Batch ****************/
    fun startBatch(ctx: FREContext, argv: FREArgv): FREObject? {
        firestoreController.startBatch()
        return null
    }

    fun setBatch(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 2 } ?: return FreArgException("setBatch")
        val path = String(argv[0]) ?: return FreConversionException("path")
        val documentData: Map<String, Any> = Map(argv[1])
                ?: return FreConversionException("documentData")
        val merge = Boolean(argv[2]) ?: return FreConversionException("merge")
        firestoreController.setBatch(path, documentData, merge)
        return null
    }

    fun deleteBatch(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("deleteBatch")
        val path = String(argv[0]) ?: return FreConversionException("path")
        firestoreController.deleteBatch(path)
        return null
    }

    fun updateBatch(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 1 } ?: return FreArgException("updateBatch")
        val path = String(argv[0]) ?: return FreConversionException("path")
        val documentData: Map<String, Any> = Map(argv[1])
                ?: return FreConversionException("documentData")
        firestoreController.updateBatch(path, documentData)
        return null
    }

    fun commitBatch(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("commitBatch")
        val asId = String(argv[0])
        firestoreController.commitBatch(asId)
        return null
    }

    fun enableNetwork(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("commitBatch")
        val eventId = String(argv[0])
        firestoreController.enableNetwork(eventId)
        return null
    }

    fun disableNetwork(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("commitBatch")
        val eventId = String(argv[0])
        firestoreController.disableNetwork(eventId)
        return null
    }

    override val TAG: String
        get() = this::class.java.canonicalName
    private var _context: FREContext? = null
    override var context: FREContext?
        get() = _context
        set(value) {
            _context = value
            FreKotlinLogger.context = _context
        }
}