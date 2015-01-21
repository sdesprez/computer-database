package com.excilys.computerdatabase.domain;

import java.util.List;

import com.excilys.computerdatabase.dao.impl.ColumnNames;

/**
 * Class representing a Page of results from an SQL request
 */
public class Page<T> {

	/**
	 * Number of the page (1 by default)
	 */
	private int pageNumber;
	/**
	 * List of elements of the page
	 */
	private List<T> list;
	/**
	 * Max number of elements the page can have
	 */
	private int nbResultsPerPage;
	/**
	 * Total number of elements the SQL could return (20 by default)
	 */
	private int nbResults;

	private int nbPages;
	
	private String search;
	
	private ColumnNames order;
	
	
	
	public Page() { 
		pageNumber = 1;
		nbResultsPerPage = 10;
		search = "";
		order = ColumnNames.ID;
	}

	public Page(final int pageNumber, final List<T> list,
			final int nbResultsPerPage, final int nbResults, final int nbPages, final String search, final ColumnNames order) {
		this.pageNumber = pageNumber;
		this.list = list;
		this.nbResultsPerPage = nbResultsPerPage;
		this.nbResults = nbResults;
		this.nbPages = nbPages;
		this.search = search;
		this.order = order;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(final int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(final List<T> list) {
		this.list = list;
	}

	public int getNbResultsPerPage() {
		return nbResultsPerPage;
	}

	public void setNbResultsPerPage(final int nbResultsPerPage) {
		this.nbResultsPerPage = nbResultsPerPage;
	}

	public int getNbResults() {
		return nbResults;
	}

	public void setNbResults(final int nbResults) {
		this.nbResults = nbResults;
	}
	
	public ColumnNames getOrder() {
		return order;
	}

	public void setOrder(final ColumnNames order) {
		this.order = order;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(final String search) {
		this.search = search;
	}

	public int getNbPages() {
		return nbPages;
	}

	public void setNbPages(final int nbPages) {
		this.nbPages = nbPages;
	}

	/**
	 * Increment the pageNumber if there is more pages.
	 * @return true if there is a next page
	 */
	public boolean nextPage() {
		//Check if there is a next page before incrementing
		if (pageNumber < nbPages) {
			pageNumber++;
			return true;
		}
		return false;
	}
	
	/**
	 * Decrement the pageNumber if there is a previous page.
	 * @return true if there is a previous page.
	 */
	public boolean previousPage() {
		//Check if there is a previous page before decrementing
		if (pageNumber > 1) {
			pageNumber--;
			return true;
		}
		return false;
	}

	public void refreshNbPages() {
		if (nbResultsPerPage != 0) {
			nbPages = nbResults / nbResultsPerPage;
			if (nbResults % nbResultsPerPage != 0) {
				nbPages++;
			}
		} else {
			nbPages = 0;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + nbPages;
		result = prime * result + nbResults;
		result = prime * result + nbResultsPerPage;
		result = prime * result + pageNumber;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final Page<T> other = (Page<T>) obj;
		if (list == null) {
			if (other.list != null) {
				return false;
			}
		} else if (!list.equals(other.list)) {
			return false;
		}
		if (nbPages != other.nbPages) {
			return false;
		}
		if (nbResults != other.nbResults) {
			return false;
		}
		if (nbResultsPerPage != other.nbResultsPerPage) {
			return false;
		}
		if (pageNumber != other.pageNumber) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Page [pageNumber=" + pageNumber + ", list=" + list
				+ ", nbResultsPerPage=" + nbResultsPerPage + ", nbResults="
				+ nbResults + ", nbPages=" + nbPages + ", search=" + search + ", order=" + order + "]";
	}

	
	
	
}
