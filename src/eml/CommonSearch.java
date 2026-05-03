package eml;

public abstract class CommonSearch {

    public static long[] hShapeCount;
    public static long[] hAssignmentCount;

    // 二分木の形状数を計算 (Catalan構造)
    public static void initShapeCounts() {
        hShapeCount = new long[Const.MAX_LEAVES + 1];
        hShapeCount[1] = 1;
        for (int leaves = 2; leaves <= Const.MAX_LEAVES; ++leaves) {
            long total = 0;
            for (int left = 1; left < leaves; ++left) {
                total += hShapeCount[left] * hShapeCount[leaves - left];
                if (total < 0) {
                    System.err.println("Overflow: leaves=" + leaves + ", total="+ total);
                    System.exit(0);
                }
            }
            hShapeCount[leaves] = total;
        }
    }

    // カタラン構造と3進法割り当ての初期化
    public static void initCounts() {
        initShapeCounts();
        hAssignmentCount = new long[Const.MAX_LEAVES + 1];
        hAssignmentCount[0] = 1;
        hAssignmentCount[1] = 3; // 葉の選択肢 {1, γ, G}
        for (int leaves = 2; leaves <= Const.MAX_LEAVES; ++leaves) {
            hAssignmentCount[leaves] = hAssignmentCount[leaves - 1] * 3;
        }
    }

    // ランクからRPN（逆ポーランド記法）形式の文字列を生成
    public static String getRpn0(int leaves, long rank) {
        if (leaves == 1) return Const.LEAF_1;

        int leftLeaves = 0;
        long leftRank = 0, rightRank = 0;

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
        if (leftLeaves == 0) return Const.ERROR;

        return getRpn0(leftLeaves, leftRank) + " " + 
               getRpn0(leaves - leftLeaves, rightRank) + " " + Const.LEAF_E;
    }

    // ランクからRPN（逆ポーランド記法）形式の文字列を生成
    public static String getRpn1(int leaves, long shapeRank, int leafMask, int[] leafPos) {
        if (leaves == 1) {
            boolean isVar = ((leafMask >> leafPos[0]) & 1) != 0;
            leafPos[0]++;
            return isVar ? Const.LEAF_x : Const.LEAF_1;
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
        if (leftLeaves == 0) return Const.ERROR;

        return getRpn1(leftLeaves, leftRank, leafMask, leafPos) + " " + 
               getRpn1(leaves - leftLeaves, rightRank, leafMask, leafPos) + " " + Const.LEAF_E;
    }

    // ランクからRPN（逆ポーランド記法）形式の文字列を生成
    public static String getRpn2(int leaves, long shapeRank, long leafCode) {
        if (leaves == 1) {
            int type = (int)(leafCode % 3);
            if (type == 0) return Const.LEAF_1;
            if (type == 1) return Const.LEAF_x;
            return Const.LEAF_y;
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
        if (leftLeaves == 0) return Const.ERROR;

        long leftBase = hAssignmentCount[leftLeaves];
        return getRpn2(leftLeaves, leftRank, leafCode % leftBase) + " " + 
               getRpn2(leaves - leftLeaves, rightRank, leafCode / leftBase) + " " + Const.LEAF_E;
    }

}
