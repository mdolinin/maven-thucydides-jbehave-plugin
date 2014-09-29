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
import java.io.FilenameFilter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: dmytro.gryshchenko
 * Date: 9/29/14
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class SoapGenerateBddStepsTest {
    GenerateSoapStepsMojo plugin;
    File outputDirectory;
    File expectedFilesDirectory;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setupPlugin() throws IOException {
        outputDirectory = temporaryFolder.newFolder("soap-output");

        plugin = new GenerateSoapStepsMojo();
        plugin.setOutputDirectory(new File("./target/generated-test-sources"));
        plugin.setOutputDirectory(outputDirectory);
        plugin.setPackageForSoapSteps("net.thucydides.maven.plugin.test.example");
        plugin.setClassesDirectory(new File("net.thucydides.test"));
        plugin.setTestClassesDirectory(new File("./target/"));
        plugin.setProject(new MavenProject());
//        expectedFilesDirectory = getResourcesAt("/src/test/java/net/thucydides/maven/plugin/test/example/HelloWorldImplServiceSteps");
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
        File destinationDirectory = new File(outputDirectory, plugin.packageForSoapSteps.replaceAll("\\.", "/"));
        String[] generatedFileNames = destinationDirectory.list(javaFiles());

        for(String fileName : generatedFileNames) {
            File actualFile = new File(destinationDirectory, fileName);
            File expectedFile = new File(expectedFilesDirectory, fileName);
            System.err.println("actualFile = " + actualFile);
            System.err.println("expectedFile = " + expectedFile);
            assertEquals(FileUtils.readLines(actualFile), FileUtils.readLines(expectedFile));
        }
//        boolean equals = new File("./src/test/net/thucydides/maven/plugin/test/HelloWorldImplServiceSteps").equals(new File("./target/ganarated-test-sources/net/thucydides/maven/plugin/test/example/HelloWorldImplServiceSteps"));
//        System.err.println("equals" + equals);
//        boolean b = FilenameUtils.equalsNormalized("./src/test/net/thucydides/maven/plugin/test/HelloWorldImplServiceSteps", "./target/ganarated-test-sources/net/thucydides/maven/plugin/test/example/HelloWorldImplServiceSteps");
//        System.err.println("b = " + b);
//        assertEquals(new File("./src/test/net/thucydides/maven/plugin/test/HelloWorldImplServiceSteps"), new File("./target/ganarated-test-sources/net/thucydides/maven/plugin/test/example/HelloWorldImplServiceSteps"));

    }

    private File getResourcesAt(String path) {
        System.err.println("getClass().getResource(path).getFile() = " + getClass().getResource(path).getFile());
        return new File(getClass().getResource(path).getFile());
    }

    private FilenameFilter javaFiles() {
        return new FilenameFilter() {

            @Override
            public boolean accept(File file, String filename) {
                return filename.endsWith(".java");
            }
        };
    }

}
