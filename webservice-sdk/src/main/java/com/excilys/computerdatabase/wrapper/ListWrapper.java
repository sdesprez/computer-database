package com.excilys.computerdatabase.wrapper;

import java.util.List;

/**
 * Class to wrap a List for a jax-ws
 *
 * @param <E> : type of objects contained inside the list 
 */
public class ListWrapper<E> {
	
	private List<E> items;

	public List<E> getItems() {
		return items;
	}

	public void setItems(List<E> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListWrapper [items=").append(items).append("]");
		return builder.toString();
	}
}
