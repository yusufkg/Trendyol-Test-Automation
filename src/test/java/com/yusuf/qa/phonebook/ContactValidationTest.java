package com.yusuf.qa.phonebook;
import org.junit.Test;
import static org.junit.Assert.*;
public class ContactValidationTest {
    private final ContactRepository repository = new ContactRepository();
    @Test public void rejectsBlankFirstNameWithoutDatabase() {
        assertThrows(IllegalArgumentException.class, () -> repository.add("", "Yılmaz", "05551234567", null));
    }
    @Test public void rejectsInvalidPhoneWithoutDatabase() {
        assertThrows(IllegalArgumentException.class, () -> repository.add("Ayşe", "Yılmaz", "abc", null));
    }
}
