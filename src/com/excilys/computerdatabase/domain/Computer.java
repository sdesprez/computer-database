package com.excilys.computerdatabase.domain;

import java.time.LocalDate;

/**
 * Class representing a computer.
 * @author Sylvain DESPREZ
 *
 */
public class Computer {

	/**
	 * Id of the computer (generated by the database).
	 */
	protected Long id;
	/**
	 * Name of the computer (should not be null or empty).
	 */
	protected String name;
	/**
	 * Date of introduction of the computer (optional)
	 */
	protected LocalDate introducedDate;
	/**
	 * Date of discontinuation of the computer (optional)
	 */
	protected LocalDate discontinuedDate;
	protected Company company;
	
	public Computer() {

	}

	public Computer(Long id, String name, LocalDate introducedDate,
			LocalDate discontinuedDate, Company company) {
		this.id = id;
		setName(name);
		this.introducedDate = introducedDate;
		this.discontinuedDate = discontinuedDate;
		this.company = company;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	/**
	 * Check if the name is null or is empty before setting the name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getIntroducedDate() {
		return introducedDate;
	}

	public void setIntroducedDate(LocalDate introducedDate) {
		this.introducedDate = introducedDate;
	}

	public LocalDate getDiscontinuedDate() {
		return discontinuedDate;
	}

	public void setDiscontinuedDate(LocalDate discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Computer other = (Computer) obj;
		if (id.compareTo(other.id) != 0) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("Computer [id=").append(id)
				.append(", name=").append(name)
				.append(", introducedDate=").append(introducedDate)
				.append(", discontinuedDate=").append(discontinuedDate)
				.append(", company=").append(company).append("]");
		return buffer.toString();
	}

}
