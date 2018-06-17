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
import FirebaseFirestore

public extension DocumentSnapshot {
    @objc func toDictionary() -> [String: Any] {
        var ret = [String: Any]()
        ret["id"] = self.documentID
        ret["data"] = self.data()
        ret["exists"] = self.exists
        ret["metadata"] = [String: Any](
            dictionaryLiteral: ("isFromCache", self.metadata.isFromCache),
            ("hasPendingWrites", self.metadata.hasPendingWrites)
        )
        return ret
    }
}