import java.util.*;

public class Gaia {

    private double luminosity;
    private int size;
    private int maxAge;
    private double globalTemperature;
    private int numBlacks;
    private int numWhites;
    private String scenarioPhase;
    private Patch[][] patches;

    private int tickCounts;

    ArrayList<Double> luminosityList = new ArrayList<>();
    ArrayList<Double> globalTemperatureList = new ArrayList<>();
    ArrayList<Integer> whitePopulation = new ArrayList<>();
    ArrayList<Integer> blackPopulation = new ArrayList<>();

    public Gaia(int size) {
        this.size = size;
        patches = new Patch[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                patches[i][j] = new Patch();

        }
        this.globalTemperature = 0;
    }

    public void setUp() {

        this.luminosity = Params.SOLAR_LUMINOSITY;
//        System.out.println(luminosity);
        this.numBlacks = Params.BLACK_START;
        this.numWhites = Params.WHITE_START;
        startUpRandomDaisiesGenerator();
    }



    /**
     * initialize daisies
     */
    public void startUpRandomDaisiesGenerator() {
        int numWhitesCount = this.numWhites;
        int numBlacksCount = this.numBlacks;
        Random rand = new Random();
        while (numBlacksCount != 0 || numWhitesCount != 0) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if (patches[x][y].getDaisy() == null) {
                int temp = rand.nextInt(2);
                if (temp == 0 && numWhitesCount > 0) {//white
                    patches[x][y].setDaisy(new DaisyWhite(rand.nextInt(25)));
                    numWhitesCount--;
                }
                if (temp == 1 && numBlacksCount > 0) {//black
                    patches[x][y].setDaisy(new DaisyBlack(rand.nextInt(25)));
                    numBlacksCount--;
                }
            }
        }
    }


    public void go() throws InterruptedException {
        while (tickCounts <= Params.TICKS) {
            tickCounts++;
            this.update();
            Thread.sleep(1);
            //System.out.println("ticks: " + tickCounts);
        }

        toCSV();
    }

    public void update() {
        traverseMatrix();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                patches[i][j].calcTemperature(luminosity);
            }
        }

        diffuse();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (patches[i][j].getDaisy() == null) {
                    continue;
                }

                if (patches[i][j].isCheckState()) {
                    patches[i][j].setCheckState(false);
                    continue;
                }
                int tmp = patches[i][j].updateSprout();
                if (tmp == 1) {
                    sprout(i, j);
                }
                if (tmp == 0) {
                }
                if (tmp == -1) {
                }
            }
        }

        if (Params.SCENARIO.equals("ramp-up-ramp-down")) {
            if (tickCounts > 200 && tickCounts <= 400) {
                double precision = 0.005;
                double newSolarLuminosity = luminosity + precision;
                luminosity = Math.round(newSolarLuminosity * 10000.0) / 10000.0; // Rounding to 4 decimal places
            }
            if (tickCounts > 600 && tickCounts <= 850) {
                double precision = 0.0025;
                double newSolarLuminosity = luminosity - precision;
                luminosity = Math.round(newSolarLuminosity * 10000.0) / 10000.0; // Rounding to 4 decimal places
            }
        }
        updateTemperature();
    }

    public void updateTemperature() {//update global temperature
        double temp = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                temp += patches[i][j].getTemperature();
            }
        }
        this.globalTemperature = temp / (size * size);
        //System.out.println(globalTemperature);
        globalTemperatureList.add(globalTemperature);
        luminosityList.add(luminosity);
    }

    public void diffuse() {
        Patch[][] updatedPatches = new Patch[this.size][this.size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                patches[i][j].setCheckState(false);
                double newTemperature = 0.0;
                int count = 0;

                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        if (di == 0 && dj == 0) {
                            continue;
                        }
                        int ni = i + di;
                        int nj = j + dj;
                        if (ni >= 0 && ni < size && nj >= 0 && nj < size) {
                            newTemperature += patches[ni][nj].getTemperature();
                            count++;
                        }
                    }
                }
                newTemperature = newTemperature / count;
                updatedPatches[i][j] = new Patch();
                updatedPatches[i][j].setTemperature(0.5 * newTemperature + 0.5 * patches[i][j].getTemperature());
            }
        }
        // Update the grid
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                patches[i][j].setTemperature(updatedPatches[i][j].getTemperature());
            }
        }
    }

    public void sprout(int x, int y) {
        boolean allNeighborFlag = false;
        int[][] patchCheck = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                patchCheck[i][j] = 0;
            }
        }
        Random rand = new Random();
        while (!allNeighborFlag) {
            allNeighborFlag = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (patchCheck[i][j] == 0)
                        allNeighborFlag = false;
                }
            }
            int xOffset = rand.nextInt(3);
            int yOffset = rand.nextInt(3);
            if (patchCheck[xOffset][yOffset] == 1)
                continue;
            if (x + xOffset - 1 < 0 || x + xOffset - 1 >= size || y + yOffset - 1 < 0 || y + yOffset - 1 >= size) {
                patchCheck[xOffset][yOffset] = 1;
                continue;
            }
            //sprout if selected patch empty
            if (patches[x + xOffset - 1][y + yOffset - 1].getDaisy() == null) {
                patches[x + xOffset - 1][y + yOffset - 1].setDaisy(patches[x][y].getDaisy().createDaisy());
                patches[x + xOffset - 1][y + yOffset - 1].setCheckState(true);
                return;
            }
            patchCheck[xOffset][yOffset] = 1;
        }
    }


    public void traverseMatrix() {
        //Data summary
        int countWhite = 0;
        int countBlack = 0;
        int countPetalvore = 0;
        int countEmpty = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (patches[i][j].getDaisy() == null) {
                    countEmpty++;
                    //System.out.print(" " + " ");
                } else if (patches[i][j].getDaisy() instanceof DaisyWhite) {
                    countWhite++;
                    //System.out.print("W" + " ");
                } else if (patches[i][j].getDaisy() instanceof DaisyBlack) {
                    countBlack++;
                    //System.out.print("B" + " ");
                }
            }
//            System.out.println();
        }
        whitePopulation.add(countWhite);
        blackPopulation.add(countBlack);
//        System.out.println("Empty Patch : " + countEmpty + "\n" +
//                "Black Daisy: " + countBlack + "\n" +
//                "White Daisy : " + countWhite);
    }
    public void toCSV() {
        toCSV t = new toCSV();
        t.writeArrayListsToCSV(luminosityList, globalTemperatureList, whitePopulation, blackPopulation, "result.csv");
//        t.writeAveragesToCSV(luminosityList, globalTemperatureList, whitePopulation, blackPopulation, "avg_overallData3.csv");

    }

}
