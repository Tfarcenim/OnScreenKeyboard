package tfar.onscreenkeyboard;

import it.unimi.dsi.fastutil.chars.CharCharPair;

import java.util.ArrayList;
import java.util.List;

public record KeyboardLayout(List<CharCharPair> row1,
                             List<CharCharPair> row2,
                             List<CharCharPair> row3,
                             List<CharCharPair> row4
) {

    public static final KeyboardLayout QWERTY = buildQwerty();

    static KeyboardLayout buildQwerty() {

        List<CharCharPair> row1 = convertArray(new char[][]{
                new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '='},
                new char[]{'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+'},
        });


        List<CharCharPair> row2 = convertArray(new char[][]{
                new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', '\\', '`'},
                new char[]{'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}', '|', '~'}
        });


        List<CharCharPair> row3 = convertArray(new char[][]{
                new char[]{'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\'', '/'},
                new char[]{'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', ':', '\"', '?'}
        });


        List<CharCharPair> row4 = convertArray(new char[][]{
                new char[]{'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.'},
                new char[]{'Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '>'}

        });
        return new KeyboardLayout(row1,row2,row3,row4);
    }

    static List<CharCharPair> convertArray(char[][] chars) {
        if (chars.length != 2) throw new RuntimeException("Invalid height: " +chars.length);
        List<CharCharPair> charPairs = new ArrayList<>(chars[0].length);
        char[] aChar = chars[0];
        for (int i = 0; i < aChar.length; i++) {
            charPairs.add(CharCharPair.of(chars[0][i],chars[1][i]));
        }
        return charPairs;
    }

}
