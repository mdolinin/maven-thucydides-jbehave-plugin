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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @goal generate-sources
 * @phase generate-sources
 */
public class GenerateThucydidesJUnitStoriesMojo extends AbstractMojo {

    /**
     * Directory with story files
     * @parameter expression="${project.stories.directory}" default-value="${project.basedir}/src/test/resources/stories"
     * @required
     */
    public File storiesDirectory;

    /**
     * Location of the file.
     *
     * @parameter expression="${project.junit.stories.directory}" default-value="${project.basedir}/src/test/java"
     * @required
     */
    public File outputDirectory;

    /**
     * Package name for ThucydidesJUnitStory stubs
     *
     * @parameter expression="${project.junit.stories.package}"
     * @required
     */
    public String packageForStoryStubs;

    public void execute() throws MojoExecutionException {
        File storiesDir = storiesDirectory;
        findStoryFilesAndGenerateStubs(storiesDir);
    }

    private void findStoryFilesAndGenerateStubs(File storiesDir) throws MojoExecutionException {
        if(storiesDir.exists() && storiesDir.isDirectory()){
            for(File file : getFiles(storiesDir)) {
                if (file.isFile() && getExtension(file.getPath()).equals("story")) {
                    generateStubFromStoryFile(file);
                } else if (file.isDirectory()) {
                    findStoryFilesAndGenerateStubs(file);
                }
            }
        }
    }

    private File[] getFiles(File storiesDir) throws MojoExecutionException {
        try{
            return storiesDir.listFiles();
        } catch (NullPointerException e){
            throw new MojoExecutionException("No files in directory: " + storiesDir.getName(), e);
        }

    }

    private void generateStubFromStoryFile(File story) throws MojoExecutionException {
        String className = getClassNameFrom(story.getName());
        String classText = createThucydidesJunitStoryFor(className);
        try {
            createJavaClass(className, classText);
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + className, e);
        }
    }

    private String createThucydidesJunitStoryFor(String name) {
        return "package " + packageForStoryStubs + ";\n" +
                "\n" +
                "import net.thucydides.jbehave.ThucydidesJUnitStory;\n" +
                "\n" +
                "public class " + name +" extends ThucydidesJUnitStory {}";
    }

    private String getClassNameFrom(String name) {
        int extensionIndex = name.lastIndexOf('.');
        String nameWithOutExtension = name.substring(0, extensionIndex);
        String[] words = nameWithOutExtension.split("_");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(Character.toTitleCase(word.charAt(0)))
                    .append(word.substring(1));
        }
        return builder.toString();
    }

    private void createJavaClass(String name, String text) throws IOException {
        File pd = new File(outputDirectory, packageForStoryStubs.replaceAll("\\.", "/"));
        pd.mkdirs();
        FileWriter out = new FileWriter(new File(pd, name + ".java"));
        try {
            out.append(text);
        } finally {
            out.flush();
            out.close();
        }
    }

    public String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }
}
