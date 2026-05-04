package eml;

public abstract class CommonRealSearch extends CommonSearch {

    // EML演算子の定義: eml(x, y) = exp(x) - ln(y)
    public static double emlEval(double left, double right) {
        if (Double.isNaN(left) || Double.isNaN(right)) return Const.NaN;

        if (!Const.ALLOW_INF) {
            // 有限実数モード: 負数やゼロの対数は許可しない
            if (!Double.isFinite(left) || !Double.isFinite(right) || right <= 0.0) {
                return Const.NaN;
            }
            double out = Math.exp(left) - Math.log(right);
            return Double.isFinite(out) ? out : Const.NaN;
        }

        // 拡張実数モード
        if (right < 0.0) return Const.NaN;

        double expLeft = (left == Double.NEGATIVE_INFINITY) ? 0.0 : Math.exp(left);
        double logRight;
        if (right == 0.0) {
            logRight = Double.NEGATIVE_INFINITY;
        } else if (Double.isInfinite(right)) {
            logRight = Double.POSITIVE_INFINITY;
        } else {
            logRight = Math.log(right);
        }

        // inf - inf は未定義(NaN)
        if (Double.isInfinite(expLeft) && Double.isInfinite(logRight) && 
            expLeft > 0 && logRight > 0) {
            return Const.NaN;
        }

        return expLeft - logRight;
    }

    public static boolean isSame(double result, double target) {
        //if (!Double.isNaN(result) && Math.abs(result - target) < Const.THRESHOLD) {
        if (Double.isFinite(result) && Math.abs(result - target) < Const.THRESHOLD) {
            return true;
        }
        return false;
    }

}
