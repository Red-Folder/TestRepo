package WeHaveSecrets.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dockerBuild
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dotnetPublish
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dotnetRestore
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dotnetTest

object WeHaveSecrets_Build : BuildType({
    uuid = "a5238805-0de5-4282-8c71-3c75cd3374c3"
    id = "WeHaveSecrets_Build"
    name = "01. Unit Test and Build Docker images"

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
            name = "Build the DB Setup Docker Image"
            source = path {
                path = "Db.WeHaveSecrets/Dockerfile"
            }
            namesAndTags = "wehavesecrets-db-setup"
        }
        dockerBuild {
            name = "Build wehavesecrets image"
            source = path {
                path = "WeHaveSecrets/Dockerfile"
            }
            contextDir = "WeHaveSecrets"
            namesAndTags = "wehavesecrets"
        }
        dockerBuild {
            name = "Build wehavesecrets image (1)"
            source = path {
                path = "Cypress/Dockerfile"
            }
            contextDir = "Cypress"
            namesAndTags = "wehavesecrets-tests-end2end"
        }
    }
})
