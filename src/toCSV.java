import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class toCSV {
    public void writeArrayListsToCSV(ArrayList<Double> luminosityList, ArrayList<Double> globalTemperatureList,
                                     ArrayList<Integer> whitePopulation, ArrayList<Integer> blackPopulation,
                                     String csvFilePath) {
        try {
            FileWriter csvWriter = new FileWriter(csvFilePath);

            csvWriter.append("Luminosity,GlobalTemperature,WhitePopulation,BlackPopulation");
            csvWriter.append("\n");

            int maxLength = Math.max(luminosityList.size(),
                    Math.max(globalTemperatureList.size(),
                            Math.max(whitePopulation.size(), blackPopulation.size())));

            for (int i = 0; i < maxLength; i++) {
                StringBuilder csvLine = new StringBuilder();

                if (i < luminosityList.size()) {
                    csvLine.append(luminosityList.get(i));
                }
                csvLine.append(",");

                if (i < globalTemperatureList.size()) {
                    csvLine.append(globalTemperatureList.get(i));
                }
                csvLine.append(",");

                if (i < whitePopulation.size()) {
                    csvLine.append(whitePopulation.get(i));
                }
                csvLine.append(",");

                if (i < blackPopulation.size()) {
                    csvLine.append(blackPopulation.get(i));
                }

                csvWriter.append(csvLine.toString());
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

            System.out.println("Data has been successfully written to the CSV file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
