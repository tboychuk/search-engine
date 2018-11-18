package com.tarasboychuk;

import com.tarasboychuk.exception.DocumentKeyAlreadyExistsException;
import com.tarasboychuk.exception.DocumentNotFoundException;
import com.tarasboychuk.persistence.DocumentRepository;
import com.tarasboychuk.service.DocumentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTests {
    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    @Test
    public void testAddDocument() {
        String documentKey = "A";
        String documentBody = "aa bb cc dd";
        when(documentRepository.putDocument(documentKey, documentBody)).thenReturn(true);

        documentService.putDocument(documentKey, documentBody);

        verify(documentRepository, times(1)).putDocument(documentKey, documentBody);
    }

    @Test
    public void testAddDocumentWithExistingKey() {
        String existingKey = "B";
        when(documentRepository.putDocument(not(eq(existingKey)), anyString())).thenReturn(true);
        when(documentRepository.putDocument(eq(existingKey), anyString())).thenReturn(false);

        try {
            documentService.putDocument(existingKey, "some new document");
            fail("DocumentKeyAlreadyExistsException should be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof DocumentKeyAlreadyExistsException);
            assertThat(e.getMessage(), equalTo(String.format("Document with key=%s already exists.", existingKey)));
        }
    }

    @Test
    public void testGetDocumentByKey() {
        String docKey = "C";
        String docBody = "hello hey hi aloha";
        when(documentRepository.findByKey(docKey)).thenReturn(Optional.of(docBody));

        String foundDoc = documentService.getDocument(docKey);

        assertThat(foundDoc, equalTo(docBody));
    }

    @Test
    public void testGetDocumentByNotExistingKey() {
        String notExistingKey = "X";
        when(documentRepository.findByKey(notExistingKey)).thenReturn(Optional.empty());

        try {
            documentService.getDocument(notExistingKey);
            fail("DocumentNotFoundException should be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof DocumentNotFoundException);
            assertThat(e.getMessage(), equalTo(String.format("Document not found by key=%s", notExistingKey)));
        }
    }
}
