# Jenkinsfile Syntax Highlighter

![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-green)

A comprehensive IntelliJ IDEA plugin providing syntax highlighting and language support for Jenkinsfile (Jenkins Pipeline) files.

![Plugin Icon](src/main/resources/META-INF/pluginIcon.svg)

## Features

### 🎨 Syntax Highlighting
- **Jenkins Pipeline DSL Keywords** - Full highlighting for pipeline, stages, stage, steps, and more
- **Groovy Language Support** - Syntax highlighting for def, if, else, for, while, try, catch
- **Build Steps** - Highlighting for sh, bat, echo, checkout, build, and 30+ more Jenkins steps
- **String & Comments** - Proper highlighting for single/double-quoted strings, triple-quoted strings, and comments
- **Regex Patterns** - Support for Groovy regex patterns with `/pattern/` and `~/pattern/` syntax

### 📁 Code Structure
- **Code Folding** - Collapse and expand pipeline blocks, stages, and steps
- **Brace Matching** - Automatic matching of {}, [], and () pairs
- **File Detection** - Automatic recognition of `Jenkinsfile` and `*.jenkinsfile` files

### ✏️ Editing Features
- **Smart Commenting** - Quick comment/uncomment with `Cmd+/` (Mac) or `Ctrl+/` (Windows/Linux)
- **Code Completion** - Basic auto-completion for Jenkins Pipeline keywords

## Installation

### From JetBrains Marketplace
1. Open IntelliJ IDEA (or any JetBrains IDE)
2. Go to `Settings/Preferences` → `Plugins` → `Marketplace`
3. Search for **"Jenkinsfile Syntax Highlighter"**
4. Click `Install` and restart the IDE

### Manual Installation
1. Download the latest `.zip` file from [Releases](https://github.com/bashtovoi/jenkinsfile-syntax-highlighter/releases)
2. Go to `Settings/Preferences` → `Plugins` → `⚙️` (gear icon) → `Install Plugin from Disk...`
3. Select the downloaded `.zip` file
4. Restart the IDE

## Compatibility

- **IntelliJ IDEA**: 2024.1+
- **Compatible IDEs**: All IntelliJ-based IDEs (WebStorm, PyCharm, PhpStorm, RubyMine, GoLand, CLion, etc.)
- **Platform**: Windows, macOS, Linux

## Building from Source

```bash
# Clone the repository
git clone https://github.com/bashtovoi/jenkinsfile-syntax-highlighter.git
cd jenkinsfile-syntax-highlighter

# Build the plugin
./gradlew buildPlugin

# The plugin ZIP will be in: build/distributions/jenkinsfile-syntax-highlighter-1.0.0.zip
```

## Development

```bash
# Run the plugin in a sandbox IDE
./gradlew runIde

# Run tests
./gradlew test

# Verify plugin configuration
./gradlew verifyPluginConfiguration
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

## Support

- **Issues**: Report bugs or request features on [GitHub Issues](https://github.com/bashtovoi/jenkinsfile-syntax-highlighter/issues)
- **Email**: valerii@motivateclock.org

## Acknowledgments

- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/)
- Jenkins logo © Jenkins Project

## Related Documentation

For more details, see:
- [Build Configuration](build.gradle.kts) - Gradle build configuration

## Planned features
- [ ] Quick documentation (Ctrl+Q)
- [ ] Parameter info hints
- [ ] Code templates/live templates
- [ ] Structure view
- [ ] Error detection and validation
- [ ] Navigate to stage definition
- [ ] Refactoring support
- [ ] Pipeline visualization

---

Made with ❤️ for the DevOps community.
