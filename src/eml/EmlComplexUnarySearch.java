package eml;

import java.util.*;

public class EmlComplexUnarySearch extends CommonComplexSearch {

    // 評価点
    //private static final Complex X_SAMP = Complex.real(Const.ONE);
    //private static final Complex X_SAMP = Complex.real(Const.EulerGamma);
    private static final Complex X_SAMP = new Complex(Const.EulerGamma, Const.Catalan);
    //private static final Complex X_SAMP = new Complex(0.0, Math.PI);

    // 指定された形状(rank)と葉の構成(mask)で式を評価 (Source)
    private static Complex evaluateExpr(int leaves, long shapeRank, int leafMask, int[] leafPos, Complex x) {
        if (leaves == 1) {
            // マスクのビットが1なら変数x、0なら定数1を返す (Source)
            boolean isVar = ((leafMask >> leafPos[0]) & 1) != 0;
            leafPos[0]++;
            return isVar ? x : Complex.ONE;
        }

        int leftLeaves = 0;
        long leftRank = 0, rightRank = 0;
        long tempRank = shapeRank;

        for (int left = 1; left < leaves; ++left) {
            int right = leaves - left;
            long block = hShapeCount[left] * hShapeCount[right];
            if (tempRank < block) {
                leftLeaves = left;
                leftRank = tempRank / hShapeCount[right];
                rightRank = tempRank % hShapeCount[right];
                break;
            }
            tempRank -= block;
        }
        if (leftLeaves == 0) return Complex.NaN;

        Complex leftVal = evaluateExpr(leftLeaves, leftRank, leafMask, leafPos, x);
        Complex rightVal = evaluateExpr(leaves - leftLeaves, rightRank, leafMask, leafPos, x);
        return emlEval(leftVal, rightVal);
    }

    private static String FUNC = "exp(x)";
    //private static String FUNC = "e-ln(x)";
    //private static String FUNC = "e-x";
    //private static String FUNC = "ln(x)";
    //private static String FUNC = "exp(e)-x";
    //
    //private static String FUNC = "x";
    //private static String FUNC = "x-1";
    //private static String FUNC = "ln(ln(x))";
    //private static String FUNC = "x-e";
    //private static String FUNC = "x-exp(e)";
    //
    //private static String FUNC = "-x";
    //private static String FUNC = "1/x";
    //private static String FUNC = "x^2";
    //private static String FUNC = "2x";
    //private static String FUNC = "x+1";
    //
    //private static String FUNC = "x/2";
    private static Complex func(Complex x) {
        Complex target = Complex.exp(x);
        //Complex target = Complex.E.sub(Complex.log(x));
        //Complex target = Complex.E.sub(x);
        //Complex target = Complex.log(x);
        //Complex target = Complex.exp(Complex.E).sub(x);
        //
        //Complex target = x;
        //Complex target = x.sub(Const.ONE);
        //Complex target = Complex.log(Complex.log(x));
        //Complex target = x.sub(Const.E);
        //Complex target = x.sub(Complex.exp(Complex.E));
        //
        //Complex target = Complex.ZERO.sub(x);
        //Complex target = Complex.ONE.div(x);
    	//Complex target = x.mul(x);
        //Complex target = x.mul(Const.TWO);
        //Complex target = x.add(Const.ONE);
        //
        //Complex target = x.div(Const.TWO);
        return target;
    }

    private static boolean validate(int leaves, long rank, int mask) {
        double[] ary_re = { //0.0,
                1.0, 2.0, 3.0, 4.0, 5.0,
                //Const.E
                };
        for (double x_re : ary_re) {
            Complex x = Complex.real(x_re);
            Complex target = func(x);
            Complex result = evaluateExpr(leaves, rank, mask, new int[]{0}, x);
            if (isSame(result, target)) {
                //ok
            } else {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        initShapeCounts();
        
        Complex x = X_SAMP;
        Complex target = func(x);

        System.out.println("Searching for Unary Function Complex target: " + FUNC + " at x=" + x + Const.MSG_ALLOW_INF);
        long start = System.currentTimeMillis();
        // 命令長 K = 1, 3, 5, ... の順に探索 (Source)
        for (int K = 1; K <= 31; K += 2) {
            int leaves = (K + 1) / 2;
            long totalShapes = hShapeCount[leaves];
            int totalMasks = 1 << leaves; // 各葉が 1 か x の 2^leaves 通り
            List<Integer> masks = new ArrayList<>();
            for (int mask = 0; mask < totalMasks; mask++) {
                //if (Integer.bitCount(mask)>3) continue;
                masks.add(mask);
            }
            System.out.print("Testing K=" + K + " (Shapes: " + totalShapes + ", Masks: " + masks.size() + ")... ");

            for (long rank = 0; rank < totalShapes; rank++) {
                //for (int mask = 0; mask < totalMasks; mask++) {
                for (int mask : masks) {
                    Complex result = evaluateExpr(leaves, rank, mask, new int[]{0}, x);
                    if (isSame(result, target)) {
                        long end = System.currentTimeMillis();
                        if (validate(leaves, rank, mask)) {
                            System.out.println("break. " + (end-start)/1000.0 + "s");
                            System.out.println("[HIT!] Found at K=" + K + ", rank=" + rank + ", mask=" + mask);
                            System.out.println("RPN: " + getRpn1(leaves, rank, mask, new int[]{0}));
                            return;
                        }
                    }
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("done. " + (end-start)/1000.0 + "s");
        }
    }
}
