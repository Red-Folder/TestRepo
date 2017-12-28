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
                docker rm -f wehavesecrets-db
                docker run -d \
                --network wehavesecrets_secrets-network \
                --ip 172.20.0.22 \
                --name wehavesecrets-db \
                -e "ACCEPT_EULA"="${'$'}WEHAVESECRETS_SQLLICENCEACCEPTED" \
                -e "SA_PASSWORD"="${'$'}WEHAVESECRETS_SQLPASSWORD" \
                -p ${'$'}WEHAVESECRETS_SQLLOCALPORT:1433 \
                -v ${'$'}WEHAVESECRETS_WORKINGFOLDER/backups:/app/wwwroot/backups \
                microsoft/mssql-server-linux:latest
            """.trimIndent()
        }
        script {
            name = "Run DB Setup"
            scriptContent = """
                docker rm -f wehavesecrets-db-setup
                docker run \
                --network wehavesecrets_secrets-network \
                --ip 172.20.0.23 \
                --name wehavesecrets-db-setup \
                -e "CONNECTIONSTRING"="Server=172.20.0.22;Database=WeHaveSecrets;User Id=sa;Password=${'$'}WEHAVESECRETS_SQLPASSWORD;" \
                wehavesecrets-db-setup:latest
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
