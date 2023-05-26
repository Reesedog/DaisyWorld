public class Params {

    public static int TICKS = 200;

    public static int BOARD_SIZE = 29;
    public static double SOLAR_LUMINOSITY = 1.0;
    public static int MAX_AGE = 25;

    public static double SURFACE_ALBEDO = 0.40;

    public static int WHITE_START = 168;
    public static int BLACK_START = 168;

    public static double WHITE_ALBEDO = 0.75;
    public static double BLACK_ALBEDO = 0.25;

    public static String SCENARIO = "our-solar-luminosity";


    public static void printParams() {
        System.out.println("Params:");
        System.out.println("TICKS = " + TICKS);
        System.out.println("BOARD_SIZE = " + BOARD_SIZE);
        System.out.println("SOLAR_LUMINOSITY = " + SOLAR_LUMINOSITY);
        System.out.println("MAX_AGE = " + MAX_AGE);
        System.out.println("SURFACE_ALBEDO = " + SURFACE_ALBEDO);
        System.out.println("WHITE_START = " + WHITE_START);
        System.out.println("BLACK_START = " + BLACK_START);
        System.out.println("WHITE_ALBEDO = " + WHITE_ALBEDO);
        System.out.println("BLACK_ALBEDO = " + BLACK_ALBEDO);
        System.out.println("SCENARIO = " + SCENARIO);
    }

}
