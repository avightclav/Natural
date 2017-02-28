import com.sun.xml.internal.bind.v2.TODO;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;

import static java.lang.Math.negateExact;
import static java.lang.Math.pow;

/**
 * Created by avigh_000 on 2/14/2017.
 */
public class Natural implements Comparable {
    private static final int NUMERALS_IN_CELL = 5;

    private final int[] mag;

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
        this(Integer.toString(Int));
    }

    private Natural(int[] mag) {
        this.mag = mag;
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
            int[] exResult = new int[result.length + 1];
            System.arraycopy(result, 0, exResult, 1, result.length);
            exResult[0] = 1;
            return exResult;
        }

        return result;

    }

    public Natural minus(Natural other) {
        if (this.mag[0] == 0) throw new IllegalArgumentException("The result is \n -" + other.toString() + "\n" +
                "but I can't store it \n");
        int cmp = this.compareTo(other);
        if (cmp == 0) return new Natural(0);
        if (cmp > 0) return new Natural(minus(this.mag, other.mag));
        else throw new IllegalArgumentException();
    }

    private static int[] minus(int[] big, int[] little) {
        int bigIndex = big.length;
        int littleIndex = little.length;
        int[] result = new int[bigIndex];
        int difference = 0;
        final int borrow = (int) pow(10.0, NUMERALS_IN_CELL);
        boolean isMore = true;

        while (littleIndex > 0) {
            if (!isMore) big[bigIndex - 1] -= 1;
            isMore = big[--bigIndex] >= little[--littleIndex];
            difference = (isMore ? 0 : borrow) + big[bigIndex] - little[littleIndex];
            result[bigIndex] = difference;
        }

        while (bigIndex > 0 && !isMore)
            isMore = !((result[--bigIndex] = big[bigIndex] - 1) == -1);

        while (bigIndex > 0)
            result[--bigIndex] = big[bigIndex];

        return deleteZeros(result);
    }

    private static int[] deleteZeros(int[] arr) {
        if (arr[0] == 0 && arr.length > 0) {
            int i = 0;
            while (arr[i] == 0) i++;

            int len = arr.length - i;
            int[] result = new int[len];

            System.arraycopy(arr, i, result, 0, len);

            return result;
        } else return arr;
    }

    @Override
    public String toString() {

        if (mag == null)
            return "null";
        int endPoint = mag.length - 1;
        if (endPoint == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        boolean first = true;

        for (int f : mag) {
            b.append(f);
            if (Integer.toString(f).length() != NUMERALS_IN_CELL && !first) {
                for (int zeroCounts = NUMERALS_IN_CELL - Integer.toString(f).length();
                     zeroCounts > 0; zeroCounts--) {
                    b.append("0");
                }
            }
            first = false;
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

    @Override
    public int compareTo(Object o) {
        if (this.equals(o)) return 0;
        if (o instanceof Natural) {
            int lenN = this.mag.length;
            int lenO = ((Natural) o).mag.length;
            if (lenN > lenO) return 1;
            else if (lenN < lenO) return -1;
            else {
                int i = 0;
                while (this.mag[i] == ((Natural) o).mag[i]) i++;
                if (this.mag[i] > ((Natural) o).mag[i]) return 1; else return -1;
            }
        } else throw new IllegalArgumentException();
    }
}
