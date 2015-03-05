package net.thucydides.maven.plugin;

import org.apache.commons.collections.CollectionUtils;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        plugin.setSoapServicePackages(new String[]{"net.thucydides.maven.plugin.test.example"});
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
        File destinationDirectory = new File(outputDirectory, plugin.soapServicePackages[0].replaceAll("\\.", "/"));
        File actualFile = new File(destinationDirectory, "HelloWorldImplServiceSteps.java");
        File expectedFile = new File(expectedPathToFile);
        List<String> actualFileContent = FileUtils.readLines(actualFile);
        List<String> expectedFileContent = FileUtils.readLines(expectedFile);
        assertEquals("Number of lines in files is different", expectedFileContent.size(), actualFileContent.size());
        List<String> actualFileCodeBlocks = splitFileByEmptyLines(actualFileContent);
        List<String> expectedFileCodeBlocks = splitFileByEmptyLines(expectedFileContent);
        assertTrue("Files are not equivalent", CollectionUtils.isEqualCollection(expectedFileCodeBlocks, actualFileCodeBlocks));
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
}
