public class DaisyWhite extends Daisy {
    public DaisyWhite() {
        super(0);
    }

    public Daisy createDaisy(){
        return new DaisyWhite();
    }
}
