import java.sql.SQLOutput;
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

    //Extension
    private int numPetalvore;

    private int tickCount;


    //test variable
    int deathCount;

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

        this.numPetalvore = Params.PETALVORE_START;
        startUpRandomDaisiesGenerator();

    }

    /**
     * initialize daisies
     */
    public void startUpRandomDaisiesGenerator() {
        int numWhitesCount = this.numWhites;
        int numBlacksCount = this.numBlacks;

        //Generate PetalVore at the beginning
        int numPetalvoreCount = this.numPetalvore;
        Random rand = new Random();
        while (numBlacksCount != 0 || numWhitesCount != 0 || numPetalvoreCount != 0) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if (patches[x][y].getDaisy() == null && patches[x][y].getPetalvore() == null) {
                //Extension increase flag bound to 3
                int flag = rand.nextInt(3);
                if (flag == 0 && numWhitesCount > 0) {//white
                    patches[x][y].setDaisy(new DaisyWhite(rand.nextInt(25)));
                    numWhitesCount--;
                } else if (flag == 1 && numBlacksCount > 0) {//black
                    patches[x][y].setDaisy(new DaisyBlack(rand.nextInt(25)));
                    numBlacksCount--;
                } else if (flag == 2 && numPetalvoreCount > 0) {//Petalvore
                    patches[x][y].setPetalvore(new Petalvore(10));
                    numPetalvoreCount--;
                }
            }
        }
    }

    public void go() throws InterruptedException {
        while (tickCount < 3000) {
            tickCount++;
            this.update();
            System.out.println("Tick : " + tickCount);
            Thread.sleep(1);
        }
        System.out.println(deathCount);
    }

    public void update() {
        //Update temperature of each patch and global temperature
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                patches[i][j].calcTemperature();
            }
        }
        diffuse();
        updateGlobalTemperature();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                if (patches[i][j].getDaisy() == null) {
                    //System.out.print(0 + " ");
                    continue;
                }

                if (patches[i][j].isCheckState()) {
                    patches[i][j].setCheckState(false);
                    //System.out.print(1 + " ");
                    continue;
                }
                int status = patches[i][j].updateSprout();
                //status == 1, able to sprout
                if (status == 1) {
                    sprout(i, j);
                    //System.out.print("1 ");
                }
                //do nothing
                if (status == 0) {
                    //System.out.print(1 + " ");
                }
                //remove the daisy here? why print 3?
                if (status == -1) {
                    //System.out.print(3 + " ");
                }
            }
            //System.out.println();
        }
        traverseMatrix();
        petalvoreBehaviour();

    }



    public void updateGlobalTemperature() {//update global temperature
        double temp = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                temp += patches[i][j].getTemperature();
            }
        }
        this.globalTemperature = temp / (size * size);
        System.out.println(globalTemperature);
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
        //TODO default 0
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
            if (patches[x + xOffset - 1][y + yOffset - 1].getDaisy() == null
                    && patches[x + xOffset - 1][y + yOffset - 1].getPetalvore() == null
                    ) {
                patches[x + xOffset - 1][y + yOffset - 1].setDaisy(patches[x][y].getDaisy().createDaisy());
                patches[x + xOffset - 1][y + yOffset - 1].setCheckState(true);
//                System.out.print("n ");
                return;
            }
            patchCheck[xOffset][yOffset] = 1;
        }
        //no neighbor empty, renew itself
//        patches[x][y].setDaisy(patches[x][y].getDaisy().createDaisy());
//        System.out.print("s ");
    }

    //get a list of coordinates which can be sprouted or eat.
    public ArrayList<int[]> checkAround(int x, int y, int command) {
        ArrayList<int[]> targetList = new ArrayList<>();
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IllegalArgumentException("Error Coordinates!");
        }
        //search 3*3 matrix around the (curX,curY)
        int[][] offsets = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };
        for (int[] offset : offsets) {
            int targetX = x + offset[0];
            int targetY = y + offset[1];
            //Check if there is Index out of boundary
            if (targetX >= 0 && targetX < size && targetY >= 0 && targetY < size) {
                //command == 1: find black daisy to eat
                if (command == 0) {
                    if (patches[targetX][targetY].getDaisy() != null &&
                            patches[targetX][targetY].getDaisy() instanceof DaisyBlack) {
                        int[] coordinates = new int[]{targetX, targetY};
                        targetList.add(coordinates);
                    }
                } else if (command == 1) {
                    //command == 1: find white daisy to eat
                    if (patches[targetX][targetY].getDaisy() != null &&
                            patches[targetX][targetY].getDaisy() instanceof DaisyWhite) {
                        int[] coordinates = new int[]{targetX, targetY};
                        targetList.add(coordinates);
                    }
                }
//                if(command == 0){
//                    if(patches[targetX][targetY].getDaisy() != null){
//                        int[] coordinates = new int[]{targetX, targetY};
//                        targetList.add(coordinates);
//                    }
//                }
                //command == 2 : find a place to sprout
                else if (command == 2) {
                    //find an empty patch without petalvore
                    if (patches[targetX][targetY].getPetalvore() == null) {
                        int[] coordinates = new int[]{targetX, targetY};
                        targetList.add(coordinates);
                    }
                }
            }
        }
