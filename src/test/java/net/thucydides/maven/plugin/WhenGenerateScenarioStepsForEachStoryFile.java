package net.thucydides.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class WhenGenerateScenarioStepsForEachStoryFile {

    private GenerateScenarioStepsMojo plugin;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    File outputDirectory;

    @Before
    public void setupPlugin() throws IOException {
        outputDirectory = temporaryFolder.newFolder("sample-output");

        plugin = new GenerateScenarioStepsMojo();
        plugin.outputDirectory = outputDirectory;
        plugin.packageForScenarioSteps = "net.thucydides.maven.plugin.test";
        plugin.storiesDirectory = getResourcesAt("/stories");
    }

    @Test
    public void should_create_scenario_steps_class_for_each_story_file() throws MojoExecutionException {
        plugin.execute();
        File destinationDirectory = new File(outputDirectory, plugin.packageForScenarioSteps.replaceAll("\\.", "/"));
        assertThat(destinationDirectory.list(javaFiles())).hasSize(2);
    }

    private FilenameFilter javaFiles() {
        return new FilenameFilter() {

            @Override
            public boolean accept(File file, String filename) {
                return filename.endsWith(".java");
            }
        };
    }

    private File getResourcesAt(String path) {
        return new File(getClass().getResource(path).getFile());
    }
}