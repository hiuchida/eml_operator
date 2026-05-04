package eml;

public class Const {

    // 無限大経由の探索を許可
    public static final boolean ALLOW_INF = true;
    public static final String MSG_ALLOW_INF = " (allow_inf=" + Const.ALLOW_INF + ")";

    public static final double THRESHOLD = 1e-10;

    // long maskで管理した場合、totalがlongmaxに収まる最大数。
    // 探索設定 (MAX_TOKENS=71, MAX_LEAVES=36)
    // total=3,116,285,494,907,301,262
    // long<=9,223,372,036,854,775,807
    //public static final int MAX_TOKENS = 71;
    //public static final int MAX_LEAVES = (MAX_TOKENS + 1) / 2;

    // int maskで管理しているため、MAX_LEAVES=31が最大となる
    // 探索設定 (MAX_TOKENS=61, MAX_LEAVES=31)
    // total=    3,814,986,502,092,304
    // long<=9,223,372,036,854,775,807
    public static final int MAX_TOKENS = 61;
    public static final int MAX_LEAVES = (MAX_TOKENS + 1) / 2;

    // 定数0.0
    public static final double ZERO = 0.0;
    // 定数1.0
    public static final double ONE = 1.0;
    // 定数2.0
    public static final double TWO = 2.0;
    // 定数3.0
    public static final double THREE = 3.0;
    // 定数4.0
    public static final double FOUR = 4.0;
    // 定数e
    public static final double E = Math.E;
    // 定数π
    public static final double PI = Math.PI;
    // 定数 (Source: EulerGamma)
    public static final double EulerGamma = 0.57721566490153286;
    // 定数 (Source: Catalan)
    public static final double Catalan = 0.91596559417721901;
    // 定数+INF
    public static final double PINF = Double.POSITIVE_INFINITY;
    // 定数-INF
    public static final double NINF = Double.NEGATIVE_INFINITY;
    // 定数NaN
    public static final double NaN = Double.NaN;

    // RPN文字列
    public static final String LEAF_1 = "1";
    public static final String LEAF_x = "x";
    public static final String LEAF_y = "y";
    public static final String LEAF_E = "E";
    public static final String ERROR = "ERROR";

}
