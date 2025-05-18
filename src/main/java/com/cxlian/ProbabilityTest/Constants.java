package com.cxlian.ProbabilityTest;

import java.util.concurrent.ThreadLocalRandom;

public final class Constants {
    private static final int[] p = new int[98];
    public static final int count = 500000;

    static {
        for (int i = 1; i <= 90; i++) {
            p[i] = to_get(i);
        }
    }

    private static int to_get(int x) {
        if (x <= 65) return 8;
        if (x <= 70) {
            return 8 + 40 * (x - 65);
        }
        if (x <= 75) {
            return 208 + 80 * (x - 70);
        }
        return 608 + 100 * (x - 75);
    }

    public static int get(int pos) {

        return p[pos];
    }
}
