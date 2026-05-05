package eml;

import java.util.Stack;

public class ParseRpn extends CommonSearch {

    public static class Answer {
        boolean isFunc;
        boolean isBinary;
        int K;
        long rank;
        long leafCode;
        Answer(boolean isFunc, boolean isBin, int k, long rank, long code) {
            this.isFunc = isFunc;
            this.isBinary = isBin;
            this.K = k;
            this.rank = rank;
            this.leafCode = code;
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("K=").append(K);
            sb.append(" rank=").append(rank);
            if (isBinary) sb.append(" code=").append(leafCode);
            else if (isFunc) sb.append(" mask=").append(leafCode);
            return sb.toString();
        }
    }

    // 復元したツリーの情報を保持する内部クラス
    private static class Node {
        long rank;
        long leafCode;
        int leaves;

        Node(long rank, long leafCode, int leaves) {
            this.rank = rank;
            this.leafCode = leafCode;
            this.leaves = leaves;
        }
    }

    private static String[] split(String rpn) {
        String[] tokens = new String[rpn.length()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = rpn.substring(i, i + 1);
        }
        return tokens;
    }

    public static Answer parseRpn(String rpn) {
        initCounts(); // hShapeCount, hAssignmentCount の初期化
        boolean isFunc = rpn.contains("x");
        boolean isBinary = rpn.contains("y");
        Stack<Node> stack = new Stack<>();
        String[] tokens = split(rpn);
        for (String token : tokens) {
            if (token.equals("E")) {
                // 演算子 'E' の場合：2つのノードをポップして結合
                Node right = stack.pop();
                Node left = stack.pop();
                
                int totalLeaves = left.leaves + right.leaves;
                
                // 1. オフセットの計算 (left 枝数が現在の数未満の全パターン)
                long offset = 0;
                for (int i = 1; i < left.leaves; i++) {
                    offset += hShapeCount[i] * hShapeCount[totalLeaves - i];
                }
                
                // 2. 形状ランクの合成 [会話履歴]
                long shapeRank = offset + (left.rank * hShapeCount[right.leaves] + right.rank);
                
                // 3. leafCode の合成 (左右の割り当てを統合)
                long weight;
                if (isBinary) {
                    weight = hAssignmentCount[left.leaves];
                } else {
                    weight = 1L << left.leaves;
                }
                long leafCode = right.leafCode * weight + left.leafCode;
                
                stack.push(new Node(shapeRank, leafCode, totalLeaves));
            } else {
                // 葉 (1, x, y) の場合
                long type = 0; // '1'
                if (token.equals("x")) type = 1;
                if (token.equals("y")) type = 2;
                
                stack.push(new Node(0, type, 1)); // 葉1枚のランクは常に0
            }
        }

        Node root = stack.pop();
        int k = 2 * root.leaves - 1;
        Answer ans = new Answer(isFunc, isBinary, k, root.rank, root.leafCode);
        return ans;
    }

    public static void main(String[] args) {
        String input = "1 1 E";
        if (args.length > 0) {
            input = args[0];
        }
        String rpn = input.replaceAll(" ", "");
        Answer ans = parseRpn(input);
        System.out.println("Input: " + input);
        System.out.println("Trim: " + rpn);
        System.out.println("Result: " + ans);
    }

}
