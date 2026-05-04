package eml;

public abstract class CommonComplexSearch extends CommonSearch {

    // EML演算子の定義: eml(x, y) = exp(x) - ln(y)
    public static Complex emlEval(Complex left, Complex right) {
        if (left.isNaN() || right.isNaN()) return Complex.NaN;

        if (!Const.ALLOW_INF) {
            // 有限実数モード: 負数やゼロの対数は許可しない
            if (!left.isFinite() || !right.isFinite() || (right.equals(Complex.ZERO))) {
                return Complex.NaN;
            }
        }

        // 無限大を許容する場合の評価
        Complex expLeft = Complex.exp(left);
        Complex logRight = Complex.log(right);

        // INF - INF は未定義(NaN)とする (実数版のロジックに準拠)
        if (Double.isInfinite(expLeft.re) && Double.isInfinite(logRight.re) &&
            Math.signum(expLeft.re) == Math.signum(logRight.re)) {
            return Complex.NaN;
        }
        return expLeft.sub(logRight);
    }

    public static boolean isSame(Complex result, Complex target) {
        if (result.isFinite() && result.sub(target).abs() < Const.THRESHOLD) {
            return true;
        }
        return false;
    }

}
