public class Gaia {
    private int luminosity;
    private int size;
    private int maxAge;
    private double globalTemperature;
    private int numBlacks;
    private int numWhites;
    private String scenarioPhase;
    private Patch[][] patches;

    public Gaia(int size) {
        this.size = size;
        patches = new Patch[size][size];
    }

    public void go() {
        while (true) {
            this.update();
        }
    }

    public void update() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                patches[i][j].calcTemperature();
            }
        }

        diffuse();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (patches[i][j].updateSprout() == 1) {
                    sprout(i, j);
                }
            }
        }

        updateTemperature();
    }

    public void updateTemperature() {
    }

    public void diffuse() {
    }

    public void sprout(int x, int y) {
    }
}
