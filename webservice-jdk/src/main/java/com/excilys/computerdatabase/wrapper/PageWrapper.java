package com.excilys.computerdatabase.wrapper;

import java.util.List;

/**
 * Class to wrap a page for a jax-ws
 *
 * @param <E> : type of objects contained inside the page
 */
public class PageWrapper<E> {

	private int page;
	
	private int size;
	
	private long totalElements;
	
	private ListWrapper<E> content;
	
	public PageWrapper() {
		page = 0;
		size = 20;
		content = new ListWrapper<E>();
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public ListWrapper<E> getContent() {
		return content;
	}

	public void setContent(ListWrapper<E> content) {
		this.content = content;
	}
	
	public void setContent(List<E> items) {
		if (this.content != null) {
			this.content.setItems(items);
		} else {
			this.content = new ListWrapper<E>();
			this.content.setItems(items);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PageWrapper [page=").append(page).append(", size=")
				.append(size).append(", totalElements=").append(totalElements)
				.append(", content=").append(content).append("]");
		return builder.toString();
	}
	
	
	public int getNextPage() {
		if (size != 0 && page < ((float)totalElements / (float)size) - 1) {
			return page + 1;
		}
		return page;
	}
	
	public int getPreviousPage() {
		if (page > 0) {
			return page - 1;
		}
		return page;
	}
	
	public boolean hasNext() {
		if (this.page - getNextPage() >= 0) {
			return false;
		}
		return true;
	}
	
	public boolean hasPrevious() {
		if (this.page > 0) {
			return true;
		}
		return false;
	}
}
