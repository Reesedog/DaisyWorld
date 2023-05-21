public class Patch {

    private double temperature;
    private double albedo = ;
    private Daisy daisy;

    public int updateSprout() {
        switch (daisy.checkSurvivability(temperature)) {
            case 1:
                return 1;             //sprout new flower
            case -1:
                this.setToEmpty();    //remove flower
                return 0;             // do nothing
            default:
                return 0;             //do nothing
        }
    }

    public void calcTemperature() {
        double luminosity = 0;

        double heat = 0;

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
}
