/**
 * Mule Google Calendars Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.google.calendar.automation.testcases;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.module.google.calendar.automation.CalendarUtils;
import org.mule.module.google.calendar.model.AclRule;
import org.mule.modules.tests.ConnectorTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GetAllAclRulesTestCases extends GoogleCalendarTestParent {

    protected List<AclRule> insertedAclRules = new ArrayList<AclRule>();

    @Before
    public void setUp() throws Exception {
        initializeTestRunMessage("getAllAclRules");

        List<String> scopes = (List<String>) getTestRunMessageValue("scopes");

        // Insert the different scopes
        for (String scope : scopes) {
            upsertOnTestRunMessage("scope", scope);
            AclRule aclRule = runFlowAndGetPayload("insert-acl-rule");
            insertedAclRules.add(aclRule);
        }

    }

    @Category({RegressionTests.class})
    @Test
    public void testGetAllAclRules() {
        try {

            List<AclRule> aclRuleList = runFlowAndGetPayload("get-all-acl-rules");

            for (AclRule insertedAclRule : insertedAclRules) {
                assertTrue(CalendarUtils.isAclRuleInList(aclRuleList, insertedAclRule));
            }

        } catch (Exception e) {
            fail(ConnectorTestUtils.getStackTrace(e));
        }
    }

    @After
    public void tearDown() throws Exception {
        for (AclRule aclRule : insertedAclRules) {
            upsertOnTestRunMessage("ruleId", aclRule.getId());
            runFlowAndGetPayload("delete-acl-rule");
        }
    }
}
