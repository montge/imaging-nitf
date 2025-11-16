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
package org.codice.imaging.nitf.core.common.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.codice.imaging.nitf.core.common.DateTime;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Tests for DateTimeParser class
 */
public class DateTimeParserTest {
    TestLogger logger = TestLoggerFactory.getTestLogger(DateTimeParser.class);

    public DateTimeParserTest() {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDateParsingWithoutReaderVersion() throws NitfFormatException {
        AbstractSegmentParser parser = mock(AbstractSegmentParser.class, CALLS_REAL_METHODS);
        NitfReader mockReader = mock(NitfReader.class);
        parser.reader = mockReader;
        when(mockReader.getFileType()).thenReturn(FileType.UNKNOWN);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Need to set NITF file type prior to reading dates");
        DateTime date = parser.readNitfDateTime();

    }

    // Test when we have yyyyMMddHH----
    @Test
    public void testPaddedDateHourParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("2014070423----");
        ZonedDateTime expectedDate = ZonedDateTime.of(2014, 7, 4, 23, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    // Test when we have yyyyMMdd------
    @Test
    public void testPaddedDateParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20140704------");
        ZonedDateTime expectedDate = ZonedDateTime.of(2014, 7, 4, 0, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    // Test when we only have yyyyMMdd. Some commercial producers do this, although it should be yyyyMMdd------.
    @Test
    public void testDateOnlyParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20140704");
        ZonedDateTime expectedDate = ZonedDateTime.of(2014, 7, 4, 0, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    // Test when we only have all -.
    @Test
    public void testEmptyParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("--------------");
        DateTimeParser parser = new DateTimeParser();
        DateTime ndt = parser.readNitfDateTime(mockReader);
        assertNull(ndt.getZonedDateTime());
        assertEquals("--------------", ndt.getSourceString());
    }

    // Test incomplete format.
    @Test
    public void testIncompleteParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("3");
        DateTimeParser parser = new DateTimeParser();
        DateTime ndt = parser.readNitfDateTime(mockReader);
        assertNull(ndt.getZonedDateTime());
        assertEquals("3", ndt.getSourceString());
    }

    // Test when we have all - markers in NITF 2.0 .
    @Test
    public void testEmptyParsing20() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ZERO);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("--------------");
        DateTimeParser parser = new DateTimeParser();
        DateTime ndt = parser.readNitfDateTime(mockReader);
        assertNull(ndt.getZonedDateTime());
        assertEquals("--------------", ndt.getSourceString());
    }

    // Test when we have just the Z marker in NITF 2.0 .
    @Test
    public void testEmptyZParsing20() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ZERO);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("--------Z-----");
        DateTimeParser parser = new DateTimeParser();
        assertNull(parser.readNitfDateTime(mockReader).getZonedDateTime());
        assertEquals("--------Z-----", parser.readNitfDateTime(mockReader).getSourceString());
    }

    // Test when we have just spaces in NITF 2.0 .
    @Test
    public void testEmptySpaceParsing20() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ZERO);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("              ");
        DateTimeParser parser = new DateTimeParser();
        assertNull(parser.readNitfDateTime(mockReader).getZonedDateTime());
        assertEquals("              ", parser.readNitfDateTime(mockReader).getSourceString());
    }

    // ========== Edge Case Tests ==========

    @Test
    public void testFullDateTimeParsing() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20230915143025");
        ZonedDateTime expectedDate = ZonedDateTime.of(2023, 9, 15, 14, 30, 25, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testNewYearsDay() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20250101000000");
        ZonedDateTime expectedDate = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testNewYearsEve() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20241231235959");
        ZonedDateTime expectedDate = ZonedDateTime.of(2024, 12, 31, 23, 59, 59, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testLeapYearFebruary29() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20240229120000");
        ZonedDateTime expectedDate = ZonedDateTime.of(2024, 2, 29, 12, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testMidnightTransition() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20230630235959");
        ZonedDateTime expectedDate = ZonedDateTime.of(2023, 6, 30, 23, 59, 59, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testMorningMidnight() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20230701000000");
        ZonedDateTime expectedDate = ZonedDateTime.of(2023, 7, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testY2KDate() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20000101000000");
        ZonedDateTime expectedDate = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testMixedPaddingDate() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20140704231530");
        ZonedDateTime expectedDate = ZonedDateTime.of(2014, 7, 4, 23, 15, 30, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testPartialTimeWithDashes() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("201407042315--");
        ZonedDateTime expectedDate = ZonedDateTime.of(2014, 7, 4, 23, 15, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testDecemberDate() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20231215120000");
        ZonedDateTime expectedDate = ZonedDateTime.of(2023, 12, 15, 12, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testJanuaryDate() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20230105060708");
        ZonedDateTime expectedDate = ZonedDateTime.of(2023, 1, 5, 6, 7, 8, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testEarlyMorningHour() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20230715010203");
        ZonedDateTime expectedDate = ZonedDateTime.of(2023, 7, 15, 1, 2, 3, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testNoonTime() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("20230715120000");
        ZonedDateTime expectedDate = ZonedDateTime.of(2023, 7, 15, 12, 0, 0, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testNitf20FullDateTime() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ZERO);
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn("19991231235959");
        ZonedDateTime expectedDate = ZonedDateTime.of(1999, 12, 31, 23, 59, 59, 0, ZoneId.of("UTC"));
        DateTimeParser parser = new DateTimeParser();
        assertEquals(expectedDate, parser.readNitfDateTime(mockReader).getZonedDateTime());
    }

    @Test
    public void testSourceStringPreservation() throws NitfFormatException {
        NitfReader mockReader = mock(NitfReader.class);
        when(mockReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);
        String testDateString = "20230915143025";
        when(mockReader.readBytes(CommonConstants.STANDARD_DATE_TIME_LENGTH)).thenReturn(testDateString);
        DateTimeParser parser = new DateTimeParser();
        DateTime result = parser.readNitfDateTime(mockReader);
        assertEquals(testDateString, result.getSourceString());
    }
}
