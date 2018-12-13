/*
 * Copyright 2015 - 2016 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.geocoder;

import javax.json.JsonObject;

public class GisgraphyGeocoder extends JsonGeocoder {

    public GisgraphyGeocoder() {
        this("http://services.gisgraphy.com/reversegeocoding/search", 0);
    }

    public GisgraphyGeocoder(String url, int cacheSize) {
        super(url + "?format=json&lat=%f&lng=%f&from=1&to=1", cacheSize);
    }

    @Override
    public Address parseAddress(JsonObject json) {
        Address address = new Address();

        JsonObject result = json.getJsonArray("result").getJsonObject(0);

        if (result.containsKey("streetName")) {
            address.setStreet(result.getString("streetName"));
        }
        if (result.containsKey("city")) {
            address.setSettlement(result.getString("city"));
        }
        if (result.containsKey("state")) {
            address.setState(result.getString("state"));
        }
        if (result.containsKey("countryCode")) {
            address.setCountry(result.getString("countryCode"));
        }

        return address;
    }

}
