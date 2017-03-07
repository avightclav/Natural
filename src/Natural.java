import java.util.Arrays;

import static java.lang.Math.pow;
import static java.lang.Math.round;

/**
 * Created by avigh_000 on 2/14/2017.
 */
public class Natural implements Comparable<Natural> {
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
            --xIndex;
            result[xIndex] = x[xIndex] + sum / div;
        }
        while (xIndex > 0) {
            --xIndex;
            result[xIndex] = x[xIndex];
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
        else
            throw new IllegalArgumentException("-" + new Natural(minus(other.mag, this.mag)).toString());
    }

    private static int[] minus(int[] big, int[] little) {
        int bigIndex = big.length;
        int littleIndex = little.length;
        int[] result = new int[bigIndex];
        int difference = 0;
        final int borrow = (int) pow(10.0, NUMERALS_IN_CELL);
        boolean isMore = true;

        while (littleIndex > 0) {
            if (!isMore) result[bigIndex - 1] -= 1;
            isMore = big[--bigIndex] >= little[--littleIndex];
            difference = (isMore ? 0 : borrow) + big[bigIndex] - little[littleIndex];
            result[bigIndex] += difference;
        }

        while (bigIndex > 0 && !isMore) {
            --bigIndex;
            isMore = !(result[bigIndex]  == -1);
        }

        while (bigIndex > 0) {
            --bigIndex;
            result[bigIndex] = big[bigIndex];
        }
        return deleteZeros(result);
    }


    private static int[] deleteZeros(int[] arr) {
        if (arr[0] == 0 && arr.length > 0) {
            int i = 0;
            while (i != arr.length && arr[i] == 0) i++;

            int len = arr.length - i;
            int[] result = new int[len];

            System.arraycopy(arr, i, result, 0, len);

            return result;
        } else return arr;
    }

    public Natural multiply(Natural other) {
        final int len = this.mag.length;
        final int lenOther = other.mag.length;

        if (len >= lenOther) return new Natural(multiply(this.mag, other.mag));
        else return new Natural(multiply(other.mag, this.mag));
    }

    private static int[] multiply(int[] x, int[] y) {
        int xIndex = x.length;
        int yIndex = y.length;
        long[] result = new long[xIndex + yIndex];
        final int div = (int) pow(10, NUMERALS_IN_CELL);
        long carry = 0;

        for (int i = yIndex - 1; i >= 0; i--) {
            xIndex = x.length;
            carry = 0;
            while (xIndex > 0) {
                int index = xIndex + i;
                result[index] += ((long) x[--xIndex]) * ((long) y[i]) + carry;
                carry = result[index] / div;
                result[index] %= div;

            }
            result[i] += carry;
        }

        int[] fResult = Arrays.stream(result).mapToInt(w -> (int) w).toArray();
        return deleteZeros(fResult);
    }

    public Natural divide(Natural other) {
        final int xIndex = this.mag.length;
        final int yIndex = other.mag.length;

        StringBuilder sb = new StringBuilder();
        int[] partArr = new int[yIndex];
        System.arraycopy(this.mag, 0, partArr, 0, yIndex);
        Natural part = new Natural(partArr);
        if (part.compareTo(other) < 0) {
            part = part.addDigit(this, yIndex);
            partArr = part.mag;
        }
        Natural remainder = new Natural(0);


        for (int i = partArr.length; i <= xIndex; ) {

            boolean isQuotient = false;
            int quotient = (int) ((pow(10, NUMERALS_IN_CELL + 1) - 1) / 2);
            double tmp = quotient;

            while (!isQuotient) {
                tmp /= 2;
                isQuotient = (part.compareTo(new Natural(quotient).multiply(other)) >= 0) &&
                        (part.minus(new Natural(quotient).multiply(other)).compareTo(other) < 0);
                if (isQuotient) {
                    sb = sb.append(quotient);
                } else {
                    if (part.compareTo(new Natural(quotient).multiply(other)) > 0) {
                        quotient += round(tmp);
                    } else {
                        quotient -= round(tmp);
                    }
                }

            }
            remainder = part.minus(new Natural(quotient).multiply(other));
            if (i == xIndex) break;
            partArr = remainder.addDigit(this, i++).mag;
            part = new Natural(partArr);
            while (part.compareTo(other) < 0) {
                remainder = part.minus(new Natural(quotient).multiply(other));
                partArr = remainder.addDigit(this, i++).mag;
                part = new Natural(partArr);
                sb = sb.append("0");
            }
        }
        if (remainder.equals(new Natural(0))) {
            return new Natural(sb.toString());
        } else {
            throw new IllegalArgumentException("Can't implement integer division \n" +
                    "but the result is: \n" +
                    new Natural(sb.toString()) + " " + remainder + "/" + other);
        }
    }

    private Natural addDigit(Natural other, int pos) {
        int[] digit = {other.mag[pos]};
        final int index = this.mag.length;
        int[] result = new int[index + 1];
        System.arraycopy(this.mag, 0, result, 0, index);
        System.arraycopy(digit, 0, result, index, 1);
        return new Natural(result);
    }

    @Override
    public String toString() {

        if (mag == null)
            return "null";
        int endPoint = mag.length - 1;
        if (endPoint == -1)
            return "[]";

        StringBuilder b = new StringBuilder();

        b.append(this.mag[0]);

        for (int i = 1; i < this.mag.length; i++) {
            int length = Integer.toString(this.mag[i]).length();
            if (length != NUMERALS_IN_CELL) {
                for (int zeroCounts = NUMERALS_IN_CELL - length;
                     zeroCounts > 0; zeroCounts--) {
                    b.append("0");
                }
            }
            b.append(this.mag[i]);
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
    public int compareTo(Natural o) {
        if (this.equals(o)) return 0;
        int lenN = this.mag.length;
        int lenO = ((Natural) o).mag.length;
        if (lenN > lenO) return 1;
        else if (lenN < lenO) return -1;
        else {
            int i = 0;
            while (this.mag[i] == ((Natural) o).mag[i]) i++;
            if (this.mag[i] > ((Natural) o).mag[i]) return 1;
            else return -1;

        }
    }
}
