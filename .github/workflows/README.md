# GitHub Actions Workflows

This directory contains GitHub Actions workflows for the Jenkinsfile Plugin.

## Workflows

### 1. Build (`build.yml`)

**Triggers:**
- Pull requests to `main` or `develop` branches
- Pushes to `main` or `develop` branches

**What it does:**
- Sets up Java 21 environment
- Builds the plugin using Gradle
- Runs tests
- Verifies plugin integrity
- Uploads build artifacts and test results

**Usage:**
This workflow runs automatically on every PR. You don't need to do anything manually.

---

### 2. Release (`release.yml`)

**Triggers:**
- Push of version tags (e.g., `v1.0.0`, `v1.2.3`)

**What it does:**
- Builds the plugin with the version from the tag
- Runs tests and verification
- Generates categorized release notes using conventional commits
- Groups commits by type (features, fixes, docs, etc.)
- Adds emojis for better readability
- Lists all contributors
- Includes installation instructions
- Creates a GitHub release with the plugin ZIP file
- Optionally publishes to JetBrains Marketplace (if token is configured)

**Usage:**
```bash
# Create and push a version tag
git tag v1.0.1
git push origin v1.0.1
```

**Conventional Commit Format:**
For best results, use conventional commit messages:
```
feat: Add new feature
fix: Fix bug in parser
docs: Update documentation
style: Format code
refactor: Refactor lexer
perf: Improve performance
test: Add tests
build: Update dependencies
ci: Update workflow
chore: Update configuration
```

**Example Release Notes Output:**
```markdown
## What's Changed

### ✨ New Features
- Add pipeline validation (a1b2c3d)
- Add GDSL integration (d4e5f6g)

### 🐛 Bug Fixes
- Fix brace matching issue (g7h8i9j)

### 📝 Documentation
- Update README with examples (j0k1l2m)

### 👥 Contributors
- John Doe
- Jane Smith

## 📥 Installation
...
```

---

## Configuration

### Required Secrets

None required for basic functionality.

### Optional Secrets

**`JETBRAINS_MARKETPLACE_TOKEN`** (Optional)
- Required to publish to JetBrains Marketplace
- Get token from: https://plugins.jetbrains.com/author/me/tokens
- Add to repository secrets: Settings > Secrets and variables > Actions > New repository secret

**How to add:**
1. Go to your repository on GitHub
2. Navigate to Settings > Secrets and variables > Actions
3. Click "New repository secret"
4. Name: `JETBRAINS_MARKETPLACE_TOKEN`
5. Value: Your token from JetBrains

---

## Troubleshooting

### Build fails with "Permission denied"

The `gradlew` file needs execute permissions. The workflow handles this automatically with:
```yaml
- name: Grant execute permission for gradlew
  run: chmod +x gradlew
```

### Release workflow doesn't trigger

Make sure your tag follows the format `v*.*.*` (e.g., `v1.0.0`, `v2.1.3`).

```bash
# Correct
git tag v1.0.1

# Incorrect (won't trigger workflow)
git tag 1.0.1
git tag release-1.0.1
```

### Plugin not published to JetBrains Marketplace

1. Check if `JETBRAINS_MARKETPLACE_TOKEN` secret is configured
2. Verify the token is valid and has publish permissions
3. Check workflow logs for error messages

---

## Testing Workflows Locally

You can test workflows locally using [act](https://github.com/nektos/act):

```bash
# Install act (macOS)
brew install act

# Run build workflow
act pull_request -W .github/workflows/build.yml

# Run release workflow (simulate tag push)
act push -W .github/workflows/release.yml --var GITHUB_REF=refs/tags/v1.0.0
```

---

## Version Bumping

The release workflow automatically updates the version in `build.gradle.kts` based on the tag:

```bash
# Tag v1.2.3 will set version to "1.2.3" in build.gradle.kts
git tag v1.2.3
git push origin v1.2.3
```

You don't need to manually update the version before creating a tag.
