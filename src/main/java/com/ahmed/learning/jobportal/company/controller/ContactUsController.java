package com.ahmed.learning.jobportal.company.controller;

import com.ahmed.learning.jobportal.dto.ContactDto;
import com.ahmed.learning.jobportal.service.IContactUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactUsController {
	private final IContactUsService contactUsService;

	@PostMapping
	public ResponseEntity<ContactDto> create(@RequestBody ContactDto contactDto) {
		ContactDto created = contactUsService.saveContactUs(contactDto);
		return ResponseEntity.ok(created);
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
