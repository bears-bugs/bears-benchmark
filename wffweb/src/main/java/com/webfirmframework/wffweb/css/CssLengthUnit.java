/*
 * Copyright 2014-2018 Web Firm Framework
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
 * @author WFF
 */
package com.webfirmframework.wffweb.css;

import com.webfirmframework.wffweb.css.core.LengthUnit;

/**
 * @author WFF
 * @since 1.0.0
 * @see LengthUnit
 *
 *
 */
public enum CssLengthUnit implements LengthUnit {
    // should be in descending order of length
    // enum name should be upper case of unit string, if there is any special
    // case comes handle as % in the consuming part
    VMIN("vmin"), VMAX("vmax"), REM("rem"), VW("vw"), VH("vh"), PX("px"), PT(
            "pt"), PC("pc"), MM("mm"), IN(
                    "in"), EX("ex"), EM("em"), CM("cm"), CH("ch"), PER("%"),;

    private String valueString;

    private CssLengthUnit(final String unit) {
        valueString = unit;
    }

    @Override
    public String toString() {
        return valueString;
    }

    @Override
    public String getUnit() {
        return valueString;
    }
}
