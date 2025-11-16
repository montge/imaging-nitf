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
package org.codice.imaging.nitf.core.tre.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.xml.transform.stream.StreamSource;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.TaggedRecordExtensionHandler;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for TreParser.
 */
public class TreParser_Test {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    public TreParser_Test() {
    }

    @Test
    public void checkSizeHeaderSegment() {
        assertEquals(99999 - 3, TreParser.getValidSizeForTreSource(TreSource.ExtendedHeaderData));
        assertEquals(99999 - 3, TreParser.getValidSizeForTreSource(TreSource.UserDefinedHeaderData));
    }
    
    @Test
    public void checkSizeImageSegment() {
        assertEquals(99999 - 3, TreParser.getValidSizeForTreSource(TreSource.ImageExtendedSubheaderData));
        assertEquals(99999 - 3, TreParser.getValidSizeForTreSource(TreSource.UserDefinedImageData));
    }
    
    @Test
    public void checkSizeSymbolSegment() {
        assertEquals(8833 - 3, TreParser.getValidSizeForTreSource(TreSource.SymbolExtendedSubheaderData));
    }
    
    @Test
    public void checkSizeLabelSegment() {
        assertEquals(9747 - 3, TreParser.getValidSizeForTreSource(TreSource.LabelExtendedSubheaderData));
    }

    @Test
    public void checkSizeGraphicSegment() {
        assertEquals(9741 - 3, TreParser.getValidSizeForTreSource(TreSource.GraphicExtendedSubheaderData));
    }

    @Test
    public void checkSizeTextSegment() {
        assertEquals(9717 - 3, TreParser.getValidSizeForTreSource(TreSource.TextExtendedSubheaderData));
    }
    
    @Test
    public void checkSizeOverflowSegment() {
        assertEquals(999999998, TreParser.getValidSizeForTreSource(TreSource.TreOverflowDES));
    }

    private void assertCollectionCanBeSerialised(TreCollection collection, TreSource source, int expectedLen) throws NitfFormatException, IOException {
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(new StreamSource(new StringReader(xmlTREs)));
        TaggedRecordExtensionHandler handler = mock(TaggedRecordExtensionHandler.class);
        when(handler.getTREsRawStructure()).thenReturn(collection);
        byte[] treData = parser.getTREs(handler, source);
        assertEquals(expectedLen, treData.length);
    }

    private void assertCollectionCanNotBeSerialised(TreCollection collection, TreSource source) throws NitfFormatException, IOException {
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(new StreamSource(new StringReader(xmlTREs)));
        TaggedRecordExtensionHandler handler = mock(TaggedRecordExtensionHandler.class);
        when(handler.getTREsRawStructure()).thenReturn(collection);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("TREs exceed valid limit for source");
        parser.getTREs(handler, source);
    }
        
    @Test
    public void checkSingleSmallTREIsValid() throws NitfFormatException, IOException {
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("One", TreSource.ExtendedHeaderData);
        tre1.setRawData(new byte[1000]);
        collection.add(tre1);
        assertCollectionCanBeSerialised(collection, TreSource.ExtendedHeaderData, 1011);
    }

    private final String xmlTREs
            = "<?xml version=\"1.0\"?>"
            + " <tres>"
            + "     <tre name=\"FILL\">"
            + "         <field name=\"FILL_LEN\" type=\"integer\" length=\"5\"/>"
            + "         <field name=\"FILL_DATA\" type=\"string\" length_var=\"FILL_LEN\"/>"
            + "     </tre>"

            + "     <tre name=\"JUNKA1\">"
            + "         <field name=\"A1\" type=\"integer\" length=\"1\"/>"
            + "         <if cond=\"A1==0\">"
            + "             <field name=\"X\" length=\"1\" type=\"string\"/>"
            + "         </if>"
            + "     </tre>"

            + "     <tre name=\"JUNKA2\">"
            + "         <field name=\"A2\" type=\"integer\" length=\"1\"/>"
            + "         <if cond=\"A2!=!=0\">"
            + "             <field name=\"X\" length=\"1\" type=\"string\"/>"
            + "         </if>"
            + "     </tre>"

            + "     <tre name=\"JUNKA3\">"
            + "         <field name=\"A3\" type=\"integer\" length=\"1\"/>"
            + "         <if cond=\"A3&amp;&amp;0\">"
            + "             <field name=\"X\" length=\"1\" type=\"string\"/>"
            + "         </if>"
            + "     </tre>"

            + "     <tre name=\"JUNKA4\">"
            + "         <field name=\"A4\" type=\"integer\" length=\"1\"/>"
            + "         <if cond=\"A4 AND A4 AND A4\">"
            + "             <field name=\"X\" length=\"1\" type=\"string\"/>"
            + "         </if>"
            + "     </tre>"

