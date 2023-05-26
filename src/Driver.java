public class Driver {

    public static void main(String[] args) throws InterruptedException {
        argsHandler(args);
        Params.printParams();
        Gaia gaia = new Gaia(Params.BOARD_SIZE);
        gaia.setUp();
        gaia.go();
    }

    public static void argsHandler(String[]args){
        if(args.length == 0){
            System.out.println("Launched by default parameters!");
            return;
        }
        for (int i = 0; i < args.length; i += 2) {
            String option = args[i];
            String value = args[i + 1];
            switch (option) {
                case "-w":
                    Params.WHITE_START = (int)(Double.parseDouble(value) * (Params.BOARD_SIZE * Params.BOARD_SIZE));
                    break;
                case "-b":
                    Params.BLACK_START = (int)(Double.parseDouble(value) * (Params.BOARD_SIZE * Params.BOARD_SIZE));
                    break;
                case "-t":
                    Params.TICKS = Integer.parseInt(value);
                    break;
                case "-m":
                    Params.MAX_AGE = Integer.parseInt(value);
                    break;
                case "-wa":
                    Params.WHITE_ALBEDO = Double.parseDouble(value);
                    break;
                case "-ba":
                    Params.BLACK_ALBEDO = Double.parseDouble(value);
                    break;
                case "-sa":
                    Params.SURFACE_ALBEDO = Double.parseDouble(value);
                    break;
                case "-sl":
                    Params.SOLAR_LUMINOSITY = Double.parseDouble(value);
                    break;
                case "-s":
                    int scenarioIndex = Integer.parseInt(value);
                    String[] scenario = {"maintain-current-luminosity", "ramp-up-ramp-down",
                            "low-solar-luminosity", "our-solar-luminosity", "high-solar-luminosity"};
                    if (scenarioIndex >= 0 && scenarioIndex < scenario.length) {
                        String selectedScenario = scenario[scenarioIndex];
                        Params.SCENARIO = selectedScenario;
                        System.out.println("Selected scenario: " + selectedScenario);
                        switch (selectedScenario) {
                            case "ramp-up-ramp-down":

                                Params.SOLAR_LUMINOSITY = 0.8;
                                break;
                            case "maintain-current-luminosity":
                                //luminosity = 0.8;
                                break;
                            case "low-solar-luminosity":
                                Params.SOLAR_LUMINOSITY = 0.6;
                                break;
                            case "our-solar-luminosity":
                                Params.SOLAR_LUMINOSITY = 1.0;
                                break;
                            case "high-solar-luminosity":
                                Params.SOLAR_LUMINOSITY = 1.4;
                                break;
                        }
                    } else {
                        System.out.println("Invalid scenario index.");
                    }
                    break;
                default:
                    System.out.println("Unknown option: " + option);
                    break;
            }
        }

    }

}
