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
            <h3>1.0.0 - Initial Release</h3>
            <ul>
                <li><b>Syntax Highlighting</b>
                    <ul>
                        <li>Full Jenkins Pipeline DSL keyword highlighting</li>
                        <li>Groovy language syntax support</li>
                        <li>String, comment, number highlighting</li>
                        <li>Regex pattern highlighting</li>
                    </ul>
                </li>
                <li><b>Code Structure</b>
                    <ul>
                        <li>Code folding for pipeline blocks</li>
                        <li>Brace, bracket, and parenthesis matching</li>
                        <li>Automatic Jenkinsfile detection</li>
                    </ul>
                </li>
                <li><b>Editing Features</b>
                    <ul>
                        <li>Comment/uncomment support (Cmd+/ or Ctrl+/)</li>
                        <li>Basic code completion for keywords</li>
                    </ul>
                </li>
                <li><b>Supported Keywords</b>
                    <ul>
                        <li>Pipeline structure: pipeline, stages, stage, steps, parallel, matrix</li>
                        <li>Sections: agent, environment, options, parameters, triggers, post, tools</li>
                        <li>Build steps: sh, bat, echo, checkout, build, and 30+ more</li>
                        <li>Conditions: when, post conditions (success, failure, always, etc.)</li>
                    </ul>
                </li>
            </ul>
        """.trimIndent()
    }

    pluginVerification {
        ides {
            recommended()
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
