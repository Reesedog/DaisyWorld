public class DaisyBlack extends Daisy{
    public DaisyBlack() {
        super(0);
    }

    public Daisy createDaisy(){
        return new DaisyBlack();
    }
}
