# Getting Started with imaging-nitf

Welcome to imaging-nitf! This guide will help you get up and running quickly with parsing and creating NITF (National Imagery Transmission Format) files.

## Table of Contents

- [Quick Start](#quick-start)
- [Installation](#installation)
- [Basic Usage](#basic-usage)
- [Advanced Usage](#advanced-usage)
- [Building from Source](#building-from-source)
- [Running Tests](#running-tests)
- [IDE Setup](#ide-setup)
- [Troubleshooting](#troubleshooting)
- [Next Steps](#next-steps)

---

## Quick Start

### Prerequisites

- **Java 11 or higher** (JDK 11, 17, or 21 recommended)
- **Maven 3.6.3+** (or use the included Maven Wrapper)

### 5-Minute Example

```bash
# Clone the repository
git clone https://github.com/codice/imaging-nitf.git
cd imaging-nitf

# Build using Maven Wrapper (no Maven installation required!)
./mvnw clean install

# Run a simple example (create a Java file)
cat > Example.java <<'EOF'
import org.codice.imaging.nitf.core.*;
import org.codice.imaging.nitf.core.common.*;
import java.io.File;

public class Example {
    public static void main(String[] args) throws Exception {
        File nitfFile = new File("path/to/file.ntf");
        AllDataExtractionParseStrategy strategy = new AllDataExtractionParseStrategy();
        NitfReader reader = new FileReader(nitfFile);
        NitfFileParser.parse(reader, strategy);

        NitfFileHeader header = strategy.getNitfHeader();
        System.out.println("NITF Version: " + header.getFileType());
        System.out.println("Originator: " + header.getOriginatingStationId());
        System.out.println("Image Count: " + header.getImageSegmentCount());
    }
}
EOF

# Compile and run (after adding dependencies to classpath)
javac -cp "core/target/*:core-api/target/*" Example.java
java -cp ".:core/target/*:core-api/target/*" Example
```

---

## Installation

### Option 1: Maven Dependency (Recommended)

Add to your `pom.xml`:

```xml
<dependencies>
    <!-- Core NITF parsing -->
    <dependency>
        <groupId>org.codice.imaging.nitf</groupId>
        <artifactId>codice-imaging-nitf-core</artifactId>
        <version>0.11-SNAPSHOT</version>
    </dependency>

    <!-- CGM (Computer Graphics Metafile) support -->
    <dependency>
        <groupId>org.codice.imaging.nitf</groupId>
        <artifactId>codice-imaging-cgm</artifactId>
        <version>0.11-SNAPSHOT</version>
    </dependency>

    <!-- Image rendering (optional) -->
    <dependency>
        <groupId>org.codice.imaging.nitf</groupId>
        <artifactId>codice-imaging-nitf-render</artifactId>
        <version>0.11-SNAPSHOT</version>
    </dependency>

    <!-- Fluent API (optional, easier to use) -->
    <dependency>
        <groupId>org.codice.imaging.nitf</groupId>
        <artifactId>codice-imaging-nitf-fluent</artifactId>
        <version>0.11-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle

```gradle
dependencies {
    implementation 'org.codice.imaging.nitf:codice-imaging-nitf-core:0.11-SNAPSHOT'
    implementation 'org.codice.imaging.nitf:codice-imaging-cgm:0.11-SNAPSHOT'
    // Optional modules
    implementation 'org.codice.imaging.nitf:codice-imaging-nitf-render:0.11-SNAPSHOT'
    implementation 'org.codice.imaging.nitf:codice-imaging-nitf-fluent:0.11-SNAPSHOT'
}
```

### Option 3: Build from Source

```bash
git clone https://github.com/codice/imaging-nitf.git
cd imaging-nitf
./mvnw clean install
```

---

## Basic Usage

### Parsing a NITF File (Standard API)

```java
import org.codice.imaging.nitf.core.*;
import org.codice.imaging.nitf.core.common.*;
import java.io.File;

public class NitfParser {
    public static void main(String[] args) {
        try {
            // Open the NITF file
            File nitfFile = new File("sample.ntf");

            // Create a parsing strategy (extracts all data)
            AllDataExtractionParseStrategy parseStrategy = new AllDataExtractionParseStrategy();

            // Create a reader
            NitfReader reader = new FileReader(nitfFile);

            // Parse the file
            NitfFileParser.parse(reader, parseStrategy);

            // Access the parsed data
            NitfFileHeader header = parseStrategy.getNitfHeader();

            // Print basic information
            System.out.println("File Type: " + header.getFileType());
            System.out.println("File Version: " + header.getFileVersion());
            System.out.println("Complexity Level: " + header.getComplexityLevel());
            System.out.println("Originating Station: " + header.getOriginatingStationId());
            System.out.println("File Date/Time: " + header.getFileDateTime());
            System.out.println("File Title: " + header.getFileTitle());

            // Image segments
            System.out.println("\nImage Segments: " + header.getImageSegmentCount());
            parseStrategy.getImageSegments().forEach(imageSegment -> {
                System.out.println("  - ID: " + imageSegment.getIdentifier());
                System.out.println("    Dimensions: " + imageSegment.getNumberOfColumns() +
                                   "x" + imageSegment.getNumberOfRows());
                System.out.println("    Compression: " + imageSegment.getImageCompression());
            });

            // Text segments
            System.out.println("\nText Segments: " + header.getTextSegmentCount());
            parseStrategy.getTextSegments().forEach(textSegment -> {
                System.out.println("  - " + textSegment.getIdentifier());
            });

        } catch (Exception e) {
            System.err.println("Error parsing NITF file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

### Using the Fluent API (Easier!)

```java
import org.codice.imaging.nitf.fluent.*;
import java.io.File;

public class FluentExample {
    public static void main(String[] args) {
        File nitfFile = new File("sample.ntf");

        new NitfParserInputFlow()
            .file(nitfFile)
            .allData()
            .fileHeader(header -> {
                System.out.println("Title: " + header.getFileTitle());
                System.out.println("Date: " + header.getFileDateTime());
            })
            .forEachImageSegment(image -> {
                System.out.println("Image: " + image.getIdentifier());
                System.out.println("  Size: " + image.getNumberOfColumns() +
                                   "x" + image.getNumberOfRows());
                System.out.println("  Compression: " + image.getImageCompression());
            })
            .forEachTextSegment(text -> {
                System.out.println("Text: " + text.getIdentifier());
                System.out.println("  Content: " + text.getData());
            })
            .forEachGraphicSegment(graphic -> {
                System.out.println("Graphic: " + graphic.getIdentifier());
            });
    }
}
```

### Extracting Image Data

```java
import org.codice.imaging.nitf.render.*;
import org.codice.imaging.nitf.core.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageExtractor {
    public static void main(String[] args) throws Exception {
        // Parse NITF file
        File nitfFile = new File("image.ntf");
        AllDataExtractionParseStrategy strategy = new AllDataExtractionParseStrategy();
        NitfFileParser.parse(new FileReader(nitfFile), strategy);

        // Get first image segment
        ImageSegment imageSegment = strategy.getImageSegments().get(0);

        // Render to BufferedImage
        BufferedImage image = ImageSegmentRenderer.render(imageSegment);

        // Save as PNG
        ImageIO.write(image, "PNG", new File("output.png"));

        System.out.println("Image extracted to output.png");
    }
}
```

---

## Advanced Usage

### Working with TREs (Tagged Record Extensions)

```java
import org.codice.imaging.nitf.core.*;
import org.codice.imaging.nitf.core.tre.*;

// Parse file
AllDataExtractionParseStrategy strategy = new AllDataExtractionParseStrategy();
NitfFileParser.parse(new FileReader(new File("sample.ntf")), strategy);

// Access file-level TREs
TreCollection fileTres = strategy.getNitfHeader().getTREsRawStructure();
fileTres.getTREs().forEach(tre -> {
    System.out.println("TRE: " + tre.getName());
    System.out.println("  Source: " + tre.getSource());
});

// Access image-level TREs
strategy.getImageSegments().forEach(image -> {
    TreCollection imageTres = image.getTREsRawStructure();
    System.out.println("Image " + image.getIdentifier() + " TREs:");
    imageTres.getTREs().forEach(tre -> {
        System.out.println("  - " + tre.getName());
    });
});
```

### Creating a NITF File

```java
import org.codice.imaging.nitf.fluent.*;
import org.codice.imaging.nitf.core.*;
import java.io.*;

public class NitfCreator {
    public static void main(String[] args) throws Exception {
        File outputFile = new File("created.ntf");

        NitfCreationFlow creation = new NitfCreationFlow()
            .fileHeader(() -> {
                // Configure file header
                FileSecurityMetadata security = // ... create security metadata
                return new FileHeaderBuilder()
                    .fileType("NITF")
                    .version("02.10")
                    .originatingStationId("MYSTATION")
                    .fileTitle("Created NITF File")
                    .securityMetadata(security)
                    .build();
            })
            .imageSegment(() -> {
                // Add image segment
                // ... create and return ImageSegment
            });

        // Write to file
        try (OutputStream out = new FileOutputStream(outputFile)) {
            creation.write(out);
        }

        System.out.println("NITF file created: " + outputFile.getName());
    }
}
```

---

## Building from Source

### Requirements

- JDK 11 or higher
- Maven 3.6.3+ (or use `./mvnw`)
- Git

### Standard Build

```bash
# Clone repository
git clone https://github.com/codice/imaging-nitf.git
cd imaging-nitf

# Full build with tests
./mvnw clean install

# Quick build (skip tests and quality checks)
./mvnw clean install -Pquick

# Build with all quality checks
./mvnw clean install -Pquality
```

### Useful Build Profiles

| Profile | Command | Purpose |
|---------|---------|---------|
| `quick` | `./mvnw install -Pquick` | Fast build, skips tests and quality checks |
| `quality` | `./mvnw install -Pquality` | Full quality checks (Checkstyle, SpotBugs, JaCoCo) |
| `mutation` | `./mvnw verify -Pmutation` | Run mutation tests (measures test quality) |
| `security` | `./mvnw verify -Psecurity` | Run OWASP dependency security scan |
| `release` | `./mvnw install -Prelease` | Prepare for release (includes enforcer) |

---

## Running Tests

### Run All Tests

```bash
./mvnw test
```

### Run Tests for Specific Module

```bash
./mvnw test -pl core
```

### Run Specific Test Class

```bash
./mvnw test -Dtest=RoundTripNITF21WriterTest
```

### Generate Coverage Report

```bash
./mvnw test jacoco:report
# View report at: target/site/jacoco/index.html
```

### Run Mutation Tests (Test Quality)

```bash
./mvnw org.pitest:pitest-maven:mutationCoverage
# View report at: target/pit-reports/index.html
```

### Architecture Tests

Architecture constraints are enforced automatically:

```bash
./mvnw test -Dtest=ArchitectureTest
```

---

## IDE Setup

### IntelliJ IDEA

1. **Import Project:**
   - `File → Open` → Select `pom.xml`
   - Choose "Open as Project"

2. **Configure JDK:**
   - `File → Project Structure → Project`
   - Set SDK to JDK 11 or higher

3. **Install Checkstyle Plugin:**
   - `File → Settings → Plugins`
   - Search for "Checkstyle-IDEA"
   - Install and restart

4. **Import Checkstyle Config:**
   - `File → Settings → Tools → Checkstyle`
   - Add `checkstyle.xml` from project root

5. **Run Tests:**
   - Right-click on test class → `Run 'TestName'`

### Eclipse

1. **Import as Maven Project:**
   - `File → Import → Maven → Existing Maven Projects`
   - Select project directory

2. **Install Checkstyle Plugin:**
   - `Help → Eclipse Marketplace`
   - Search for "Checkstyle Plug-in"
   - Install and restart

3. **Configure Checkstyle:**
   - Right-click project → `Properties → Checkstyle`
   - Add `checkstyle.xml`

### VS Code

1. **Install Extensions:**
   - Java Extension Pack
   - Checkstyle for Java
   - Maven for Java

2. **Open Project:**
   - `File → Open Folder` → Select project directory

3. **Run Tests:**
   - Use Testing sidebar
   - Or run `./mvnw test` in terminal

---

## Troubleshooting

### Common Issues

#### "Java 11 or higher is required"

**Solution:** Install JDK 11+ and set JAVA_HOME:

```bash
# Linux/macOS
export JAVA_HOME=/path/to/jdk-11
export PATH=$JAVA_HOME/bin:$PATH

# Windows
set JAVA_HOME=C:\path\to\jdk-11
set PATH=%JAVA_HOME%\bin;%PATH%
```

#### "Maven version too old"

**Solution:** Use the Maven Wrapper (no installation needed):

```bash
./mvnw clean install  # Unix/macOS/Linux
mvnw.cmd clean install  # Windows
```

#### "Out of memory" during build

**Solution:** Increase Maven memory:

```bash
export MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=512m"
./mvnw clean install
```

#### Tests fail with "File not found"

**Solution:** Test resources are in a separate module. Ensure full build:

```bash
./mvnw clean install  # Builds all modules including test resources
```

#### Checkstyle failures

**Solution:** Auto-format code (if using pre-commit hooks):

```bash
# Install pre-commit hooks
pip install pre-commit
pre-commit install

# Run manually
pre-commit run --all-files
```

---

## Next Steps

### Learn More

- **[CONTRIBUTING.md](CONTRIBUTING.md)** - How to contribute
- **[API Documentation](https://codice.github.io/imaging-nitf/)** - Full Javadoc
- **[CHANGELOG.md](CHANGELOG.md)** - What's new
- **[Example Tests](core/src/test/java)** - Real-world usage examples

### Advanced Topics

1. **Custom TRE Parsing:**
   - See `core/src/main/java/org/codice/imaging/nitf/core/tre/`
   - Implement `TreParser` interface

2. **Streaming Large Files:**
   - Use `StreamingModeStrategy` instead of `AllDataExtractionParseStrategy`
   - Memory-efficient for large NITF files

3. **Custom Rendering:**
   - Extend `ImageSegmentRenderer`
   - Handle custom compression formats

4. **Security Metadata:**
   - Configure file-level and segment-level security
   - See `org.codice.imaging.nitf.core.security`

### Get Help

- **GitHub Discussions:** [Ask Questions](https://github.com/codice/imaging-nitf/discussions)
- **Issues:** [Report Bugs](https://github.com/codice/imaging-nitf/issues/new?template=bug_report.yml)
- **Features:** [Request Features](https://github.com/codice/imaging-nitf/issues/new?template=feature_request.yml)

---

## Example Projects

Check out these examples in the test directory:

- **Basic Parsing:** `core/src/test/java/.../Nitf21HeaderTest.java`
- **Round-Trip Testing:** `core/src/test/java/.../RoundTripNITF21WriterTest.java`
- **Image Rendering:** `render/src/test/java/.../RGBRenderTest.java`
- **Fluent API:** `fluent/src/test/java/.../CreationFlowTest.java`

---

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for:

- Code style guidelines
- Testing requirements
- Pull request process
- Development setup

Don't forget to sign the [CLA](https://cla-assistant.io/codice/imaging-nitf)!

---

**Happy coding!** 🚀

If you find imaging-nitf useful, please consider giving us a ⭐ on GitHub!
