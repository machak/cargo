package org.codehaus.cargo.intellijidea;

import javax.swing.Icon;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.j2ee.appServerIntegrations.AppServerIntegration;
import com.intellij.j2ee.run.configuration.J2EEConfigurationFactory;
import com.intellij.j2ee.run.configuration.J2EEConfigurationType;
import com.intellij.j2ee.run.configuration.JavaCommandLineStartupPolicy;
import com.intellij.j2ee.run.configuration.ServerModel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

/**
 * Abstract configuration type. Concrete subclasses Must be registered in
 * plugin.xml configuration file.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 * @see org.codehaus.cargo.intellijidea.jo.Jo1xConfigurationType
 */
public abstract class AbstractCargoConfigurationType extends J2EEConfigurationType {
    /**
     * AppServerIntegrationClass.
     */
    private Class<AbstractCargoAppServerIntegration> appServerIntegrationClass;

    /**
     * @param appServerIntegrationClass the app server integration component this
     *                                  type is dependent to.
     */
    public AbstractCargoConfigurationType(Class<AbstractCargoAppServerIntegration> appServerIntegrationClass) {
        this.appServerIntegrationClass = appServerIntegrationClass;
    }

    /**
     * Called by IntelliJIdea.
     *
     * @param factory factory.
     * @param project project
     * @param isLocal isLocal
     * @return RunConfiguration
     */
    protected RunConfiguration createJ2EEConfigurationTemplate(
            ConfigurationFactory factory, Project project, boolean isLocal) {
        // we simply pretend that this is local
        return J2EEConfigurationFactory.getInstance().createJ2EERunConfiguration(factory, project,
                createServerModel(),
                getAppServerIntegration(), isLocal,
                createStartupPolicy());
    }

    /**
     * Creates a startup policy as part of the J2EEConfigurationTemplate.
     *
     * @return a new instance of {@link CargoStartupPolicy}
     * @see #createJ2EEConfigurationTemplate
     */
    protected JavaCommandLineStartupPolicy createStartupPolicy() {
        return new CargoStartupPolicy();
    }

    /**
     * Create a {@link ServerModel} as part of the J2EEConfigurationTemplate.
     *
     * @return a new instance of {@link CargoConfigurationModel}
     * @see #createJ2EEConfigurationTemplate
     */
    protected ServerModel createServerModel() {
        return new CargoConfigurationModel();
    }

    /**
     * Returns the related {@link AppServerIntegration} instance.
     *
     * @return app server intgration instance
     */
    private AppServerIntegration getAppServerIntegration() {
        return ApplicationManager.getApplication().getComponent(appServerIntegrationClass);
    }

    /**
     * Display name.
     *
     * @return display name
     */
    public String getDisplayName() {
        return getAppServerIntegration().getPresentableName();
    }

    /**
     * Description.
     *
     * @return description
     */
    public String getConfigurationTypeDescription() {
        return getAppServerIntegration().getPresentableName() + " Configuration";
    }

    /**
     * Icon.
     *
     * @return icon
     */
    public Icon getIcon() {
        return AbstractCargoAppServerIntegration.ICON;
    }

    /**
     * Component name.
     *
     * @return component name
     */
    public String getComponentName() {
        return getClass().getName();
    }
}
