package com.tuarua.firebase

import android.content.res.Resources
import com.adobe.fre.FREContext
import com.adobe.fre.FREObject
import com.google.android.gms.common.ConnectionResult
import com.google.firebase.FirebaseApp
import com.tuarua.frekotlin.*
import com.google.android.gms.common.GoogleApiAvailability


@Suppress("unused", "UNUSED_PARAMETER", "UNCHECKED_CAST", "PrivatePropertyName")
class KotlinController : FreKotlinMainController {
    private val TRACE = "TRACE"
    fun init(ctx: FREContext, argv: FREArgv): FREObject? {
        val context = context ?: return FreException("no context").getError()
        val resources = context.activity?.resources
                ?: return FreException("no resources").getError()
        val ac = context.activity?.applicationContext
                ?: return FreException("no application context").getError()

        try {
            val apiKey = resources.getString(context.getResourceId("string.google_app_id"))
            if (apiKey == "xxx_xxx") {
                return FreException(
                        "Cannot find required google_app_id. Ensure Firebase resources file added to FirebaseANE"
                ).getError()
            }
        } catch (e: Resources.NotFoundException) {
            return FreException(
                    "Cannot find required google_app_id. Ensure Firebase resources file added to FirebaseANE"
            ).getError()
        }

        FirebaseApp.initializeApp(ac)
        return true.toFREObject()
    }

    fun getOptions(ctx: FREContext, argv: FREArgv): FREObject? {
        val resources = context?.activity?.resources
                ?: return FreException("no resources").getError()
        val context = context
                ?: return FreException("no context").getError()

        val apiKey = try {
            resources.getString(context.getResourceId("string.google_api_key"))
        } catch (e: Resources.NotFoundException) {
            null
        }
        val googleAppId = try {
            resources.getString(context.getResourceId("string.google_app_id"))
        } catch (e: Resources.NotFoundException) {
            null
        }
        val databaseUrl = try {
            resources.getString(context.getResourceId("string.firebase_database_url"))
        } catch (e: Resources.NotFoundException) {
            null
        }
        val storageBucket = try {
            resources.getString(context.getResourceId("string.google_storage_bucket"))
        } catch (e: Resources.NotFoundException) {
            null
        }
        val projectId = try {
            resources.getString(context.getResourceId("string.project_id"))
        } catch (e: Resources.NotFoundException) {
            null
        }
        val gcmSenderId = try {
            resources.getString(context.getResourceId("string.gcm_defaultSenderId"))
        } catch (e: Resources.NotFoundException) {
            null
        }
        val ret = FREObject("com.tuarua.firebase.FirebaseOptions")
        ret["apiKey"] = apiKey?.toFREObject()
        ret["googleAppId"] = googleAppId?.toFREObject()
        ret["databaseUrl"] = databaseUrl?.toFREObject()
        ret["storageBucket"] = storageBucket?.toFREObject()
        ret["projectId"] = projectId?.toFREObject()
        ret["gcmSenderId"] = gcmSenderId?.toFREObject()
        return ret

    }

    fun isGooglePlayServicesAvailable(ctx: FREContext, argv: FREArgv): FREObject? {
        val activity = context?.activity ?: return FreException("no activity").getError()
        return (GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(activity) == ConnectionResult.SUCCESS)
                .toFREObject()
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