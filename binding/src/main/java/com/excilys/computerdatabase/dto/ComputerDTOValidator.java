package com.excilys.computerdatabase.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ComputerDTOValidator implements Validator {

	/**
	 * Bean to access the date format from a properties file
	 */
	@Autowired
	private MessageSourceAccessor messageSourceAccessor;
	
	private static final String FORMAT_CODE = "dateFormat";
	
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(final Class<?> classz) {
		return ComputerDTO.class.equals(classz);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate(final Object obj, final Errors e) {
		final ComputerDTO dto = (ComputerDTO) obj;
		//Check the validity of the name
		if (GenericValidator.isBlankOrNull(dto.getName())) {
			e.rejectValue("name", "error.name");
		}
		
		//Get the date format from the .properties file
		final String dateFormat = messageSourceAccessor.getMessage(FORMAT_CODE);
		
		//Check the validity of the dates
		if (!GenericValidator.isBlankOrNull(dto.getIntroduced()) && !GenericValidator.isDate(dto.getIntroduced(), dateFormat, false)) {
			e.rejectValue("introduced",	"error.date");
		}
		if (!GenericValidator.isBlankOrNull(dto.getDiscontinued()) && !GenericValidator.isDate(dto.getDiscontinued(), dateFormat, false)) {
			e.rejectValue("discontinued", "error.date");
		}
		
		//Check the validity of the ids
		if (dto.getId() < 0) {
			e.rejectValue("id", "error.id");
		}
		if (dto.getCompany() < 0) {
			e.rejectValue("company", "error.company");
		}
	}
	
	/**
	 * Validate a ComputerDTO and return a List of String containing the errors found.
	 * @param computerDTO ComputerDTO to check
	 * @return A List of error messages
	 */
	public static List<String> validate(final ComputerDTO computerDTO) {
		List<String> errors = new ArrayList<String>();
		//Check the validity of the name
		if (GenericValidator.isBlankOrNull(computerDTO.getName()) || computerDTO.getName().length() > 255) {
			errors.add("Incorrect name : a name can't be empty or only spaces");
		}
		
		//Check the validity of the dates
		if (!GenericValidator.isBlankOrNull(computerDTO.getIntroduced()) && !GenericValidator.isDate(computerDTO.getIntroduced(), DEFAULT_DATE_FORMAT, false)) {
			errors.add("The introduced date is not valid, valid format is yyyy-MM-dd. You can also leave this field emtpy");
		}
		if (!GenericValidator.isBlankOrNull(computerDTO.getDiscontinued()) && !GenericValidator.isDate(computerDTO.getDiscontinued(), DEFAULT_DATE_FORMAT, false)) {
			errors.add("The discontinued date is not valid, valid format is yyyy-MM-dd. You can also leave this field emtpy");
		}
		
		//Check the validity of the ids
		if (computerDTO.getId() < 0) {
			errors.add("Incorrect Computer identifier");
		}
		if (computerDTO.getCompany() < 0) {
			errors.add("Incorrect Company identifier");
		}
		
		return errors;
	}
}
