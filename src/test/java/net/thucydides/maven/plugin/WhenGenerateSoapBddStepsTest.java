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
import java.util.Iterator;
import java.util.List;

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
        List<String> actualFileContent = removeEmptyLines(FileUtils.readLines(actualFile));
        List<String> expectedFileContent = removeEmptyLines(FileUtils.readLines(expectedFile));
        assertEquals("Number of lines in files is different", expectedFileContent.size(), actualFileContent.size()); // checks only size
        assertThatLinesAreEqual(expectedFileContent, actualFileContent, expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
        testsPassed =true;
    }

    private void assertThatLinesAreEqual(List<String> expectedLines, List<String> actualLines, String expPath, String actPath) {
        if (!expectedLines.equals(actualLines)) {
            StringBuilder failReport = new StringBuilder();
            for (int index = 0; index < actualLines.size(); index++) {
                String expectedLine = expectedLines.get(index);
                String actualLine = actualLines.get(index);
                if (!actualLine.equals(expectedLine)) {
                    int fileLine = index + 1;
                    failReport.append("Failed test for lines equality on line #").append(fileLine).append("\n")
                            .append("EXPECTED\n<").append(expectedLine).append(">\nACTUAL\n<").append(actualLine).append(">").append(DELIMITER);
                }
            }
            failReport.append("Expected file path = ").append(expPath).append("\n")
                    .append("Actual file path = ").append(actPath);
            throw new AssertionError(failReport);
        }
    }

    private List<String> removeEmptyLines(List<String> lines){
        Iterator<String> iterator = lines.iterator();
        while (iterator.hasNext()){
            if (iterator.next().isEmpty()){
                iterator.remove();
            }
        }
        return lines;
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
