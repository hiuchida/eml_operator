package eml;

public abstract class CommonComplexSearch extends CommonSearch {

    // EML演算子の定義: eml(x, y) = exp(x) - ln(y)
    public static Complex emlEval(Complex left, Complex right) {
        if (!left.isFinite() || !right.isFinite() || (right.re == 0 && right.im == 0)) {
            return Complex.NaN;
        }
        return Complex.exp(left).sub(Complex.log(right));
    }

}
