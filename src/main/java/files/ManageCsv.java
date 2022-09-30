package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class ManageCsv {
	String uploadFilePath;
	String[][] uploadArr;
	String[][] downloadArr;
	FileReader userFile;

	public void writeDataToUpload(String filePath) {
		try {
			System.out.println("filePath: " + filePath);
			this.uploadFilePath = filePath;
			File file = new File(filePath);
			FileWriter uploadFile = new FileWriter(file);

			CSVWriter writer = new CSVWriter(uploadFile);

			String[] header = { "First Name", "Last Name" };
			writer.writeNext(header);

			writer.writeNext(new String[] { "Tasneem", "Zahdeh" });
			writer.writeNext(new String[] { "Sarah", "Winner" });
			System.out.println("In the CSV File!");

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readFile(String filePath) throws FileNotFoundException {
		this.userFile = new FileReader(filePath);
		return "";
	}

	public int getFileNumnerOfRows(String filePath, boolean isDownload) {
		try {
			FileReader userFile;
			if (isDownload) {
				userFile = this.userFile;
			} else {
				userFile = new FileReader(filePath);
			}
			CSVReader csvReader = new CSVReader(userFile);

			List<String[]> fileRows = csvReader.readAll();

			int totalRows = fileRows.size();
			if (isDownload) {
				this.downloadArr = new String[totalRows][1];
			} else {
				this.uploadArr = new String[totalRows][1];
			}

			int numOfRows = 0;
			for (String[] row : fileRows) {
				if (isDownload) {
					this.downloadArr[numOfRows] = row;
				} else {
					this.uploadArr[numOfRows] = row;
				}
				numOfRows++;
			}

			csvReader.close();
			System.out.println("The File number of rows: " + totalRows);
			return totalRows;
		} catch (CsvException | IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public String getFileValue(int index, boolean isDownload) {
		return isDownload ? downloadArr[index][0] : uploadArr[index][0];
	}
}
