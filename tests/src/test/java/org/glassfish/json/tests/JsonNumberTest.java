/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
// Portions Copyright 2024 Payara Foundation and/or its affiliates
// Payara Foundation and/or its affiliates elects to include this software in this distribution under the GPL Version 2 license

package org.glassfish.json.tests;

import junit.framework.TestCase;

import javax.json.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.json.api.JsonConfig;

/**
 * @author Jitendra Kotamraju
 */
public class JsonNumberTest extends TestCase {

    // π as JsonNumber with 500 source characters
    static final String Π_500
            = "3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706"
            + "7982148086513282306647093844609550582231725359408128481117450284102701938521105559644622948954930381"
            + "9644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412"
            + "7372458700660631558817488152092096282925409171536436789259036001133053054882046652138414695194151160"
            + "9433057270365759591953092186117381932611793105118548074462379962749567351885752724891227938183011949";

    // π as JsonNumber with 501 source characters
    static final String Π_501 = Π_500 + "1";

    // π as JsonNumber with 1100 source characters
    private static final String Π_1100 = Π_500
            + "1298336733624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051"
            + "3200056812714526356082778577134275778960917363717872146844090122495343014654958537105079227968925892"
            + "3542019956112129021960864034418159813629774771309960518707211349999998372978049951059731732816096318"
            + "5950244594553469083026425223082533446850352619311881710100031378387528865875332083814206171776691473"
            + "0359825349042875546873115956286388235378759375195778185778053217122680661300192787661119590921642019"
            + "8938095257201065485863278865936153381827968230301952035301852968995773622599413891249721775283479131";

    // π as JsonNumber with 1101 source characters
    private static final String Π_1101 = Π_1100 + "5";

    public JsonNumberTest(String testName) {
        super(testName);
    }

    public void testFloating() throws Exception {
        JsonArray array1 = Json.createArrayBuilder().add(10.4).build();
        JsonReader reader = Json.createReader(new StringReader("[10.4]"));
        JsonArray array2 = reader.readArray();

        assertEquals(array1.get(0), array2.get(0));
        assertEquals(array1, array2);
    }

    public void testBigDecimal() throws Exception {
        JsonArray array1 = Json.createArrayBuilder().add(new BigDecimal("10.4")).build();
        JsonReader reader = Json.createReader(new StringReader("[10.4]"));
        JsonArray array2 = reader.readArray();

        assertEquals(array1.get(0), array2.get(0));
        assertEquals(array1, array2);
    }

    public void testIntNumberType() throws Exception {
        JsonArray array1 = Json.createArrayBuilder()
                .add(Integer.MIN_VALUE)
                .add(Integer.MAX_VALUE)
                .add(Integer.MIN_VALUE + 1)
                .add(Integer.MAX_VALUE - 1)
                .add(12)
                .add(12l)
                .add(new BigInteger("0"))
                .build();
        testNumberType(array1, true);

        StringReader sr = new StringReader("[" +
                "-2147483648, " +
                "2147483647, " +
                "-2147483647, " +
                "2147483646, " +
                "12, " +
                "12, " +
                "0 " +
                "]");
        JsonReader reader = Json.createReader(sr);
        JsonArray array2 = reader.readArray();
        reader.close();
        testNumberType(array2, true);

        assertEquals(array1, array2);
    }

    private void testNumberType(JsonArray array, boolean integral) {
        for (JsonValue value : array) {
            assertEquals(integral, ((JsonNumber) value).isIntegral());
        }
    }

