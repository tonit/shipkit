package org.shipkit.internal.gradle

import org.shipkit.gradle.BumpVersionFileTask
import org.shipkit.internal.version.VersionInfo
import testutil.PluginSpecification

class VersioningPluginTest extends PluginSpecification {

    def "should initialize initVersioning task properly"() {
        when:
        project.plugins.apply(VersioningPlugin)

        then:
        InitVersioningTask task = project.getTasks().findByName(VersioningPlugin.INIT_VERSIONING_TASK)
        task.versionFile == project.file(VersioningPlugin.VERSION_FILE_NAME)
    }

    def "should initialize bumpVersionFile task properly"() {
        when:
        project.plugins.apply(VersioningPlugin)

        then:
        BumpVersionFileTask task = project.getTasks().findByName(VersioningPlugin.BUMP_VERSION_FILE_TASK)
        task.versionFile == project.file(VersioningPlugin.VERSION_FILE_NAME)
    }

    def "should set VersionInfo extension according to version.properties content if this file exists"() {
        given:
        project.file(VersioningPlugin.VERSION_FILE_NAME) << "version=1.0.0\nnotableVersions=0.1.0\n"
        when:
        project.plugins.apply(VersioningPlugin)

        then:
        def versionInfo = project.extensions.getByType(VersionInfo)
        versionInfo.versionFile == project.file(VersioningPlugin.VERSION_FILE_NAME)
        versionInfo.version == "1.0.0"
        versionInfo.notableVersions == ["0.1.0"] as LinkedList
    }

    def "should set VersionInfo extension to project.version defaults if version.properties doesn't exist"() {
        given:
        assert !project.file(VersioningPlugin.VERSION_FILE_NAME).exists()
        project.version = "1.2.3"

        when:
        project.plugins.apply(VersioningPlugin)

        then:
        def versionInfo = project.extensions.getByType(VersionInfo)
        versionInfo.versionFile == project.file(VersioningPlugin.VERSION_FILE_NAME)
        versionInfo.version == "1.2.3"
        versionInfo.notableVersions == [] as LinkedList
    }

    def "adds version bumped changes to GitCommitTask if GitPlugin applied"() {
        given:
        project.plugins.apply(ReleaseConfigurationPlugin).configuration.gitHub.repository = "http://github.com"
        project.plugins.apply(GitPlugin)

        when:
        project.plugins.apply(VersioningPlugin)

        then:
        GitCommitTask gitCommitTask = project.tasks.getByName(GitPlugin.GIT_COMMIT_TASK)
        gitCommitTask.files.contains(project.file(VersioningPlugin.VERSION_FILE_NAME).absolutePath)
        gitCommitTask.aggregatedCommitMessage.contains("version bumped")
    }

    @Override
    void configureReleaseConfigurationDefaults(){
        // ReleaseConfiguration is not needed in this test
    }
}
