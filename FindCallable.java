package com.yuanteoh.ics440pa2;
import java.util.concurrent.Callable;

// second callable
public class FindCallable implements Callable<Queue<Data>>{

	private Queue<Data> list;
	private int temp;
	private Queue<Data> resultList;
	private int minMax;
	private int minMaxIndex;
	private int listItem = 5;


	public FindCallable(Queue<Data> list, int temp ) {
		this.list = list;
		this.temp = temp;
		this.resultList = new Queue<Data>();
	}

	public Queue<Data> call() throws Exception{
		if (list.getManyItems() < 5)
			listItem=list.getManyItems();

		// transfer first 5 item from the list to resultList and remove it from the queue
		for (int i = 0; i<listItem; i++) {
			resultList.enqueue(list.getHead().getData());
			list.dequeue();
		}
		
		for (int i = 0; i < list.getManyItems(); i++) {
			getMinMax();
			if (temp == 1) {
				// if the value is greater than the current min in finalList, replace it
				if (list.getHead().getData().getValue() > minMax) {
					resultList.get(minMaxIndex).setData(list.getHead().getData());
				}
				list.dequeue();
			}
			else if (temp == 0) {
				// if the value is smaller than the current max in finalList, replace it
				if (list.getHead().getData().getValue() < minMax) {
					resultList.get(minMaxIndex).setData(list.getHead().getData());
				}
				list.dequeue();
			}
			else {
				list.dequeue();
			}
		}
		
		
		return resultList;
	}

	public void getMinMax(){
		// if temp == 1, user trying to acquire maximum temperature, if temp == 0, user trying to acquire minimum temperature
		minMax = resultList.getHead().getData().getValue();
		minMaxIndex = 0;
		for (int i = 0 ; i < resultList.getManyItems(); i++) {
			if (temp == 1) {
				// get the minimum value from the final list
				if (resultList.get(i).getData().getValue() < minMax) {
					minMax = resultList.get(i).getData().getValue();
					minMaxIndex = i;
				}
			}
			if (temp == 0) {
				// get the maximum value from the final list
				if (resultList.get(i).getData().getValue() > minMax) {
					minMax = resultList.get(i).getData().getValue();
					minMaxIndex = i;
				}
			}
		}
	}
}
