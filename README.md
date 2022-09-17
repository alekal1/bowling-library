# Bowling library

### Short overview

This repository contains source code of bowling score system.

The project uses Spring boot and can be build/deployed with Gradle.


## Usage

NB! There is not functionality to publish library into mavenCentral yet, thus _it could be used only as a local java-library_.

1. Clone repository into you favorite IDE.
2. Run `./gradlew clean build`, after that `.jar` file should be located under root `build/libs` folder.
3. Run `./gradlew publishToMavenLocal`.
4. In your application project add implementation from mavenLocal.

Gradle example `implementation 'ee.alekal:bowlingscore:{current_version}'` (Make sure you can access repos from mavenLocal `.m2` folder)

#### This plugin could be used in two different ways

[Option 1] In your project add `@EnableInternalBowlingClient` under your configuration.
This will use implementation from `src/main/java/ee/alekal/bowlingscore/internal` package.

Internal implementation is quite straightforward, but it is recommended to check how things work (for instance validation, api exception handling etc)

Furthermore, _there is no database connectivity_ in internal implementation yet.

[Option 2] In your project add `@EnableBowlingControllers` under your configuration.
 You should create your own logic by implementing `BowlingManagementService` and `BowlingGameService` classes.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.3/gradle-plugin/reference/html/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.3/reference/htmlsingle/#web)
