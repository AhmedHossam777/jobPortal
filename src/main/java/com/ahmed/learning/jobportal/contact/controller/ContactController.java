package com.ahmed.learning.jobportal.contact.controller;

import com.ahmed.learning.jobportal.contact.service.IContactService;
import com.ahmed.learning.jobportal.dto.ContactDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
	private final IContactService contactUsService;

	@PostMapping
	public ResponseEntity<String> create(@RequestBody ContactDto contactDto) {
		boolean created = contactUsService.saveContactUs(contactDto);
		if (created) {
			return ResponseEntity
							.status(HttpStatus.CREATED)
							.body("Request processed successfully!");
		} else {
			return ResponseEntity
							.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body("Request processed failed!");
		}
	}

	@GetMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<List<ContactDto>> findAll() {
		List<ContactDto> allContactUs = contactUsService.getAllContactUs();
		return ResponseEntity.ok(allContactUs);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<ContactDto> findOne(@PathVariable Long id) {
		ContactDto contact = contactUsService.getContactUsById(id);
		return ResponseEntity.ok(contact);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<ContactDto> updateOne(@PathVariable Long id,
	                                            @RequestBody ContactDto contactDto) {
		ContactDto contact = contactUsService.updateContactUs(id, contactDto);
		return ResponseEntity.ok(contact);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ContactDto> deleteOne(@PathVariable Long id) {
		contactUsService.deleteContactUs(id);
		return ResponseEntity.noContent().build();
	}
}
