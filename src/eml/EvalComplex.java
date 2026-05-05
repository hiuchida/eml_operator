package eml;

import eml.ParseRpn.Answer;

public class EvalComplex extends EmlComplexConstSearch {

	public static void main(String[] args) {
        //String input = "1 1 E";
        // πi
		//String input = "1111111E1EEEE11EEE1EE";
		// x/2: K=21, rank=12464, mask=63 (bitCount is all)
		//String input = "1111111E1EEEE11EEE1EE1111111E1EEEE11EEE1EE1111111E1EEEE11EEE1EEE1111111E1EEEE11EEE1EE1111111E1EEEE11EEE1EE1111111E1EEEE11EEE1EEE11EE1EE1EEE1E";
        // x/2: K=27, rank=189150, mask=529 (bitCount<=5)
		//String input = "1111111E1EEEE11EEE1EE1111111111E1EEEE11EEE1EE11EEEE1EE11111111E1EEEE11EEE1EEE1EE11EE1EE";
        // x/2: K=29, rank=2298175, mask=2064 (bitCount<=2)
		//String input = "11111111111E1EEEE11EEE1EEE1EEE1EE111E1EE1111111E1EEEE11EEE1EE1E1EEE1E";
		// x/2: K=35, rank=110852964, mask=16 (bitCount<=1)
		//String input = "11111111111E1EEEE11EEE1EEE1EEE1EE111111EEE1EE11EE1EEE1E";
		// exp(iπ/2)
		String input = "11111111111E1EEEE11EEE1EEE1EEE1EE111111EEE1EE11EE1EEE1E1E";
		if (args.length > 0) {
            input = args[0];
        }
        String rpn = input.replaceAll(" ", "");
        Answer ans = ParseRpn.parseRpn(input);
        int K = ans.K;
        int leaves = (K + 1) / 2;
        long rank = ans.rank;
        Complex result = evaluateRank(leaves, rank);
        System.out.println("Input: " + input);
        System.out.println("Trim: " + rpn);
        System.out.println("RpnID: " + ans);
        System.out.println("Result: " + result);
        //Complex target = new Complex(0.0, Const.PI/2.0);
        Complex target = new Complex(0.0, 1.0);
        double err = result.sub(target).abs();
        System.out.println("Error: " + err);
	}

}
