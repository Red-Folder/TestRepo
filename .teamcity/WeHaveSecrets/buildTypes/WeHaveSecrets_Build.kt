package WeHaveSecrets.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dotnetBuild
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dotnetRestore
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.dotnetTest
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
            projects = "WeHaveSecrets.sln"
        }
        dotnetBuild {
            projects = "WeHaveSecrets.sln"
        }
        dotnetTest {
            projects = "WeHaveSecrets.Tests.Unit/WeHaveSecrets.Tests.Unit.csproj"
            param("dotNetCoverage.dotCover.filters", """
                +:WeHaveSecrets
                +:WeHaveSecrets.*
                -:*.Tests.*
            """.trimIndent())
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
    }

    triggers {
        vcs {
        }
    }
})
