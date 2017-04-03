/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2017 MicroBean.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.microbean.apache.commons.cli;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;

import javax.enterprise.event.Observes;

import javax.enterprise.inject.Produces;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import org.junit.Test;

import org.microbean.main.Main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ApplicationScoped
public class TestCommonsCliExtension {

  
  /*
   * Static fields.
   */

  
  /**
   * The number of instances of this class that have been created (in
   * the context of JUnit execution; any other usage is undefined).
   */
  private static int instanceCount;


  /*
   * Constructors
   */
  
  
  public TestCommonsCliExtension() {
    super();
    instanceCount++;
  }


  /*
   * Instance methods.
   */

  @Produces
  @ApplicationScoped
  private final Options produceOptions() {
    final Options options = new Options();
    final Option option = Option.builder("fi")
      .longOpt("frobnicationInterval")
      .argName("interval")
      .desc("The interval for frobnication.")
      .hasArg()
      .type(Integer.class)
      .build();
    options.addOption(option);
    return options;
  }
  
  private final void onStartup(@Observes @Initialized(ApplicationScoped.class) final Object event,
                               final CommandLine commandLine,
                               final Options options,
                               final HelpFormatter helpFormatter) {
    assertNotNull(commandLine);
    assertNotNull(options);
    assertNotNull(helpFormatter);
    helpFormatter.printHelp("frobnicatorApp", "This is the header", options, "This is the footer", true);
  }
  
  @Test
  public void testContainerStartup() {
    final int oldInstanceCount = instanceCount;
    Main.main(null);
    assertEquals(oldInstanceCount + 1, instanceCount);
  }
  
  
}
