package com.ahmed.learning.jobportal.contact.service;

import com.ahmed.learning.jobportal.dto.ContactDto;

import java.util.List;

public interface IContactService {
	List<ContactDto> getAllContactUs();

	boolean saveContactUs(ContactDto contactDto);

	ContactDto getContactUsById(Long id);

	void deleteContactUs(Long id);

	ContactDto updateContactUs(Long id, ContactDto contactDto);
}
