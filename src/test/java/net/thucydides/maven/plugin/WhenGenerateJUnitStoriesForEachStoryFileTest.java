package net.thucydides.maven.plugin;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class WhenGenerateJUnitStoriesForEachStoryFileTest {

    private GenerateThucydidesJUnitStoriesMojo plugin;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    File outputDirectory;
    File expectedFilesDirectory;

    @Before
    public void setupPlugin() throws IOException {
        outputDirectory = temporaryFolder.newFolder("sample-output");

        plugin = new GenerateThucydidesJUnitStoriesMojo();
        plugin.outputDirectory = outputDirectory;
        plugin.packageForStoryStubs = "net.thucydides.test";
        plugin.storiesDirectory = getResourcesAt("/stories");
        expectedFilesDirectory = getResourcesAt("/sample-output/net/thucydides/test");
    }

    @Test
    public void should_create_junit_stories_class_for_each_story_file() throws MojoExecutionException, IOException {
        plugin.execute();
        File destinationDirectory = new File(outputDirectory, plugin.packageForStoryStubs.replaceAll("\\.", "/"));
        String[] generatedFileNames = destinationDirectory.list(javaFiles());
        assertThat(generatedFileNames).hasSize(2);
        for(String fileName : generatedFileNames) {
            File actualFile = new File(destinationDirectory, fileName);
            File expectedFile = new File(expectedFilesDirectory, fileName);
            assertEquals(FileUtils.readLines(actualFile), FileUtils.readLines(expectedFile));
        }
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