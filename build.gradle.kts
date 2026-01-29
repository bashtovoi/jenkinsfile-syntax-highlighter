plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.10.2"
}

group = "com.github.valerii"
version = "1.0.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        intellijIdea("2025.2.4")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

        // Add plugin dependencies for compilation here:


        bundledPlugin("org.jetbrains.kotlin")
    }

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "241"  // 2024.1+
            untilBuild = provider { null }  // Support all future versions
        }

        changeNotes = """
            <h3>1.0.6</h3>
            <ul>
                <li><b>New Features</b>
                    <ul>
                        <li>Generate change notes via Workflows</li>
                        <li>Improve Jenkinsfile icon</li>
                    </ul>
                </li>
            </ul>

            <h3>1.0.5</h3>
            <ul>
                <li><b>New Features</b>
                    <ul>
                        <li>Add icon for Jenkinsfiles</li>
                    </ul>
                </li>
                <li><b>Documentation</b>
                    <ul>
                        <li>Simplify plugin description</li>
                    </ul>
                </li>
            </ul>

            <h3>1.0.4</h3>
            <ul>
                <li><b>Documentation</b>
                    <ul>
                        <li>Add plugin screenshots</li>
                    </ul>
                </li>
                <li><b>CI/CD</b>
                    <ul>
                        <li>Fix generate categorized release notes step</li>
                    </ul>
                </li>
            </ul>

            <h3>1.0.3</h3>
            <ul>
                <li><b>CI/CD</b>
                    <ul>
                        <li>Remove Verify plugin step from Workflows</li>
                    </ul>
                </li>
            </ul>

            <h3>1.0.2</h3>
            <ul>
                <li><b>CI/CD</b>
                    <ul>
                        <li>Remove JetBrains Marketplace publish step from Release workflows</li>
                        <li>Fix GitHub Actions runner disk space issue</li>
                    </ul>
                </li>
            </ul>

            <h3>1.0.1</h3>
            <ul>
                <li><b>New Features</b>
                    <ul>
                        <li>Add comprehensive syntax highlighting for Jenkins Pipeline DSL</li>
                        <li>Support for code folding, brace matching, and code completion</li>
                        <li>Automatic Jenkinsfile detection</li>
                    </ul>
                </li>
            </ul>
        """.trimIndent()
    }

    pluginVerification {
        ides {
            // Only verify against min and current versions to save disk space in CI
            create(org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaCommunity, "2024.1.7")
            create(org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaCommunity, "2025.2.4")
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
