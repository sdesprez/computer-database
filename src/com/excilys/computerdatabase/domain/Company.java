package com.excilys.computerdatabase.domain;

import java.util.ArrayList;
import java.util.List;

public class Company {

	protected long id;
	protected String name;
	protected List<Computer> computers;

	public Company() {
		computers = new ArrayList<Computer>();
	}

	public Company(String name, List<Computer> computers) {
		this.name = name;
		this.computers = computers;
	}

	public Company(long id, String name, List<Computer> computers) {
		this.id = id;
		this.name = name;
		this.computers = computers;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Computer> getComputers() {
		return computers;
	}

	public void setComputers(List<Computer> computers) {
		this.computers = computers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addComputer(Computer computer) {
		computers.add(computer);
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
		StringBuffer buffer = new StringBuffer("Company [id=").append(id)
				.append(", name=").append(name).append(", computers=")
				.append(computers).append("]");
		return buffer.toString();
	}

}
