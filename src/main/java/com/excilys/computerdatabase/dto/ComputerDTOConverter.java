package com.excilys.computerdatabase.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.utils.Validator;

public class ComputerDTOConverter {

	public static Computer fromDTO(final ComputerDTO dto) {
		if (!Validator.validComputerDTO(dto)) {
			return null;
		}
		
		final Computer.Builder builder = Computer.builder();
		builder.id(dto.getId())
				.name(dto.getName());
		
		if (dto.getIntroduced() != null) {
			builder.introducedDate(LocalDate.parse(dto.getIntroduced(), DateTimeFormatter.ISO_LOCAL_DATE));
		}
		if (dto.getDiscontinued() != null) {
			builder.discontinuedDate(LocalDate.parse(dto.getDiscontinued(), DateTimeFormatter.ISO_LOCAL_DATE));
		}
		if (dto.getCompany() != 0) {
			builder.company(new Company(dto.getCompany(), dto.getCompanyName()));
		}
		return builder.build();
	}
	

	public static List<Computer> fromDTO(final List<ComputerDTO> dtos) {
		final List<Computer> computers = dtos.stream().map(dto -> {
			final Computer computer = ComputerDTOConverter.fromDTO(dto);
			if (computer != null) {
				return computer;
			}
			return null;
		}).collect(Collectors.toList());
		return computers;
	}
	
	public static ComputerDTO toDTO(final Computer computer) {
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
			builder.companyName(computer.getCompany().getName());
		} else {
			builder.company(0);
		}
		
		return builder.build();
	}
	
	
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
