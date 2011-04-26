/*
 * ========================================================================
 *
 * Copyright 2004-2005 Vincent Massol.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ========================================================================
 */
package org.codehaus.cargo.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.codehaus.cargo.container.Container;

/**
 * Main class for simple command line interface.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public final class Main {

    /**
     * Private constructor. Class is not meant to be instantiated.
     */
    private Main() {
    }

    /**
     * Main method.
     *
     * @param args accepts only one argument, a file name with a @ prefix
     * @throws IOException if an io problem occurs
     */
    public static void main(String[] args) throws IOException {
        Container container = null;
        if (args.length == 1 && args[0].startsWith("@")) {
            final File file = new File(args[0].substring(1));
            final Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            container = CLIConfigurationBinding.createConfiguredContainer(properties);
        } else {
            // add other means of passing parameters here.
            usage();
        }
        Runtime.getRuntime().addShutdownHook(new ContainerShutdownHook(container));
        container.start();
    }

    /**
     * Prints usage information.
     */
    private static void usage() {
        throw new IllegalArgumentException("Currently only @<PropertiesFile> is supported.");
    }


    /**
     * Shutdown hook for a clean shutdown.
     */
    private static class ContainerShutdownHook extends Thread {
        /**
         * Container.
         */
        private Container container;

        /**
         * @param container Container to shut down on VM exit.
         */
        public ContainerShutdownHook(Container container) {
            super("Cargo Container Shutdown: " + container);
            this.container = container;
        }

        /**
         * Calls {@link Container#stop()}.
         */
        public void run() {
            container.stop();
        }
    }
}
