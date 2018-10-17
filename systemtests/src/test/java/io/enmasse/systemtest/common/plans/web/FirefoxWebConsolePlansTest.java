/*
 * Copyright 2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.enmasse.systemtest.common.plans.web;

import io.enmasse.systemtest.AddressType;
import io.enmasse.systemtest.bases.web.WebConsolePlansTest;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class FirefoxWebConsolePlansTest extends WebConsolePlansTest {

    @Override
    protected String getDefaultPlan(AddressType addressType) {
        return null;
    }

    @Override
    public WebDriver buildDriver() {
        return getFirefoxDriver();
    }

    @Test
    public void testCreateAddressPlan() throws Exception {
        doTestCreateAddressPlan();
    }
}
