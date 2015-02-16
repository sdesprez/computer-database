package com.excilys.computerdatabase.wrapper;

import java.io.Serializable;
import java.util.List;

public class ListWrapper<E> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<E> items;

	public List<E> getItems() {
		return items;
	}

	public void setItems(List<E> items) {
		this.items = items;
	}

}
