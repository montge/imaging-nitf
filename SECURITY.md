# Security Policy

## Supported Versions

We release security updates for the following versions of imaging-nitf:

| Version | Supported          |
| ------- | ------------------ |
| 0.11.x  | :white_check_mark: |
| 0.10.x  | :white_check_mark: |
| < 0.10  | :x:                |

## Reporting a Vulnerability

We take the security of imaging-nitf seriously. If you believe you have found a security vulnerability, please report it to us as described below.

### Please Do Not

- **Do not** open a public GitHub issue for security vulnerabilities
- **Do not** disclose the vulnerability publicly until we've had a chance to address it

### How to Report

**Please report security vulnerabilities by emailing the project maintainers.**

You can reach us at:
- Create a [security advisory](https://github.com/codice/imaging-nitf/security/advisories/new) on GitHub (preferred)
- Or contact the project maintainers listed in the [pom.xml](pom.xml) file

Please include the following information in your report:

1. **Description** of the vulnerability
2. **Steps to reproduce** the issue
3. **Potential impact** of the vulnerability
4. **Suggested fix** (if you have one)
5. **Your name/handle** for acknowledgment (optional)

### What to Expect

- **Acknowledgment**: We will acknowledge receipt of your vulnerability report within 48 hours
- **Updates**: We will send you regular updates about our progress
- **Timeline**: We aim to release a fix within 90 days of the report
- **Credit**: We will credit you in the security advisory (unless you prefer to remain anonymous)

### Security Update Process

1. **Triage**: We will investigate and validate the vulnerability
2. **Fix**: We will develop and test a fix
3. **Release**: We will release a patched version
4. **Disclosure**: We will publish a security advisory with details
5. **Credit**: We will acknowledge the reporter (with permission)

## Security Best Practices for Users

When using imaging-nitf in your projects:

1. **Keep Updated**: Always use the latest version to get security fixes
2. **Validate Input**: Always validate NITF files from untrusted sources
3. **Resource Limits**: Set appropriate memory limits when parsing large files
4. **Dependency Scanning**: Use tools like OWASP Dependency-Check to scan for vulnerabilities
5. **Monitor Advisories**: Watch this repository for security advisories

## Security Features

imaging-nitf implements several security measures:

- **Input Validation**: Strict parsing according to NITF 2.0/2.1 specifications
- **Bounds Checking**: Protection against buffer overflows
- **Resource Limits**: Configurable limits to prevent denial-of-service
- **Static Analysis**: Code scanned with SpotBugs and Checkstyle
- **Automated Scanning**: CodeQL security scanning on all commits

## Known Security Considerations

When working with NITF files, be aware of:

1. **Large Files**: NITF files can be very large; ensure adequate memory
2. **Untrusted Sources**: Always validate files from untrusted sources
3. **Embedded Data**: NITF files can contain various embedded data types
4. **Compression**: Some compression formats may have known vulnerabilities

## Security Disclosure History

Security advisories will be published at:
https://github.com/codice/imaging-nitf/security/advisories

## Contact

For general security questions (not vulnerability reports), you can:
- Open a discussion on GitHub
- Contact the maintainers listed in pom.xml

Thank you for helping keep imaging-nitf and its users safe!
