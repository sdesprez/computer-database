package com.excilys.computerdatabase.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.CompanyDBService;

@Component
public class ComputerDTOConverter {
	
	
	/**
	 * Create a Computer from a ComputerDTO
	 * @param dto The ComputerDTO to be converted
	 * @return A Computer corresponding to the ComputerDTO or null if the ComputerDTO wasn't valid
	 */
	public static Computer fromDTO(final ComputerDTO dto, final CompanyDBService companyDBService, final String dateFormat) {
		final Computer.Builder builder = Computer.builder();
		builder.id(dto.getId()).name(dto.getName().trim());
		
		if (!GenericValidator.isBlankOrNull(dto.getIntroduced())) {
			builder.introduced(LocalDate.parse(dto.getIntroduced(), DateTimeFormatter.ofPattern(dateFormat)));
		}
		if (!GenericValidator.isBlankOrNull(dto.getDiscontinued())) {
			builder.discontinued(LocalDate.parse(dto.getDiscontinued(), DateTimeFormatter.ofPattern(dateFormat)));
		}
		if (dto.getCompany() > 0) {
			builder.company(companyDBService.getById(dto.getCompany()));
		}
		return builder.build();
	}
	

	/**
	 * Create a List of Computer from a List of ComputerDTO
	 * @param dto The List of ComputerDTO to be converted
	 * @return The List of Computer corresponding to the List of ComputerDTO who are valid or an empty List if none were valid
	 */
	public static List<Computer> fromDTO(final List<ComputerDTO> dtos, final CompanyDBService companyDBService, final String dateFormat) {
		final List<Computer> computers = dtos.stream().map(dto -> {
			final Computer computer = ComputerDTOConverter.fromDTO(dto, companyDBService, dateFormat);
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
		
		if (computer.getIntroduced() != null) {
			builder.introduced(computer.getIntroduced().toString());
		}
		if (computer.getDiscontinued() != null) {
			builder.discontinued(computer.getDiscontinued().toString());
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
}
