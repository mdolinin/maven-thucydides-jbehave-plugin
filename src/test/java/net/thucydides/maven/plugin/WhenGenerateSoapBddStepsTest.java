package net.thucydides.maven.plugin;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WhenGenerateSoapBddStepsTest {
    private String expectedPathToFile = "./src/test/java/net/thucydides/maven/plugin/test/example/HelloWorldImplServiceSteps.java";
    GenerateSoapStepsMojo plugin;
    File outputDirectory;
    private static final String DELIMITER = "\n\n---------------------------------------\n\n";
    private volatile boolean testsPassed;

    public TemporaryFolder temporaryFolder;

    @Before
    public void setupPlugin() throws IOException {
        temporaryFolder = new TemporaryFolder();
        temporaryFolder.create();
        outputDirectory = temporaryFolder.newFolder("soap-output");
        plugin = new GenerateSoapStepsMojo();
        plugin.setOutputDirectory(new File("./target/generated-test-sources"));
        plugin.setOutputDirectory(outputDirectory);
        plugin.setSoapServicePackages(new String[]{"net.thucydides.maven.plugin.test.example"});
        plugin.setClassesDirectory(new File("net.thucydides.test"));
        plugin.setTestClassesDirectory(new File("./target/"));
        plugin.setProject(new MavenProject());
    }

    @After
    public void removeTmpFolderIfTestsPassed(){
        if(testsPassed){
            outputDirectory.delete();
            temporaryFolder.delete();
        }
    }

    @Test
    public void generateBddStepsFormSoapCheckNumberLinesInFile() throws IOException {
        testsPassed =false;
        try {
            plugin.execute();
        } catch (MojoExecutionException e) {
            e.printStackTrace();
        } catch (MojoFailureException e) {
            e.printStackTrace();
        }
        File destinationDirectory = new File(outputDirectory, plugin.soapServicePackages[0].replaceAll("\\.", "/"));

        File actualFile = new File(destinationDirectory, "HelloWorldImplServiceSteps.java");
        File expectedFile = new File(expectedPathToFile);
        String actual = FileUtils.readFileToString(actualFile);
        String expected = FileUtils.readFileToString(expectedFile);
        actual = actual.replaceAll("[ \n]", "");
        expected = expected.replaceAll("[ \n]", "");
        char[] actualChars = actual.toCharArray();
        char[] expectedChars = expected.toCharArray();
        Arrays.sort(actualChars);
        Arrays.sort(expectedChars);
        assertArrayEquals("Files is different", expectedChars, actualChars);
    }

    @Test
    public void generateBddStepsFormSoapCheckClassFiles() throws Exception {
        testsPassed = false;
        File expectedfile = new File(expectedPathToFile);
        File actualFile = new File(outputDirectory.toURI());
        try {
            URL expUrl = expectedfile.toURL();
            URL ActualUrl = actualFile.toURL();
            URL[] expUrls = new URL[]{expUrl};
            URL[] actUrls = new URL[]{ActualUrl};
            ClassLoader expCL = new URLClassLoader(expUrls);
            ClassLoader actualCL = new URLClassLoader(actUrls);
            Class expResult = expCL.loadClass("net.thucydides.maven.plugin.test.example.HelloWorldImplServiceSteps");
            Class actualResult = actualCL.loadClass("net.thucydides.maven.plugin.test.example.HelloWorldImplServiceSteps");
            assertEquals("Files are not equivalent", expResult, actualResult);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        testsPassed = true;
    }
}
