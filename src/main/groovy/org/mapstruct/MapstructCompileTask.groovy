package org.mapstruct

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.JavaCompile

import static org.apache.commons.lang.StringUtils.defaultIfEmpty

/**
 * @author Yoav Aharoni
 */
class MapstructCompileTask extends JavaCompile {
    @TaskAction
    protected void mapstruct() {
        ext.aptDumpDir = file("${buildDir}/tmp/apt/mapstruct")
        destinationDir = aptDumpDir;

        classpath = compileJava.classpath + configurations.mapstruct
        setSource(sourceSets.main.originalJavaSrcDirs)
        ext.sourceDestDir = file(project.ext.generatedMapperSourcesDir)

        def source = defaultIfEmpty(project.mapstruct.javaSourceVersion, project.sourceCompatibility)
        def target = defaultIfEmpty(project.mapstruct.target, javaTargetVersion.targetCompatibility)

        options.define(
                compilerArgs: [
                        "-Amapstruct.defaultComponentModel=${project.mapstruct.defaultComponentModel}",
                        "-nowarn",
                        "-proc:only",
                        "-encoding", project.mapstruct.enconding,
                        "-processor", "org.mapstruct.ap.MappingProcessor",
                        "-s", ext.sourceDestDir.absolutePath,
                        "-source", source,
                        "-target", target,
                ]
        );

        inputs.dir source
        outputs.dir project.ext.generatedMapperSourcesDir;

        doFirst {
            sourceDestDir.mkdirs()
        }
        doLast {
            aptDumpDir.delete()
        }
    }
}
