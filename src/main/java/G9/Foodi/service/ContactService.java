package G9.Foodi.service;

import G9.Foodi.dto.ContactDto;
import G9.Foodi.model.Contact;
import G9.Foodi.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public Contact createContact(ContactDto contactDto) {
        Contact contact = new Contact();
        contact.setName(contactDto.getName());
        contact.setEmail(contactDto.getEmail());
        contact.setSubject(contactDto.getSubject());
        contact.setMessage(contactDto.getMessage());
        contact.setCreatedAt(LocalDateTime.now());
        return contactRepository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public void deleteContact(String id) {
        contactRepository.deleteById(id);
    }
} 