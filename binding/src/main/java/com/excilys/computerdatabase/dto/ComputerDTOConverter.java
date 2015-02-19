package com.excilys.computerdatabase.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.validator.GenericValidator;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;


public class ComputerDTOConverter {
	
	
	/**
	 * Create a Computer from a ComputerDTO
	 * @param dto The ComputerDTO to be converted
	 * @param companyDBService CompanyDBService used to get the correct Company from the Database
	 * @param dateFormat Format of the LocalDate to parse
	 * @return A Computer corresponding to the ComputerDTO
	 */
	public static Computer fromDTO(final ComputerDTO dto, final String dateFormat) {
		final Computer.Builder builder = Computer.builder();
		builder.id(dto.getId()).name(dto.getName().trim());
		
		if (!GenericValidator.isBlankOrNull(dto.getIntroduced())) {
			builder.introduced(LocalDate.parse(dto.getIntroduced(), DateTimeFormatter.ofPattern(dateFormat)));
		}
		if (!GenericValidator.isBlankOrNull(dto.getDiscontinued())) {
			builder.discontinued(LocalDate.parse(dto.getDiscontinued(), DateTimeFormatter.ofPattern(dateFormat)));
		}
		if (dto.getCompany() > 0) {
			builder.company(new Company(dto.getCompany(), dto.getCompanyName()));
		}
		return builder.build();
	}
	

	/**
	 * Create a List of Computer from a List of ComputerDTO
	 * @param dtos The List of ComputerDTO to be converted
	 * @param companyDBService CompanyDBService used to get the correct companies from the Database
	 * @param dateFormat Format of the LocalDate to parse
	 * @return The List of Computer corresponding to the List of ComputerDTO
	 */
	public static List<Computer> fromDTO(final List<ComputerDTO> dtos, final String dateFormat) {
		final List<Computer> computers = dtos.stream().map(dto -> {
			final Computer computer = ComputerDTOConverter.fromDTO(dto, dateFormat);
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
	 * @param dateFormat the format we want the LocalDates to be
	 * @return The ComputerDTO corresponding to the Computer or null if the computer was null
	 */
	public static ComputerDTO toDTO(final Computer computer, String dateFormat) {
		if (computer == null) {
			return null;
		}
		final ComputerDTO.Builder builder = ComputerDTO.builder();
		builder.id(computer.getId())
				.name(computer.getName());
		
		if (computer.getIntroduced() != null) {
			builder.introduced(computer.getIntroduced().format(DateTimeFormatter.ofPattern(dateFormat)).toString());
		}
		if (computer.getDiscontinued() != null) {
			builder.discontinued(computer.getDiscontinued().format(DateTimeFormatter.ofPattern(dateFormat)).toString());
		}		
		if (computer.getCompany() != null) {
			builder.company(computer.getCompany().getId());
			builder.companyName(computer.getCompany().getName());
		} else {
			builder.company(0);
		}
		
		return builder.build();
	}
	
	/**
	 * Create a List of ComputerDTO from a List of Computer
	 * @param List of Computer to be converted
	 * @param dateFormat the format we want the LocalDates to be
	 * @return The List ComputerDTO corresponding to the List Computer of computers who are valid or an empty List if none were valid
	 */
	public static List<ComputerDTO> toDTO(final List<Computer> computers, String dateFormat) {
		final List<ComputerDTO> dtos = computers.stream().map(computer -> {
			final ComputerDTO dto = ComputerDTOConverter.toDTO(computer, dateFormat);
			if (dto != null) {
				return dto;
			}
			return null;
		}).collect(Collectors.toList());
		return dtos;
	}
}
