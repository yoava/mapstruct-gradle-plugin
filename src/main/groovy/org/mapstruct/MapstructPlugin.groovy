package org.mapstruct

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * @author Yoav Aharoni
 */
class MapstructPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.pluginManager.apply 'java'

        def extension = project.extensions.create("mapstruct", MapstructPluginExtension)
        def mapstructGeneratedSourcesDir = "${project.buildDir}/generated-src/mapstruct/main"

        project.sourceSets.main {
            // save original java.srcDirs
            ext.originalJavaSrcDirs = project.sourceSets.main.java.srcDirs
            ext.mapstructGeneratedSourcesDir = mapstructGeneratedSourcesDir

            // add mapstructGeneratedSourcesDir to java.srcDirs
            java.srcDir mapstructGeneratedSourcesDir
        }

        // define mapstruct dependencies
        project.configurations {
            mapstruct
        }

        project.gradle.taskGraph.whenReady {
            project.dependencies {
                compile "org.mapstruct:mapstruct:${extension.version}"
                mapstruct "org.mapstruct:mapstruct-processor:${extension.version}"
            }
        }

        // define mapstruct task
        Task mapstructTask = project.task([
                type       : MapstructCompileTask,
                description: 'Generate Mapstruct ',
                group      : 'mapstruct'
        ], "mapstruct");

        // execute mapstructTask when compileJava is executed
        project.compileJava.dependsOn mapstructTask
    }
}


