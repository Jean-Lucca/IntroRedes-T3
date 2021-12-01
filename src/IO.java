import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IO {
    
    public static String inputFile(String file) {
        String fileName = "../input/"+file;
        String line = null;
        StringBuffer buffer = new StringBuffer();
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null){
                buffer.append(line+"\n");
            }

        } catch (IOException ex){
            System.out.println("Erro");
        }
        return buffer.toString();
    }

	public static void output(String str) {
        try (FileWriter writer = new FileWriter("../output/Output.txt", true);
            BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(str);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}
