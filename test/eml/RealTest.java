package eml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RealTest {
	public static final double E = Const.E;
	public static final double PI = Const.PI;

	public static double log(double v) {
		return Real.log(v);
	}
	public static double exp(double v) {
		return Real.exp(v);
	}
	public static double sub(double x, double y) {
		return Real.sub(x, y);
	}

    @Test
    void testExp() {
        assertEquals(Const.ZERO, exp(Const.NINF));
        assertEquals(Const.ONE, exp(Const.ZERO));
        assertEquals(Const.E, exp(Const.ONE));

        assertEquals(1.0/E, exp(-1));

        assertEquals(PI, exp(log(PI)));

        assertEquals(Const.NaN, exp(Const.NaN));
        assertEquals(Const.PINF, exp(Const.PINF));
        assertEquals(Const.ZERO, exp(Const.NINF));
    }

	@Test
    void testLog() {
        assertEquals(Const.ONE, log(Const.E));
        assertEquals(Const.ZERO, log(Const.ONE));
        assertEquals(Const.NINF, log(Const.ZERO));

        assertEquals(-1, log(1.0/E));
        assertEquals(Const.NaN, log(-1));

        assertEquals(Const.PINF, log(Const.PINF));
        assertEquals(Const.NaN, log(Const.NINF));
        assertEquals(Const.NaN, log(Const.NaN));
    }

    @Test
    void testSub() {
    	assertEquals(Const.PINF, sub(Const.ZERO, Const.NINF));
    	assertEquals(Const.PINF, sub(Const.ONE, Const.NINF));
    	assertEquals(Const.PINF, sub(Const.E, Const.NINF));

    	assertEquals(Const.NINF, sub(Const.ZERO, Const.PINF));
    	assertEquals(Const.NINF, sub(Const.ONE, Const.PINF));
    	assertEquals(Const.NINF, sub(Const.E, Const.PINF));
    }

}
