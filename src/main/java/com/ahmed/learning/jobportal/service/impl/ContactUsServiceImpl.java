package com.ahmed.learning.jobportal.service.impl;

import com.ahmed.learning.jobportal.dto.ContactDto;
import com.ahmed.learning.jobportal.entity.Contact;
import com.ahmed.learning.jobportal.repository.ContactRepository;
import com.ahmed.learning.jobportal.service.IContactUsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactUsServiceImpl implements IContactUsService {
	private final ContactRepository contactRepository;

	@Override
	public List<ContactDto> getAllContactUs() {
		List<Contact> contactList = contactRepository.findAll();
		return contactList.stream().map(this::contactToContactDto).toList();
	}

	@Override
	public ContactDto saveContactUs(ContactDto contactDto) {
		Contact contact = new Contact();
		applyDtoToContact(contactDto, contact);
		contact.setCreatedAt(Instant.now());
		contact.setUpdatedAt(Instant.now());
		contact.setCreatedBy("SYSTEM");
		if (contact.getStatus() == null) {
			contact.setStatus("NEW");
		}
		Contact saved = contactRepository.save(contact);
		return contactToContactDto(saved);
	}

	@Override
	public ContactDto getContactUsById(Long id) {
		Contact contact = contactRepository
						.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Contact not " +
										"found" + " " + "with id: " + id));

		return contactToContactDto(contact);
	}

	@Override
	public ContactDto updateContactUs(Long id, ContactDto contactDto) {
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

	private ContactDto contactToContactDto(Contact contact) {
		return new ContactDto(
						contact.getId(), contact.getEmail(),
						contact.getMessage(), contact.getName(), contact.getStatus(),
						contact.getSubject(), contact.getUserType()
		);
	}

	private void applyDtoToContact(ContactDto contactDto, Contact contact) {
		contact.setEmail(contactDto.email());
		contact.setMessage(contactDto.message());
		contact.setName(contactDto.name());
		contact.setSubject(contactDto.subject());
		contact.setUserType(contactDto.userType());
		if (contactDto.status() != null) {
			contact.setStatus(contactDto.status());
		}
	}
}
