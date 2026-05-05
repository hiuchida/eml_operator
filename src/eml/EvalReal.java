package eml;

import eml.ParseRpn.Answer;

public class EvalReal extends EmlRealConstSearch {

	public static void main(String[] args) {
        String input = "1 1 E";
        if (args.length > 0) {
            input = args[0];
        }
        String rpn = input.replaceAll(" ", "");
        Answer ans = ParseRpn.parseRpn(input);
        int K = ans.K;
        int leaves = (K + 1) / 2;
        long rank = ans.rank;
        double result = evaluateRank(leaves, rank);
        System.out.println("Input: " + input);
        System.out.println("Trim: " + rpn);
        System.out.println("RpnID: " + ans);
        System.out.println("Result: " + result);
        double target = Const.E;
        double err = Math.abs(result - target);
        System.out.println("Error: " + err);
	}

}
