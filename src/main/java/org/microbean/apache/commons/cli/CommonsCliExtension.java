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

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.event.Observes;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

import javax.enterprise.inject.literal.SingletonLiteral;

import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import javax.inject.Named;
import javax.inject.Singleton; // for javadoc only

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option; // for javadoc only
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * A <a
 * href="http://docs.jboss.org/cdi/spec/2.0-PRD/cdi-spec.html#spi"
 * target="_parent">CDI 2.0 portable extension</a> that exposes the <a
 * href="https://commons.apache.org/proper/commons-cli/">Apache
 * Commons CLI project</a> as CDI beans.
 *
 * @author <a href="http://about.me/lairdnelson"
 * target="_parent">Laird Nelson</a>
 *
 * @see CommandLine
 *
 * @see CommandLineParser
 *
 * @see Option
 *
 * @see Options
 */
public class CommonsCliExtension implements Extension {


  /*
   * Constructors.
   */

  
  /**
   * Creates a new {@link CommonsCliExtension}.
   */
  public CommonsCliExtension() {
    super();
  }


  /*
   * Instance methods.
   */
  

  /**
   * Adds the {@link DefaultParser} and {@link HelpFormatter} classes
   * as annotated types in {@linkplain ApplicationScoped application
   * scope} and {@link Singleton} scope respectively.
   *
   * @param event the {@link BeforeBeanDiscovery} event indicating
   * that bean discovery has started; may be {@code null} in which
   * case no action will be taken
   *
   * @see BeforeBeanDiscovery#addAnnotatedType(Class, String)
   *
   * @see DefaultParser
   *
   * @see HelpFormatter
   */
  private final void beforeBeanDiscovery(@Observes final BeforeBeanDiscovery event) {
    if (event != null) {
      event.addAnnotatedType(DefaultParser.class, "commons-cli").add(ApplicationScoped.Literal.INSTANCE);
      event.addAnnotatedType(HelpFormatter.class, "commons-cli").add(SingletonLiteral.INSTANCE);
      // Somehow, the Producers nested class below is automatically
      // discovered.  I think this is a bug.  If it is fixed, then
      // uncomment this line.
      // event.addAnnotatedType(Producers.class, "commons-cli").add(ApplicationScoped.Literal.INSTANCE);
    }
  }


  /*
   * Inner and nested classes.
   */


  /**
   * A class housing several <a
   * href="http://docs.jboss.org/cdi/spec/2.0-PRD/cdi-spec.html#producer_method">producer
   * methods</a> that produce certain <a
   * href="https://commons.apache.org/proper/commons-cli/">Apache
   * Commons CLI components</a>.
   *
   * @author <a href="http://about.me/lairdnelson"
   * target="_parent">Laird Nelson</a>
   */
  private static final class Producers {


    /*
     * Constructors.
     */


    /**
     * Creates a new {@link Producers} instance.
     */
    private Producers() {
      super();
    }
    

    /*
     * Static methods.
     */


    /**
     * {@linkplain Produces Produces} a {@link CommandLine} object in
     * {@linkplain ApplicationScoped application} scope.
     *
     * <p>This method never returns {@code null}.</p>
     *
     * @param parser a {@link CommandLineParser}; must not be {@code
     * null}
     *
     * @param optionsInstance an {@link Instance} of {@link Options};
     * may be {@code null} or {@linkplain Instance#isUnsatisfied()
     * unsatisfied} in which case a new, "empty" {@link Options}
     * instance will be parsed instead
     *
     * @param commandLineArguments an {@link Instance} representing
     * the actual command line arguments; may be {@code null} or
     * {@linkplain Instance#isUnsatisfied()}
     *
     * @return a {@link CommandLine} instance; never {@code null}
     *
     * @exception NullPointerException if {@code parser} is {@code null}
     *
     * @exception ParseException if there was a problem parsing the
     * command line arguments
     *
     * @see CommandLineParser#parse(Options, String[])
     */
    @Produces
    @ApplicationScoped
    private static final CommandLine produceCommandLine(final CommandLineParser parser,
                                                        final Instance<Options> optionsInstance,
                                                        @Named("commandLineArguments") final Instance<String[]> commandLineArgumentsInstance)
      throws ParseException {      
      Objects.requireNonNull(parser);      
      final Options options;
      if (optionsInstance != null && !optionsInstance.isUnsatisfied()) {
        options = optionsInstance.get();
      } else {
        options = new Options();
      }
      assert options != null;
      final String[] commandLineArguments;
      if (commandLineArgumentsInstance != null && !commandLineArgumentsInstance.isUnsatisfied()) {
        commandLineArguments = commandLineArgumentsInstance.get();
      } else {
        commandLineArguments = null;
      }
      final CommandLine returnValue = parser.parse(options, commandLineArguments, true);
      return returnValue;
    }
    
  }
  
}