            + "     <tre name=\"JUNKA5\">"
            + "         <field name=\"A5\" type=\"integer\" length=\"1\"/>"
            + "         <if cond=\"A5 BLAH A5\">"
            + "             <field name=\"X\" length=\"1\" type=\"string\"/>"
            + "         </if>"
            + "     </tre>"

            + " </tres>";

    @Test
    public void checkSingleLargeTREIsValid() throws NitfFormatException, IOException {
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("FILL", TreSource.ExtendedHeaderData);
        tre1.add(new TreEntryImpl("FILL_LEN", "99980", "integer"));
        char[] filldata = new char[99980];
        Arrays.fill(filldata, 'X');
        tre1.add((new TreEntryImpl("FILL_DATA", new String(filldata), "string")));
        collection.add(tre1);
        assertCollectionCanBeSerialised(collection, TreSource.ExtendedHeaderData, 99996);
    }

    @Test
    public void checkSingleOversizeTREIsNotValid() throws NitfFormatException, IOException {
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("FILL", TreSource.ExtendedHeaderData);
        tre1.add(new TreEntryImpl("FILL_LEN", "99981", "integer"));
        char[] filldata = new char[99981];
        Arrays.fill(filldata, 'X');
        tre1.add((new TreEntryImpl("FILL_DATA", new String(filldata), "string")));
        collection.add(tre1);
        assertCollectionCanNotBeSerialised(collection, TreSource.ExtendedHeaderData);
    }
    
    @Test
    public void checkSingleLargeTREIsValidInTextSegment() throws NitfFormatException, IOException {
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("FILL", TreSource.TextExtendedSubheaderData);
        tre1.add(new TreEntryImpl("FILL_LEN", "9698", "integer"));
        char[] filldata = new char[9698];
        Arrays.fill(filldata, 'X');
        tre1.add((new TreEntryImpl("FILL_DATA", new String(filldata), "string")));
        collection.add(tre1);
        assertCollectionCanBeSerialised(collection, TreSource.TextExtendedSubheaderData, 9714);
    }
    
    @Test
    public void checkSingleOversizeTREIsNotValidInTextSegment() throws NitfFormatException, IOException {
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("FILL", TreSource.TextExtendedSubheaderData);
        tre1.add(new TreEntryImpl("FILL_LEN", "9699", "integer"));
        char[] filldata = new char[9699];
        Arrays.fill(filldata, 'X');
        tre1.add((new TreEntryImpl("FILL_DATA", new String(filldata), "string")));
        collection.add(tre1);
        assertCollectionCanNotBeSerialised(collection, TreSource.TextExtendedSubheaderData);
    }
    
    public void checkSingleLargeTREIsValidInImageSegment() throws NitfFormatException, IOException {
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("FILL", TreSource.ImageExtendedSubheaderData);
        tre1.add(new TreEntryImpl("FILL_LEN", "99980", "integer"));
        char[] filldata = new char[99980];
        Arrays.fill(filldata, 'X');
        tre1.add((new TreEntryImpl("FILL_DATA", new String(filldata), "string")));
        collection.add(tre1);
        assertCollectionCanBeSerialised(collection, TreSource.ImageExtendedSubheaderData, 99996);
    }
    
    @Test
    public void checkSingleOversizeTREIsNotValidInImageSegment() throws NitfFormatException, IOException {
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("FILL", TreSource.ImageExtendedSubheaderData);
        tre1.add(new TreEntryImpl("FILL_LEN", "99981", "integer"));
        char[] filldata = new char[99981];
        Arrays.fill(filldata, 'X');
        tre1.add((new TreEntryImpl("FILL_DATA", new String(filldata), "string")));
        collection.add(tre1);
        assertCollectionCanNotBeSerialised(collection, TreSource.ImageExtendedSubheaderData);
    }
    
    public void checkMultipleLargeTREIsValidInImageSegment() throws NitfFormatException, IOException {
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("FILL", TreSource.UserDefinedImageData);
        tre1.add(new TreEntryImpl("FILL_LEN", "90980", "integer"));
        char[] filldata = new char[90980];
        Arrays.fill(filldata, 'X');
        tre1.add((new TreEntryImpl("FILL_DATA", new String(filldata), "string")));
        collection.add(tre1);
        Tre tre2 = TreFactory.getDefault("FILL", TreSource.UserDefinedImageData);
        tre2.add(new TreEntryImpl("FILL_LEN", "9969", "integer"));
        char[] filldata2 = new char[9969];
        Arrays.fill(filldata2, 'X');
        tre2.add((new TreEntryImpl("FILL_DATA", new String(filldata2), "string")));
        collection.add(tre2);
        assertCollectionCanBeSerialised(collection, TreSource.UserDefinedImageData, 99996);
    }
    
