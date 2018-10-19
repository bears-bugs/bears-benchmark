/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2010-2018 SonarOpenCommunity
 * http://github.com/SonarOpenCommunity/sonar-cxx
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.cxx.tag;

public final class Tag {

  // checks and rules use following tags
  public static final String BRAIN_OVERLOAD = "brain-overload";
  public static final String BUG = "bug";
  public static final String CERT = "cert";
  public static final String CONVENTION = "convention";
  public static final String MISRA = "misra";
  public static final String SECURITY = "security";
  public static final String TOOL_ERROR = "tool-error";
  public static final String PITFALL = "pitfall";
  public static final String PREPROCESSOR = "preprocessor";
  public static final String UNUSED = "unused";
  public static final String BAD_PRACTICE = "bad-practice";
  public static final String CONFUSING = "confusing";

  // tags not used
  public static final String CLUMSY = "clumsy";
  public static final String CWE = "cwe";
  public static final String DENIAL_OF_SERVICE = "denial-of-service";
  public static final String DESIGN = "design";
  public static final String ERROR_HANDLING = "error-handling";
  public static final String LEAK = "leak";
  public static final String LOCK_IN = "lock-in";
  public static final String MULTI_THREADING = "multi-threading";
  public static final String OBSOLETE = "obsolete";
  public static final String OWASP_A1 = "owasp-a1";
  public static final String OWASP_A2 = "owasp-a2";
  public static final String OWASP_A6 = "owasp-a6";
  public static final String OWASP_A9 = "owasp-a9";
  public static final String PERFORMANCE = "performance";
  public static final String SANS_TOP_25_INSECURE = "sans-top25-insecure";
  public static final String SANS_TOP_25_POROUS = "sans-top25-porous";
  public static final String SANS_TOP_25_RISKY = "sans-top25-risky";
  public static final String SERIALIZATION = "serialization";
  public static final String SQL = "sql";
  public static final String SUSPICIOUS = "suspicious";
  public static final String UNPREDICTABLE = "unpredictable";

  private Tag() {
  }
}
