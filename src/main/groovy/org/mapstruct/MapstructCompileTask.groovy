package org.mapstruct

import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

/**
 * @author Yoav Aharoni
 */
class MapstructCompileTask extends JavaCompile {
    MapstructCompileTask() {
        def mapstructGeneratedSourcesDir = project.sourceSets.main.mapstructGeneratedSourcesDir

        ext.aptTempDir = project.file("${project.buildDir}/tmp/apt/mapstruct")
        ext.generatedSourcesDir = project.file(mapstructGeneratedSourcesDir)

        // set compile task folders
        setSource(project.sourceSets.main.ext.originalJavaSrcDirs);
        destinationDir = aptTempDir;
        classpath = project.tasks.compileJava.classpath + project.configurations.mapstruct
        inputs.dir source
        outputs.dir mapstructGeneratedSourcesDir;
    }

    @Override
    protected void compile(IncrementalTaskInputs inputs) {
        // define compiler arguments
        def mapstruct = project.mapstruct
        def sourceCompatibility = defaultIfEmpty(mapstruct.javaSourceVersion, project.sourceCompatibility)
        def targetCompatibility = defaultIfEmpty(mapstruct.javaTargetVersion, project.targetCompatibility)
        options.define(
                compilerArgs: [
                        "-Amapstruct.defaultComponentModel=${mapstruct.defaultComponentModel}",
                        "-nowarn",
                        "-proc:only",
                        "-encoding", mapstruct.enconding,
                        "-processor", "org.mapstruct.ap.MappingProcessor",
                        "-s", generatedSourcesDir.absolutePath,
                        "-source", sourceCompatibility,
                        "-target", targetCompatibility,
                ]
        );

        // execute mapstruct compiler
        generatedSourcesDir.mkdirs()
        super.compile(inputs)
        aptTempDir.delete()
    }

    public static String defaultIfEmpty(String str, Object defaultStr) {
        return str != null && !str.isEmpty() ? str : defaultStr == null ? null : defaultStr.toString();
    }
}
