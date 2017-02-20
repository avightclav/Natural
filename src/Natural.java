import com.sun.xml.internal.bind.v2.TODO;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;

import static java.lang.Math.pow;

/**
 * Created by avigh_000 on 2/14/2017.
 */
public class Natural {
    private static final int NUMERALS_IN_CELL = 5;

    final int[] mag;

    public Natural(String s) {
        final int len = s.length();
        int cursor = 0;

        if (cursor == len)
            throw new NumberFormatException("Zero length BigInteger");

        while (cursor < len && s.charAt(cursor) == '0') {
            cursor++;
        }

        if (cursor == len) {
            mag = new int[1];
            mag[0] = 0;
            return;
        }

        final int numDigits = len - cursor;

        if (numDigits <= NUMERALS_IN_CELL) {
            mag = new int[1];
            mag[0] = Integer.parseInt(s.substring(cursor, len));
            return;
        }

        final int arrLength = (numDigits) / NUMERALS_IN_CELL + ((numDigits) % NUMERALS_IN_CELL == 0 ? 0 : 1);

        int[] magnitude = new int[arrLength];

        int firstGroupLen = numDigits % NUMERALS_IN_CELL;
        if (firstGroupLen == 0)
            firstGroupLen = NUMERALS_IN_CELL;
        int cell = Integer.parseInt(s.substring(cursor, cursor += firstGroupLen));
        magnitude[0] = cell;

        for (int i = 1; i < arrLength; i++) {
            if (cursor + NUMERALS_IN_CELL < len) {
                cell = Integer.parseInt(s.substring(cursor, cursor += NUMERALS_IN_CELL));
                magnitude[i] = cell;

            } else {
                cell = Integer.parseInt(s.substring(cursor));
                magnitude[i] = cell;
            }
        }

        mag = magnitude;
    }

    public Natural(int Int) {
        mag = new Natural(Integer.toString(Int)).mag;
    }

    public Natural plus(Natural other) {
        return new Natural(plus(mag, other.mag));
    }

    private static int[] plus(int[] x, int[] y) {
        if (x.length < y.length) {
            int[] tmp = x;
            x = y;
            y = tmp;
        }

        int xIndex = x.length;
        int yIndex = y.length;
        int[] result = new int[xIndex];
        final int div = (int) pow(10.0, NUMERALS_IN_CELL);
        int sum = 0;
        while (yIndex > 0) {
            sum = x[--xIndex] + y[--yIndex] + sum / div;
            result[xIndex] = sum % div;
        }

        while ((xIndex > 0) && (sum / div > 0)) {
            result[--xIndex] = x[xIndex] + sum / div;
        }
        while (xIndex > 0) {
            result[--xIndex] = x[xIndex];
        }

        if (sum / div > 0) {
            int[] exResult = new int[result.length+1];
            System.arraycopy(result, 0, exResult, 1, result.length);
            exResult[0] = 1;
            return exResult;
        }

        return result;

    }

    private Natural(int[] mag) {
        this.mag = mag;
    }

    @Override
    public String toString() {

        if (mag == null)
            return "null";
        int endPoint = mag.length - 1;
        if (endPoint == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        for (int f: mag) {
            b.append(f);
        }
        return b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Natural natural = (Natural) o;

        return Arrays.equals(mag, natural.mag);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mag) * 67;
    }

}
