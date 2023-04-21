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

package org.glassfish.json;

import org.glassfish.json.api.BufferPool;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import javax.json.spi.JsonProvider;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
public class JsonProviderImpl extends JsonProvider {

    private final BufferPool bufferPool = new BufferPoolImpl();
    private final JsonContext emptyContext = new JsonContext(null, bufferPool);

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return new JsonGeneratorImpl(writer, emptyContext);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return new JsonGeneratorImpl(out, emptyContext);
    }

    @Override
    public JsonParser createParser(Reader reader) {
        return new JsonParserImpl(reader, emptyContext);
    }

    @Override
    public JsonParser createParser(InputStream in) {
        return new JsonParserImpl(in, emptyContext);
    }

    @Override
    public JsonParserFactory createParserFactory(Map<String, ?> config) {
        return new JsonParserFactoryImpl(new JsonContext(config, bufferPool));
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config) {
        return config == null
                ? new JsonGeneratorFactoryImpl(emptyContext)
                : new JsonGeneratorFactoryImpl(
                        new JsonContext(config, bufferPool,
                                        JsonGenerator.PRETTY_PRINTING,
                                        JsonContext.PROPERTY_BUFFER_POOL));
    }

    @Override
    public JsonReader createReader(Reader reader) {
        return new JsonReaderImpl(reader, emptyContext);
    }

    @Override
    public JsonReader createReader(InputStream in) {
        return new JsonReaderImpl(in, emptyContext);
    }

    @Override
    public JsonWriter createWriter(Writer writer) {
        return new JsonWriterImpl(writer, emptyContext);
    }

    @Override
    public JsonWriter createWriter(OutputStream out) {
        return new JsonWriterImpl(out, emptyContext);
    }

    @Override
    public JsonWriterFactory createWriterFactory(Map<String, ?> config) {
        return config == null
                ? new JsonWriterFactoryImpl(emptyContext)
                : new JsonWriterFactoryImpl(
                        new JsonContext(config, bufferPool,
                                        JsonGenerator.PRETTY_PRINTING,
                                        JsonContext.PROPERTY_BUFFER_POOL));
    }

    @Override
    public JsonReaderFactory createReaderFactory(Map<String, ?> config) {
        return config == null
                ? new JsonReaderFactoryImpl(emptyContext)
                : new JsonReaderFactoryImpl(
                        new JsonContext(config, bufferPool,
                                        JsonContext.PROPERTY_BUFFER_POOL));
    }

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return new JsonObjectBuilderImpl(emptyContext);
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return new JsonArrayBuilderImpl(emptyContext);
    }

    @Override
    public JsonBuilderFactory createBuilderFactory(Map<String,?> config) {
        return config == null
                ? new JsonBuilderFactoryImpl(emptyContext)
                : new JsonBuilderFactoryImpl(
                new JsonContext(config, bufferPool,
                        JsonContext.PROPERTY_BUFFER_POOL));
    }

}
