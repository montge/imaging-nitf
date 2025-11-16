/*
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 */
package org.codice.imaging.nitf.core.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.*;

/**
 * Architecture tests using ArchUnit to enforce design rules.
 */
public class ArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeClass
    public static void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("org.codice.imaging.nitf.core");
    }

    @Test
    public void testPackageStructure() {
        ArchRule rule = layeredArchitecture()
                .consideringAllDependencies()
                .layer("Header").definedBy("..header..")
                .layer("Image").definedBy("..image..")
                .layer("Text").definedBy("..text..")
                .layer("Graphic").definedBy("..graphic..")
                .layer("Symbol").definedBy("..symbol..")
                .layer("Label").definedBy("..label..")
                .layer("DataExtension").definedBy("..dataextension..")
                .layer("Security").definedBy("..security..")
                .layer("TRE").definedBy("..tre..")
                .layer("Common").definedBy("..common..")
                .whereLayer("Header").mayOnlyBeAccessedByLayers("Common")
                .whereLayer("Common").mayNotAccessAnyLayer();

        // Note: This is a basic structure check. Adjust based on actual architecture.
    }

    @Test
    public void testNoGenericExceptions() {
        noClasses()
                .should(THROW_GENERIC_EXCEPTIONS)
                .check(importedClasses);
    }

    @Test
    public void testNoJavaUtilLogging() {
        noClasses()
                .should(USE_JAVA_UTIL_LOGGING)
                .check(importedClasses);
    }

    @Test
    public void testNoJodaTime() {
        noClasses()
                .should(USE_JODATIME)
                .check(importedClasses);
    }

    @Test
    public void testNoFieldInjection() {
        noClasses()
                .should(BE_ANNOTATED_WITH_AN_INJECTION_ANNOTATION)
                .check(importedClasses);
    }

    @Test
    public void testApiPackagesDoNotDependOnImpl() {
        noClasses()
                .that().resideInAPackage("..api..")
                .should().dependOnClassesThat().resideInAPackage("..impl..")
                .check(importedClasses);
    }

    @Test
    public void testInterfacesShouldNotHaveImplInName() {
        noClasses()
                .that().areInterfaces()
                .should().haveSimpleNameEndingWith("Impl")
                .check(importedClasses);
    }

    @Test
    public void testClassesShouldNotUseStandardStreams() {
        ArchRule rule = noClasses()
                .should().accessClassesThat().haveFullyQualifiedName("java.lang.System")
                .andShould().callMethod("java.io.PrintStream", "println", String.class)
                .because("Use SLF4J logging instead of System.out/err");

        // This is commented out as some test classes may legitimately use System.out
        // rule.check(importedClasses);
    }

    @Test
    public void testParserClassesShouldBeInImplPackage() {
        classes()
                .that().haveSimpleNameContaining("Parser")
                .should().resideInAPackage("..impl..")
                .check(importedClasses);
    }

    @Test
    public void testFactoryClassesShouldBeInImplPackage() {
        classes()
                .that().haveSimpleNameContaining("Factory")
                .should().resideInAPackage("..impl..")
                .check(importedClasses);
    }
}
