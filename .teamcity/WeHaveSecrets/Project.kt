package WeHaveSecrets

import WeHaveSecrets.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.VersionedSettings
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.versionedSettings

object Project : Project({
    uuid = "ea6efa2a-49ab-43a8-9100-e57541850c4f"
    id = "WeHaveSecrets"
    parentId = "_Root"
    name = "WeHaveSecrets"

    buildType(WeHaveSecrets_PrepareDatabase)
    buildType(WeHaveSecrets_Build)
    buildType(WeHaveSecrets_StartUpWeHaveSecretsWebsite)

    features {
        versionedSettings {
            id = "PROJECT_EXT_2"
            mode = VersionedSettings.Mode.ENABLED
            buildSettingsMode = VersionedSettings.BuildSettingsMode.USE_CURRENT_SETTINGS
            rootExtId = "WeHaveSecretsConfig"
            showChanges = false
            settingsFormat = VersionedSettings.Format.KOTLIN
            storeSecureParamsOutsideOfVcs = true
        }
    }
})