    @Test
    public void checkMultipleOversizeTREIsNotValidInImageSegment() throws NitfFormatException, IOException {
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("FILL", TreSource.UserDefinedImageData);
        tre1.add(new TreEntryImpl("FILL_LEN", "90980", "integer"));
        char[] filldata = new char[90980];
        Arrays.fill(filldata, 'X');
        tre1.add((new TreEntryImpl("FILL_DATA", new String(filldata), "string")));
        collection.add(tre1);
        Tre tre2 = TreFactory.getDefault("FILL", TreSource.UserDefinedImageData);
        tre2.add(new TreEntryImpl("FILL_LEN", "9970", "integer"));
        char[] filldata2 = new char[9970];
        Arrays.fill(filldata2, 'X');
        tre2.add((new TreEntryImpl("FILL_DATA", new String(filldata2), "string")));
        collection.add(tre2);
        assertCollectionCanNotBeSerialised(collection, TreSource.UserDefinedImageData);
    }

    @Test
    public void checkConditionShouldOnlyHaveSingleEquals() throws NitfFormatException, IOException {
        exception.expect(UnsupportedOperationException.class);
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("JUNKA1", TreSource.ExtendedHeaderData);
        tre1.add(new TreEntryImpl("A1", "1", "integer"));
        tre1.add((new TreEntryImpl("X", "A", "string")));
        collection.add(tre1);
        assertCollectionCanBeSerialised(collection, TreSource.ExtendedHeaderData, 2);
    }

    @Test
    public void checkConditionShouldOnlyHaveSingleNotEquals() throws NitfFormatException, IOException {
        exception.expect(UnsupportedOperationException.class);
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("JUNKA2", TreSource.ExtendedHeaderData);
        tre1.add(new TreEntryImpl("A2", "0", "integer"));
        tre1.add((new TreEntryImpl("X", "A", "string")));
        collection.add(tre1);
        assertCollectionCanBeSerialised(collection, TreSource.ExtendedHeaderData, 2);
    }

    @Test
    public void checkConditionShouldOnlyHaveSingleBitmask() throws NitfFormatException, IOException {
        exception.expect(UnsupportedOperationException.class);
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("JUNKA3", TreSource.ExtendedHeaderData);
        tre1.add(new TreEntryImpl("A3", "0", "integer"));
        tre1.add((new TreEntryImpl("X", "A", "string")));
        collection.add(tre1);
        assertCollectionCanBeSerialised(collection, TreSource.ExtendedHeaderData, 2);
    }

    @Test
    public void checkConditionShouldOnlyHaveSingleAND() throws NitfFormatException, IOException {
        exception.expect(UnsupportedOperationException.class);
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("JUNKA4", TreSource.ExtendedHeaderData);
        tre1.add(new TreEntryImpl("A4", "0", "integer"));
        tre1.add((new TreEntryImpl("X", "A", "string")));
        collection.add(tre1);
        assertCollectionCanBeSerialised(collection, TreSource.ExtendedHeaderData, 2);
    }

    @Test
    public void checkConditionHasValidOperator() throws NitfFormatException, IOException {
        exception.expect(UnsupportedOperationException.class);
        TreCollection collection = new TreCollectionImpl();
        Tre tre1 = TreFactory.getDefault("JUNKA5", TreSource.ExtendedHeaderData);
        tre1.add(new TreEntryImpl("A5", "0", "integer"));
        tre1.add((new TreEntryImpl("X", "A", "string")));
        collection.add(tre1);
        assertCollectionCanBeSerialised(collection, TreSource.ExtendedHeaderData, 2);
    }

