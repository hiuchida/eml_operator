package eml;

public class EmlComplexConstSearch extends CommonComplexSearch {

    // 指定されたランクのツリーを再帰的に評価 (Source)
    public static Complex evaluateRank(int leaves, long rank) {
        if (leaves == 1) return Complex.ONE; // 全ての葉は定数1

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
        if (leftLeaves == 0) return Complex.NaN;

        Complex leftVal = evaluateRank(leftLeaves, leftRank);
        Complex rightVal = evaluateRank(leaves - leftLeaves, rightRank);
        return emlEval(leftVal, rightVal);
    }

    public static void main(String[] args) {
        initShapeCounts();

        // ターゲット: 0.5 (1/2)
        Complex target = Complex.real(0.5);
        double threshold = 1e-10;

        System.out.println("Searching for Constant Complex target: " + target);
        long start = System.currentTimeMillis();
        // 命令長 K = 1, 3, 5, ... の順に探索 (Source)
        for (int K = 1; K <= 43; K += 2) {
            int leaves = (K + 1) / 2;
            long totalShapes = hShapeCount[leaves];
            System.out.print("Testing K=" + K + " (Shapes: " + totalShapes + ")... ");

            for (long rank = 0; rank < totalShapes; rank++) {
                Complex result = evaluateRank(leaves, rank);
                if (result.isFinite() && result.sub(target).abs() < threshold) {
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
