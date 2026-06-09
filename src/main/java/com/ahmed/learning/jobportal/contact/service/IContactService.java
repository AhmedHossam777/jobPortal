package com.ahmed.learning.jobportal.contact.service;

import com.ahmed.learning.jobportal.dto.ContactDto;
import com.ahmed.learning.jobportal.dto.ContactResponseDto;

import java.util.List;

public interface IContactService {
	List<ContactResponseDto> getAllContactUs();

	boolean saveContactUs(ContactDto contactDto);

	ContactResponseDto getContactUsById(Long id);

	void deleteContactUs(Long id);

	ContactResponseDto updateContactUs(Long id, ContactDto contactDto);
}
