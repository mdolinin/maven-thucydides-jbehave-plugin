maven-thucydides-jbehave-plugin
===============================

Maven plugin for generate junit story stubs and scenario-steps from jbehave story files.

## Build

    mvn clean install

#### [Compiled jar and pom](https://github.com/mdolinin/maven-thucydides-jbehave-plugin/releases)
[ ![Download](https://api.bintray.com/packages/mdolinin/maven/maven-thucydides-jbehave-plugin/images/download.svg) ](https://bintray.com/mdolinin/maven/maven-thucydides-jbehave-plugin/_latestVersion)

## Usage of generate-sources goal

To use it add to your pom.xml build section with goal generate-sources 
and define project.junit.stories.package(where to put generated stubs) in properties section.

```xml
<properties>
    ...
    <project.junit.stories.package>com.your.package</project.junit.stories.package>
    ...
</properties>
```
```xml
  <plugin>
      <groupId>net.thucydides.maven.plugin</groupId>
      <artifactId>maven-thucydides-jbehave-plugin</artifactId>
      <version>0.9.223</version>
      <executions>
          <execution>
              <goals>
                  <goal>generate-sources</goal>
              </goals>
          </execution>
      </executions>
  </plugin>
```

#### [Article where and how to use this goal](http://mdolinin.github.io/blog/2014/01/17/thucydides-plus-jbehave-plus-maven-run-tests-in-parallel/) 

## Usage of generate-steps goal

To use it add to your pom.xml build section with goal generate-steps. 

```xml
  <plugin>
      <groupId>net.thucydides.maven.plugin</groupId>
      <artifactId>maven-thucydides-jbehave-plugin</artifactId>
      <version>0.9.223</version>
      <executions>
          <execution>
              <goals>
                  <goal>generate-steps</goal>
              </goals>
          </execution>
      </executions>
  </plugin>
```

#### [Article where and how to use this goal](http://mdolinin.github.io/blog/2014/07/07/thucydides-plus-jbehave-plus-maven-generate-new-steps-from-scenarios/)
