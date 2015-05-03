package org.mapstruct

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * @author Yoav Aharoni
 */
class MapstructPlugin implements Plugin<Project> {

    void apply(Project project) {
        def extension = project.extensions.create("mapstruct", MapstructPluginExtension)
        project.pluginManager.apply 'java'

        def main = project.sourceSets.main
        def generatedMapperSourcesDir = "${project.buildDir}/generated-src/mapstruct/main"
        project.sourceSets.main {
            ext.generatedMapperSourcesDir = generatedMapperSourcesDir
            ext.originalJavaSrcDirs = main.java.srcDirs
            java.srcDir generatedMapperSourcesDir
        }

        // define mapstruct task
        Task mapstructTask = project.task([
                type       : MapstructCompileTask,
                description: 'Generate Mapstruct ',
                group      : 'mapstruct'
        ], "mapstruct");

        // define mapstruct dependencies
        project.configurations {
            mapstruct
        }

        project.dependencies {
            compile "org.mapstruct:mapstruct:${extension.version}"
            mapstruct "org.mapstruct:mapstruct-processor:${extension.version}"
        }

        // execute mapstructTask when compileJava is executed
        project.compileJava.dependsOn mapstructTask
    }
}