//        for (int[] i : targetList) {
//            System.out.println(Arrays.toString(i));
//        }
        return targetList;
    }


    /**
     * Use random to generate a random coordinate
     *
     * @return int[]: (x,y) of the target coordinate
     */
    public int[] findTargetPatch(ArrayList<int[]> targetList) {
        int length = targetList.size();
        //No target coordinates in the list
        if (length == 0) {
            return new int[]{-1, -1};
        } else {
            Random random = new Random();
            int randomIndex = random.nextInt(length);
            return targetList.get(randomIndex);
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
                    if (patches[i][j].getPetalvore() != null) {
                        countPetalvore++;
                        System.out.print("P" + " ");
                    } else {
                        countEmpty++;
                        System.out.print(" " + " ");
                    }
                } else if (patches[i][j].getDaisy() instanceof DaisyWhite) {
                    countWhite++;
                    System.out.print(String.format("%.2f", patches[i][j].getDaisy().getAlbedo() )+ " " + "W" + " ");
                } else if (patches[i][j].getDaisy() instanceof DaisyBlack) {
                    countBlack++;
                    System.out.print(String.format("%.2f", patches[i][j].getDaisy().getAlbedo()) + " " + "B" + " ");
                }
            }
            System.out.println();
        }
        System.out.println("Empty Patch : " + countEmpty + "\n" +
                "Black Daisy: " + countBlack + "\n" +
                "White Daisy : " + countWhite + "\n" +
                "Petalvore : " + countPetalvore);
    }

    public int[] checkCoordinates(int i, int j, int operator){
        int x = findTargetPatch(checkAround(i,j,operator))[0];
        int y = findTargetPatch(checkAround(i,j,operator))[1];
        //x == -1 && y == -1 means : there is no daisy to eat
        if(x == -1 && y == -1){
            return new int[]{-1,-1};
        }
        return new int[]{x,y};
    }

    public void petalvoreBehaviour(){
        //After update daisy, begin to update petalvore
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (patches[i][j].getPetalvore() != null) {
                    //if there is an alive petalvore
                    if(patches[i][j].getPetalvore().checkSurvivability()){
                        //first find daisy to eat
                        if(patches[i][j].getPetalvore().getSatiety() > 15){
                            continue;
                        }

                        double patchTemperature = patches[i][j].getTemperature();
                        int diet = patches[i][j].getPetalvore().checkDiet(patchTemperature,globalTemperature);

                        //Petalvore find food to eat by command 0/1
                        //No daisy around, skip this tick
                        if(checkCoordinates(i,j,diet)[0] < 0){
                            continue;
                        }
                        int x = checkCoordinates(i,j,diet)[0];
                        int y = checkCoordinates(i,j,diet)[1];
                        //Remove the daisy and move petalvore to there
                        patches[x][y].setToEmpty();
                        patches[x][y].setPetalvore(patches[i][j].getPetalvore());
                        if(patches[x][y].getPetalvore().getSatiety()<=0){
                        patches[x][y].getPetalvore().eat(25);}
                        patches[i][j].removePetalvore();
                        //System.out.println(patches[x][y].getPetalvore() == null);
                        //Petalvore try to sprout
                        if(patches[x][y].getPetalvore() != null && patches[x][y].getPetalvore().ableToSprout()){
                            //if there is no place to sprout
                            if(checkCoordinates(i,j,2)[0] < 0){
                                continue;
                            }
                            int xSprout = checkCoordinates(i,j,2)[0];
                            int ySprout = checkCoordinates(i,j,2)[1];
                            patches[x][y].getPetalvore().sprout();
                            //if the target patch is null, sprout petalvore directly
                            if(patches[xSprout][ySprout].getDaisy() == null){
                                patches[xSprout][ySprout].setPetalvore(new Petalvore(10));
                            }
                            //if the target patch not null, remove the daisy then sprout petalvore
                            else {
                                patches[xSprout][ySprout].setToEmpty();
                                patches[xSprout][ySprout].setPetalvore(new Petalvore(10));
                            }
                        }
                    }
                    //if checkSurvivability() is false, the petalvore is dead, remove it from the patch
                    deathCount++;

                    patches[i][j].removePetalvore();
                }

            }
        }
    }

}
