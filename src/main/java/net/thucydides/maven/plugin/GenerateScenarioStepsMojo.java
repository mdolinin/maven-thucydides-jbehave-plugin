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

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.thucydides.maven.plugin.generate.model.ScenarioStepsClassModel;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.model.Story;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.parsers.StoryParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @goal generate-steps
 * @phase process-test-classes
 * @requiresDependencyResolution test
 */
public class GenerateScenarioStepsMojo extends AbstractMojo {

    public static final String MODEL_KEY = "scenarioStepsClass";
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
     * Package name for Scenario jbehave steps
     *
     * @parameter expression="${project.scenario.steps.package}" default-value="${project.groupId}"
     * @required
     */
    public String packageForScenarioSteps;

    /**
     * The Maven Project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    public MavenProject project = null;

    /**
     * The directory containing generated test classes of the project being tested. This will be included at the
     * beginning of the test classpath.
     *
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     */
    public File testClassesDirectory;

    /**
     * The directory containing generated classes of the project being tested. This will be included after the test
     * classes in the test classpath.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    public File classesDirectory;

    private ScenarioStepsFactory scenarioStepsFactory;
    private List<ScenarioStepsClassModel> scenarioStepsClassModels = new ArrayList<ScenarioStepsClassModel>();
    private StoryParser storyParser;
    private File template;

    public void execute() throws MojoExecutionException, MojoFailureException {
        storyParser = new RegexStoryParser(new LocalizedKeywords());
        scenarioStepsFactory = new ScenarioStepsFactory(packageForScenarioSteps, getClassLoader());
        findStoryFilesAndGenerateScenarioStepsClassModels(storiesDirectory);
        createJavaClasses();
    }

    private void findStoryFilesAndGenerateScenarioStepsClassModels(File storiesDir) throws MojoExecutionException {
        if (storiesDir.exists() && storiesDir.isDirectory()) {
            for (File file : getFiles(storiesDir)) {
                if (file.isFile() && getExtension(file.getPath()).equals("story")) {
                    generateStubFromStoryFile(file);
                } else if (file.isDirectory()) {
                    findStoryFilesAndGenerateScenarioStepsClassModels(file);
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
        try {
            String storyText = FileUtils.readFileToString(story);
            Story parsedStory = storyParser.parseStory(storyText);
            parsedStory.namedAs(story.getName());
            scenarioStepsClassModels.add(scenarioStepsFactory.createScenarioStepsClassModelFrom(parsedStory));
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + className, e);
        }
    }

    private void createJavaClasses() throws MojoExecutionException {
        Template temp;
        try {
            Configuration cfg = new Configuration();
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            cfg.setDefaultEncoding("UTF-8");
            cfg.setDirectoryForTemplateLoading(getTemplateFile().getParentFile());
            temp = cfg.getTemplate(getTemplateFile().getName());
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        for (ScenarioStepsClassModel scenarioStepsClassModel : scenarioStepsClassModels) {
            Writer fileWriter = null;
            String name = scenarioStepsClassModel.getClassNamePrefix() + "Steps";
            Map<String, ScenarioStepsClassModel> modelMap = new HashMap<String, ScenarioStepsClassModel>();
            modelMap.put(MODEL_KEY, scenarioStepsClassModel);
            try {
                fileWriter = new FileWriter(getOutputFile(name));
                temp.process(modelMap, fileWriter);
            } catch (TemplateException e) {
                throw new MojoExecutionException("Error with template " + temp.getName(), e);
            } catch (IOException e) {
                throw new MojoExecutionException("Error with file " + name, e);
            }
        }
    }

    private File getTemplateFile() throws IOException {
        if (template == null) {
            template = File.createTempFile("template", ".ftl");
            FileUtils.copyInputStreamToFile(GenerateScenarioStepsMojo.class.getResourceAsStream("/ScenarioStepsClass.ftl"), template);
        }
        return template;
    }

    private static String getClassNameFrom(String name) {
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

    private File getOutputFile(String name) {
        File pd = new File(outputDirectory, packageForScenarioSteps.replaceAll("\\.", "/"));
        pd.mkdirs();
        return new File(pd, name + ".java");
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

    /**
     * Generate the test classpath.
     *
     * @return List containing the classpath elements
     * @throws MojoFailureException when it happens
     */
    private Set<URL> getUrlsForCustomClasspath() throws MojoFailureException, MojoExecutionException {
        Set<URL> classpath = new HashSet<URL>(2 + getProject().getArtifacts().size());

        try {
            classpath.add(getTestClassesDirectory().getAbsoluteFile().toURI().toURL());
            classpath.add(getClassesDirectory().getAbsoluteFile().toURI().toURL());

            @SuppressWarnings("unchecked") Set<Artifact> classpathArtifacts = getProject().getArtifacts();

            for (Artifact artifact : classpathArtifacts) {
                if (artifact.getArtifactHandler().isAddedToClasspath() && !artifact.getGroupId().startsWith("org.apache.maven")) {
                    File file = artifact.getFile();
                    if (file != null) {
                        classpath.add(file.toURI().toURL());
                    }
                }
            }
        } catch (MalformedURLException e) {
            getLog().error(e.getMessage(), e);
        }
        return classpath;
    }

    public MavenProject getProject() {
        return project;
    }

    public File getTestClassesDirectory() {
        return testClassesDirectory;
    }

    public File getClassesDirectory() {
        return classesDirectory;
    }

    private ClassLoader getClassLoader() throws MojoFailureException, MojoExecutionException {
        ClassLoader pluginClassLoader = getClass().getClassLoader();
        Set<URL> projectClasspathList = getUrlsForCustomClasspath();
        ClassLoader projectClassLoader = new URLClassLoader(
                projectClasspathList.toArray(
                        new URL[projectClasspathList.size()]), pluginClassLoader);
        return projectClassLoader;
    }
}
