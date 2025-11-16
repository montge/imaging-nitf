# Contributing to imaging-nitf

Thank you for your interest in contributing to imaging-nitf! This document provides guidelines and information for contributors.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Making Changes](#making-changes)
- [Testing](#testing)
- [Code Style](#code-style)
- [Pull Request Process](#pull-request-process)
- [Contributor License Agreement](#contributor-license-agreement)

## Code of Conduct

This project follows a code of conduct to ensure a welcoming environment for all contributors. By participating, you are expected to uphold professional and respectful communication.

## Getting Started

### Prerequisites

- **JDK 11 or higher** (JDK 11, 17, or 21 recommended)
- **Apache Maven 3.6.3+** (Maven 3.9+ recommended)
- **Git** for version control

### Fork and Clone

1. Fork the repository on GitHub
2. Clone your fork locally:
   ```bash
   git clone https://github.com/YOUR-USERNAME/imaging-nitf.git
   cd imaging-nitf
   ```

3. Add the upstream repository:
   ```bash
   git remote add upstream https://github.com/codice/imaging-nitf.git
   ```

## Development Setup

### Building from Source

```bash
# Build and run all tests
mvn clean install

# Build without tests (faster)
mvn clean install -DskipTests

# Build with parallel execution
mvn -T3 clean install
```

### IDE Setup

#### IntelliJ IDEA
1. Open the project: `File → Open` and select the `pom.xml`
2. Enable annotation processing if prompted
3. Install Checkstyle-IDEA plugin for code style checking
4. Import the `checkstyle.xml` configuration

#### Eclipse
1. Import as Maven project: `File → Import → Maven → Existing Maven Projects`
2. Install Checkstyle Eclipse plugin
3. Configure Checkstyle with the project's `checkstyle.xml`

#### VS Code
1. Install Java Extension Pack
2. Install Checkstyle extension
3. Open the project folder

### Running Tests

```bash
# Run all tests
mvn test

# Run tests for a specific module
mvn test -pl core

# Run a specific test class
mvn test -Dtest=RoundTripNITF21WriterTest

# Run with coverage report
mvn test jacoco:report
```

View coverage reports at: `target/site/jacoco/index.html`

## Making Changes

### Before You Start

1. Check existing issues and pull requests to avoid duplication
2. For major changes, open an issue first to discuss the approach
3. Keep changes focused - one feature or fix per pull request

### Branch Naming

Use descriptive branch names:
- `feature/add-nitf-30-support` - New features
- `fix/parsing-error-in-tre` - Bug fixes
- `docs/improve-api-examples` - Documentation
- `refactor/simplify-parser` - Code refactoring

### Commit Messages

Write clear, descriptive commit messages:

```
Add support for NITF 3.0 file format parsing

- Implement NITF 3.0 header parser
- Add test cases for NITF 3.0 samples
- Update documentation with NITF 3.0 support

Fixes #123
```

**Format:**
- First line: Short summary (50 chars or less)
- Blank line
- Detailed description (wrap at 72 characters)
- Reference related issues

## Testing

### Test Requirements

All contributions must include tests:

- **Unit tests** for new functionality
- **Integration tests** for complex features
- **Round-trip tests** for file format changes
- **Regression tests** for bug fixes

### Test Organization

```
src/test/java/
├── org/codice/imaging/nitf/core/
│   ├── *Test.java           # Unit tests
│   ├── RoundTrip*Test.java  # Round-trip tests
│   └── tre/impl/*_Test.java # TRE-specific tests
```

### Test Best Practices

1. **Use descriptive test names**: `testParseNitf21HeaderWithMultipleImages()`
2. **Follow AAA pattern**: Arrange, Act, Assert
3. **One assertion per test** (when possible)
4. **Use test resources**: Place sample files in `shared-test-resources`
5. **Clean up resources**: Always close files and streams

### Example Test

```java
@Test
public void testParseValidNitf21File() throws Exception {
    // Arrange
    File nitfFile = getResourceFile("/JitcNitf21Samples/i_3001a.ntf");
    NitfReader reader = new FileReader(nitfFile);

    // Act
    AllDataExtractionParseStrategy strategy = new AllDataExtractionParseStrategy();
    NitfFileParser.parse(reader, strategy);

    // Assert
    assertNotNull(strategy.getNitfHeader());
    assertEquals("NITF02.10", strategy.getNitfHeader().getFileType());
}
```

## Code Style

### Checkstyle

This project uses Checkstyle to enforce code style. Configuration: `checkstyle.xml`

**Key rules:**
- Sun Java coding conventions
- 4 spaces for indentation (no tabs)
- Line length: 120 characters maximum
- Javadoc required for public and protected methods
- No trailing whitespace

### Running Checkstyle

```bash
# Check all modules
mvn checkstyle:check

# Check specific module
mvn checkstyle:check -pl core
```

### Javadoc

All public APIs must have Javadoc:

```java
/**
 * Parses a NITF file and extracts all data segments.
 *
 * @param reader the NITF file reader
 * @param strategy the parsing strategy to use
 * @return the parsed NITF file header
 * @throws NitfFormatException if the file format is invalid
 * @throws IOException if an I/O error occurs
 */
public NitfFileHeader parse(NitfReader reader, ParseStrategy strategy)
        throws NitfFormatException, IOException {
    // Implementation
}
```

### Static Analysis

Code is analyzed with SpotBugs:

```bash
# Run SpotBugs
mvn spotbugs:check

# Generate HTML report
mvn spotbugs:spotbugs
# View at: target/spotbugs.html
```

## Pull Request Process

### Before Submitting

1. **Sync with upstream**:
   ```bash
   git fetch upstream
   git rebase upstream/master
   ```

2. **Run full build**:
   ```bash
   mvn clean install
   ```

3. **Verify code quality**:
   ```bash
   mvn checkstyle:check spotbugs:check
   ```

4. **Check test coverage**:
   ```bash
   mvn test jacoco:report
   # Ensure new code has >70% coverage
   ```

### Submitting a Pull Request

1. Push your branch to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```

2. Open a pull request on GitHub

3. Fill out the PR template completely:
   - **Title**: Clear, descriptive summary
   - **Description**: What changed and why
   - **Testing**: How you tested the changes
   - **Issues**: Reference related issues (Fixes #123)

4. Ensure CI checks pass:
   - ✅ Build successful
   - ✅ All tests pass
   - ✅ Checkstyle passes
   - ✅ SpotBugs passes
   - ✅ CodeQL security scan passes

### Review Process

1. **Automated checks** run on all PRs
2. **Maintainer review** (usually within 1 week)
3. **Address feedback** by pushing new commits
4. **Approval and merge** by maintainers

### After Your PR is Merged

1. Delete your feature branch:
   ```bash
   git branch -d feature/your-feature-name
   git push origin --delete feature/your-feature-name
   ```

2. Sync your fork:
   ```bash
   git checkout master
   git pull upstream master
   git push origin master
   ```

## Contributor License Agreement

This project uses the [CLA Assistant](https://cla-assistant.io/codice/imaging-nitf). You will be asked to sign the CLA when you submit your first pull request. This is a one-time process.

### Why a CLA?

The CLA ensures:
- You have the right to contribute your code
- The project can safely use and distribute your contribution
- The code remains open source under LGPL 2.1

## Getting Help

- **Questions?** Open a [GitHub Discussion](https://github.com/codice/imaging-nitf/discussions)
- **Bug reports?** Open an [issue](https://github.com/codice/imaging-nitf/issues)
- **Security concerns?** See [SECURITY.md](SECURITY.md)

## Recognition

Contributors are recognized in:
- The `CHANGELOG.md` file
- GitHub's contributor graph
- Release notes

Thank you for contributing to imaging-nitf!
