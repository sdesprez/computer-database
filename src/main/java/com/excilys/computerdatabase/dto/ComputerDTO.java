package com.excilys.computerdatabase.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class ComputerDTO {

	@Min(value = 0, message = "Incorrect Computer identifier")
	private long id;

	@NotNull(message = "Incorrect name : a name can't be empty or only spaces")
	@NotBlank(message = "Incorrect name : a name can't be empty or only spaces")
	private String name;
	
	@Pattern(regexp = "(" + "^\\s*$"
			+ "|((\\d{4})([-])(0[13578]|10|12)([-])(0[1-9]|[12][0-9]|3[01]))"
			+ "|((\\d{4})([-])(0[469]|11)([-])([0][1-9]|[12][0-9]|30))"
			+ "|((\\d{4})([-])(02)([-])(0[1-9]|1[0-9]|2[0-8]))"
			+ "|(([02468][048]00)([-])(02)([-])(29))"
			+ "|(([13579][26]00)([-])(02)([-])(29))"
			+ "|(([0-9][0-9][0][48])([-])(02)([-])(29))"
			+ "|(([0-9][0-9][2468][048])([-])(02)([-])(29))"
			+ "|(([0-9][0-9][13579][26])([-])(02)([-])(29))" + ")",
			message = "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-18. You can also leave this field emtpy")
	private String introduced;

	@Pattern(regexp = "(" + "^\\s*$"
			+ "|((\\d{4})([-])(0[13578]|10|12)([-])(0[1-9]|[12][0-9]|3[01]))"
			+ "|((\\d{4})([-])(0[469]|11)([-])([0][1-9]|[12][0-9]|30))"
			+ "|((\\d{4})([-])(02)([-])(0[1-9]|1[0-9]|2[0-8]))"
			+ "|(([02468][048]00)([-])(02)([-])(29))"
			+ "|(([13579][26]00)([-])(02)([-])(29))"
			+ "|(([0-9][0-9][0][48])([-])(02)([-])(29))"
			+ "|(([0-9][0-9][2468][048])([-])(02)([-])(29))"
			+ "|(([0-9][0-9][13579][26])([-])(02)([-])(29))" + ")",
			message = "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-18. You can also leave this field emtpy")
	private String discontinued;

	@Min(value = 0, message = "Incorrect Company identifier")
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
