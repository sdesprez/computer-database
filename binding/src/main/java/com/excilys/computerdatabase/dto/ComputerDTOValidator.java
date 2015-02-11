package com.excilys.computerdatabase.dto;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ComputerDTOValidator implements Validator {

	private MessageSourceAccessor messageSourceAccessor;
	
	@Override
	public boolean supports(final Class<?> classz) {
		return ComputerDTO.class.equals(classz);
	}

	@Autowired
    public void setMessageSource(final MessageSource messageSource) {
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
    }
	
	
	@Override
	public void validate(final Object obj, final Errors e) {
		final ComputerDTO dto = (ComputerDTO) obj;
		if (GenericValidator.isBlankOrNull(dto.getName()) || dto.getName().length() > 255) {
			e.rejectValue("name", "error.name");
		}
		
		final String dateFormat = messageSourceAccessor.getMessage("dateFormat");
		if (!GenericValidator.isBlankOrNull(dto.getIntroduced()) && !GenericValidator.isDate(dto.getIntroduced(), dateFormat, false)) {
			e.rejectValue("introduced",	"error.date");
		}
		if (!GenericValidator.isBlankOrNull(dto.getDiscontinued()) && !GenericValidator.isDate(dto.getDiscontinued(), dateFormat, false)) {
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
