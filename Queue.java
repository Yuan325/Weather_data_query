package com.yuanteoh.ics440pa2;

public class Queue<T> {
	private int manyItems;
	Node<T> head, tail;
	
	public Queue() {
		head = null;
		tail = null;
		manyItems = 0;
	}
	
	
	// enqueue item at the back
	public void enqueue(T obj) {
		if (head == null) {
			Node<T> newNode = new Node<T>(obj);
			head = newNode;
			tail = newNode;
			manyItems++;
		}
		else {
			Node<T> newNode = new Node<T>(obj);
			tail.setNext(newNode);
			tail = newNode;
			manyItems++;
		}
	}
	
	public Node<T> get(int i){
		int count = 0;
		Node<T> cNode= head;
		
		if (i >= manyItems) {
			System.out.println("Node not available");
			return null;
		}
		else if (i == 0) {
			return head;
		}
		else if (i == manyItems-1) {
			return tail;
		}
		
		while(count < i) {
			cNode = cNode.getNext();
			count ++;
		}
		
		return cNode;
	}
	
	public void add(Queue<T> collection) {
		if (tail == null) {
			head = collection.getHead();
			manyItems = collection.getManyItems();
		}
		else {
			tail.setNext(collection.getHead());
			manyItems = manyItems + collection.getManyItems();
		}
		tail = collection.getTail();
	}
	
	// dequeue item from the front
	public boolean dequeue() {
		if (head == null) {
			System.out.println("Nothing to dequeue.");
			return false;
		}
		
		else if (head == tail){
			head = null;
			tail = null;
			manyItems--;
			return true;
		}
		
		else{
			head = head.getNext();
			manyItems--;
			return true;
		}
	}
	
//	public boolean remove(int i) {
//		int count = 0;
//		Node<T> cNode= head;
//		Node<T> temp = null;
//		
//		if (i >= manyItems) {
//			System.out.println("Node not available");
//			return false;
//		}
//		else if (i == 0) {
//			head = head.getNext();
//			return true;
//		}
//		while(count < i) {
//			temp = cNode;
//			cNode = cNode.getNext();
//			count ++;
//		}
//		if (i == manyItems -1) {
//			tail = temp;
//		}
//		else {
//			temp.setNext(cNode.getNext());
//		}
//		return true;
//		
//	}
	
	
	public int getManyItems() {
		return manyItems;
	}
	
	public Node<T> getHead() {
		return head;
	}
	
	public Node<T> getTail() {
		return tail;
	}
	

}