    public void testLongNumberType() throws Exception {
        JsonArray array1 = Json.createArrayBuilder()
                .add(Long.MIN_VALUE)
                .add(Long.MAX_VALUE)
                .add(Long.MIN_VALUE + 1)
                .add(Long.MAX_VALUE - 1)
                .add((long) Integer.MIN_VALUE - 1)
                .add((long) Integer.MAX_VALUE + 1)
                .build();
        testNumberType(array1, true);

        StringReader sr = new StringReader("[" +
                "-9223372036854775808, " +
                "9223372036854775807, " +
                "-9223372036854775807, " +
                "9223372036854775806, " +
                "-2147483649, " +
                "2147483648 " +
                "]");
        JsonReader reader = Json.createReader(sr);
        JsonArray array2 = reader.readArray();
        reader.close();
        testNumberType(array2, true);

        assertEquals(array1, array2);
    }


//    public void testBigIntegerNumberType() throws Exception {
//        JsonArray array1 = new JsonBuilder()
//            .startArray()
//                .add(new BigInteger("-9223372036854775809"))
//                .add(new BigInteger("9223372036854775808"))
//                .add(new BigInteger("012345678901234567890"))
//            .end()
//        .build();
//        testNumberType(array1, JsonNumber.NumberType.BIG_INTEGER);
//
//        StringReader sr = new StringReader("[" +
//            "-9223372036854775809, " +
//            "9223372036854775808, " +
//            "12345678901234567890 " +
//        "]");
//        JsonReader reader = new JsonReader(sr);
//        JsonArray array2 = reader.readArray();
//        reader.close();
//        testNumberType(array2, JsonNumber.NumberType.BIG_INTEGER);
//
//        assertEquals(array1, array2);
//    }

    public void testBigDecimalNumberType() throws Exception {
        JsonArray array1 = Json.createArrayBuilder()
                .add(12d)
                .add(12.0d)
                .add(12.1d)
                .add(Double.MIN_VALUE)
                .add(Double.MAX_VALUE)
                .build();
        testNumberType(array1, false);

        StringReader sr = new StringReader("[" +
                "12.0, " +
                "12.0, " +
                "12.1, " +
                "4.9E-324, " +
                "1.7976931348623157E+308 " +
                "]");
        JsonReader reader = Json.createReader(sr);
        JsonArray array2 = reader.readArray();
        reader.close();
        testNumberType(array2, false);

        assertEquals(array1, array2);
    }

    public void testMinMax() throws Exception {
        JsonArray expected = Json.createArrayBuilder()
                .add(Integer.MIN_VALUE)
                .add(Integer.MAX_VALUE)
                .add(Long.MIN_VALUE)
                .add(Long.MAX_VALUE)
                .add(Double.MIN_VALUE)
                .add(Double.MAX_VALUE)
                .build();

        StringWriter sw = new StringWriter();
        JsonWriter writer = Json.createWriter(sw);
        writer.writeArray(expected);
        writer.close();

        JsonReader reader = Json.createReader(new StringReader(sw.toString()));
        JsonArray actual = reader.readArray();
        reader.close();

        assertEquals(expected, actual);
    }

    public void testLeadingZeroes() {
        JsonArray array = Json.createArrayBuilder()
                .add(0012.1d)
                .build();

        StringWriter sw = new StringWriter();
        JsonWriter jw = Json.createWriter(sw);
        jw.write(array);
        jw.close();

        assertEquals("[12.1]", sw.toString());
    }

    public void testBigIntegerExact() {
        try {
            JsonArray array = Json.createArrayBuilder().add(12345.12345).build();
            array.getJsonNumber(0).bigIntegerValueExact();
            fail("Expected Arithmetic exception");
        } catch (ArithmeticException expected) {
            // no-op
        }
    }

    // Test default BigInteger scale value limit using value bellow limit.
    // Call shall return value.
    public void testDefaultBigIntegerScaleBellowLimit() {
        BigDecimal value = new BigDecimal("3.1415926535897932384626433");
        JsonArray array = Json.createArrayBuilder().add(value).build();
        array.getJsonNumber(0).bigIntegerValue();
    }

    // Test default BigInteger scale value limit using value above limit.
    // Call shall throw specific UnsupportedOperationException exception.
    public void testDefaultBigIntegerScaleAboveLimit() {
        BigDecimal value = new BigDecimal("3.1415926535897932384626433")
                .setScale(100001, RoundingMode.HALF_UP);
        try {
            JsonArray array = Json.createArrayBuilder().add(value).build();
            array.getJsonNumber(0).bigIntegerValue();
            fail("No exception was thrown from bigIntegerValue with scale over limit");
        } catch (UnsupportedOperationException e) {
            // UnsupportedOperationException is expected to be thrown
            assertEquals(
                    "Scale value 100001 of this BigInteger exceeded maximal allowed value of 100000",
                    e.getMessage());
        }
    }

