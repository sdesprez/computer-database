package com.excilys.computerdatabase.dto;

import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ComputerDTOValidator implements Validator {

	
	@Override
	public boolean supports(final Class<?> classz) {
		return ComputerDTO.class.equals(classz);
	}

	@Override
	public void validate(final Object obj, final Errors e) {
		final ComputerDTO dto = (ComputerDTO) obj;
		if (GenericValidator.isBlankOrNull(dto.getName()) || dto.getName().length() > 255) {
			e.rejectValue("name", "error.name");
		}
		if (!GenericValidator.isBlankOrNull(dto.getIntroduced()) && !GenericValidator.isDate(dto.getIntroduced(), "yyyy-MM-dd", false)) {
			e.rejectValue("introduced",	"error.date");
		}
		if (!GenericValidator.isBlankOrNull(dto.getDiscontinued()) && !GenericValidator.isDate(dto.getDiscontinued(), "yyyy-MM-dd", false)) {
			e.rejectValue("discontinued", "error.date");
		}
		if (dto.getId() < 0) {
			e.rejectValue("id", "error.id");
		}
		if (dto.getCompany() < 0) {
			e.rejectValue("company", "error.company");
		}
	}
}
