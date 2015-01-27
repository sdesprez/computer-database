package com.excilys.computerdatabase.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.utils.Validator;

public class ComputerDTOConverter {
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String INTRODUCED_DATE = "iDate";
	private static final String DISCONTINUED_DATE = "dDate";
	private static final String COMPANY_ID = "companyID";
	
	
	
	/**
	 * Create a Computer from a ComputerDTO
	 * @param dto The ComputerDTO to be converted
	 * @return A Computer corresponding to the ComputerDTO or null if the ComputerDTO wasn't valid
	 */
	public static Computer fromDTO(final ComputerDTO dto, final CompanyDBService companyDBService) {
		final Computer.Builder builder = Computer.builder();
		builder.id(dto.getId())
				.name(dto.getName());
		
		if (dto.getIntroduced() != null && !dto.getIntroduced().trim().isEmpty()) {
			builder.introducedDate(LocalDate.parse(dto.getIntroduced(), DateTimeFormatter.ISO_LOCAL_DATE));
		}
		if (dto.getDiscontinued() != null && !dto.getDiscontinued().trim().isEmpty()) {
			builder.discontinuedDate(LocalDate.parse(dto.getDiscontinued(), DateTimeFormatter.ISO_LOCAL_DATE));
		}
		if (dto.getCompany() != 0) {
			builder.company(companyDBService.getById(dto.getCompany()));
		}
		return builder.build();
	}
	

	/**
	 * Create a List of Computer from a List of ComputerDTO
	 * @param dto The List of ComputerDTO to be converted
	 * @return The List of Computer corresponding to the List of ComputerDTO who are valid or an empty List if none were valid
	 */
	public static List<Computer> fromDTO(final List<ComputerDTO> dtos, final CompanyDBService companyDBService) {
		final List<Computer> computers = dtos.stream().map(dto -> {
			final Computer computer = ComputerDTOConverter.fromDTO(dto, companyDBService);
			if (computer != null) {
				return computer;
			}
			return null;
		}).collect(Collectors.toList());
		return computers;
	}
	
	
	/**
	 * Create a ComputerDTO from a Computer
	 * @param computer to be converted
	 * @return The ComputerDTO corresponding to the Computer or null if the computer was null
	 */
	public static ComputerDTO toDTO(final Computer computer) {
		if (computer == null) {
			return null;
		}
		final ComputerDTO.Builder builder = ComputerDTO.builder();
		builder.id(computer.getId())
				.name(computer.getName());
		
		if (computer.getIntroducedDate() != null) {
			builder.introduced(computer.getIntroducedDate().toString());
		}
		if (computer.getDiscontinuedDate() != null) {
			builder.discontinued(computer.getDiscontinuedDate().toString());
		}		
		if (computer.getCompany() != null) {
			builder.company(computer.getCompany().getId());
		} else {
			builder.company(0);
		}
		
		return builder.build();
	}
	
	/**
	 * Create a List of ComputerDTO from a List of Computer
	 * @param List of Computer to be converted
	 * @return The List ComputerDTO corresponding to the List Computer of computers who are valid or an empty List if none were valid
	 */
	public static List<ComputerDTO> toDTO(final List<Computer> computers) {
		final List<ComputerDTO> dtos = computers.stream().map(computer -> {
			final ComputerDTO dto = ComputerDTOConverter.toDTO(computer);
			if (dto != null) {
				return dto;
			}
			return null;
		}).collect(Collectors.toList());
		return dtos;
	}
	
	
	public static boolean validate(final ComputerDTO dto, final Map<String, String> error) {
		boolean valid = true;
		if (dto == null) {
			return false;
		}
		if (dto.getId() < 0) {
			error.put(ID, "Incorrect id : an id should be a long");
			valid = false;
		}
		if (dto.getName() == null || dto.getName().trim().isEmpty()) {
			error.put(NAME, "Incorrect name : a name can't be empty or only spaces");
			valid = false;
		}

		if (dto.getIntroduced() != null && !dto.getIntroduced().trim().isEmpty()) {
			if (!Validator.isDate(dto.getIntroduced())) {
				error.put(INTRODUCED_DATE, "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-18. You can also leave this field emtpy");
				valid = false;
			}
		}
		if (dto.getDiscontinued() != null && !dto.getDiscontinued().trim().isEmpty()) {
			if (!Validator.isDate(dto.getDiscontinued())) {
				error.put(DISCONTINUED_DATE, "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-18. You can also leave this field emtpy");
				valid = false;
			}
		}
		if (dto.getCompany() < 0) {
			error.put(COMPANY_ID, "Incorrect Company identifier");
			valid = false;
		}
		return valid;
	}
}
