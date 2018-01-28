package WeHaveSecrets.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script

object WeHaveSecrets_PrepareDatabase : BuildType({
    uuid = "0aac2189-23c9-41a7-9518-e93f53c9c3df"
    id = "WeHaveSecrets_PrepareDatabase"
    name = "02. Prepare Database"

    vcs {
        root("WeHaveSecretsCode")

    }

    steps {
        script {
            name = "Create empty database"
            scriptContent = """
                docker-compose -f setup\docker-compose.yml -p 'WeHaveSecrets' up -d --force-recreate db 
            """.trimIndent()
        }
        script {
            name = "Run DB Setup"
            scriptContent = """
                docker-compose -f setup\docker-compose.yml -p 'WeHaveSecrets' up --force-recreate db-setup
            """.trimIndent()
        }
    }

    dependencies {
        dependency(WeHaveSecrets.buildTypes.WeHaveSecrets_Build) {
            snapshot {
                onDependencyFailure = FailureAction.CANCEL
            }
        }
    }
})
