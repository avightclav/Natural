import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by avigh_000 on 2/16/2017.
 */
class NaturalTest {

    @Test
    void generateFromString() {
        assertArrayEquals(new int[]{12345, 67890}, new Natural("1234567890").mag);
        assertArrayEquals(new int[]{12345, 67890}, new Natural("0000001234567890").mag);
        assertArrayEquals(new int[]{12, 34567, 89033}, new Natural("123456789033").mag);
        assertThrows(NumberFormatException.class, () -> new Natural("890s081"));
    }

    @Test
    void generateFromInt() {
        assertArrayEquals(new int[]{26748}, new Natural(26748).mag);
        assertArrayEquals(new int[]{2, 67489}, new Natural(267489).mag);
    }

    @Test
    void plusNatural() {
        assertEquals(new Natural("19999999998"), new Natural("9999999999").plus(new Natural("9999999999")));
        assertEquals(new Natural("39999999996"), new Natural("19999999998").plus(new Natural("19999999998")));
        assertEquals(new Natural("3"), new Natural("1").plus(new Natural(2)));
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
}