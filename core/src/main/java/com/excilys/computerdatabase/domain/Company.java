package com.excilys.computerdatabase.domain;



/**
 * Class representing a company.
 */
public class Company {


	/**
	 * Id of the company (generated by the database)
	 */
	protected long id;
	/**
	 * Name of the company
	 */
	protected String name;

	public Company() {
	}

	public Company(final String name) {
		this.name = name;
	}

	public Company(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		final Company other = (Company) obj;
		if (id != other.id) {
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
		final StringBuffer buffer = new StringBuffer("Company [id=").append(id)
				.append(", name=").append(name).append("]");
		return buffer.toString();
	}

	
	public static final class Builder {
		private Company company;
		
		private Builder() {
			company = new Company();
		}
		
		public Builder id(final Long id) {
			company.id = id;
			return this;
		}
		
		public Builder name(final String name) {
			company.name = name;
			return this;
		}
		
		public Company build() {
			return company;
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
}
