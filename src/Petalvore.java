import java.util.Random;

public class Petalvore {
    private int age;
    private int diet;
    private int satiety;

    public Petalvore(){
    }

    public Petalvore(int satiety){
        this.satiety = satiety;
    }

    /**
     * Petalvore will die when reaching the PETALVORE_MAX_AGE;
     * And
     * Petalvore will die when satiety drops to zero;
     * @return true: alive;  false: dead
     * */
    public boolean checkSurvivability() {
        age = age + 1;
        satiety = satiety - 1;
        if(age >= Params.PETALVORE_MAX_AGE || satiety <= 0){
            return false;
        }
        return true;
    }

    /**
     * when local patch temperature greater than globalTemperature
     * Petalvore change diet to eat Black Daisy to cool down the local patch
     * @return 1: eat black daisy;  0: eat white daisy
     * */
    public int checkDiet(double temperature, double globalTemperature){
        if(temperature >= globalTemperature){
            return 1;
        }
        return 0;
    }

    /**
     * Petalvore can only sprout when its satiety greater than threshold.
     * @return true: able to sprout;  false: not able to sprout.
     * */
    public boolean ableToSprout(){
        if(satiety >= Params.PETALVORE_SPROUT_THRESHOLD){
            return true;
        }
        return false;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int isDiet() {
        return diet;
    }

    public void setDiet(int diet) {
        this.diet = diet;
    }

    public int getSatiety() {
        return satiety;
    }

    public void eat(int daisy) {
        this.satiety = satiety + daisy;
    }

    public void sprout(){
        this.satiety = satiety / 2;
    }
}
