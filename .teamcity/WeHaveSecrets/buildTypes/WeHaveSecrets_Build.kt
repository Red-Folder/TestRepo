package WeHaveSecrets.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dockerBuild
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dotnetPublish
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dotnetRestore
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dotnetTest
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object WeHaveSecrets_Build : BuildType({
    uuid = "a5238805-0de5-4282-8c71-3c75cd3374c3"
    id = "WeHaveSecrets_Build"
    name = "Build"

    vcs {
        root("WeHaveSecretsCode")

    }

    steps {
        dotnetRestore {
            name = "Restore"
            projects = "WeHaveSecrets.sln"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetTest {
            name = "Test"
            projects = "WeHaveSecrets.Tests.Unit/WeHaveSecrets.Tests.Unit.csproj"
            param("dotNetCoverage.dotCover.filters", """
                +:WeHaveSecrets
                +:WeHaveSecrets.*
                -:*.Tests.*
            """.trimIndent())
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dotnetPublish {
            name = "Publish"
            projects = "WeHaveSecrets.sln"
            configuration = "Release"
            outputDir = "obj/Docker/publish"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        dockerBuild {
            name = "Build wehavesecrets image"
            source = path {
                path = "WeHaveSecrets/Dockerfile"
            }
            contextDir = "WeHaveSecrets"
            namesAndTags = "wehavesecrets"
        }
        script {
            name = "Start WeHaveSecrets.com"
            scriptContent = """
                docker rm -f wehavesecrets
                docker run -d --network wehavesecrets_secrets-network --name wehavesecrets -e "ConnectionStrings:DefaultConnection"="Server=172.20.0.22;Database=WeHaveSecrets;User Id=sa;Password=AbC!23hgAAA;" -p 8080:80 wehavesecrets:latest
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }
})
