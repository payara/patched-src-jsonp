/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.json.tests;

import junit.framework.Assert;
import junit.framework.TestCase;

import javax.json.Json;
import javax.json.stream.JsonParser;

import java.io.StringReader;

public class JsonNestingTest extends TestCase {

    public void testNestingException() {
        String json = createDeepNestedDoc(500);
        StringReader stringReader = new StringReader(json);
        JsonParser parser = Json.createParser(stringReader);
        try {
            while (parser.hasNext()) {
                parser.next();
            }
            Assert.fail("Should have thrown a RuntimeException");
        } catch (RuntimeException runtimeException) {
            if (!runtimeException.getMessage().contains("Input is too deeply nested")) {
                Assert.fail("RuntimeException did not contain expected message: " + runtimeException.getMessage());
            }
        } finally {
            if (stringReader != null) {
                stringReader.close();
            }
            if (parser != null) {
                parser.close();
            }
        }
    }

    public void testNesting() {
        String json = createDeepNestedDoc(499);
        StringReader stringReader = new StringReader(json);
        JsonParser parser = Json.createParser(stringReader);
        try {
            while (parser.hasNext()) {
                parser.next();
            }
        } finally {
            if (stringReader != null) {
                stringReader.close();
            }
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static String createDeepNestedDoc(final int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < depth; i++) {
            sb.append("{ \"a\": [");
        }
        sb.append(" \"val\" ");
        for (int i = 0; i < depth; i++) {
            sb.append("]}");
        }
        sb.append("]");
        return sb.toString();
    }

}