    // Test BigInteger scale value limit set from config Map using value above limit.
    // Call shall throw specific UnsupportedOperationException exception.
    // Config Map limit is stored in target JsonObject and shall be present for later value manipulation.
    // Default value is 100000 and config Map property lowered it to 50000 so value with scale 50001
    // test shall fail with exception message matching modified limits.
    public void testConfigBigIntegerScaleAboveLimit() {
        BigDecimal value = new BigDecimal("3.1415926535897932384626433")
                .setScale(50001, RoundingMode.HALF_UP);
        Map<String, String> config = new HashMap<String, String>();
        config.put("org.eclipse.parsson.maxBigIntegerScale", "50000");
        try {
            JsonObject jsonObject = Json.createBuilderFactory(config)
                    .createObjectBuilder()
                    .add("bigDecimal", value)
                    .build();
            jsonObject.getJsonNumber("bigDecimal").bigIntegerValue();
            fail("No exception was thrown from bigIntegerValue with scale over limit");
        } catch (UnsupportedOperationException e) {
            // UnsupportedOperationException is expected to be thrown
            assertEquals(
                    "Scale value 50001 of this BigInteger exceeded maximal allowed value of 50000",
                    e.getMessage());
        }
    }

    // Test BigDecimal max source characters array length using length equal to default limit of 1100.
    // Parsing shall pass and return value equal to source String.
    public void testLargeBigDecimalBellowLimit() {
        JsonReader reader = Json.createReader(new StringReader("[" + Π_1100 + "]"));
        JsonArray array = Json.createArrayBuilder().add(new BigDecimal(Π_1100)).build();
        JsonNumber check = array.getJsonNumber(0);
        JsonValue value = reader.readArray().get(0);
        assertEquals(value.getValueType(), JsonValue.ValueType.NUMBER);
        assertEquals(value, check);
    }

    // Test BigDecimal max source characters array length using length above default limit of 1100.
    // Parsing shall throw specific UnsupportedOperationException exception.
    public void testLargeBigDecimalAboveLimit() {
        JsonReader reader = Json.createReader(new StringReader("[" + Π_1101 + "]"));
        try {
            reader.readArray().get(0);
            fail("No exception was thrown from BigDecimal parsing with source characters array length over limit");
        } catch (UnsupportedOperationException e) {
            // UnsupportedOperationException is expected to be thrown
            assertEquals(
                    "Number of BigDecimal source characters 1101 exceeded maximal allowed value of 1100",
                    e.getMessage());
        }
    }

    // Test BigDecimal max source characters array length using length equal to custom limit of 500.
    // Parsing shall pass and return value equal to source String.
    public void testLargeBigDecimalBellowCustomLimit() {
        Map<String, String> config = new HashMap<String, String>();
        config.put(JsonConfig.MAX_BIGDECIMAL_LEN, "500");

        JsonReader reader = Json.createReaderFactory(config).createReader(new StringReader("[" + Π_500 + "]"));
        JsonArray array = Json.createArrayBuilder().add(new BigDecimal(Π_500)).build();
        JsonNumber check = array.getJsonNumber(0);
        JsonValue value = reader.readArray().get(0);
        assertEquals(value.getValueType(), JsonValue.ValueType.NUMBER);
        assertEquals(value, check);
    }

    // Test BigDecimal max source characters array length using length equal to custom limit of 200.
    // Parsing shall pass and return value equal to source String.
    public void testLargeBigDecimalAboveCustomLimit() {
        Map<String, String> config = new HashMap<String, String>();
        config.put(JsonConfig.MAX_BIGDECIMAL_LEN, "500");

        JsonReader reader = Json.createReaderFactory(config).createReader(new StringReader("[" + Π_501 + "]"));
        try {
            reader.readArray().get(0);
            fail("No exception was thrown from BigDecimal parsing with source characters array length over limit");
        } catch (UnsupportedOperationException e) {
            // UnsupportedOperationException is expected to be thrown
            assertEquals(
                    "Number of BigDecimal source characters 501 exceeded maximal allowed value of 500",
                    e.getMessage());
        }
    }
}
