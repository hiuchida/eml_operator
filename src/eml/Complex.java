package eml;

import java.util.Objects;

// 複素数クラスの簡易実装
public class Complex {
    public static final Complex ZERO = Complex.real(Const.ZERO);
    public static final Complex ONE = Complex.real(Const.ONE);
    public static final Complex TWO = Complex.real(Const.TWO);
    public static final Complex THREE = Complex.real(Const.THREE);
    public static final Complex FOUR = Complex.real(Const.FOUR);
    public static final Complex E = Complex.real(Const.E);
    public static final Complex PI = Complex.real(Const.PI);
    public static final Complex EulerGamma = Complex.real(Const.EulerGamma);
    public static final Complex Catalan = Complex.real(Const.Catalan);
    public static final Complex PINF = Complex.real(Const.PINF);
    public static final Complex NINF = Complex.real(Const.NINF);
    public static final Complex NaN = new Complex(Const.NaN, Const.NaN);

    public static Complex real(double r) {
        return new Complex(r, Const.ZERO);
    }

    public static Complex exp(Complex z) {
        // 1. 実数部が -INF ならば、虚数部に関わらず 0 に収束
        if (z.re == Const.NINF) {
            return Complex.ZERO; // exp(-INF) = 0
        }
        // 2. 虚数部が無限大ならば、回転が定義できないため NaN (実数部が -INF の場合を除く)
        if (Double.isInfinite(z.im)) {
            return Complex.NaN;
        }
        // 3. 実数部が +INF の場合、符号付き無限大へ発散
        if (z.re == Const.PINF) {
            double cos = Math.cos(z.im);
            double sin = Math.sin(z.im);
            // 0.0 * INF = NaN を避けるための直接代入
            double re = (Math.abs(cos) < 1e-15) ? 0.0 : (cos > 0 ? Const.PINF : Const.NINF);
            double im = (Math.abs(sin) < 1e-15) ? 0.0 : (sin > 0 ? Const.PINF : Const.NINF);
            return new Complex(re, im);
        }
        // 通常の計算
        double r = Math.exp(z.re);
        return new Complex(r * Math.cos(z.im), r * Math.sin(z.im));
    }

    public static Complex log(Complex z) {
        double r = Math.hypot(z.re, z.im);
        // 1. 0 の対数は -INF
        if (r == Const.ZERO) {
            return Complex.NINF; // ln(0) = -INF
        }
        // 2. 絶対値が無限大（re か im のどちらかが INF）の場合
        if (Double.isInfinite(r)) {
            // 実数部を +INF とし、虚数部は atan2 の極限値（pi/2, pi, 0等）を保持
            return new Complex(Const.PINF, Math.atan2(z.im, z.re));
        }
        // 通常の計算
        return new Complex(Math.log(r), Math.atan2(z.im, z.re));
    }

    public final double re;
    public final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex add(double r) {
        return new Complex(this.re + r, this.im);
    }

    public Complex add(Complex z) {
        return new Complex(this.re + z.re, this.im + z.im);
    }

    public Complex sub(double r) {
        return new Complex(this.re - r, this.im);
    }

    public Complex sub(Complex z) {
        return new Complex(this.re - z.re, this.im - z.im);
    }

    public Complex mul(double r) {
        return new Complex(this.re * r, this.im * r);
    }

    public Complex mul(Complex z) {
        return new Complex(this.re * z.re - this.im * z.im, this.re * z.im + this.im * z.re);
    }

    public Complex div(double r) {
        if (r == Const.ZERO) {
            return Complex.NaN;
        }
        return new Complex(this.re / r, this.im / r);
    }

    public Complex div(Complex z) {
        double den = z.re * z.re + z.im * z.im;
        if (den == Const.ZERO) {
            return Complex.NaN;
        }
        return new Complex((this.re * z.re + this.im * z.im) / den, (this.im * z.re - this.re * z.im) / den);
    }

    public double abs() {
        return Math.hypot(re, im);
    }

    public boolean isNaN() {
        return Double.isNaN(re) || Double.isNaN(im);
    }

    public boolean isInfinite() {
        return Double.isInfinite(re) || Double.isInfinite(im);
    }

    public boolean isFinite() {
        return Double.isFinite(re) && Double.isFinite(im);
    }

    @Override
    public int hashCode() {
        return Objects.hash(im, re);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Complex other = (Complex) obj;
        return Double.doubleToLongBits(im) == Double.doubleToLongBits(other.im)
                && Double.doubleToLongBits(re) == Double.doubleToLongBits(other.re);
    }

    @Override
    public String toString() {
        return re + " + " + im + "i";
    }

}
