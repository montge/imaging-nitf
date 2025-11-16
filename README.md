# imaging-nitf

[![Build Status](https://github.com/codice/imaging-nitf/workflows/Linux%20JDK%2011%20GitHub%20CI/badge.svg)](https://github.com/codice/imaging-nitf/actions)
[![Multi-JDK](https://github.com/codice/imaging-nitf/workflows/Multi-JDK%20Matrix%20Build/badge.svg)](https://github.com/codice/imaging-nitf/actions)
[![CodeQL](https://github.com/codice/imaging-nitf/workflows/CodeQL%20Security%20Scanning/badge.svg)](https://github.com/codice/imaging-nitf/security/code-scanning)
[![OWASP](https://github.com/codice/imaging-nitf/workflows/OWASP%20Dependency%20Check/badge.svg)](https://github.com/codice/imaging-nitf/actions)
[![License](https://img.shields.io/badge/License-LGPL%202.1-blue.svg)](LICENSE.md)
[![CLA assistant](https://cla-assistant.io/readme/badge/codice/imaging-nitf)](https://cla-assistant.io/codice/imaging-nitf)

> **Pure Java National Imagery Transmission Format (NITF) file support**

A comprehensive, modern Java library for parsing and creating NITF 2.0, NITF 2.1, and NSIF 1.0 files with enterprise-grade quality, security, and testing.

---

## ✨ Features

- **📖 Complete NITF Support:** Full parsing for NITF 2.0, NITF 2.1, and NATO NSIF 1.0
- **🖼️ Image Processing:** Extract and render images (JPEG, JPEG2000, RGB, Monochrome, Multiband)
- **🎨 Graphics Support:** Computer Graphics Metafile (CGM) rendering
- **🏷️ TRE Parsing:** 30+ Tagged Record Extension types supported
- **💧 Fluent API:** Easy-to-use functional API for common operations
- **🔒 Enterprise Security:** CodeQL scanning, OWASP dependency checks, automated updates
- **✅ High Quality:** Mutation testing, architecture constraints, 70%+ coverage
- **🌍 Cross-Platform:** Tested on JDK 11/17/21 across Linux, Windows, macOS
- **📚 Well Documented:** Comprehensive guides, examples, and API documentation

---

## 🚀 Quick Start

### Prerequisites

- **Java 11 or higher** (JDK 11, 17, or 21)
- **Maven 3.6.3+** (or use the included `./mvnw` wrapper)

### Installation

#### Maven

```xml
<dependency>
    <groupId>org.codice.imaging.nitf</groupId>
    <artifactId>codice-imaging-nitf-core</artifactId>
    <version>0.11-SNAPSHOT</version>
</dependency>
```

#### Gradle

```gradle
implementation 'org.codice.imaging.nitf:codice-imaging-nitf-core:0.11-SNAPSHOT'
```

#### Build from Source

```bash
git clone https://github.com/codice/imaging-nitf.git
cd imaging-nitf
./mvnw clean install
```

### Basic Usage

#### Standard API

```java
import org.codice.imaging.nitf.core.*;
import java.io.File;

File nitfFile = new File("sample.ntf");
AllDataExtractionParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
NitfReader reader = new FileReader(nitfFile);
NitfFileParser.parse(reader, parseStrategy);

NitfFileHeader header = parseStrategy.getNitfHeader();
System.out.println("File Type: " + header.getFileType());
System.out.println("Image Count: " + header.getImageSegmentCount());
```

#### Fluent API (Recommended)

```java
import org.codice.imaging.nitf.fluent.*;
import java.io.File;

new NitfParserInputFlow()
    .file(new File("sample.ntf"))
    .allData()
    .fileHeader(header -> System.out.println("Title: " + header.getFileTitle()))
    .forEachImageSegment(image -> {
        System.out.println("Image: " + image.getIdentifier());
        System.out.println("Size: " + image.getNumberOfColumns() + "x" + image.getNumberOfRows());
    })
    .forEachTextSegment(text -> System.out.println("Text: " + text.getData()));
```

**👉 See [GETTING_STARTED.md](GETTING_STARTED.md) for complete examples and tutorials!**

---

## 📚 Documentation

| Document | Description |
|----------|-------------|
| **[GETTING_STARTED.md](GETTING_STARTED.md)** | Comprehensive guide with examples, IDE setup, troubleshooting |
| **[CONTRIBUTING.md](CONTRIBUTING.md)** | How to contribute, code style, PR process |
| **[SECURITY.md](SECURITY.md)** | Security policy and vulnerability reporting |
| **[CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)** | Community standards and expectations |
| **[CHANGELOG.md](CHANGELOG.md)** | Version history and release notes |
| **[API Documentation](https://codice.github.io/imaging-nitf/)** | Full Javadoc (auto-deployed) |

---

## 🏗️ Building

### Quick Build (Fast Iteration)

```bash
./mvnw clean install -Pquick
```

### Full Build with Quality Checks

```bash
./mvnw clean install -Pquality
```

### Available Build Profiles

| Profile | Command | Purpose |
|---------|---------|---------|
| `quick` | `./mvnw install -Pquick` | Fast build, skips tests and checks |
| `quality` | `./mvnw install -Pquality` | Full quality gates (Checkstyle, SpotBugs, JaCoCo) |
| `mutation` | `./mvnw verify -Pmutation` | Mutation testing (test quality) |
| `security` | `./mvnw verify -Psecurity` | OWASP vulnerability scan |
| `release` | `./mvnw install -Prelease` | Release preparation |

---

## 🧪 Testing

### Run Tests

```bash
./mvnw test                              # All tests
./mvnw test -pl core                     # Specific module
./mvnw test -Dtest=RoundTripNITF21*      # Specific test pattern
```

### Coverage Report

```bash
./mvnw test jacoco:report
# View: target/site/jacoco/index.html
```

### Mutation Testing (Test Quality)

```bash
./mvnw verify -Pmutation
# View: target/pit-reports/index.html
```

### Architecture Tests

```bash
./mvnw test -Dtest=ArchitectureTest
```

**Current Test Statistics:**
- **113 test files** with comprehensive coverage
- **279 test resource files** (JITC, GDAL, OSGEO samples)
- **7 test environments** (JDK 11/17/21 × Linux/Windows/macOS)
- **30+ TRE types** tested

---

## 🔒 Security

This project maintains enterprise-grade security standards:

- ✅ **CodeQL Scanning:** Automated security analysis on every commit
- ✅ **OWASP Dependency-Check:** Weekly vulnerability scans
- ✅ **Dependabot:** Automated dependency updates
- ✅ **Pre-commit Hooks:** Secret detection and validation
- ✅ **Maven Enforcer:** Build-time dependency validation

**Report vulnerabilities:** See [SECURITY.md](SECURITY.md)

---

## 🏢 Modules

| Module | Description |
|--------|-------------|
| **core** | Main NITF parsing and writing engine |
| **core-api** | Core API interfaces |
| **cgm** | Computer Graphics Metafile support |
| **render** | Image rendering (JPEG, JPEG2000, RGB, etc.) |
| **fluent** | Fluent API implementation |
| **fluent-api** | Fluent API interfaces |
| **trewrap** | TRE wrapper utilities |
| **deswrap** | Data Extension Segment wrappers |
| **imagecompare** | Image comparison tools |
| **metadata-comparison** | Metadata comparison utilities |
| **registryparser** | Registry parsing utilities |
| **shared-test-resources** | Common test files |

---

## 🤝 Contributing

We welcome contributions! Here's how to get started:

1. **Read the guides:**
   - [CONTRIBUTING.md](CONTRIBUTING.md) - Development process
   - [GETTING_STARTED.md](GETTING_STARTED.md) - Setup instructions
   - [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) - Community standards

2. **Sign the CLA:**
   - [Contributor License Agreement](https://cla-assistant.io/codice/imaging-nitf)

3. **Set up your environment:**
   ```bash
   git clone https://github.com/codice/imaging-nitf.git
   cd imaging-nitf
   ./mvnw clean install
   ```

4. **Make your changes:**
   - Follow code style (Checkstyle enforced)
   - Add tests (70%+ coverage required)
   - Update documentation

5. **Submit a PR:**
   - Use the PR template
   - Wait for CI checks to pass
   - Address review feedback

**Quick contribution checklist:**
- [ ] Tests added/updated
- [ ] Checkstyle passes (`./mvnw checkstyle:check`)
- [ ] SpotBugs clean (`./mvnw spotbugs:check`)
- [ ] Documentation updated
- [ ] CHANGELOG.md updated

---

## 📊 Project Status

### Build Health

| Check | Status |
|-------|--------|
| Build (JDK 11) | [![Build](https://github.com/codice/imaging-nitf/workflows/Linux%20JDK%2011%20GitHub%20CI/badge.svg)](https://github.com/codice/imaging-nitf/actions) |
| Multi-JDK Matrix | [![Multi-JDK](https://github.com/codice/imaging-nitf/workflows/Multi-JDK%20Matrix%20Build/badge.svg)](https://github.com/codice/imaging-nitf/actions) |
| Security Scan | [![CodeQL](https://github.com/codice/imaging-nitf/workflows/CodeQL%20Security%20Scanning/badge.svg)](https://github.com/codice/imaging-nitf/security/code-scanning) |
| Dependencies | [![OWASP](https://github.com/codice/imaging-nitf/workflows/OWASP%20Dependency%20Check/badge.svg)](https://github.com/codice/imaging-nitf/actions) |

### Quality Metrics

- **Test Coverage:** 70%+ (JaCoCo)
- **Mutation Score:** 60%+ (PIT)
- **Code Quality:** Checkstyle + SpotBugs enforced
- **Supported JDK:** 11, 17, 21
- **Supported OS:** Linux, Windows, macOS

---

## 🔗 Resources

### Getting Help

- **Documentation:** [GETTING_STARTED.md](GETTING_STARTED.md)
- **Discussions:** [GitHub Discussions](https://github.com/codice/imaging-nitf/discussions)
- **Bug Reports:** [File an Issue](https://github.com/codice/imaging-nitf/issues/new?template=bug_report.yml)
- **Feature Requests:** [Request a Feature](https://github.com/codice/imaging-nitf/issues/new?template=feature_request.yml)

### Standards & Specifications

- [NITF 2.1 Specification](https://nsgreg.nga.mil/doc/view?i=4201)
- [NATO NSIF 1.0](https://nsgreg.nga.mil/doc/view?i=4337)
- [Tagged Record Extensions](https://nsgreg.nga.mil/tre.jsp)

### Related Projects

- [GeoTools NITF Plugin](https://docs.geotools.org/latest/userguide/library/coverage/nitf.html)
- [GDAL NITF Driver](https://gdal.org/drivers/raster/nitf.html)

---

## 📜 License

This project is licensed under the **GNU Lesser General Public License v2.1** - see [LICENSE.md](LICENSE.md) for details.

This allows you to use the library in both open source and proprietary applications, as long as you comply with the LGPL terms.

---

## 🙏 Acknowledgments

- **JITC** for NITF test samples
- **GDAL, OSGEO, VTS** for additional test data
- **Contributors** who have improved this library
- **Codice Foundation** for project stewardship

---

## ⭐ Support

If you find imaging-nitf useful, please:

- ⭐ **Star this repository** on GitHub
- 📢 **Share** with colleagues working with NITF files
- 🐛 **Report bugs** to help improve quality
- 💡 **Suggest features** for future releases
- 🤝 **Contribute** code or documentation

---

## 📈 Version History

See [CHANGELOG.md](CHANGELOG.md) for detailed version history.

### Recent Releases

- **0.11-SNAPSHOT** (current) - Modernization, security improvements, updated dependencies
- **0.10** (2021-10-15) - Jakarta XML Bind 4.0, Java 11 migration
- **0.9** - Initial feature-complete release

---

## 🔮 Roadmap

- [ ] JUnit 5 migration
- [ ] NITF 3.0 support (when specification available)
- [ ] Performance optimizations for large files
- [ ] Additional TRE implementations
- [ ] Spring Boot starter module
- [ ] REST API examples

See [Issues](https://github.com/codice/imaging-nitf/issues) for more details and vote on features!

---

<div align="center">

**Built with ❤️ by the Codice community**

[Report Bug](https://github.com/codice/imaging-nitf/issues/new?template=bug_report.yml) •
[Request Feature](https://github.com/codice/imaging-nitf/issues/new?template=feature_request.yml) •
[View Documentation](https://codice.github.io/imaging-nitf/)

</div>
