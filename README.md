# TfL cost summariser

A command line application written in Kotlin to produce the total amount spent from a CSV file provided by TfL for your
contactless payment card.

## Building and running

Gradle is used for dependency management and building. Running

```shell
./gradlew createTar
```

will create a tarball containing the JAR and a shell script to run the application with

```shell
./tfl-cost-summariser.sh <path-to-csv-file>
```
