package com.excilys.computerdatabase.domain;

import java.util.List;

/**
 * Class representing a Page of results from an SQL request
 * @author Sylvain DESPREZ
 */
public class Page {

	/**
	 * Number of the page (1 by default)
	 */
	private int pageNumber;
	/**
	 * List of elements of the page
	 */
	private List<?> list;
	/**
	 * Max number of elements the page can have
	 */
	private int nbResultsPerPage;
	/**
	 * Total number of elements the SQL could return (20 by default)
	 */
	private int nbResults;

	public Page() { 
		pageNumber = 1;
		nbResultsPerPage = 20;
	}

	public Page(int pageNumber, List<?> list,
			int nbResultsPerPage, int nbResults) {
		this.pageNumber = pageNumber;
		this.list = list;
		this.nbResultsPerPage = nbResultsPerPage;
		this.nbResults = nbResults;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public int getNbResultsPerPage() {
		return nbResultsPerPage;
	}

	public void setNbResultsPerPage(int nbResultsPerPage) {
		this.nbResultsPerPage = nbResultsPerPage;
	}

	public int getNbResults() {
		return nbResults;
	}

	public void setNbResults(int nbResults) {
		this.nbResults = nbResults;
	}
	
	/**
	 * Increment the pageNumber if there is more pages.
	 * @return true if there is a next page
	 */
	public boolean nextPage() {
		//Check if there is a next page befor incrementing
		if (pageNumber * nbResultsPerPage < nbResults) {
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
}
