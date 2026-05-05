package eml;

public class Real {

	public static double exp(double v) {
		if (v == Const.NINF) {
			return Const.ZERO;
		}
		return Math.exp(v);
	}

	public static double log(double v) {
		if (v < Const.ZERO) {
			return Const.NaN;
		} else if (v == Const.ZERO) {
			return Const.NINF;
		} else if (v == Const.PINF) {
			return Const.PINF;
		}
		return Math.log(v);
	}

	public static double sub(double x, double y) {
		return x - y;
	}

}
