package eml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ComplexTest {
	public static final double E = Const.E;
	public static final double PI = Const.PI;

	public static Complex log(Complex z) {
		return Complex.log(z);
	}
	public static Complex exp(Complex z) {
		return Complex.exp(z);
	}

    void assertEqualsComplex(Complex expected, Complex actual) {
        assertEquals(0.0, actual.sub(expected).abs(), 1e-10);
    }

    @Test
    void testExp() {
        assertEquals(Complex.NINF, exp(new Complex(Const.PINF, PI)));
        assertEquals(Complex.ZERO, exp(Complex.NINF));
        assertEquals(Complex.ONE, exp(Complex.ZERO));
        assertEquals(Complex.E, exp(Complex.ONE));

        assertEqualsComplex(new Complex(0.0, PI), exp(new Complex(Math.log(PI), PI/2.0)));
        assertEqualsComplex(Complex.real(-1.0), exp(new Complex(0.0, PI)));
        assertEquals(Complex.ONE.div(Complex.E), exp(Complex.real(-1.0)));

        assertEquals(Complex.NaN, exp(Complex.NaN));
        assertEquals(Complex.PINF, exp(Complex.PINF));
        assertEquals(Complex.ZERO, exp(Complex.NINF));
    }

    @Test
    void testLog() {
        assertEquals(Complex.ONE, log(Complex.E));
        assertEquals(Complex.ZERO, log(Complex.ONE));
        assertEquals(Complex.NINF, log(Complex.ZERO));
        assertEquals(new Complex(Const.PINF, PI), log(Complex.NINF));
        assertEquals(Complex.PINF, log(new Complex(Const.PINF, PI)));

        assertEquals(Complex.real(-1.0), log(Complex.ONE.div(Complex.E)));
        assertEquals(new Complex(0.0, PI), log(Complex.real(-1.0)));
        assertEquals(new Complex(Math.log(PI), PI/2.0), log(new Complex(0.0, PI)));

        assertEquals(Complex.PINF, log(Complex.PINF));
        assertEquals(new Complex(Const.PINF, PI), log(Complex.NINF));
        assertEquals(Complex.NaN, log(Complex.NaN));
    }

    @Test
    void testSub() {
    	assertEquals(Complex.PINF, Complex.ZERO.sub(Complex.NINF));
    	assertEquals(Complex.PINF, Complex.ONE.sub(Complex.NINF));
    	assertEquals(Complex.PINF, Complex.E.sub(Complex.NINF));

    	assertEquals(Complex.NINF, Complex.ZERO.sub(Complex.PINF));
    	assertEquals(Complex.NINF, Complex.ONE.sub(Complex.PINF));
    	assertEquals(Complex.NINF, Complex.E.sub(Complex.PINF));
    }

}
