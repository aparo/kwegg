package com.kwegg.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * a synchronized implementation of circular linked list
 * @author parag
 *
 * @param <T>
 */
public class CircularLinkedList<T> {

	public class Node<T> {

		T value;
		Node<T> next = null;

		Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
	}

	private Node<T> head = null;

	private Node<T> tail = null;

	private Node<T> current = null;

	private AtomicInteger size = new AtomicInteger(0);

	private ReentrantLock lock = new ReentrantLock();

	/**
	 * adds to the tail of the list
	 * 
	 * @param value
	 */
	public void add(T value) {
		try {
			lock.lock();
			Node<T> newNode = new Node<T>(value, head);
			if (size.get() == 0)
				head = tail = current = newNode;
			tail.next = newNode;
			tail = newNode;
			size.incrementAndGet();
		} finally {
			lock.unlock();
		}
	}

	/**
     * 
     */
	@SuppressWarnings("unused")
	private void print() {
		if (size.get() > 0) {
			String currentStr = (head == current ? "Current : " : "");
			System.out.println(currentStr + "Head : " + head.value.toString());
			Node<T> temp = head.next;
			while (temp != head) {
				if (temp == current)
					System.out.println("Current : " + temp.value.toString());
				else if (temp == tail)
					System.out.println("Tail : " + temp.value.toString());
				else
					System.out.println(temp.value.toString());
				temp = temp.next;
			}
		}
	}

	public int getSize() {
		return size.get();
	}

	/**
	 * maintains an iterator and returns the next value. This keeps moving
	 * through the circular list.
	 * 
	 * @return current.next
	 */
	public T getNext() {
		try {
			lock.lock();
			if (current != null) {
				if(current.next.value==null)
					return current.value;
				current = current.next;
				return current.value;
			}
			return current.value;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * removes the first occurence of an element
	 * 
	 * @param value
	 * @return true if the element was removed
	 */
	public boolean remove(T value) {
		try {
			lock.lock();
			if (value == null || size.get() == 0)
				return false;
			Node<T> node = null, prevNode = null;
			if (head.value.equals(value)) {
				node = head;
				prevNode = tail;
			} else {
				Node<T> temp = head;
				while (temp != tail) {
					if (temp.next.value.equals(value)) {
						node = temp.next;
						prevNode = temp;
						break;
					}
					temp = temp.next;
				}
			}
			if (node != null) {
				prevNode.next = node.next;
				if (node == tail)
					tail = prevNode;
				if (node == head)
					head = node.next;
				if (node == current)
					current = node.next;
				node = null;
				if (size.decrementAndGet() == 0) {
					head = tail = current = null;
				}
				return true;
			}
			return false;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @param sessions
	 * @return
	 */
	public T[] toArray(T[] values) {
		try {
			lock.lock();
			List<T> list = new ArrayList<T>();
			if (size.get() > 0) {
				list.add(current.value);
				Node<T> tempNode = current.next;
				while (tempNode != current) {
					list.add(tempNode.value);
					tempNode = tempNode.next;
				}
			}
			return list.toArray(values);
		} finally {
			lock.unlock();
		}
	}
}
