package eml;

public class EmlRealConstSearch extends CommonRealSearch {

    // 指定されたランクのツリーを再帰的に評価 (Source)
    public static double evaluateRank(int leaves, long rank) {
        if (leaves == 1) return Const.ONE; // 全ての葉は定数1

        int leftLeaves = 0;
        long leftRank = 0, rightRank = 0;

        // 指定ランクから左右の枝の葉の数とそれぞれのランクを決定 (Source)
        long tempRank = rank;
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

        double leftVal = evaluateRank(leftLeaves, leftRank);
        double rightVal = evaluateRank(leaves - leftLeaves, rightRank);
        return emlEval(leftVal, rightVal);
    }

    private static String FUNC = "1";
    //private static String FUNC = "e";
    //private static String FUNC = "e-1";
    //private static String FUNC = "exp(e)";
    //private static String FUNC = "0";
    //
    //private static String FUNC = "exp(e)-1";
    //private static String FUNC = "-1";
    //private static String FUNC = "e-2";
    //private static String FUNC = "exp(e)-2";
    //private static String FUNC = "2";
    //
    //private static String FUNC = "e-3";
    //private static String FUNC = "-2";
    //private static String FUNC = "exp(e)-3";
    //private static String FUNC = "3";
    //private static String FUNC = "1/2";
    //
    //private static String FUNC = "exp(e)-4";
    //private static String FUNC = "-3";
    //private static String FUNC = "4";
    private static double func() {
        double target = Const.ONE;
        //double target = Const.E;
        //double target = Const.E-Const.ONE;
        //double target = Math.exp(Const.E);
        //double target = Const.ZERO;
        //
        //double target = Math.exp(Const.E)-Const.ONE;
        //double target = -Const.ONE;
        //double target = Const.E-Const.TWO;
        //double target = Math.exp(Const.E)-Const.TWO;
        //double target = Const.TWO;
        //
        //double target = Const.E-Const.THREE;
        //double target = -Const.TWO;
        //double target = Math.exp(Const.E)-Const.THREE;
        //double target = Const.THREE;
        //double target = 0.5;
        //
        //double target = Math.exp(Const.E)-Const.FOUR;
        //double target = -Const.THREE;
        //double target = Const.FOUR;
        return target;
    }

    public static void main(String[] args) {
        initShapeCounts();

        // ターゲット: 0.5 (1/2)
        double target = func();

        System.out.println("Searching for Constant Real target: " + FUNC + " v=" + target  + Const.MSG_ALLOW_INF);
        long start = System.currentTimeMillis();
        // 命令長 K = 1, 3, 5, ... の順に探索 (Source)
        for (int K = 1; K <= 43; K += 2) {
            int leaves = (K + 1) / 2;
            long totalShapes = hShapeCount[leaves];
            System.out.print("Testing K=" + K + " (Shapes: " + totalShapes + ")... ");

            for (long rank = 0; rank < totalShapes; rank++) {
                double result = evaluateRank(leaves, rank);
                if (isSame(result, target)) {
                    long end = System.currentTimeMillis();
                    System.out.println("break. " + (end-start)/1000.0 + "s");
                    System.out.println("[HIT!] Found at K=" + K + ", rank=" + rank);
                    System.out.println("RPN: " + getRpn0(leaves, rank));
                    return;
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("done. " + (end-start)/1000.0 + "s");
        }
    }
}
