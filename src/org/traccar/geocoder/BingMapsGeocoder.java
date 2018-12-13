/*
 * Copyright 2014 - 2015 Stefaan Van Dooren (stefaan.vandooren@gmail.com)
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

import javax.json.JsonArray;
import javax.json.JsonObject;

public class BingMapsGeocoder extends JsonGeocoder {

    public BingMapsGeocoder(String url, String key, int cacheSize) {
        super(url + "/Locations/%f,%f?key=" + key + "&include=ciso2", cacheSize);
    }

    @Override
    public Address parseAddress(JsonObject json) {
        JsonArray result = json.getJsonArray("resourceSets");
        if (result != null) {
            JsonObject location =
                    result.getJsonObject(0).getJsonArray("resources").getJsonObject(0).getJsonObject("address");
            if (location != null) {
                Address address = new Address();
                if (location.containsKey("addressLine")) {
                    address.setStreet(location.getString("addressLine"));
                }
                if (location.containsKey("locality")) {
                    address.setSettlement(location.getString("locality"));
                }
                if (location.containsKey("adminDistrict2")) {
                    address.setDistrict(location.getString("adminDistrict2"));
                }
                if (location.containsKey("adminDistrict")) {
                    address.setState(location.getString("adminDistrict"));
                }
                if (location.containsKey("countryRegionIso2")) {
                    address.setCountry(location.getString("countryRegionIso2").toUpperCase());
                }
                if (location.containsKey("postalCode")) {
                    address.setPostcode(location.getString("postalCode"));
                }
                return address;
            }
        }
        return null;
    }

}
