public class Patch {

    private double temperature;
    private double albedo = Params.SURFACE_ALBEDO;
    private Daisy daisy;
    private boolean checkState;

    //Extension
    private Petalvore petalvore;


    Patch() {
        this.temperature = 22.5;
        this.daisy = null;
        this.checkState = false;

        //Extension
        this.petalvore = null;
    }

    public int updateSprout() {
        switch (daisy.checkSurvivability(temperature)) {
            case 1:
                return 1;             //sprout new flower
            case -1:
                this.setToEmpty();    //remove flower
//                System.out.println("D");
                return -1;             // do nothing
            default:
                return 0;             //do nothing
        }
    }

    public void calcTemperature() {
        double luminosity = 0;

        double heat = 0;

        if(petalvore != null){
            temperature = 65;
            return;
        }

        if (daisy != null) {
            luminosity = (1 - daisy.getAlbedo()) * Params.SOLAR_LUMINOSITY;
        } else {
            luminosity = (1 - this.albedo) * Params.SOLAR_LUMINOSITY;
        }

        if (luminosity > 0) {
            heat = 72 * (Math.log(luminosity)) + 80;
        } else {
            heat = 80;
        }
        temperature = (heat + temperature) / 2;
    }

    public void setToBlack() {
        this.daisy = new DaisyBlack();
    }

    public void setToWhite() {
        this.daisy = new DaisyWhite();
    }

    public void setToEmpty() {
        this.daisy = null;
    }

    public void setDaisy(Daisy daisy) {
        this.daisy = daisy;
    }

    public Daisy getDaisy() {
        return daisy;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isCheckState() {
        return checkState;
    }

    public void setCheckState(boolean checkState) {
        this.checkState = checkState;
    }


    //Extension



    public Petalvore getPetalvore() {
        return petalvore;
    }

    public void setPetalvore(Petalvore petalvore) {
        this.petalvore = petalvore;
    }

    public int checkDiet(int globalTemperature){
        return petalvore.checkDiet(temperature, globalTemperature);
    }

    public void removePetalvore(){
        this.petalvore = null;
    }

    public boolean petalvoreSprout(){
        return petalvore.ableToSprout();
    }


}
