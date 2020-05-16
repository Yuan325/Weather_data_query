package com.yuanteoh.ics440pa2;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Driver {
	static int minMax;
	static int minMaxIndex;
	static Queue<Data> finalList = new Queue<Data>();

	public static void main(String[] args) throws FileNotFoundException {
		Scanner input = new Scanner(System.in);
		String thisLine;

		int startYear = 0;
		int endYear = 0;
		int startMonth = 0;
		int endMonth = 0;
		int temp;
		String id;
		String fileName;
		File file2;

		// start asking information from user
		System.out.println("Please enter the starting year: ");
		startYear = input.nextInt();

		System.out.println("\nPlease enter ending year: ");
		endYear = input.nextInt();

		System.out.println("\nPlease enter starting month: ");
		startMonth = input.nextInt();

		System.out.println("\nPlease enter ending month: ");
		endMonth = input.nextInt();

		System.out.println("\nWould you like to calculate the maximum (enter 1) or minimum temperature (enter 0)? ");
		temp = input.nextInt();
		while (temp != 1 && temp != 0) {
			System.out.println("\nWould you like to calculate the maximum (enter 1) or minimum temperature (enter 0)? ");
			temp = input.nextInt();
		}

		
		ExecutorService executor = Executors.newFixedThreadPool(8);
		List<Future<Queue<Data>>> listFuture1 = new ArrayList<Future<Queue<Data>>>();
		Callable<Queue<Data>> callable;
		Queue<Data> combineList = new Queue<Data>();

		// get station data
		File file = new File("weather_data/ghcnd-stations.txt");
		Scanner scan = new Scanner(file);

		while (scan.hasNextLine()) {
			thisLine = scan.nextLine();
			// ignore lines that is not data from the US
			if (!thisLine.substring(0,3).equalsIgnoreCase("USC") && !thisLine.substring(0,3).equalsIgnoreCase("USW")) {
			}else {
				id = thisLine.substring(0,11);
				fileName = id + ".dly";
				file2 = new File("weather_data/ghcnd_hcn/ghcnd_hcn/"+ fileName);
				// assign future only if the corresponding weather file is available
				if (file2.exists() == true) {

					Data sd = new Data(); 
					sd.setId(thisLine.substring(0,11));
					sd.setLatitude(Float.valueOf(thisLine.substring(12,20).trim())); 
					sd.setLongitude(Float.valueOf(thisLine.substring(21,30).trim()));
					sd.setElevation(Float.valueOf(thisLine.substring(31,37).trim()));
					sd.setState(thisLine.substring(38,40));
					sd.setName(thisLine.substring(41,71)); 

					// create callable and submit to future
					callable = new DataCallable(sd, fileName, startYear, endYear, startMonth, endMonth, temp);
					Future<Queue<Data>> future = executor.submit(callable);
					
					listFuture1.add(future);
				}
			}
		}
		scan.close();
		input.close();

		// combining first future list
		for (Future<Queue<Data>> fut: listFuture1) {
			try {
				if (fut.get().getManyItems() != 0) {
					combineList.add(fut.get());
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		// dividing combined list into 4 parts and send it to 4 futures
		List<Future<Queue<Data>>> listFuture2 = new ArrayList<Future<Queue<Data>>>();
		int divide = combineList.getManyItems()/4;
		Queue<Data> list1 = new Queue<Data>();
		Queue<Data> list2 = new Queue<Data>();
		Queue<Data> list3 = new Queue<Data>();
		for (int i = 0; i < divide ; i++) {
			list1.enqueue(combineList.getHead().getData());
			combineList.dequeue();
			list2.enqueue(combineList.getHead().getData());
			combineList.dequeue();
			list3.enqueue(combineList.getHead().getData());
			combineList.dequeue();
		}
		
		Queue<Queue<Data>> listQ = new Queue<Queue<Data>>();
		listQ.enqueue(list1);
		listQ.enqueue(list2);
		listQ.enqueue(list3);
		listQ.enqueue(combineList);
		
		// create callable and submit to future
		for (int i = 0; i < 4; i++) {
			callable = new FindCallable(listQ.get(i).getData(), temp); 
			Future<Queue<Data>> future = executor.submit(callable);
			listFuture2.add(future);
		}
		
		// combining second list that consist of 20 data
		Queue<Data> combineList2 = new Queue<Data>();
		for (Future<Queue<Data>> fut : listFuture2) {
			try {
				combineList2.add(fut.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		// choose 5 data (max or min) from the final 20 data and save it to finalList
		int listItem = 5;
		if (combineList2.getManyItems() <5)
			listItem = combineList2.getManyItems();
		
		for (int i = 0; i<listItem; i++) {
			finalList.enqueue(combineList2.getHead().getData());
			combineList2.dequeue();
		}
		minMax(temp);
		
		int count = combineList2.getManyItems();
		for (int i = 0; i< count; i++) {
			minMax(temp);
			if (temp == 1) {
				// if the value is greater than the current min in finalList, replace it
				if (combineList2.getHead().getData().getValue() > minMax) {
					finalList.get(minMaxIndex).setData(combineList2.getHead().getData());
				}
				combineList2.dequeue();
			}
			else if (temp == 0) {
				// if the value is smaller than the current max in finalList, replace it
				if (combineList2.getHead().getData().getValue() < minMax) {
					finalList.get(minMaxIndex).setData(combineList2.getHead().getData());
				}
				combineList2.dequeue();
			}
			else {
				combineList2.dequeue();
			}
		}
		
		// print result from finalList
		System.out.println("\n\n ----- RESULTS ----- \n");
		Data cNodeData;
		for (int i = 0; i < finalList.getManyItems(); i++) {
			cNodeData = finalList.get(i).getData();
			System.out.println("id=" + cNodeData.getId() + " year=" + cNodeData.getYear() + " month=" + cNodeData.getMonth() + " day=" + cNodeData.getDay() + " element=" + cNodeData.getElement() + " value=" + cNodeData.getValue()/10.0 + "C qflag=" + cNodeData.getQflag());
			System.out.println("latitude=" + cNodeData.getLatitude() + " longitude=" + cNodeData.getLongitude() + " elevation=" + cNodeData.getElevation() + " state=" + cNodeData.getState() + " name=" + cNodeData.getName());
			System.out.println("\n");
		}

	}

	
	public static void minMax(int temp) {
		// if temp == 1, user trying to acquire maximum temperature, if temp == 0, user trying to acquire minimum temperature
		minMax = finalList.getHead().getData().getValue();
		minMaxIndex = 0;
		for (int i = 0; i < finalList.getManyItems(); i++) {
			if (temp == 1) {
				// get the minimum value from the final list
				if (finalList.get(i).getData().getValue() < minMax) {
					minMax= finalList.get(i).getData().getValue();
					minMaxIndex = i;
				}
			}
			if (temp == 0) { 
				// get the maximum value from the final list
				if (finalList.get(i).getData().getValue() > minMax) {
					minMax= finalList.get(i).getData().getValue();
					minMaxIndex = i;
				}
			}
		}
	}

}