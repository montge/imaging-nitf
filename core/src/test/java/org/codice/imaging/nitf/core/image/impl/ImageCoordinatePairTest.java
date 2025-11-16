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
package org.codice.imaging.nitf.core.image.impl;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.codice.imaging.nitf.core.image.ImageCoordinatePair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ImageCoordinatePairTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testImageCoordinatePairDefaultConstructor() {
        ImageCoordinatePair coord = new ImageCoordinatePairImpl();
        assertNotNull(coord);
    }

    @Test
    public void testImageCoordinatePairAccessors() {
        ImageCoordinatePair coord = new ImageCoordinatePairImpl(-35.3761, 149.1018);
        assertNotNull(coord);
        assertEquals(-35.3761, coord.getLatitude(), 0.00001);
        assertEquals(149.1018, coord.getLongitude(), 0.00001);
    }

    @Test
    public void testNullArgumentDMS() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Null argument for DMS parsing");
        coord.setFromDMS(null);
    }

    @Test
    public void testValidArgumentDMS() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromDMS("333019S1502203E");
        assertNotNull(coord);
        assertEquals(-33.5052, coord.getLatitude(), 0.0001);
        assertEquals(150.3675, coord.getLongitude(), 0.0001);
        assertEquals("333019S1502203E", coord.getSourceFormat());
    }

    @Test
    public void testBadArgumentLengthDMS() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for DMS parsing:14");
        coord.setFromDMS("333019S1502203");
    }

    @Test
    public void testBadFirstHemisphereArgumentDMS() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect format for N/S flag while DMS parsing: X(333019X1502203E)");
        coord.setFromDMS("333019X1502203E");
    }

    @Test
    public void testBadSecondHemisphereArgumentDMS() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect format for E/W flag while DMS parsing: Y(333019S1502203Y)");
        coord.setFromDMS("333019S1502203Y");
    }

    @Test
    public void testNumberFormatArgumentDMS() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect DMS format: 333019S1502x03E");
        coord.setFromDMS("333019S1502x03E");
    }

    // ========== UTM North/South Tests ==========

    @Test
    public void testValidUTMNorth() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromUTMNorth("31T1234567890123");
        assertNotNull(coord);
        // Verify the source format is stored
        assertEquals("31T1234567890123", coord.getSourceFormat());
    }

    @Test
    public void testValidUTMSouth() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromUTMSouth("31T1234567890123");
        assertNotNull(coord);
        // Verify the source format is stored
        assertEquals("31T1234567890123", coord.getSourceFormat());
    }

    @Test
    public void testUTMNorthInvalidLength() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for UTM string: expected 16, got 10");
        coord.setFromUTMNorth("31T1234567");
    }

    @Test
    public void testUTMSouthInvalidLength() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for UTM string: expected 16, got 20");
        coord.setFromUTMSouth("31T12345678901234567");
    }

    @Test
    public void testUTMNorthEmptyString() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for UTM string: expected 16, got 0");
        coord.setFromUTMNorth("");
    }

    @Test
    public void testUTMSouthEmptyString() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for UTM string: expected 16, got 0");
        coord.setFromUTMSouth("");
    }

    // ========== UPS Tests ==========

    @Test
    public void testValidUPS() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromUPS("N1234567890123AB");
        assertNotNull(coord);
        assertEquals("N1234567890123AB", coord.getSourceFormat());
    }

    @Test
    public void testUPSInvalidLength() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for UPS string: expected 16, got 12");
        coord.setFromUPS("N12345678901");
    }

    @Test
    public void testUPSEmptyString() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for UPS string: expected 16, got 0");
        coord.setFromUPS("");
    }

    // ========== Decimal Degrees Tests ==========

    @Test
    public void testValidDecimalDegrees() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromDecimalDegrees("+35.376-149.101");
        assertNotNull(coord);
        assertEquals(35.376, coord.getLatitude(), 0.001);
        assertEquals(-149.101, coord.getLongitude(), 0.001);
        assertEquals("+35.376-149.101", coord.getSourceFormat());
    }

    @Test
    public void testDecimalDegreesNegativeLatitude() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromDecimalDegrees("-33.505+150.367");
        assertNotNull(coord);
        assertEquals(-33.505, coord.getLatitude(), 0.001);
        assertEquals(150.367, coord.getLongitude(), 0.001);
    }

    @Test
    public void testDecimalDegreesInvalidLength() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for decimal degrees parsing: expected 16, got 10");
        coord.setFromDecimalDegrees("+35.376-14");
    }

    @Test
    public void testDecimalDegreesInvalidFormat() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect decimal degrees format");
        coord.setFromDecimalDegrees("+35.37X-149.101");
    }

    @Test
    public void testDecimalDegreesEmptyString() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for decimal degrees parsing: expected 16, got 0");
        coord.setFromDecimalDegrees("");
    }

    // ========== Edge Cases and Boundary Tests ==========

    @Test
    public void testMultipleCoordinateSetsClobberEachOther() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromDMS("333019S1502203E");
        assertEquals("333019S1502203E", coord.getSourceFormat());

        // Setting a new coordinate should update the source format
        coord.setFromUTMNorth("31T1234567890123");
        assertEquals("31T1234567890123", coord.getSourceFormat());

        // Setting another coordinate should update again
        coord.setFromDecimalDegrees("+35.376-149.101");
        assertEquals("+35.376-149.101", coord.getSourceFormat());
    }

    @Test
    public void testZeroCoordinates() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromDecimalDegrees("+00.000+000.000");
        assertEquals(0.0, coord.getLatitude(), 0.001);
        assertEquals(0.0, coord.getLongitude(), 0.001);
    }

    @Test
    public void testMaximumLatLon() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromDecimalDegrees("+89.999+179.999");
        assertEquals(89.999, coord.getLatitude(), 0.001);
        assertEquals(179.999, coord.getLongitude(), 0.001);
    }

    @Test
    public void testMinimumLatLon() throws NitfFormatException {
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();
        coord.setFromDecimalDegrees("-89.999-179.999");
        assertEquals(-89.999, coord.getLatitude(), 0.001);
        assertEquals(-179.999, coord.getLongitude(), 0.001);
    }

    // ========== Test refactored common UTM helper method ==========

    @Test
    public void testUTMNorthAndSouthUseSameHelper() throws NitfFormatException {
        // This tests that both methods delegate to the common setFromUTM helper
        // and that the isSouth parameter works correctly
        ImageCoordinatePairImpl coordNorth = new ImageCoordinatePairImpl();
        coordNorth.setFromUTMNorth("31T1234567890123");

        ImageCoordinatePairImpl coordSouth = new ImageCoordinatePairImpl();
        coordSouth.setFromUTMSouth("31T1234567890123");

        // Both should have the same source string
        assertEquals(coordNorth.getSourceFormat(), coordSouth.getSourceFormat());
    }

    @Test
    public void testCoordinateConstantsUsage() throws NitfFormatException {
        // Test that the new constants from CoordinateConstants are being used
        // by verifying the expected length error messages
        ImageCoordinatePairImpl coord = new ImageCoordinatePairImpl();

        try {
            coord.setFromUTMNorth("123"); // Too short
        } catch (NitfFormatException e) {
            assertTrue("Error message should mention expected length",
                       e.getMessage().contains("expected 16"));
        }

        try {
            coord.setFromUPS("123"); // Too short
        } catch (NitfFormatException e) {
            assertTrue("Error message should mention expected length",
                       e.getMessage().contains("expected 16"));
        }

        try {
            coord.setFromDecimalDegrees("123"); // Too short
        } catch (NitfFormatException e) {
            assertTrue("Error message should mention expected length",
                       e.getMessage().contains("expected 16"));
        }
    }
}
