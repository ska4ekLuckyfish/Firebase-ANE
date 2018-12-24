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

import Foundation
import FreSwift
import FirebaseCore

public extension FirebaseOptions {
    func toFREObject() -> FREObject? {
        guard let ret = FreObjectSwift(className: "com.tuarua.firebase.FirebaseOptions") else { return nil }
        ret.bundleId = bundleID
        ret.androidClientID = androidClientID
        ret.trackingId = trackingID
        ret.apiKey = apiKey
        ret.googleAppId = googleAppID
        ret.databaseUrl = databaseURL
        ret.storageBucket = storageBucket
        ret.clientId = clientID
        ret.projectId = projectID
        ret.gcmSenderId = gcmSenderID
        ret.deepLinkUrlScheme = deepLinkURLScheme
        return ret.rawValue
    }
}
