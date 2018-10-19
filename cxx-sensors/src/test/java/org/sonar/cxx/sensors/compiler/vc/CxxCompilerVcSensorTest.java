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
package org.sonar.cxx.sensors.compiler.vc;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.cxx.CxxLanguage;
import org.sonar.cxx.sensors.compiler.CxxCompilerSensor;
import org.sonar.cxx.sensors.utils.TestUtils;

public class CxxCompilerVcSensorTest {

  private DefaultFileSystem fs;
  private CxxLanguage language;
  private final MapSettings settings = new MapSettings();

  @Before
  public void setUp() {
    fs = TestUtils.mockFileSystem();
    language = TestUtils.mockCxxLanguage();
    when(language.getPluginProperty(CxxCompilerVcSensor.REPORT_PATH_KEY)).thenReturn("sonar.cxx." + CxxCompilerVcSensor.REPORT_PATH_KEY);
    when(language.getPluginProperty(CxxCompilerVcSensor.REPORT_CHARSET_DEF)).thenReturn("sonar.cxx." + CxxCompilerVcSensor.REPORT_CHARSET_DEF);
    when(language.getPluginProperty(CxxCompilerVcSensor.REPORT_REGEX_DEF)).thenReturn("sonar.cxx." + CxxCompilerVcSensor.REPORT_REGEX_DEF);
  }

  @Test
  public void sensorDescriptorVc() {
    DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
    CxxCompilerVcSensor sensor = new CxxCompilerVcSensor(language);
    sensor.describe(descriptor);
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(descriptor.name()).isEqualTo(language.getName() + " CxxCompilerVcSensor");
    softly.assertThat(descriptor.languages()).containsOnly(language.getKey());
    softly.assertThat(descriptor.ruleRepositories())
      .containsOnly(CxxCompilerVcRuleRepository.getRepositoryKey(language));
    softly.assertAll();
  }

  @Test
  public void shouldReportACorrectVcViolations() {
    SensorContextTester context = SensorContextTester.create(fs.baseDir());

    settings.setProperty(language.getPluginProperty(CxxCompilerVcSensor.REPORT_PATH_KEY), "compiler-reports/BuildLog.htm");
    settings.setProperty(language.getPluginProperty(CxxCompilerVcSensor.REPORT_CHARSET_DEF), "UTF-16");
    context.setSettings(settings);

    context.fileSystem().add(TestInputFileBuilder.create("ProjectKey", "zipmanager.cpp")
      .setLanguage("cpp").initMetadata("asd\nasdas\nasda\n").build());

    CxxCompilerSensor sensor = new CxxCompilerVcSensor(language);
    sensor.execute(context);
    assertThat(context.allIssues()).hasSize(9);
  }

  @Test
  public void shouldReportBCorrectVcViolations() {
    when(language.getStringOption(language.getPluginProperty(CxxCompilerVcSensor.REPORT_CHARSET_DEF))).thenReturn(Optional.of("UTF-8"));
    SensorContextTester context = SensorContextTester.create(fs.baseDir());

    settings.setProperty(language.getPluginProperty(CxxCompilerVcSensor.REPORT_PATH_KEY), "compiler-reports/VC-report.vclog");
    settings.setProperty(language.getPluginProperty(CxxCompilerVcSensor.REPORT_CHARSET_DEF), "UTF-8");
    settings.setProperty(language.getPluginProperty(CxxCompilerVcSensor.REPORT_REGEX_DEF),
      ".*>(?<file>.*)\\((?<line>\\d+)\\):\\x20warning\\x20(?<id>C\\d+):(?<message>.*)");
    context.setSettings(settings);

    context.fileSystem().add(TestInputFileBuilder.create("ProjectKey", "Server/source/zip/zipmanager.cpp")
      .setLanguage("cpp").initMetadata("asd\nasdas\nasda\n").build());

    CxxCompilerSensor sensor = new CxxCompilerVcSensor(language);
    sensor.execute(context);
    assertThat(context.allIssues()).hasSize(9);
  }

}
