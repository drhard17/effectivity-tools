package ru.yakovlev.effectivity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yakovlev.effectivity.service.EffectivityServiceImpl.parseJournalString;
import static ru.yakovlev.effectivity.service.EffectivityServiceImpl.parseTcString;
import static ru.yakovlev.effectivity.service.EffectivityServiceImpl.toJournalString;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.yakovlev.effectivity.model.Effectivity;


/**
 * Unit test for Effectivity class
 */
public class EffectivityTest {

    @Test
    public void wrongParameterTest() {
        Exception e = assertThrows(Exception.class, () -> parseTcString("abc"));
        assertEquals("Wrong TC effectivity string - abc", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
        abc
        a123
        0011; 0013-UP; 99012; 990151
        1-UP
        99001
    """ + " ")
    void wrongTcStringTest(String tcEffectivity) {
        assertThrows(Exception.class, () -> parseTcString(tcEffectivity));
        // Exception e = assertThrows(Exception.class, () -> Effectivity.parseTcString(tcEffectivity));
        // assertEquals("Wrong TC effectivity string - " + tcEffectivity, e.getMessage());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
        8-11, 13-UP (MC-21) 990151, 99012 (ST-21)   |   0008; 0009; 0010; 0011; 0013-UP; 99012; 990151
        1-3, 4 (MC-21) 99011, 99011 (ST-21)         |   0001; 0002; 0003; 0004; 99011
        1-3, 4-UP (MC-21)                           |   0001-UP
        99001 (ST-21) 1-3, 6-UP (MC-21)             |   0001; 0002; 0003; 0006-UP; 99001
        9-11 (MC-21) 13-UP (MC-21)                  |   0009; 0010; 0011; 0013-UP
    """)
    void tcToJournalConverterTest(String tcEffectivity, String journalEffectivity) {
        assertDoesNotThrow( () -> {
            Effectivity eff = parseTcString(tcEffectivity);
            assertEquals(journalEffectivity, toJournalString(eff));
        });
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
        0001;99012; 0002 0003; 0012;                |   0001; 0002; 0003; 0012; 99012
        0001; 0002; 0003-UP                         |   0001-UP
        990011; 0001; 0013-UP                       |   0001; 0013-UP; 990011
        00001; 0002; 0003; 0004;                    |   0001; 0002; 0003; 0004
        001; 0002; 0003; 0004                       |   0001; 0002; 0003; 0004
    """)
    void journalParsingTest(String source, String expected) {
        Effectivity eff = parseJournalString(source);
        assertEquals(expected, toJournalString(eff));
    }

    @Test
    void journalParsingTestWithCrlf() {
        String source = "0001; 0002\r0003;\n0004\n99012";
        String expected = "0001; 0002; 0003; 0004; 99012";
        Effectivity eff = parseJournalString(source);
        assertEquals(toJournalString(eff), expected);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
        11, 13-UP (MC-21) 990151, 99012 (ST-21)     |   11 (MC-21) 990151, 99012, 990151 (ST-21)
        99001 (ST-21) 1-3, 6-UP (MC-21)             |   1-3, 12 (MC-21) 99012 (ST-21)
        1-UP (MC-21)                                |   100-UP (MC-21)
    """)
    void containsTest(String source, String check) {
        Effectivity effSource = parseTcString(source);
        Effectivity effCheck = parseTcString(check);
        assertTrue(effSource.contains(effCheck));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
        1-3, 12 (MC-21) 99012 (ST-21)               |   11 (MC-21) 990151, 99012, 990151 (ST-21)
        1-3, 12 (MC-21) 99012 (ST-21)               |   1, 13-UP (MC-21) 99012 (ST-21)
        100-UP (MC-21)                              |   1-UP (MC-21)
    """)
    void doesNotContainTest(String source, String check) {
        Effectivity effSource = parseTcString(source);
        Effectivity effCheck = parseTcString(check);
        assertFalse(effSource.contains(effCheck));
    }
    
    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
        0001; 0002; 0003-UP                         |   0001-UP
        0003; 0002; 0001; 0001                      |   0001; 0002; 0003
        0001; 0003-UP; 0002                         |   0001-UP
    """)
    void equalsTest(String source, String check) {
        Effectivity effSource = parseJournalString(source);
        Effectivity effCheck = parseJournalString(check);
        assertTrue(effSource.equals(effCheck));
    }
    
    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
        0001; 0002; 0003-UP                         |   0001; 0002; 0003
    """)
    void doenNotEqualTest(String source, String check) {
        Effectivity effSource = parseJournalString(source);
        Effectivity effCheck = parseJournalString(check);
        assertFalse(effSource.equals(effCheck));
    }
    
}
