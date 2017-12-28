package WeHaveSecrets.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object WeHaveSecrets_RunEnd2EndTests : BuildType({
    uuid = "a08e8c31-ac23-4ac8-bbc9-f896dc5ed9c1"
    id = "WeHaveSecrets_RunEnd2EndTests"
    name = "04. Run End2End Tests"

    vcs {
        root("WeHaveSecretsCode")

    }

    steps {
        script {
            name = "Create output folder for video"
            scriptContent = """mkdir c:\tmp\wehavesecrets\end2end\%system.build.number%"""
        }
        script {
            name = "Run End2End tests"
            scriptContent = """
                docker rm -f wehavesecrets-tests-end2end
                docker run \
                --name wehavesecrets-tests-end2end \
                --network 'wehavesecrets_secrets-network' \
                -e 'CYPRESS_WEHAVESECRETS_URL=http://wehavesecrets' \
                -v ${'$'}WEHAVESECRETS_WORKINGFOLDER/end2end/%system.build.number%:/app/cypress/videos \
                wehavesecrets-tests-end2end
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    dependencies {
        dependency(WeHaveSecrets.buildTypes.WeHaveSecrets_StartUpWeHaveSecretsWebsite) {
            snapshot {
                onDependencyFailure = FailureAction.CANCEL
            }
        }
    }
})
