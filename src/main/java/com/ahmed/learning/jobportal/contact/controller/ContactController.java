package com.ahmed.learning.jobportal.contact.controller;

import com.ahmed.learning.jobportal.contact.service.IContactService;
import com.ahmed.learning.jobportal.dto.ContactDto;
import com.ahmed.learning.jobportal.dto.ContactResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
	private final IContactService contactUsService;

	@PostMapping
	public ResponseEntity<String> create(@RequestBody @Valid ContactDto contactDto) {
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
	public ResponseEntity<List<ContactResponseDto>> findAll() {
		List<ContactResponseDto> allContactUs = contactUsService.getAllContactUs();
		return ResponseEntity.ok(allContactUs);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<ContactResponseDto> findOne(@PathVariable Long id) {
		ContactResponseDto contact = contactUsService.getContactUsById(id);
		return ResponseEntity.ok(contact);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<ContactResponseDto> updateOne(@PathVariable Long id,
	                                                    @RequestBody ContactDto contactDto) {
		ContactResponseDto contact = contactUsService.updateContactUs(id,
						contactDto);
		return ResponseEntity.ok(contact);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ContactDto> deleteOne(@PathVariable Long id) {
		contactUsService.deleteContactUs(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/open-conctact")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<String> fetchOpenContacts(@RequestParam @Validated @NotBlank(message = "Status is required") String status) {
		return ResponseEntity.ok("These are the contacts with the given status " + status);
	}

}
