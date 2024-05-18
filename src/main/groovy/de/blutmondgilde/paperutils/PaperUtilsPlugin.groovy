package de.blutmondgilde.paperutils

import groovy.json.JsonSlurper
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class PaperUtilsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.add("paperUtils", PaperUtilsPluginExtension)

        project.tasks.register("setupTestserver") {
            def testServerDir = project.extensions.paperUtils.testServerDir
            def minecraftVersion = project.extensions.paperUtils.minecraftVersion
            // Create TestServer Directory
            testServerDir.mkdirs()
            // Delete potential old Paper Jars
            testServerDir.listFiles().findAll { it.name.startsWith("paper-$minecraftVersion") && it.name.endsWith('.jar') }.each { it.delete() }
            // Download latest Paper Build
            def serverFile = downloadLatestPaperBuild(testServerDir, minecraftVersion)
            // Create runConfig
            def runConfigFile = new File("${project.getRootDir().path}/.idea/runConfigurations/Start_Testserver.xml")
            runConfigFile.parentFile.mkdirs()
            runConfigFile.write("""<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="Start Testserver" type="JarApplication">
    <option name="JAR_PATH" value="${serverFile.path}" />
    <option name="VM_PARAMETERS" value="-Xmx4096M -Xms4096M -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+ParallelRefProcEnabled -XX:+PerfDisableSharedMem -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1HeapRegionSize=8M -XX:G1HeapWastePercent=5 -XX:G1MaxNewSizePercent=40 -XX:G1MixedGCCountTarget=4 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1NewSizePercent=30 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:G1ReservePercent=20 -XX:InitiatingHeapOccupancyPercent=15 -XX:MaxGCPauseMillis=200 -XX:MaxTenuringThreshold=1 -XX:SurvivorRatio=32 -Dusing.aikars.flags=https://mcflags.emc.gs -Daikars.new.flags=true" />
    <option name="PROGRAM_PARAMETERS" value="nogui" />
    <option name="WORKING_DIRECTORY" value="${testServerDir.path}" />
    <option name="ALTERNATIVE_JRE_PATH" />
    <method v="2" />
  </configuration>
</component>
""")

            def pluginDir = new File("${testServerDir.path}/plugins")
            pluginDir.mkdirs()
            // Remove jar files
            pluginDir.listFiles().findAll { it.name.endsWith('.jar') }.each { it.delete() }
            // Download Dependencies
            downloadDependency(new File("${pluginDir.path}/CommandAPI-${commandApiVersion}.jar"), "https://github.com/JorelAli/CommandAPI/releases/download/${commandApiVersion}/CommandAPI-${commandApiVersion}.jar")
        }

        project.tasks.register('copyLatestArtifact', Copy) {
            dependsOn project.tasks.named("build")

            def testServerDir = project.extensions.paperUtils.testServerDir
            def libsDir = new File("${project.layout.buildDirectory.get()}/libs")

            if (!libsDir.exists() || libsDir.listFiles().length == 0) {
                throw new GradleException("Das Verzeichnis ${libsDir} existiert nicht oder enthält keine Dateien.")
            }
            def latestFile = libsDir.listFiles().sort { -it.lastModified() }[0]
            from libsDir
            include latestFile.name

            def projectName = project.name
            new File("${testServerDir.path}/plugins").listFiles().findAll { it.name.startsWith(projectName) && it.name.endsWith('.jar') }.each { it.delete() }

            into "${testServerDir.path}/plugins"
        }

        project.tasks.named('build').configure {
            finalizedBy 'copyLatestArtifact'
        }
    }

    static def httpRequest(url) {
        def urlCon = new URL(url)
        def connection = urlCon.openConnection()
        connection.connect()

        def response = connection.getInputStream().getText()
        def json = new JsonSlurper().parseText(response)
        return json
    }

    static def downloadDependency(targetFile, downloadUrl) {
        def url = new URL(downloadUrl)

        url.withInputStream { downloadStream ->
            targetFile.withOutputStream { fileOut ->
                fileOut << downloadStream
            }
        }

        return targetFile
    }


    static def downloadLatestPaperBuild(targetDir, minecraftVersion) {
        // Get latest Paper Build
        def build = httpRequest("https://api.papermc.io/v2/projects/paper/versions/$minecraftVersion/").builds.max()
        // Download Paper Jar
        return downloadDependency(new File("${targetDir.path}/paper-$minecraftVersion-${build}.jar"), "https://papermc.io/api/v2/projects/paper/versions/$minecraftVersion/builds/${build}/downloads/paper-$minecraftVersion-${build}.jar")
    }
}