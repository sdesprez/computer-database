package com.excilys.computerdatabase.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Computer {

	protected Long id;
	protected String name;
	protected LocalDate introducedDate;
	protected LocalDate discontinuedDate;

	public Computer() {

	}

	public Computer(Long id, String name, LocalDate introducedDate,
			LocalDate discontinuedDate, Company company) {
		this.id = id;
		setName(name);
		this.introducedDate = introducedDate;
		this.discontinuedDate = discontinuedDate;
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

	public void setName(String name) {
		if (name == null) {
			throw new RuntimeException("Name cannot be null");
		}
		name = name.trim();
		if (name.isEmpty()) {
			throw new RuntimeException("Name cannot be empty");
		}
		this.name = name;
	}

	public LocalDate getIntroducedDate() {
		return introducedDate;
	}

	public void setIntroducedDate(LocalDate introducedDate) {
		this.introducedDate = introducedDate;
	}

	public void setIntroducedDate(Timestamp introducedDate) {
		if (discontinuedDate != null) {
			this.introducedDate = introducedDate.toLocalDateTime().toLocalDate();
		} else {
			this.introducedDate = null;
		}
	}

	public LocalDate getDiscontinuedDate() {
		return discontinuedDate;
	}

	public void setDiscontinuedDate(LocalDate discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}

	public void setDiscontinuedDate(Timestamp discontinuedDate) {
		if (discontinuedDate != null) {
			this.discontinuedDate = discontinuedDate.toLocalDateTime()
					.toLocalDate();
		} else {
			this.introducedDate = null;
		}

	}

	public Timestamp getIntroducedTimestamp() {
		if (introducedDate != null) {
			return Timestamp.valueOf(introducedDate.atStartOfDay());
		}
		return null;
	}

	public Timestamp getDiscontinuedTimestamp() {
		if (discontinuedDate != null) {
			return Timestamp.valueOf(discontinuedDate.atStartOfDay());
		}
		return null;
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
				.append(", name=").append(name).append(", introducedDate=")
				.append(introducedDate).append(", discontinuedDate=")
				.append(discontinuedDate).append("]");
		return buffer.toString();
	}

}
