package com.excilys.computerdatabase.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.utils.Validator;

public class ComputerDTOConverter {

	public static Computer fromDTO(final ComputerDTO dto) {
		final Computer.Builder builder = Computer.builder();
		builder.id(dto.getId())
				.name(dto.getName());
		
		if (Validator.isDate(dto.getIntroduced())) {
			builder.introducedDate(LocalDate.parse(dto.getIntroduced(), DateTimeFormatter.ISO_LOCAL_DATE));
		}
		if (Validator.isDate(dto.getDiscontinued())) {
			builder.discontinuedDate(LocalDate.parse(dto.getDiscontinued(), DateTimeFormatter.ISO_LOCAL_DATE));
		}
		if (dto.getCompany() != 0) {
			builder.company(new Company(dto.getCompany(), dto.getCompanyName()));
		}
		return builder.build();
	}
	

	public static List<Computer> fromDTO(final List<ComputerDTO> dtos) {
		final List<Computer> computers = new ArrayList<Computer>();
		dtos.forEach(d -> { final Computer computer = ComputerDTOConverter.fromDTO(d);
							if (computer != null) {
								computers.add(computer);
							}
							});
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
		final List<ComputerDTO> dtos = new ArrayList<ComputerDTO>();
		computers.forEach(c -> { final ComputerDTO dto = ComputerDTOConverter.toDTO(c);
								if (dto != null) {
									dtos.add(dto);
								}
								});
		return dtos;
	}
}
