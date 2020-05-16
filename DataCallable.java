package com.yuanteoh.ics440pa2;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Callable;

// first callable
public class DataCallable implements Callable<Queue<Data>> {

	private Data data;
	private Queue<Data> collection;
	private String stationName;
	private int startYear = 0;
	private int endYear = 0;
	private int startMonth = 0;
	private int endMonth = 0;
	private int temp;

	private int minMax;
	private int minMaxIndex;

	public DataCallable(Data data, String stationName, int startYear, int endYear, int startMonth, int endMonth, int temp) {
		this.data = data;
		this.stationName = stationName;
		this.startYear = startYear;
		this.endYear = endYear;
		this.startMonth = startMonth;
		this.endMonth = endMonth;
		this.temp = temp;
		this.collection = new Queue<Data>();
	}

	public Queue<Data> call() throws Exception{
		File file = new File("weather_data/ghcnd_hcn/ghcnd_hcn/"+ stationName);

		Scanner scan = new Scanner(file);
		String thisLine;

		while(scan.hasNextLine()) {

			thisLine = scan.nextLine();
			int year = Integer.valueOf(thisLine.substring(11,15).trim());
			int month = Integer.valueOf(thisLine.substring(15,17).trim());
			String element = thisLine.substring(17,21);
			int days =(thisLine.length() - 21) / 8; // Calculate the number of days in the line

			String targetElement = null;
			if (temp == 1) 
				targetElement = "TMAX";
			else if (temp == 0)
				targetElement = "TMIN";
			else {
				System.out.println(" no element found ");
			}

			if (element.equalsIgnoreCase(targetElement) && year >= startYear && year <= endYear && month >= startMonth && month <= endMonth) {
				for (int i = 0; i < days; i++) {         // Process each day in the line.
					int value = Integer.valueOf(thisLine.substring(21+8*i,26+8*i).trim());
					String qflag = thisLine.substring(27+8*i,28+8*i);
					// if qflag is anything else than space, or value is invalid (-9999), skip
					if (!qflag.equals(" ") || value == -9999) {
						break;
					}
					// prepare initial collection data
					if (collection.getManyItems() < 5) {
						Data tempData = data.clone();
						tempData.setDay(i + 1);
						tempData.setYear(year);
						tempData.setMonth(month);
						tempData.setElement(element);
						tempData.setValue(value);
						tempData.setQflag(qflag);
						collection.enqueue(tempData);
					}
					else {
						getMinMax();
						if (temp == 1) {
							// only if value is greater than the minimum value in the collection, replace it
							if (value > minMax) {
								Data tempData = collection.get(minMaxIndex).getData();
								tempData.setDay(i + 1);
								tempData.setYear(year);
								tempData.setMonth(month);
								tempData.setElement(element);
								tempData.setValue(value);
								tempData.setQflag(qflag);

							}
						}

						if (temp == 0) {
							// only if value is smaller than the maximum value in the collection, replace it
							if (value < minMax) {
								Data tempData = collection.get(minMaxIndex).getData();
								tempData.setDay(i + 1);
								tempData.setYear(year);
								tempData.setMonth(month);
								tempData.setElement(element);
								tempData.setValue(value);
								tempData.setQflag(qflag);
							}
						}

					}
				}
			}
		}
		scan.close();

		return collection;
	}

	public void getMinMax(){
		// if temp == 1, user trying to acquire maximum temperature, if temp == 0, user trying to acquire minimum temperature
		minMax = collection.getHead().getData().getValue();
		minMaxIndex = 0;
		for (int i = 0 ; i < 5; i++) {
			if (temp == 1) {
				// get the minimum value from the final list
				if (collection.get(i).getData().getValue() < minMax) {
					minMax = collection.get(i).getData().getValue();
					minMaxIndex = i;
				}
			}
			if (temp == 0) {
				// get the maximum value from the final list
				if (collection.get(i).getData().getValue() > minMax) {
					minMax = collection.get(i).getData().getValue();
					minMaxIndex = i;
				}
			}
		}
	}
}