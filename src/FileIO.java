import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileIO {

	public ArrayList<String> readFile(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			return null;
		}
		Scanner in = null;
		try {
			ArrayList<String> lines = new ArrayList<String>();
			FileReader reader = new FileReader(file);
			in = new Scanner(reader);
			while (in.hasNextLine()) {
				lines.add(in.nextLine());
			}

			return lines;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		} finally {
			if (in != null)
				in.close();
		}
	}
}