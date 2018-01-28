package WeHaveSecrets.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script

object WeHaveSecrets_StartUpWeHaveSecretsWebsite : BuildType({
    uuid = "277b208f-d0d6-4646-aca9-52638b76b939"
    id = "WeHaveSecrets_StartUpWeHaveSecretsWebsite"
    name = "03. Start up WeHaveSecrets website"

    vcs {
        root("WeHaveSecretsCode")

    }

    steps {
        script {
            name = "Start WeHaveSecrets.com"
            scriptContent = """
                docker-compose -p 'WeHaveSecrets' up -d --force-recreate web
            """.trimIndent()
        }
    }

    dependencies {
        dependency(WeHaveSecrets.buildTypes.WeHaveSecrets_PrepareDatabase) {
            snapshot {
                onDependencyFailure = FailureAction.CANCEL
            }
        }
    }
})
