public class DaisyBlack extends Daisy{
    public DaisyBlack() {
        super(Params.BLACK_ALBEDO);
    }

    public Daisy createDaisy(){
        return new DaisyBlack();
    }
}
