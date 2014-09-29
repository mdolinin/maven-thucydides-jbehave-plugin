package net.thucydides.maven.plugin;

import com.sun.codemodel.JClassAlreadyExistsException;
import net.thucydides.maven.plugin.saop2bdd.GenerateStepsApp;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @goal generate-soap-steps
 * @phase process-test-resources
 */
public class GenerateSoapStepsMojo extends AbstractMojo {
    private final static Logger logger = Logger.getLogger(GenerateSoapStepsMojo.class.getName());

    /**
     * The directory containing generated classes of the project being tested. This will be included after the test
     * classes in the test classpath.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private File classesDirectory;

    /**
     * Package name for Soap jbehave steps
     *
     * @parameter expression="${project.soap.steps.package}" default-value="${project.groupId}"
     * @required
     */
    public String packageForSoapSteps;

    /**
     * Location of the file.
     *
     * @parameter expression="${project.soap.steps.stories.directory}" default-value="${project.build.directory}/generated-test-sources"
     * @required
     */
    private File outputDirectory;

    /**
     * The Maven Project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The directory containing generated test classes of the project being tested. This will be included at the
     * beginning of the test classpath.
     *
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     */
    private File testClassesDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        GenerateStepsApp stepsApp = new GenerateStepsApp();
        Log log = getLog();
        try {
            stepsApp.init(packageForSoapSteps, getClassLoader(), outputDirectory, log);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (JClassAlreadyExistsException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Generate the test classpath.
     *
     * @return List containing the classpath elements
     * @throws MojoFailureException when it happens
     */
    public Set<URL> getUrlsForCustomClasspath() throws MojoFailureException, MojoExecutionException {
        Set<URL> classpath = new HashSet<URL>(2 + getProject().getArtifacts().size());

        try {
            classpath.add(getTestClassesDirectory().getAbsoluteFile().toURI().toURL());
            classpath.add(getClassesDirectory().getAbsoluteFile().toURI().toURL());

            @SuppressWarnings("unchecked") Set<Artifact> classpathArtifacts = getProject().getArtifacts();

            for (Artifact artifact : classpathArtifacts) {
                if (artifact.getArtifactHandler().isAddedToClasspath() && !artifact.getGroupId().startsWith("org.apache.maven")) {
                    File file = artifact.getFile();
                    if (file != null) {
                        classpath.add(file.toURI().toURL());
                    }
                }
            }
        } catch (MalformedURLException e) {
            getLog().error(e.getMessage(), e);
        }
        return classpath;
    }

    private ClassLoader getClassLoader() throws MojoFailureException, MojoExecutionException {
        ClassLoader pluginClassLoader = getClass().getClassLoader();
        Set<URL> projectClasspathList = getUrlsForCustomClasspath();
        ClassLoader projectClassLoader = new URLClassLoader(
                projectClasspathList.toArray(
                        new URL[projectClasspathList.size()]), pluginClassLoader);
        return projectClassLoader;
    }

    private File getClassesDirectory() {
        return classesDirectory;
    }

    private File getTestClassesDirectory() {
        return testClassesDirectory;
    }

    private MavenProject getProject() {
        return project;
    }

}
