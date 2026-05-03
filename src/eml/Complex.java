package eml;

import java.util.Objects;

// 複素数クラスの簡易実装 (Source の C 構造体に相当)
public class Complex {
    public static final Complex ONE = Complex.real(Const.ONE);
    public static final Complex NaN = new Complex(Const.NaN, Const.NaN);

    public static Complex real(double r) {
        return new Complex(r, 0.0);
    }

    public static Complex exp(Complex z) {
        double ere = Math.exp(z.re);
        return new Complex(ere * Math.cos(z.im), ere * Math.sin(z.im));
    }

    public static Complex log(Complex z) {
        double r = Math.hypot(z.re, z.im);
        return new Complex(Math.log(r), Math.atan2(z.im, z.re));
    }

    public final double re;
    public final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex add(Complex z) {
        return new Complex(this.re + z.re, this.im + z.im);
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
        if (r == 0) {
            return Complex.NaN;
        }
        return new Complex(this.re / r, this.im / r);
    }

    public Complex div(Complex z) {
        double den = z.re * z.re + z.im * z.im;
        if (den == 0) {
            return Complex.NaN;
        }
        return new Complex((this.re * z.re + this.im * z.im) / den, (this.im * z.re - this.re * z.im) / den);
    }

    public double abs() {
        return Math.hypot(re, im);
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
