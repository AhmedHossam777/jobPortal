package com.ahmed.learning.jobportal.contact.service.impl;

import com.ahmed.learning.jobportal.contact.service.IContactService;
import com.ahmed.learning.jobportal.dto.ContactDto;
import com.ahmed.learning.jobportal.dto.ContactResponseDto;
import com.ahmed.learning.jobportal.entity.Contact;
import com.ahmed.learning.jobportal.repository.ContactRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {
	private final ContactRepository contactRepository;

	@Override
	public List<ContactResponseDto> getAllContactUs() {
		List<Contact> contactList = contactRepository.findAll();
		return contactList.stream().map(this::contactToContactDto).toList();
	}

	@Override
	public boolean saveContactUs(ContactDto contactDto) {
		boolean result = false;
		Contact contact = contactRepository.save(transformToEntity(contactDto));
		if (contact != null && contact.getId() != null) {
			result = true;
		}

		return result;
	}

	@Override
	public ContactResponseDto getContactUsById(Long id) {
		Contact contact = contactRepository
						.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Contact not " +
										"found" + " " + "with id: " + id));

		return contactToContactDto(contact);
	}

	@Override
	public ContactResponseDto updateContactUs(Long id, ContactDto contactDto) {
		Contact contact = contactRepository
						.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Contact not " +
										"found" + " " + "with id: " + id));
		contact.setUpdatedAt(Instant.now());
		contact.setUpdatedBy("SYSTEM");
		Contact updatedContact = contactRepository.save(contact);
		return contactToContactDto(updatedContact);
	}

	@Override
	public void deleteContactUs(Long id) {
		if (!contactRepository.existsById(id)) {
			throw new EntityNotFoundException("Contact not found with id: " + id);
		}
		contactRepository.deleteById(id);
	}

	private ContactResponseDto contactToContactDto(Contact contact) {
		return new ContactResponseDto(
						contact.getId(),
						contact.getEmail(),
						contact.getMessage(), contact.getName(), contact.getStatus(),
						contact.getSubject(), contact.getUserType()
		);
	}

	private Contact transformToEntity(ContactDto contactDto) {
		Contact contact = new Contact();
		BeanUtils.copyProperties(contactDto, contact);
		contact.setCreatedAt(Instant.now());
		contact.setCreatedBy("SYSTEM");
		contact.setStatus("NEW");

		return contact;
	}
}
