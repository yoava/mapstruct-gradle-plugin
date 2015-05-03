package org.mapstruct

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 * @author Yoav Aharoni
 */
class MapstructPluginTest {
    @Test
    public void mapstructPluginAddsMapstructTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'mapstruct'

        assertTrue(project.tasks.mapstruct != null)
    }
}
