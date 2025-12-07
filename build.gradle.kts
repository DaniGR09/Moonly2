// Archivo de Nivel Superior (por ejemplo, settings.gradle.kts o build.gradle)

buildscript {
    dependencies {
        // La dependencia de Hilt CLASSPATH ha sido eliminada.
        // Solo dejamos la de Google Services si está en este bloque.
        classpath("com.google.gms:google-services:4.4.1")
    }
}

// Bloque de plugins (donde se definen las versiones de los plugins aplicables)
plugins {
    // Declaración de versiones de los plugins (apply false)
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false

    // ✅ Versión de Hilt declarada aquí (forma moderna)
    id("com.google.dagger.hilt.android") version "2.50" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}