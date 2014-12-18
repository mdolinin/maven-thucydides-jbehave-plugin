package net.thucydides.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import static net.thucydides.maven.plugin.utils.NameUtils.getClassNameFrom;

/**
 * @goal generate-sources
 * @phase generate-sources
 */
public class GenerateThucydidesJUnitStoriesMojo extends AbstractMojo {

    /**
     * Directory with story files
     *
     * @parameter expression="${project.stories.directory}" default-value="${project.basedir}/src/test/resources/stories"
     * @required
     */
    public File storiesDirectory;

    /**
     * Location of the file.
     *
     * @parameter expression="${project.junit.stories.directory}" default-value="${project.build.directory}/generated-test-sources"
     * @required
     */
    public File outputDirectory;

    /**
     * Package name for ThucydidesJUnitStory stubs
     *
     * @parameter expression="${project.junit.stories.package}" default-value="${project.groupId}"
     * @required
     */
    public String packageForStoryStubs;

    /**
     * The Maven Project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        addMavenClassPath(outputDirectory);
        File storiesDir = storiesDirectory;
        findStoryFilesAndGenerateStubs(storiesDir);
    }

    private void findStoryFilesAndGenerateStubs(File storiesDir) throws MojoExecutionException {
        if (storiesDir.exists() && storiesDir.isDirectory()) {
            for (File file : getFiles(storiesDir)) {
                if (file.isFile() && getExtension(file.getPath()).equals("story")) {
                    generateStubFromStoryFile(file);
                } else if (file.isDirectory()) {
                    findStoryFilesAndGenerateStubs(file);
                }
            }
        }
    }

    private File[] getFiles(File storiesDir) throws MojoExecutionException {
        try {
            return storiesDir.listFiles();
        } catch (NullPointerException e) {
            throw new MojoExecutionException("No files in directory: " + storiesDir.getName(), e);
        }

    }

    private void generateStubFromStoryFile(File story) throws MojoExecutionException {
        String className = getClassNameFrom(story.getName());
        String classText = createThucydidesJunitStoryFor(className);
        try {
            createJavaClass(className, classText);
            moveResource();
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + className, e);
        }
    }

    private void moveResource() {
        File utils = createUtils();
        File customJUnitStoryFile = getFileFromResourcesByFilePath("/CustomJUnitStory.java");
        try {
            FileUtils.copyFile(customJUnitStoryFile, new File(utils.getAbsolutePath() + "/" + customJUnitStoryFile.getName()));
        } catch (IOException e) {
            getLog().error("Error while move resource " + e.getMessage());
        }
    }

    private String createThucydidesJunitStoryFor(String name) {
        createUtils();
        return "package " + packageForStoryStubs + ";\n" +
                "\n" +
                "import " + packageForStoryStubs + ".utils.CustomJUnitStory;\n" +
                "\n" +
                "public class " + name + "IT" + " extends CustomJUnitStory {}";
    }

    private File createUtils() {
        File utils = new File(outputDirectory, (packageForStoryStubs.replaceAll("\\.", "/") + "/utils"));
        utils.mkdir();
        return utils;
    }

    private static File getFileFromResourcesByFilePath(String filePath) {
        URL url = FileUtils.class.getResource(filePath);
        String file = url.getFile();
        if (isFileInJar(file)) {
            try {
                File tempFile = new File(Files.createTempDir(), FilenameUtils.getName(url.getFile()));
                IOUtils.copy(url.openStream(), org.apache.commons.io.FileUtils.openOutputStream(tempFile));
                return tempFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new File(file);
    }

    private static boolean isFileInJar(String file) {
        return file.contains(".jar!/");
    }

    private void createJavaClass(String name, String text) throws IOException {
        File pd = new File(outputDirectory, packageForStoryStubs.replaceAll("\\.", "/"));
        pd.mkdirs();
        FileWriter out = new FileWriter(new File(pd, name + "IT" + ".java"));
        try {
            out.append(text);
        } finally {
            out.flush();
            out.close();
        }
    }

    private String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    private MavenProject getProject() {
        return project;
    }

    private void addMavenClassPath(File path) {
        if (getProject() != null)
            getProject().addTestCompileSourceRoot(path.getAbsolutePath());
    }
}
