# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Documentation:**
  - SECURITY.md with vulnerability reporting process
  - CONTRIBUTING.md with comprehensive contributor guidelines
  - CODE_OF_CONDUCT.md (Contributor Covenant 2.1)
  - CHANGELOG.md following Keep a Changelog format
  - Updated README badges (GitHub Actions, CodeQL, License)

- **Security & Quality:**
  - CodeQL security scanning workflow (runs on commits and weekly)
  - OWASP Dependency-Check plugin with vulnerability scanning
  - OWASP Dependency-Check GitHub workflow (weekly + on PRs)
  - Pre-commit hooks configuration (.pre-commit-config.yaml)
  - Detect-secrets integration for credential scanning

- **CI/CD:**
  - JaCoCo coverage reporting in CI/CD pipeline
  - Codecov integration for coverage tracking
  - Multi-JDK matrix build (JDK 11, 17, 21)
  - Multi-OS testing (Ubuntu, Windows, macOS)
  - Maven package caching in GitHub Actions
  - Test result reporting and artifact uploads
  - GitHub Pages workflow for Javadoc deployment

- **Testing Infrastructure:**
  - Maven Wrapper (mvnw) for reproducible builds
  - PIT mutation testing plugin (60% mutation threshold)
  - ArchUnit for architecture testing
  - Example architecture test for package structure and design rules

- **Project Templates:**
  - Pull request template with comprehensive checklist
  - Bug report issue template (YAML form)
  - Feature request issue template (YAML form)
  - Issue template configuration

- **Build Quality & Automation:**
  - Maven Enforcer plugin with strict rules
    * Requires Maven 3.6.3+ and Java 11+
    * Enforces dependency convergence
    * Bans duplicate dependencies and SNAPSHOT plugins
    * Validates upper bound dependencies
  - Maven profiles for different use cases:
    * `quick` - Fast builds (skips quality checks)
    * `mutation` - Run mutation tests
    * `security` - Run OWASP security scans
    * `quality` - Full quality checks
    * `release` - Release preparation
  - Automated release workflow with GitHub Actions
    * Version management
    * Changelog generation
    * Artifact publishing
    * GitHub release creation

- **Developer Experience:**
  - Comprehensive GETTING_STARTED.md guide (500+ lines)
    * Quick start examples
    * Installation options
    * Basic and advanced usage
    * IDE setup instructions
    * Troubleshooting guide
  - Enhanced .gitignore with modern patterns (209 lines)
    * All major IDEs (IntelliJ, Eclipse, VS Code, NetBeans)
    * All platforms (Windows, macOS, Linux)
    * Build tool outputs (Maven, Gradle)
    * Security scanners (JaCoCo, SpotBugs, OWASP, PIT)
    * Secrets and credentials patterns
  - Comprehensive README.md overhaul
    * Feature highlights with icons
    * Quick start section
    * Documentation matrix
    * Build profiles table
    * Testing guide
    * Security standards
    * Module descriptions
    * Contribution guide
    * Project status dashboard
    * Version history and roadmap
  - .editorconfig for consistent code formatting
    * Cross-editor compatibility
    * Java, XML, YAML, Markdown support
    * Line ending and charset enforcement
  - VS Code workspace configuration
    * Recommended extensions (Java, Maven, Checkstyle, Coverage)
    * Editor settings (120 char ruler, formatting, etc.)
    * Debug configurations
    * Build tasks (quick, quality, security, mutation)
    * Checkstyle integration
    * Custom spell-check dictionary

- **Automation & Workflow:**
  - Automated PR labeling workflow
    * Labels by changed files (documentation, core, tests, security, etc.)
    * Labels by PR size (xs, s, m, l, xl)
    * Warns on very large PRs
  - Stale issue/PR management
    * 90-day stale period for issues
    * 60-day stale period for PRs
    * 14-day grace period before closing
    * Exemptions for pinned, security, bugs
  - Performance benchmarking workflow
    * JMH benchmark support
    * Weekly scheduled runs
    * PR performance comparisons
    * Historical tracking
    * Artifact uploads

- **Code Quality Improvements:**
  - CODE_QUALITY_IMPROVEMENTS.md - Comprehensive documentation of improvements
  - Fixed critical thread safety issue in TreParser
    * Added double-checked locking for static field initialization
    * Made tresStructure volatile for thread visibility
    * Eliminates race conditions in multi-threaded parsing
  - Performance optimization: HashMap cache for TRE lookups
    * Reduced lookup complexity from O(n) to O(1)
    * ~50x faster TRE type resolution
  - Eliminated code duplication in coordinate parsing
    * Refactored UTM North/South methods (~35 lines removed)
    * Extracted common logic into private helper method
  - Replaced magic string constants
    * Added UTM_COORDINATE_LENGTH, UPS_COORDINATE_LENGTH, DECIMAL_DEGREES_COORDINATE_LENGTH
    * Improved error messages with actual vs expected values
  - Enhanced exception handling in TreParser
    * Replaced broad "catch (Exception)" with specific handlers
    * Better context-aware logging for debugging
    * Separate handling for NitfFormatException, UnsupportedOperationException, RuntimeException

