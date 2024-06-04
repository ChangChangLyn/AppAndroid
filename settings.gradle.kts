pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        /*google()
        //mavenCentral()
        jcenter()*/

        google()
        maven("https://jitpack.io")
        mavenCentral()
        jcenter()

    }
}

rootProject.name = "Viettel"
include(":app")
