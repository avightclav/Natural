import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by avigh_000 on 2/16/2017.
 */
class NaturalTest {

    @Test
    void generateFromString() {
        assertEquals("99900000000000", new Natural("99900000000000").toString());
        assertEquals("1234567890", new Natural("1234567890").toString());
        assertEquals("1234567890", new Natural("0000001234567890").toString());
        assertEquals("123456789033", new Natural("123456789033").toString());
        assertThrows(NumberFormatException.class, () -> new Natural("890s081"));
    }

    @Test
    void generateFromInt() {
        assertEquals("26748", new Natural(26748).toString());
        assertEquals("267489", new Natural(267489).toString());
    }

    @Test
    void plusNatural() {
        assertEquals(new Natural("19999999998"), new Natural("9999999999").plus(new Natural("9999999999")));
        assertEquals(new Natural("39999999996"), new Natural("19999999998").plus(new Natural("19999999998")));
        assertEquals(new Natural("3"), new Natural("1").plus(new Natural(2)));
    }

    @Test
    void minus() {
        assertEquals(new Natural("1"), new Natural("123456789987654321").minus(new Natural("123456789987654320")));
        assertEquals(new Natural("99900000000000"), new Natural("99999999999999").minus(new Natural("99999999999")));
        assertEquals(new Natural("88888888888"), new Natural("99999999999").minus(new Natural("11111111111")));
        assertThrows(IllegalArgumentException.class, () -> new Natural("0").minus(new Natural("11111111111")));
    }

    @Test
    void NaturalToString() {
        assertEquals("45678912343590235902352345", new Natural("45678912343590235902352345").toString());
    }

    @Test
    void isEqual() {
        assertEquals(true, new Natural(45799543).equals(new Natural("45799543")));
        assertEquals(true, new Natural("34212572958265972385010").equals(new Natural("34212572958265972385010")));
        assertEquals(false, new Natural("115234523456734").equals("23124456765431"));
    }

    @Test
    void compareTo() {
        assertEquals(1, new Natural("123456789987654321").compareTo(new Natural("123456789987654320")));
        assertEquals(1, new Natural("999999999999999999999999999999").compareTo(new Natural("1111111111111")));
        assertEquals(-1, new Natural("15234523452345352345235")
                .compareTo(new Natural("999999999999999999999999999999999999999")));
        assertEquals(0, new Natural("999999999999999999999999999999")
                .compareTo(new Natural("999999999999999999999999999999")));
        assertEquals(1, new Natural("999999999999999999999999999999")
                .compareTo(new Natural("129999999999999999999999999999")));
        assertEquals(-1, new Natural("899999999999999999999999999999")
                .compareTo(new Natural("929999999999999999999999999999")));
    }

    @Test
    void multiply() {
        assertEquals("9999961711036578", new Natural("9999981711").multiply(new Natural(999998)).toString());
        assertEquals("999966342513078043304412675042",
                new Natural("999978171136578").multiply(new Natural("999988171118289")).toString());
        assertEquals("999988171118289", new Natural("9999981711").multiply(new Natural(99999)).toString());
        assertEquals("9999800001", new Natural(99999).multiply(new Natural(99999)).toString());
        assertEquals("999966342513078043304412675042", new Natural("999978171136578").multiply(new Natural("999988171118289")).toString());
    }
}