package eml;

public class EmlComplexBinarySearch extends CommonComplexSearch {

    // 評価点 (Source: EulerGamma, Catalan)
    private static final Complex SYMBOL1 = Complex.EulerGamma;
    private static final Complex SYMBOL2 = Complex.Catalan;
    private static final Complex[] LEAF_VALUES = {
        Complex.ONE, SYMBOL1, SYMBOL2
    };

    // 指定された形状(rank)と葉の割り当て(code)で式を評価 (Source: evaluate_expr_complex64)
    private static Complex evaluateExpr(int leaves, long shapeRank, long leafCode) {
        if (leaves == 1) {
            return LEAF_VALUES[(int)(leafCode % 3)];
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

        // leafCodeを左右のサブツリーに分割 (Source:)
        long leftBase = hAssignmentCount[leftLeaves];
        long leftCode = leafCode % leftBase;
        long rightCode = leafCode / leftBase;

        Complex leftVal = evaluateExpr(leftLeaves, leftRank, leftCode);
        Complex rightVal = evaluateExpr(leaves - leftLeaves, rightRank, rightCode);
        return emlEval(leftVal, rightVal);
    }

    private static String FUNC = "x-y";
    //private static String FUNC = "ln(x)+ln(y)";
    //private static String FUNC = "ln(x)-ln(y)";
    //private static String FUNC = "xy";
    //private static String FUNC = "x/y";
    //private static String FUNC = "x+y";
    //private static String FUNC = "x^y";
    //private static String FUNC = "log_x(y)";

    private static Complex func(Complex x, Complex y) {
    	Complex target = x.sub(y);
    	//Complex target = Complex.log(x).add(Complex.log(y));
    	//Complex target = Complex.log(x).sub(Complex.log(y));
    	//Complex target = x.mul(y);
    	//Complex target = x.div(y);
    	//Complex target = x.add(y);
    	//Complex target = x.pow(y);
    	//Complex target = Complex.log(x, y);
        return target;
    }

    public static void main(String[] args) {
        initCounts();

        Complex x = SYMBOL1;
        Complex y = SYMBOL2;
        Complex target = func(x, y);

        System.out.println("Searching for Binary Function Complex target: " + FUNC + " at x=" + x + ", y=" + y + Const.MSG_ALLOW_INF);
        long start = System.currentTimeMillis();
        // 命令長 K = 1, 3, 5, ... の順に探索 (Source)
        for (int K = 1; K <= 29; K += 2) {
            int leaves = (K + 1) / 2;
            long totalShapes = hShapeCount[leaves];
            long totalCodes = hAssignmentCount[leaves]; // 3^leaves
            System.out.print("Testing K=" + K + " (Shapes: " + totalShapes + ", Assignments: " + totalCodes + ")... ");

            for (long rank = 0; rank < totalShapes; rank++) {
                for (long code = 0; code < totalCodes; code++) {
                    Complex result = evaluateExpr(leaves, rank, code);
                    if (isSame(result, target)) {
                        long end = System.currentTimeMillis();
                        System.out.println("break. " + (end-start)/1000.0 + "s");
                        System.out.println("[HIT!] Found at K=" + K + ", rank=" + rank + ", code=" + code);
                        System.out.println("RPN: " + getRpn2(leaves, rank, code));
                        return;
                    }
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("done. " + (end-start)/1000.0 + "s");
        }
    }
}
