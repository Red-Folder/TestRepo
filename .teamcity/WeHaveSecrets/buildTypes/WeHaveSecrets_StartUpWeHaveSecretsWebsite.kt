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
                docker rm -f wehavesecrets
                docker run -d \
                --network wehavesecrets_secrets-network \
                --ip 172.20.0.30 \
                --name wehavesecrets \
                -e "ConnectionStrings:DefaultConnection"="Server=172.20.0.22;Database=WeHaveSecrets;User Id=sa;Password=${'$'}WEHAVESECRETS_SQLPASSWORD;" \
                -p 8080:80 \
                -v ${'$'}WEHAVESECRETS_WORKINGFOLDER/backups:/app/wwwroot/backups \
                wehavesecrets:latest
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
