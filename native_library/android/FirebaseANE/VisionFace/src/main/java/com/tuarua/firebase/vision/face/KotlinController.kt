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
package com.tuarua.firebase.vision.face

import com.adobe.fre.FREContext
import com.adobe.fre.FREObject
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.gson.Gson
import com.tuarua.firebase.vision.extensions.FirebaseVisionImage
import com.tuarua.firebase.vision.face.events.FaceEvent
import com.tuarua.firebase.vision.face.extensions.*
import com.tuarua.frekotlin.*
import java.util.*

@Suppress("unused", "UNUSED_PARAMETER", "UNCHECKED_CAST", "PrivatePropertyName")
class KotlinController : FreKotlinMainController {
    private val TRACE = "TRACE"
    private var options: FirebaseVisionFaceDetectorOptions? = null
    private var results: MutableMap<String, MutableList<FirebaseVisionFace>> = mutableMapOf()
    private val gson = Gson()

    fun init(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("init")
        options = FirebaseVisionFaceDetectorOptions(argv[0])
        return true.toFREObject()
    }

    fun createGUID(ctx: FREContext, argv: FREArgv): FREObject? {
        return UUID.randomUUID().toString().toFREObject()
    }

    fun detect(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 1 } ?: return FreArgException("detect")
        val image = FirebaseVisionImage(argv[0]) ?: return FreConversionException("image")
        val eventId = String(argv[1]) ?: return FreConversionException("eventId")
        val options = this.options

        val detector: FirebaseVisionFaceDetector = if (options != null) {
            FirebaseVision.getInstance().getVisionFaceDetector(options)
        } else {
            FirebaseVision.getInstance().visionFaceDetector
        }

        detector.detectInImage(image).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (!task.result.isEmpty()) {
                    results[eventId] = task.result
                    dispatchEvent(FaceEvent.DETECTED,
                            gson.toJson(FaceEvent(eventId, null)))
                }
            } else {
                val error = task.exception
                dispatchEvent(FaceEvent.DETECTED,
                        gson.toJson(
                                FaceEvent(eventId, mapOf(
                                        "text" to error?.message.toString(),
                                        "id" to 0))
                        )
                )
            }
        }

        return null
    }

    fun getResults(ctx: FREContext, argv: FREArgv): FREObject? {
        argv.takeIf { argv.size > 0 } ?: return FreArgException("getResults")
        val eventId = String(argv[0]) ?: return FreConversionException("eventId")
        val result = results[eventId] ?: return null
        try {
            val freArray = FREArray("com.tuarua.firebase.vision.Face",
                    result.size, true)
            for (i in result.indices) {
                freArray[i] = result[i].toFREObject()
            }
            return freArray
        } catch (e: FreException) {
            trace(e.message)
            trace(e.stackTrace)
        }
        return null
    }

    override val TAG: String
        get() = this::class.java.canonicalName
    private var _context: FREContext? = null
    override var context: FREContext?
        get() = _context
        set(value) {
            _context = value
        }

}
