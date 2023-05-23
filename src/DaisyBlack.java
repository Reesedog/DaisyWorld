public class DaisyBlack extends Daisy{
    public DaisyBlack() {
        super(Params.BLACK_ALBEDO);
    }

    public DaisyBlack(int age) {
        super(Params.BLACK_ALBEDO, age);
    }

    public Daisy createDaisy(){
        return new DaisyBlack();
    }
}
