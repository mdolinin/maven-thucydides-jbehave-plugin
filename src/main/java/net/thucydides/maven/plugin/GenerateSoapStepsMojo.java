package net.thucydides.maven.plugin;

import com.sun.codemodel.JClassAlreadyExistsException;
import net.thucydides.maven.plugin.saop2bdd.SoapStepsGenerator;
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

/**
 * @goal generate-soap-steps
 * @phase generate-test-sources
 */
public class GenerateSoapStepsMojo extends AbstractMojo {

    /**
     * The directory containing generated classes of the project being tested.
     * This will be included after the test
     * classes in the test classpath.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private File classesDirectory;

    /**
     * Package name for Soap jbehave steps
     *
     * @parameter
     * @required
     */
    public String soapStepsPackage;

    /**
     * Packages with soap services to scan.
     *
     * @parameter
     * @required
     */
    public String[] soapServicePackages;

    /**
     * Location of the file.
     *
     * @parameter expression="${project.soap.steps.stories.directory}"
     * default-value="${project.build.directory}/generated-test-sources/soap-steps"
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
        File outputDir = getOutputDirectory();
        if (!outputDir.exists()
                && outputDir.mkdirs()) {
            outputDir = getOutputDirectory();
        }
        SoapStepsGenerator stepsGenerator = new SoapStepsGenerator();
        Log log = getLog();
        String soapStepsPackage = getSoapStepsPackage() == null ? "" : getSoapStepsPackage() + ".";
        try {
            for (String soapServicePack : getSoapServicePackages()) {
                stepsGenerator.generateFor(soapServicePack, getClassLoader(), outputDir, log, soapStepsPackage);
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (JClassAlreadyExistsException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        getProject().addTestCompileSourceRoot(outputDir.getAbsolutePath());
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

    public File getClassesDirectory() {
        return classesDirectory;
    }

    public String getSoapStepsPackage() {
        return soapStepsPackage;
    }

    public String[] getSoapServicePackages() {
        return soapServicePackages;
    }

    public File getTestClassesDirectory() {
        return testClassesDirectory;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setClassesDirectory(File classesDirectory) {
        this.classesDirectory = classesDirectory;
    }

    public void setSoapStepsPackage(String soapStepsPackage) {
        this.soapStepsPackage = soapStepsPackage;
    }

    public void setSoapServicePackages(String[] soapServicePackages) {
        this.soapServicePackages = soapServicePackages;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public void setTestClassesDirectory(File testClassesDirectory) {
        this.testClassesDirectory = testClassesDirectory;
    }

}