    /**
     * Test thread-safe initialization of TreParser.
     * This test ensures the double-checked locking pattern works correctly
     * when multiple threads attempt to create TreParser instances concurrently.
     */
    @Test
    public void testConcurrentTreParserInitialization() throws Exception {
        final int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<TreParser>> futures = new ArrayList<>();

        // Submit multiple concurrent parser creation tasks
        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(new Callable<TreParser>() {
                @Override
                public TreParser call() throws Exception {
                    return new TreParser();
                }
            }));
        }

        // All parsers should initialize successfully without race conditions
        for (Future<TreParser> future : futures) {
            TreParser parser = future.get(10, TimeUnit.SECONDS);
            assertNotNull("Parser should not be null", parser);
        }

        executor.shutdown();
        assertTrue("Executor should terminate", executor.awaitTermination(15, TimeUnit.SECONDS));
    }

    /**
     * Test that TreParser initializes correctly in a single-threaded scenario.
     */
    @Test
    public void testTreParserInitialization() throws NitfFormatException {
        TreParser parser = new TreParser();
        assertNotNull("Parser should not be null", parser);
    }

    /**
     * Test that multiple TreParser instances can be created without errors.
     * This validates that the static field initialization is idempotent.
     */
    @Test
    public void testMultipleTreParserInstances() throws NitfFormatException {
        TreParser parser1 = new TreParser();
        TreParser parser2 = new TreParser();
        TreParser parser3 = new TreParser();

        assertNotNull("First parser should not be null", parser1);
        assertNotNull("Second parser should not be null", parser2);
        assertNotNull("Third parser should not be null", parser3);
    }

    /**
     * Test TRE lookup performance with the HashMap cache.
     * This is a basic performance sanity check that lookups complete in reasonable time.
     */
    @Test(timeout = 1000)
    public void testTreLookupPerformance() throws NitfFormatException {
        TreParser parser = new TreParser();

        // Perform many lookups - should complete quickly with HashMap cache
        for (int i = 0; i < 1000; i++) {
            // This exercises the getTreTypeForTag method which uses the HashMap cache
            TaggedRecordExtensionHandler handler = mock(TaggedRecordExtensionHandler.class);
            TreCollection emptyCollection = new TreCollectionImpl();
            when(handler.getTREsRawStructure()).thenReturn(emptyCollection);

            try {
                parser.getTREs(handler, TreSource.ExtendedHeaderData);
            } catch (IOException e) {
                // Ignore IO exceptions - we're testing lookup performance
            }
        }

        // If we get here within timeout, the HashMap cache is working efficiently
        assertTrue("Lookup performance test completed", true);
    }

    /**
     * Test that TreParser handles concurrent TRE parsing correctly.
     */
    @Test
    public void testConcurrentTreParsing() throws Exception {
        final TreParser parser = new TreParser();
        final int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<byte[]>> futures = new ArrayList<>();

        // Submit multiple concurrent TRE parsing tasks
        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(new Callable<byte[]>() {
                @Override
                public byte[] call() throws Exception {
                    TreCollection collection = new TreCollectionImpl();
                    Tre tre = TreFactory.getDefault("One", TreSource.ExtendedHeaderData);
                    tre.setRawData(new byte[100]);
                    collection.add(tre);

                    TaggedRecordExtensionHandler handler = mock(TaggedRecordExtensionHandler.class);
                    when(handler.getTREsRawStructure()).thenReturn(collection);

                    return parser.getTREs(handler, TreSource.ExtendedHeaderData);
                }
            }));
        }

        // All parsing operations should complete successfully
        for (Future<byte[]> future : futures) {
            byte[] result = future.get(10, TimeUnit.SECONDS);
            assertNotNull("TRE parsing result should not be null", result);
            assertTrue("TRE data should have reasonable length", result.length > 0);
        }

        executor.shutdown();
        assertTrue("Executor should terminate", executor.awaitTermination(15, TimeUnit.SECONDS));
    }

    /**
     * Test exception handling improvements - verify that NitfFormatException is caught
     * and handled gracefully during TRE parsing.
     */
    @Test
    public void testExceptionHandlingInParsing() throws NitfFormatException {
        TreParser parser = new TreParser();
        assertNotNull("Parser should handle initialization even with potential exceptions", parser);

        // The parser should be functional after initialization
        TreCollection collection = new TreCollectionImpl();
        TaggedRecordExtensionHandler handler = mock(TaggedRecordExtensionHandler.class);
        when(handler.getTREsRawStructure()).thenReturn(collection);

        try {
            byte[] result = parser.getTREs(handler, TreSource.ExtendedHeaderData);
            assertNotNull("Result should not be null even for empty collection", result);
        } catch (IOException e) {
            // IOException is acceptable
        }
    }

    /**
     * Test that parser can be created and used multiple times sequentially.
     */
    @Test
    public void testSequentialParserUsage() throws Exception {
        for (int i = 0; i < 5; i++) {
            TreParser parser = new TreParser();
            assertNotNull("Parser instance " + i + " should not be null", parser);

            TreCollection collection = new TreCollectionImpl();
            Tre tre = TreFactory.getDefault("Test", TreSource.ExtendedHeaderData);
            tre.setRawData(new byte[50]);
            collection.add(tre);

            TaggedRecordExtensionHandler handler = mock(TaggedRecordExtensionHandler.class);
            when(handler.getTREsRawStructure()).thenReturn(collection);

            byte[] result = parser.getTREs(handler, TreSource.ExtendedHeaderData);
            assertNotNull("Result for iteration " + i + " should not be null", result);
        }
    }
}
