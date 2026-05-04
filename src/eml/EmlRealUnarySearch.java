package eml;

import java.util.*;

public class EmlRealUnarySearch extends CommonRealSearch {

    // 評価点
    //private static final double X_SAMP = Const.ONE;
    private static final double X_SAMP = Const.EulerGamma;
    //private static final double X_SAMP = Const.E+1; //for ln(ln(x))

    // 指定された形状(rank)と葉の割り当て(mask)で式を評価 (Source)
    private static double evaluateExpr(int leaves, long shapeRank, int leafMask, int[] leafPos, double x) {
        if (leaves == 1) {
            // マスクのビットが1なら変数x、0なら定数1を返す
            boolean isVar = ((leafMask >> leafPos[0]) & 1) != 0;
            leafPos[0]++;
            return isVar ? x : Const.ONE;
        }

        int leftLeaves = 0;
        long leftRank = 0, rightRank = 0;

        // 指定ランクから左右の枝の葉の数とそれぞれのランクを決定 (Source)
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
        if (leftLeaves == 0) return Const.NaN;

        double leftVal = evaluateExpr(leftLeaves, leftRank, leafMask, leafPos, x);
        double rightVal = evaluateExpr(leaves - leftLeaves, rightRank, leafMask, leafPos, x);
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
    private static double func(double x) {
        double target = Math.exp(x);
        //double target = Const.E-Math.log(x);
        //double target = Const.E-x;
        //double target = Math.log(x);
        //double target = Math.exp(Const.E)-x;
        //
        //double target = x;
        //double target = x-1;
        //double target = Math.log(Math.log(x));
        //double target = x-Const.E;
        //double target = x-Math.exp(Const.E);
        //
        //double target = -x;
        //double target = 1/x;
    	//double target = x*x;
        //double target = 2*x;
        //double target = x+1;
        //
        //double target = x/2;
        return target;
    }

    private static boolean validate(int leaves, long rank, int mask) {
        double[] ary = { //0.0, 
                1.0, 2.0, 3.0, 4.0, 5.0,
                //Const.E
                };
        for (double x : ary) {
            double target = func(x);
            double result = evaluateExpr(leaves, rank, mask, new int[]{0}, x);
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

        double x = X_SAMP;
        double target = func(x);

        System.out.println("Searching for Unary Function Real target: " + FUNC + " at x=" + x + Const.MSG_ALLOW_INF);
        long start = System.currentTimeMillis();
        // 命令長 K = 1, 3, 5, ... の順に探索 (Source)
        for (int K = 1; K <= 31; K += 2) {
            int leaves = (K + 1) / 2;
            long totalShapes = hShapeCount[leaves];
            int totalMasks = 1 << leaves; 
            List<Integer> masks = new ArrayList<>();
            for (int mask = 0; mask < totalMasks; mask++) {
                //if (Integer.bitCount(mask)>3) continue;
                masks.add(mask);
            }
            System.out.print("Testing K=" + K + " (Shapes: " + totalShapes + ", Masks: " + masks.size() + ")... ");

            for (long rank = 0; rank < totalShapes; rank++) {
                //for (int mask = 0; mask < totalMasks; mask++) {
                for (int mask : masks) {
                    double result = evaluateExpr(leaves, rank, mask, new int[]{0}, x);
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
