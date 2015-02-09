package com.excilys.computerdatabase.dto;

public class ComputerDTO {

	private long id;

	private String name;
	
	private String introduced;

	private String discontinued;

	private long company;

	public ComputerDTO() {

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

	public String getIntroduced() {
		return introduced;
	}

	public void setIntroduced(final String introduced) {
		this.introduced = introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(final String discontinued) {
		this.discontinued = discontinued;
	}

	public long getCompany() {
		return company;
	}

	public void setCompany(final long company) {
		this.company = company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (company ^ (company >>> 32));
		result = prime * result
				+ ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ComputerDTO [id=").append(id).append(", name=")
				.append(name).append(", introduced=").append(introduced)
				.append(", discontinued=").append(discontinued)
				.append(", company=").append(company).append("]");
		return builder.toString();
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
		final ComputerDTO other = (ComputerDTO) obj;
		if (company != other.company) {
			return false;
		}
		if (discontinued == null) {
			if (other.discontinued != null) {
				return false;
			}
		} else if (!discontinued.equals(other.discontinued)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (introduced == null) {
			if (other.introduced != null) {
				return false;
			}
		} else if (!introduced.equals(other.introduced)) {
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

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private ComputerDTO computerDTO;

		private Builder() {
			computerDTO = new ComputerDTO();
		}

		public Builder id(final long id) {
			computerDTO.id = id;
			return this;
		}

		public Builder name(final String name) {
			computerDTO.name = name;
			return this;
		}

		public Builder introduced(final String introduced) {
			computerDTO.introduced = introduced;
			return this;
		}

		public Builder discontinued(final String discontinued) {
			computerDTO.discontinued = discontinued;
			return this;
		}

		public Builder company(final long company) {
			computerDTO.company = company;
			return this;
		}

		public ComputerDTO build() {
			return computerDTO;
		}
	}
}
