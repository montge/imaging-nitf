# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- SECURITY.md with vulnerability reporting process
- CONTRIBUTING.md with comprehensive contributor guidelines
- CodeQL security scanning workflow
- JaCoCo coverage reporting in CI/CD pipeline
- Maven package caching in GitHub Actions
- Codecov integration for coverage tracking
- Updated README badges (GitHub Actions, CodeQL, License)

### Changed
- Fixed Dependabot configuration to properly track Maven dependencies
- Updated GitHub Actions to use latest versions (v4)
- Enhanced CI/CD to run on both push and pull_request events
- Updated README to reflect current version (0.11-SNAPSHOT)
- Removed outdated Travis CI badge

### Security
- Enabled automated dependency vulnerability scanning
- Added weekly Dependabot updates for Maven dependencies
- Configured CodeQL for security analysis on all commits

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
