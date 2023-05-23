import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class toCSV {
    public void writeArrayListToCSV(ArrayList<Double> data, String csvFilePath) {
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            for (Double value : data) {
                writer.append(value.toString());
                writer.append("\n");
            }
            System.out.println("Writing to csv!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeArrayListToCSV2(ArrayList<Integer> data, String csvFilePath) {
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            for (Integer value : data) {
                writer.append(value.toString());
                writer.append("\n");
            }
            System.out.println("Writing to csv!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
