package net.thucydides.maven.plugin;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WhenGenerateSoapBddStepsTest {
    private String expectedPathToFile = "./src/test/java/net/thucydides/maven/plugin/test/example/HelloWorldImplServiceSteps.java";
    GenerateSoapStepsMojo plugin;
    File outputDirectory;
    private static final String DELIMITER ="\n\n---------------------------------------\n\n";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setupPlugin() throws IOException {
        outputDirectory = temporaryFolder.newFolder("soap-output");
        plugin = new GenerateSoapStepsMojo();
        plugin.setOutputDirectory(new File("./target/generated-test-sources"));
        plugin.setOutputDirectory(outputDirectory);
        plugin.setSoapServicePackages(new String[]{"net.thucydides.maven.plugin.test.example"});
        plugin.setClassesDirectory(new File("net.thucydides.test"));
        plugin.setTestClassesDirectory(new File("./target/"));
        plugin.setProject(new MavenProject());
    }

    @Test
    public void generateBddStepsFormSoapCheckNumberLinesInFile() throws IOException {
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
        List<String> actualFileContent = FileUtils.readLines(actualFile);
        List<String> expectedFileContent = FileUtils.readLines(expectedFile);
        assertEquals("Number of lines in files is different", expectedFileContent.size(), actualFileContent.size()); // checks only size
        assertThatLinesAreEqual(expectedFileContent, actualFileContent);
    }

    private void assertThatLinesAreEqual(List<String> expectedLines, List<String> actualLines) {
        if (!expectedLines.equals(actualLines)) {
            StringBuilder failReport = new StringBuilder();
            for (int lineNumber = 0; lineNumber < actualLines.size(); lineNumber++) {
                String actualLine = actualLines.get(lineNumber);
                String expectedLine = expectedLines.get(lineNumber);
                if (!actualLine.equals(expectedLine)) {
                    failReport.append("Failed test for lines equality on line #").append(lineNumber).append("\n")
                            .append("EXPECTED\n<").append(expectedLine).append(">\nACTUAL\n<").append(actualLine).append(DELIMITER);

                }
            }
            throw new AssertionError(failReport);
        }
    }

    private List<String> splitFileByEmptyLines(List<String> fileLines) {
        List<String> result = new ArrayList<String>();
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : fileLines) {
            if (line.isEmpty()) {
                result.add(stringBuilder.toString());
                //clean string builder
                stringBuilder.setLength(0);
            } else {
                stringBuilder.append(line);
            }
        }
        //add last line
        result.add(stringBuilder.toString());
        return result;
    }

    @Test
    public void generateBddStepsFormSoapCheckClassFiles() throws Exception {
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
    }
}