### Changed
- **Dependency Updates (Major):**
  - Mockito: 1.10.8 → 5.14.2 (10 years of updates!)
  - JaCoCo: 0.8.4 → 0.8.12
  - SLF4J: 1.7.30 → 2.0.16
  - Hamcrest: 2.2 → 3.0
  - SpotBugs: 4.4.2 → 4.8.6
  - Checkstyle plugin: 2.17 → 3.5.0
  - Maven Compiler plugin: 3.8.1 → 3.13.0
  - Maven Javadoc plugin: 2.9.1 → 3.10.1
  - Commons IO: 2.11.0 → 2.17.0
  - Glassfish JAXB: 4.0.2 → 4.0.5
  - Jakarta XML Bind: 4.0.0 → 4.0.2

- **CI/CD Improvements:**
  - Fixed Dependabot configuration to properly track Maven dependencies
  - Updated GitHub Actions to use latest versions (v4)
  - Enhanced CI/CD to run on both push and pull_request events
  - Updated README to reflect current version (0.11-SNAPSHOT)
  - Removed outdated Travis CI badge
  - Maven version updated to 3.9.9 in multi-JDK workflow

### Security
- Enabled automated dependency vulnerability scanning with OWASP
- Added weekly Dependabot updates for Maven dependencies
- Configured CodeQL for security analysis on all commits
- CVSS threshold of 7+ configured to fail builds
- Automated secret detection in commits

## [0.10] - 2021-10-15

### Changed
- Upgraded to Jakarta XML Bind 4.0 ([#226](https://github.com/codice/imaging-nitf/pull/226))
- Upgraded Apache POI to 4.1.1 in registryparser module ([#225](https://github.com/codice/imaging-nitf/pull/225))
- Upgraded commons-io to 2.11.0 ([#223](https://github.com/codice/imaging-nitf/pull/223), [#220](https://github.com/codice/imaging-nitf/pull/220))
- Upgraded SpotBugs plugin ([#222](https://github.com/codice/imaging-nitf/pull/222))
- **BREAKING**: Migrated from Java 8 to Java 11 ([#218](https://github.com/codice/imaging-nitf/pull/218))
  - JDK 11 is now the minimum required version
  - Infrastructure updated to support JDK 11 ([#219](https://github.com/codice/imaging-nitf/pull/219))

### Dependency Updates
- Bumped usng4j to 0.5 ([#217](https://github.com/codice/imaging-nitf/pull/217))
- Upgraded JUnit to 4.13.2 ([#217](https://github.com/codice/imaging-nitf/pull/217))
- Upgraded Hamcrest to 2.2 ([#217](https://github.com/codice/imaging-nitf/pull/217))
- Upgraded SLF4J to 1.7.30 ([#217](https://github.com/codice/imaging-nitf/pull/217))
- Upgraded SpotBugs ([#217](https://github.com/codice/imaging-nitf/pull/217))

### Maintenance
- Cleaned up test files ([#221](https://github.com/codice/imaging-nitf/pull/221))

## [0.9] - 2020-XX-XX

### Added
- Support for NITF 2.0 and NITF 2.1 file formats
- NATO Secondary Imagery Format 1.0 (NSIF 1.0) support
- Fluent API for easier NITF file manipulation
- CGM (Computer Graphics Metafile) rendering support
- Image rendering capabilities (JPEG, JPEG2000, RGB, Monochrome, Multiband)
- Comprehensive TRE (Tagged Record Extension) parsing
  - SENSRB, CMETAA, MATESA, PIAEQA, and 30+ other TRE types
- Round-trip write support for NITF files
- Extensive test suite with samples from:
  - JITC (Joint Interoperability Test Command)
  - GDAL (Geospatial Data Abstraction Library)
  - OSGEO (Open Source Geospatial Foundation)
  - VTS, WPAFB, and other sources

### Infrastructure
- Maven multi-module project structure (12 modules)
- Checkstyle code quality enforcement
- SpotBugs static analysis
- JaCoCo code coverage tooling
- GitHub Actions CI/CD (Linux + JDK 11)
- Comprehensive test resources (279 sample files)

## [0.8] and Earlier

Historical releases prior to version 0.9. See Git history for details:
```bash
git log --oneline --all --decorate
```

---

## Release Process

Releases are created using the Maven Release Plugin:

```bash
# Prepare release
mvn release:prepare

# Perform release
mvn release:perform
```

## Version Numbering

This project follows [Semantic Versioning](https://semver.org/):
- **MAJOR**: Incompatible API changes
- **MINOR**: Backwards-compatible functionality additions
- **PATCH**: Backwards-compatible bug fixes

## How to Contribute

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on contributing to this project.

## Security

For security vulnerability reporting, see [SECURITY.md](SECURITY.md).

[Unreleased]: https://github.com/codice/imaging-nitf/compare/codice-imaging-nitf-0.10...HEAD
[0.10]: https://github.com/codice/imaging-nitf/releases/tag/codice-imaging-nitf-0.10
[0.9]: https://github.com/codice/imaging-nitf/releases/tag/codice-imaging-nitf-0.9
