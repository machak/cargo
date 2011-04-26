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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.cargo.container.Container;
import org.codehaus.cargo.container.configuration.Configuration;
import org.codehaus.cargo.container.configuration.ConfigurationType;
import org.codehaus.cargo.container.deployable.Deployable;
import org.codehaus.cargo.container.deployable.DeployableType;
import org.codehaus.cargo.container.deployable.EAR;
import org.codehaus.cargo.container.deployable.WAR;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;
import org.codehaus.cargo.generic.deployable.DefaultDeployableFactory;


/**
 * Creates a container from a configuration (CLIProperties)
 * or creates a configuration from a configured {@link Container}.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public final class CLIConfigurationBinding {
    /**
     * CLI property key.
     */
    private static final String ID = "cli.id";
    /**
     * CLI property key.
     */
    private static final String TYPE = "cli.type";
    /**
     * CLI property key.
     */
    private static final String HOME = "cli.home";
    /**
     * CLI property key.
     */
    private static final String CONFIG = "cli.config";
    /**
     * CLI property key.
     */
    private static final String EXTRA_CLASSPATH_PREFIX = "cli.extra_classpath.";
    /**
     * CLI property key.
     */
    private static final String WAR_LOCATION_PREFIX = "cli.war.location.";
    /**
     * CLI property key.
     */
    private static final String EAR_LOCATION_PREFIX = "cli.ear.location";
    /**
     * CLI property key.
     */
    private static final String WAR_CONTEXT_PREFIX = "cli.war.contextpath.";

    /**
     * Private no-arg constructor.
     */
    private CLIConfigurationBinding() {
    }

    /**
     * Creates a CLIProperty map from the passed container.
     *
     * @param container configured container
     * @return a map with CLIProperties
     */
    public static Map createCLIProperties(Container container) {
        final Map cliProperties = new LinkedHashMap();
        cliProperties.put(ID, container.getId());
        if (container.getHomeDir() != null) {
            cliProperties.put(HOME, container.getHomeDir().toString());
        }
        // add extraclasspath
        final String[] extraClasspath = container.getExtraClasspath();
        if (extraClasspath != null) {
            for (int i = 0; i < extraClasspath.length; i++) {
                cliProperties.put(EXTRA_CLASSPATH_PREFIX + i, extraClasspath[i]);
            }
        }
        final Configuration configuration = container.getConfiguration();
        if (configuration != null) {
            // TODO: For now we don't have a means of finding out the config type
            cliProperties.put(TYPE, ConfigurationType.STANDALONE.getType());
            // add general properties
            if (configuration.getDir() != null) {
                cliProperties.put(CONFIG, configuration.getDir().toString());
            }
            cliProperties.putAll(configuration.getProperties());
            // add deployables
            int warNo = 0;
            int earNo = 0;
            for (Iterator deployables = configuration.getDeployables().iterator();
                 deployables.hasNext(); ) {
                Deployable deployable = (Deployable) deployables.next();
                if (deployable instanceof WAR) {
                    cliProperties.put(WAR_LOCATION_PREFIX + warNo,
                            deployable.getFile().toString());
                    if (((WAR) deployable).getContext() != null) {
                        cliProperties.put(WAR_CONTEXT_PREFIX + warNo,
                                ((WAR) deployable).getContext());
                    }
                    warNo++;
                } else if (deployable instanceof EAR) {
                    cliProperties.put(EAR_LOCATION_PREFIX + earNo,
                            deployable.getFile().toString());
                    earNo++;
                }

            }

        }
        return cliProperties;
    }

    /**
     * Creates a {@link Container} based on the passed map.
     *
     * @param cliProperties map with container properties
     * @return container
     */
    private static Container createContainer(Map cliProperties) {
        ConfigurationType configurationType = getConfigurationType(cliProperties);
        String containerId = getContainerId(cliProperties);
        if (containerId == null) {
            throw new IllegalArgumentException("Missing parameter: cli.id");
        }
        Container container = null;
        if (configurationType != null) {
            container =
                    new DefaultContainerFactory().createContainer(containerId, configurationType);
        } else {
            container = new DefaultContainerFactory().createContainer(containerId);
        }
        String homeDir = (String) cliProperties.get(HOME);
        if (homeDir != null) {
            container.setHomeDir(homeDir);
        }
        // create extra classpath
        List extraClasspathList = new ArrayList();
        for (int i = 0; true; i++) {
            String extraClasspathElement = (String) cliProperties.get(EXTRA_CLASSPATH_PREFIX + i);
            if (extraClasspathElement == null) {
                if (extraClasspathList.isEmpty()) {
                    break;
                }
                container.setExtraClasspath((String[]) extraClasspathList.toArray(new String[i]));
            }
        }

        return container;
    }

    /**
     * @param cliProperties properties
     * @return id
     */
    private static String getContainerId(Map cliProperties) {
        return (String) cliProperties.get(ID);
    }

    /**
     * @param cliProperties properties
     * @return ConfigurationType
     */
    private static ConfigurationType getConfigurationType(Map cliProperties) {
        String configurationTypeValue = (String) cliProperties.get(TYPE);
        ConfigurationType configurationType = null;
        if (configurationTypeValue != null) {
            configurationType = ConfigurationType.toType(configurationTypeValue);
        }
        return configurationType;
    }

    /**
     * Creates a list of {@link Deployable}s based on the passed map.
     *
     * @param cliProperties property map
     * @return list of Deployables
     */
    private static List<Deployable> createDeployables(Map cliProperties) {
        String containerId = getContainerId(cliProperties);
        if (containerId == null) {
            throw new IllegalArgumentException("Missing parameter: cli.id");
        }
        List<Deployable> deployables = new ArrayList<Deployable>();
        for (int i = 0; true; i++) {
            String warPath = (String) cliProperties.get(WAR_LOCATION_PREFIX + i);
            if (warPath == null) {
                break;
            }
            final WAR war = (WAR) new DefaultDeployableFactory().createDeployable(containerId, warPath,
                    DeployableType.WAR);
            war.setContext((String) cliProperties.get(WAR_CONTEXT_PREFIX + i));
            deployables.add(war);
        }
        for (int i = 0; true; i++) {
            String earPath = (String) cliProperties.get(EAR_LOCATION_PREFIX + i);
            if (earPath == null) {
                break;
            }
            deployables.add(new DefaultDeployableFactory().createDeployable(containerId, earPath,
                    DeployableType.EAR));
        }
        return deployables;
    }

    /**
     * Creates a {@link Configuration} based on the passed properties.
     *
     * @param cliProperties map with properties
     * @return configuration
     */
    private static Configuration createConfiguraion(Map cliProperties) {
        String containerId = getContainerId(cliProperties);
        if (containerId == null) {
            throw new IllegalArgumentException("Missing parameter: cli.id");
        }
        String configDir = (String) cliProperties.get(CONFIG);
        Configuration configuration = null;
        if (configDir != null) {
            configuration =
                    new DefaultConfigurationFactory().createConfiguration(containerId,
                            getConfigurationType(cliProperties), new File(configDir));
        } else {
            configuration =
                    new DefaultConfigurationFactory().createConfiguration(containerId,
                            getConfigurationType(cliProperties));
        }
        // add other properties
        for (Iterator properties = cliProperties.entrySet().iterator(); properties.hasNext(); ) {
            Map.Entry property = (Map.Entry) properties.next();
            configuration.setProperty((String) property.getKey(), (String) property.getValue());
        }
        return configuration;
    }

    /**
     * Creates a configured {@link org.codehaus.cargo.container.Container}.
     *
     * @param cliProperties properties
     * @return a configured container
     */
    public static Container createConfiguredContainer(Map cliProperties) {
        Configuration configuration = createConfiguraion(cliProperties);
        List deployables = createDeployables(cliProperties);
        for (Iterator iterator = deployables.iterator(); iterator.hasNext(); ) {
            configuration.addDeployable((Deployable) iterator.next());
        }
        Container container = createContainer(cliProperties);
        container.setConfiguration(configuration);
        return container;
    }
}
