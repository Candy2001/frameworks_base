/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.integrity.model;

import static com.google.common.truth.Truth.assertThat;

import android.content.integrity.AtomicFormula;
import android.content.integrity.Rule;
import android.util.StatsLog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class IntegrityCheckResultTest {

    @Test
    public void createAllowResult() {
        IntegrityCheckResult allowResult = IntegrityCheckResult.allow();

        assertThat(allowResult.getEffect()).isEqualTo(IntegrityCheckResult.Effect.ALLOW);
        assertThat(allowResult.getRule()).isNull();
        assertThat(allowResult.getLoggingResponse())
                .isEqualTo(StatsLog.INTEGRITY_CHECK_RESULT_REPORTED__RESPONSE__ALLOWED);
    }

    @Test
    public void createAllowResultWithRule() {
        String packageName = "com.test.deny";
        Rule forceAllowRule =
                new Rule(
                        new AtomicFormula.StringAtomicFormula(AtomicFormula.PACKAGE_NAME,
                                packageName),
                        Rule.FORCE_ALLOW);

        IntegrityCheckResult allowResult = IntegrityCheckResult.allow(forceAllowRule);

        assertThat(allowResult.getEffect()).isEqualTo(IntegrityCheckResult.Effect.ALLOW);
        assertThat(allowResult.getRule()).isEqualTo(forceAllowRule);
        assertThat(allowResult.getLoggingResponse())
                .isEqualTo(StatsLog.INTEGRITY_CHECK_RESULT_REPORTED__RESPONSE__FORCE_ALLOWED);
    }

    @Test
    public void createDenyResultWithRule() {
        String packageName = "com.test.deny";
        Rule failedRule =
                new Rule(
                        new AtomicFormula.StringAtomicFormula(AtomicFormula.PACKAGE_NAME,
                                packageName),
                        Rule.DENY);

        IntegrityCheckResult denyResult = IntegrityCheckResult.deny(failedRule);

        assertThat(denyResult.getEffect()).isEqualTo(IntegrityCheckResult.Effect.DENY);
        assertThat(denyResult.getRule()).isEqualTo(failedRule);
        assertThat(denyResult.getLoggingResponse())
                .isEqualTo(StatsLog.INTEGRITY_CHECK_RESULT_REPORTED__RESPONSE__REJECTED);
    }
}
