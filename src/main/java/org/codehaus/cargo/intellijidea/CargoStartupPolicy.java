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
package org.codehaus.cargo.intellijidea;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.j2ee.appServerIntegrations.ApplicationServer;
import com.intellij.j2ee.deployment.DeploymentModel;
import com.intellij.j2ee.deployment.DeploymentSource;
import com.intellij.j2ee.j2eeDom.J2EEModuleProperties;
import com.intellij.j2ee.make.ModuleBuildProperties;
import com.intellij.j2ee.run.configuration.CommonModel;
import com.intellij.j2ee.run.configuration.JavaCommandLineStartupPolicy;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.projectRoots.ProjectJdk;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import org.codehaus.cargo.cli.CLIConfigurationBinding;
import org.codehaus.cargo.cli.Main;
import org.codehaus.cargo.container.Container;
import org.codehaus.cargo.container.configuration.Configuration;
import org.codehaus.cargo.container.deployable.Deployable;
import org.codehaus.cargo.container.deployable.WAR;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.codehaus.cargo.generic.deployable.DefaultDeployableFactory;
import org.codehaus.cargo.generic.deployable.DeployableType;

/**
 * Startup policy for Cargo servers.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class CargoStartupPolicy implements JavaCommandLineStartupPolicy {

    /**
     * Creates parameters for starting Cargo in a separate JVM.
     *
     * @param commonModel model
     * @return JVM parameters
     * @throws ExecutionException if something goes wrong
     */
    public JavaParameters createCommandLine(CommonModel commonModel) throws ExecutionException {
        try {
            final JavaParameters parameters = new JavaParameters();
            final CargoConfigurationModel cargoConfigurationModel = (CargoConfigurationModel) commonModel.getServerModel();
            final Configuration cargoConfiguration = cargoConfigurationModel.getCargoConfiguration();
            final Container cargoContainer = new DefaultContainerFactory().createContainer(cargoConfigurationModel.getContainerId(), cargoConfiguration);

            // add extra classpath to container
            setExtraClasspath(cargoContainer, commonModel);
            // set home dir
            final ApplicationServer applicationServer = commonModel.getApplicationServer();
            final CargoContainerModel cargoContainerModel
                    = (CargoContainerModel) applicationServer.getPersistentData();
            cargoContainer.setHomeDir(cargoContainerModel.getHomeDir());

            // remove deployables
            cargoConfiguration.getDeployables().clear();
            // add deployables
            for (int i = 0; i < commonModel.getModules().length; i++) {
                final Deployable deployable = createDeployable(commonModel,
                        commonModel.getModules()[i], cargoContainer);
                cargoConfiguration.addDeployable(deployable);
            }

            final Map cliProperties = CLIConfigurationBinding.createCLIProperties(cargoContainer);
            final Properties properties = new Properties();
            properties.putAll(cliProperties);
            File cliPropertiesFile = File.createTempFile(cargoContainer.getId() + "_", ".cargo");
            cliPropertiesFile.deleteOnExit();
            properties.store(new FileOutputStream(cliPropertiesFile),
                    "Temporary configuration file for Cargo container. " + new Date());
            parameters.setMainClass(Main.class.getName());
            parameters.getProgramParametersList().add("@" + cliPropertiesFile);
            ProjectJdk projectJdk = ProjectRootManager.getInstance(commonModel.getProject())
                    .getProjectJdk();
            if (projectJdk == null) {
                throw new ExecutionException("Project JDK not set.");
            }
            parameters.setJdk(projectJdk);
            addJarToClasspath(Container.class, parameters);
            addJarToClasspath(CLIConfigurationBinding.class, parameters);
            addJarToClasspath(org.apache.tools.ant.BuildListener.class, parameters);
            return parameters;
        } catch (IOException ioe) {
            throw new ExecutionException(ioe.toString());
        }
    }

    /**
     * Creates a Cargo Deployable.
     *
     * @param commonModel    commonModel
     * @param module         module
     * @param cargoContainer container
     * @return Deployable
     */
    private Deployable createDeployable(CommonModel commonModel, final Module module,
                                        final Container cargoContainer) {
        Deployable deployable = null;
        final String deploymentPath = getDeploymentPath(commonModel, module);
        if (module.getModuleType().equals(ModuleType.WEB)) {
            deployable = new DefaultDeployableFactory().createDeployable(
                    cargoContainer.getId(), deploymentPath, DeployableType.WAR);
            final CargoModuleDeploymentModel cargoModuleDeploymentModel
                    = ((CargoModuleDeploymentModel) getDeploymentModel(module, commonModel));
            String contextPath = cargoModuleDeploymentModel.getContextPath();
            // remove slash
            if (contextPath != null && contextPath.startsWith("/")) {
                contextPath = contextPath.substring(1);
            }
            ((WAR) deployable).setContext(contextPath);
        } else if (module.getModuleType().equals(ModuleType.J2EE_APPLICATION)) {
            deployable = new DefaultDeployableFactory().createDeployable(
                    cargoContainer.getId(), deploymentPath, DeployableType.EAR);
        }
        return deployable;
    }

    /**
     * Compute deployment path.
     *
     * @param module      module
     * @param commonModel comonModel
     * @return deployment path.
     */
    private static String getDeploymentPath(CommonModel commonModel, Module module) {
        DeploymentModel deploymentModel = getDeploymentModel(module, commonModel);
        ModuleBuildProperties buildProperties = ((ModuleBuildProperties) ModuleBuildProperties
                .getInstance(module));

        if (buildProperties == null) {
            return null;
        }

        String deploymentPath = null;

        if (DeploymentSource.FROM_EXPLODED == deploymentModel.getDeploymentSource()) {
            deploymentPath = buildProperties.getExplodedPath();
        }

        if (DeploymentSource.FROM_JAR == deploymentModel.getDeploymentSource()) {
            deploymentPath = buildProperties.getJarPath();
        }

        return deploymentPath != null ? deploymentPath.replace('/', File.separatorChar) : null;
    }

    /**
     * Get DeploymentModel.
     *
     * @param module      module
     * @param commonModel current commonModel
     * @return deployment model
     */
    private static DeploymentModel getDeploymentModel(Module module, CommonModel commonModel) {
        J2EEModuleProperties j2EEModuleProperties = J2EEModuleProperties.getInstance(module);
        return commonModel.getDeploymentModel(j2EEModuleProperties);
    }


    /**
     * Sets the extra classpath
     * ({@link org.codehaus.cargo.container.Container#getExtraClasspath()}).
     *
     * @param cargoContainer cargo container
     * @param commonModel    model
     */
    private void setExtraClasspath(final Container cargoContainer, CommonModel commonModel) {
        List<String> extraClasspathList = new ArrayList<String>();
        VirtualFile[] files = commonModel.getApplicationServer().getLibrary().getFiles(OrderRootType.CLASSES);
        for (VirtualFile file : files) {
            extraClasspathList.add(file.getPresentableUrl());
        }
        cargoContainer.setExtraClasspath((String[]) extraClasspathList.toArray(new String[extraClasspathList.size()]));
    }

    /**
     * Adds the jar a particular class is in to the classpath of the given
     * JavaParameters.
     *
     * @param clazz      class that's in the target jar
     * @param parameters JavaParameters
     * @throws ExecutionException if the jar cannot be determined
     * @throws IOException        if something else goes wrong
     */
    private void addJarToClasspath(final Class<?> clazz, final JavaParameters parameters)
            throws ExecutionException, IOException {
        final URL classJarURL = clazz.getResource('/' + clazz.getName().replace('.', '/') + ".class");
        if (classJarURL == null) {
            throw new ExecutionException("Failed to find " + clazz.getName() + " URL");
        }
        final JarURLConnection jarURLConnection = (JarURLConnection) classJarURL.openConnection();
        final JarFile jarFile = jarURLConnection.getJarFile();
        final String containerJarFile = jarFile.getName();
        parameters.getClassPath().add(containerJarFile);
    }

}
