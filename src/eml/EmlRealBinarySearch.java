package eml;

public class EmlRealBinarySearch extends CommonRealSearch {

    // 評価用の定数
    private static final double SYMBOL1 = Const.EulerGamma;
    private static final double SYMBOL2 = Const.Catalan;
    private static final double[] LEAF_VALUES = { Const.ONE, SYMBOL1, SYMBOL2 };

    // 式の再帰的評価
    private static double evaluateExpr(int leaves, long shapeRank, long leafCode) {
        if (leaves == 1) {
            return LEAF_VALUES[(int)(leafCode % 3)];
        }

        long tempRank = shapeRank;
        int leftLeaves = 0;
        long leftRank = 0, rightRank = 0;

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

        // leafCodeを3進法に基づいて分割
        long leftBase = hAssignmentCount[leftLeaves];
        long leftCode = leafCode % leftBase;
        long rightCode = leafCode / leftBase;

        double leftVal = evaluateExpr(leftLeaves, leftRank, leftCode);
        double rightVal = evaluateExpr(leaves - leftLeaves, rightRank, rightCode);
        
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

    private static double func(double x, double y) {
        double target = x - y;
        //double target = Math.log(x) + Math.log(y);
        //double target = Math.log(x) - Math.log(y);
        //double target = x * y;
        //double target = x / y;
        //double target = x + y;
        //double target = Math.pow(x, y);
        //double target = Math.log(y) / Math.log(x);
        return target;
    }

    public static void main(String[] args) {
        initCounts();

        double x = SYMBOL1;
        double y = SYMBOL2;
        double target = func(x, y);

        System.out.println("Searching for Binary Function Real target: " + FUNC + " at x=" + x + ", y=" + y + Const.MSG_ALLOW_INF);
        long start = System.currentTimeMillis();
        // 命令長 K = 1, 3, 5, ... の順に探索 (Source)
        for (int K = 25; K <= 29; K += 2) {
            int leaves = (K + 1) / 2;
            long totalShapes = hShapeCount[leaves];
            long totalCodes = hAssignmentCount[leaves];
            System.out.print("Testing K=" + K + " (Shapes: " + totalShapes + ", Assignments: " + totalCodes + ")... ");

            for (long rank = 195379; rank < totalShapes; rank++) {
                for (long code = 0; code < totalCodes; code++) {
                    double result = evaluateExpr(leaves, rank, code);
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
