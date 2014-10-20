package net.thucydides.maven.plugin;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class WhenGenerateSoapBddStepsTest {
    private String expectedPathToFile = "./src/test/java/net/thucydides/maven/plugin/test/example/HelloWorldImplServiceSteps.java";
    GenerateSoapStepsMojo plugin;
    File outputDirectory;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setupPlugin() throws IOException {
        outputDirectory = temporaryFolder.newFolder("soap-output");
        plugin = new GenerateSoapStepsMojo();
        plugin.setOutputDirectory(new File("./target/generated-test-sources"));
        plugin.setOutputDirectory(outputDirectory);
        plugin.setSoapStepsPackages(new String[]{"net.thucydides.maven.plugin.test.example"});
        plugin.setClassesDirectory(new File("net.thucydides.test"));
        plugin.setTestClassesDirectory(new File("./target/"));
        plugin.setProject(new MavenProject());
    }

    @Test
    public void generateBddStepsFormSoap() throws IOException {
        try {
            plugin.execute();
        } catch (MojoExecutionException e) {
            e.printStackTrace();
        } catch (MojoFailureException e) {
            e.printStackTrace();
        }
        File destinationDirectory = new File(outputDirectory, plugin.soapStepsPackages[0].replaceAll("\\.", "/"));
        File expectedFile = new File(destinationDirectory, "HelloWorldImplServiceSteps.java");
        File actualFile = new File(expectedPathToFile);
        assertEquals(FileUtils.readLines(actualFile), FileUtils.readLines(expectedFile));
    }
}
