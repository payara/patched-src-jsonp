/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.json;


import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Defines string formatting method for each constant in the resource file
 *
 * @author Jitendra Kotamraju
 */
final class JsonMessages {
    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("org.glassfish.json.messages");

    static String PARSER_GETSTRING_ERR(Object arg0) {
        return localize("parser.getString.err", arg0);
    }

    static String PARSER_ISINTEGRALNUMBER_ERR(Object arg0) {
        return localize("parser.isIntegralNumber.err", arg0);
    }

    static String PARSER_GETINT_ERR(Object arg0) {
        return localize("parser.getInt.err", arg0);
    }

    static String PARSER_GETLONG_ERR(Object arg0) {
        return localize("parser.getLong.err", arg0);
    }

    static String PARSER_GETBIGDECIMAL_ERR(Object arg0) {
        return localize("parser.getBigDecimal.err", arg0);
    }

    static String PARSER_EXPECTED_EOF(Object arg0) {
        return localize("parser.expected.eof", arg0);
    }

    static String PARSER_TOKENIZER_CLOSE_IO() {
        return localize("parser.tokenizer.close.io");
    }

    static String PARSER_INVALID_TOKEN(Object token, Object location, Object expectedTokens) {
        return localize("parser.invalid.token", token, location, expectedTokens);
    }

    static String GENERATOR_FLUSH_IO_ERR() {
        return localize("generator.flush.io.err");
    }

    static String GENERATOR_CLOSE_IO_ERR() {
        return localize("generator.close.io.err");
    }

    static String GENERATOR_WRITE_IO_ERR() {
        return localize("generator.write.io.err");
    }

    private static String localize(String key, Object ... args) {
        try {
            String msg = BUNDLE.getString(key);
            return MessageFormat.format(msg, args);
        } catch (Exception e) {
            return getDefaultMessage(key, args);
        }
    }

    private static String getDefaultMessage(String key, Object ... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("[failed to localize] ");
        sb.append(key);
        if (args != null) {
            sb.append('(');
            for (int i = 0; i < args.length; ++i) {
                if (i != 0)
                    sb.append(", ");
                sb.append(String.valueOf(args[i]));
            }
            sb.append(')');
        }
        return sb.toString();
    }

}
