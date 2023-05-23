import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

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
        while (tickCounts <= 1000) {
            tickCounts++;
            this.update();
            Thread.sleep(1);
            System.out.println("ticks: " + tickCounts);
        }
        toCSV();
    }

    public void update() {
        traverseMatrix();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                patches[i][j].calcTemperature();
            }
        }

        diffuse();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (patches[i][j].getDaisy() == null) {
                    // System.out.print(0 + " ");
                    continue;
                }

                if (patches[i][j].isCheckState()) {
                    patches[i][j].setCheckState(false);
                    // System.out.print(1 + " ");
                    continue;
                }
                int tmp = patches[i][j].updateSprout();
                if (tmp == 1) {
                    sprout(i, j);
                    //System.out.print("1 ");
                }
                if (tmp == 0) {
                    // System.out.print(1 + " ");
                }
                if (tmp == -1) {
                    // System.out.print(3 + " ");
                }
            }
            //System.out.println();
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
        System.out.println(globalTemperature);
        globalTemperatureList.add(globalTemperature);
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
//                System.out.print("n ");
                return;
            }
            patchCheck[xOffset][yOffset] = 1;
        }
        //no neighbor empty, renew itself
        //patches[x][y].setDaisy(patches[x][y].getDaisy().createDaisy());
//        System.out.print("s ");
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
                    System.out.print("E" + " ");
                } else if (patches[i][j].getDaisy() instanceof DaisyWhite) {
                    countWhite++;
                    System.out.print(patches[i][j].getDaisy().getAge()+" ");
                } else if (patches[i][j].getDaisy() instanceof DaisyBlack) {
                    countBlack++;
                    System.out.print("B" + " ");
                }
            }
            System.out.println();
        }
        whitePopulation.add(countWhite);
        blackPopulation.add(countBlack);
        System.out.println("Empty Patch : " + countEmpty + "\n" +
                "Black Daisy: " + countBlack + "\n" +
                "White Daisy : " + countWhite + "\n" +
                "Petalvore : " + countPetalvore);
        System.out.println(whitePopulation.size() + " " + blackPopulation.size());
    }


    //    ArrayList<Double> luminosityList = new ArrayList<>();
//    ArrayList<Double> globalTemperatureList = new ArrayList<>();
//    ArrayList<Integer> whitePopulation = new ArrayList<>();
//    ArrayList<Integer> blackPopulation = new ArrayList<>();


    public void toCSV() {

        toCSV t = new toCSV();
        t.writeArrayListToCSV(luminosityList,"luminosity.csv");
        t.writeArrayListToCSV(globalTemperatureList,"globalTemperature.csv");
        t.writeArrayListToCSV2(whitePopulation,"whitePopulation.csv");
        t.writeArrayListToCSV2(blackPopulation,"blackPopulation.csv");
    }
}
