package numbers;

import java.util.HashSet;
import java.util.Set;

import static numbers.Number.Properties.*;

public class Number {
    final Set<Properties> properties; // this collection is filled with the properties of the current number, defined in the enum Properties.
    final long VALUE;

    Number(long i) {
        this.VALUE = i;
        this.properties = new HashSet<>();
        isBuzz();
        isDuck();
        isPalindromic();
        isGapful();
        isSpy();
        isSquare();
        isSunny();
        isEven();
        isJumping();
        isHappy();
    }

    private void isEven() {
        if (VALUE % 2 == 0) {
            properties.add(EVEN);
        } else {
            properties.add(ODD);
        }
    }

    private void isBuzz() {
        if (endsWith7(VALUE) || isDividableBy7(VALUE)) {
            properties.add(BUZZ);
        }
    }

    private void isDuck() {
        long value = VALUE;
        do {
            if (value % 10 == 0) {
                properties.add(DUCK);
                return;
            }
            value /= 10;
        }
        while (value / 10 != 0);
    }

    private void isGapful() {
        String number = String.valueOf(VALUE);
        String digits;
        if (number.length() > 2) {
            digits = number.charAt(0) + number.substring(number.length() - 1);
            if (Long.parseLong(number) % Integer.parseInt(digits) == 0) {
                properties.add(GAPFUL);
            }
        }
    }

    private void isPalindromic() {
        String original = String.valueOf(VALUE);
        StringBuilder reversed = new StringBuilder(original).reverse();
        if (original.equals(reversed.toString())) {
            properties.add(PALINDROMIC);
        }
    }

    private void isSpy () {
        char[] number = String.valueOf(VALUE).toCharArray();
        int sum = 0;
        int product = 1;

        for (char c : number) {
            sum += Character.getNumericValue(c);
        }

        for (char c : number) {
            product *= Character.getNumericValue(c);
        }

        if (sum == product) {
            properties.add(SPY);
        }
    }

    private void isSquare() {
        if (Math.sqrt(VALUE) % 1 == 0) {
            properties.add(SQUARE);
        }
    }

    private void isSunny() {
        if (isSquare(VALUE + 1)) {
            properties.add(SUNNY);
        }
    }

    boolean isSquare(long i) {
        return Math.sqrt(i) % 1 == 0;
    }

    private void isJumping() {
        char[] number = String.valueOf(VALUE).toCharArray();
        int totalDiff = 0;
        for (int i = 1; i < number.length; i++) {
            totalDiff += Math.abs((number[i - 1] - 48) -  (number[i] - 48));
            if (totalDiff != i) {
                return;
            }
        }
        properties.add(JUMPING);
    }

    private void isHappy() {
        String number = String.valueOf(VALUE);
        Set<Integer> numbers = new HashSet<>();
        int sum = 0;

        while(sum != 1) {
            sum = 0;
            for (int i = 0; i < number.length(); i++) {
                sum += Math.pow(Character.getNumericValue(number.charAt(i)), 2);
            }
            if(numbers.contains(sum)) {
                properties.add(SAD);
                return;
            } else {
                numbers.add(sum);
                number = String.valueOf(sum);
            }
        }
        properties.add(HAPPY);
    }

    private boolean endsWith7(long i) {
        return i % 10 == 7;
    }

    private boolean isDividableBy7(long i) {
        return i % 7 == 0;
    }

    enum Properties {
        EVEN ("even", "ODD"),
        ODD("odd", "EVEN"),
        BUZZ("buzz", ""),
        DUCK("duck", "SPY"),
        PALINDROMIC("palindromic", ""),
        GAPFUL("gapful", ""),
        SPY("spy", "DUCK"),
        SQUARE("square", "SUNNY"),
        SUNNY("sunny", "SQUARE"),
        JUMPING("jumping", ""),
        HAPPY("happy", "SAD"),
        SAD("sad", "HAPPY");

        final String name;
        final String mutualExclusive;

        Properties(String name, String mutualExclusive) {
            this.name = name;
            this.mutualExclusive = mutualExclusive;
        }

    }
}
