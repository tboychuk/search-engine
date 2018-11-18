package com.tarasboychuk;

import com.tarasboychuk.service.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SearchMvcAppTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentService documentService;

    @Before
    public void setup() {
        documentService.clearDocuments();
    }

    @Test
    public void testGetDocumentForm() throws Exception {
        mockMvc.perform(
                get("/docs/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"));
    }

    @Test
    public void testSaveNewDocument() throws Exception {
        mockMvc.perform(
                post("/docs")
                        .param("key", "a")
                        .param("document", "ab cd ef"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", true))
                .andExpect(view().name("form"));
    }

    @Test
    public void testSaveDocumentWithExistingKey() throws Exception {
        String documentKey = "a";
        documentService.putDocument(documentKey, "xxxx");

        mockMvc.perform(
                post("/docs")
                        .param("key", documentKey)
                        .param("document", "yyyy"))
                .andExpect(model().attribute("error", String.format("Document with key=%s already exists.", documentKey)))
                .andExpect(view().name("form"));

    }

    @Test
    public void testAddEmptyDocument() throws Exception {
        mockMvc.perform(
                post("/docs")
                        .param("key", "a")
                        .param("document", ""))
                .andExpect(model().attribute("error", "Document cannot be empty."))
                .andExpect(view().name("form"));
    }

    @Test
    public void testGetDocumentByKey() throws Exception {
        String documentKey = "b";
        String documentBody = "some text";
        documentService.putDocument(documentKey, documentBody);

        mockMvc.perform(
                get("/docs")
                        .param("key", documentKey))
                .andExpect(status().isOk())
                .andExpect(view().name("document"))
                .andExpect(model().attribute("document", documentBody));
    }

    @Test
    public void testGetDocumentByNotExistingKey() throws Exception {
        String notExistingKey = "x";

        mockMvc.perform(
                get("/docs")
                        .param("key", notExistingKey))
                .andExpect(status().isOk())
                .andExpect(view().name("document"))
                .andExpect(model().attributeDoesNotExist("document"))
                .andExpect(model().attribute("error", String.format("Document not found by key=%s", notExistingKey)));
    }

    @Test
    public void testSearchDocumentsByQuery() throws Exception {
        String searchQuery = "abc xyz";

        documentService.putDocument("a", "abc");
        documentService.putDocument("b", "abc opq hij xyz end");
        documentService.putDocument("c", "start abc opq hij end");
        documentService.putDocument("d", "start abc opq hij xyz end");
        documentService.putDocument("e", "start xyz end");
        documentService.putDocument("f", "start xyz abc end");

        mockMvc.perform(
                get("/docs/search")
                        .param("query", searchQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("searchResult"))
                .andExpect(model().attribute("foundKeys", containsInAnyOrder("b", "d", "f")));
    }

    @Test
    public void testSearchInRealDocumentByQuery() throws Exception {
        String documentKey = "indexing";
        String documentText = "When dealing with a small number of documents, it is possible for the full-text-search" +
                " engine to directly scan the contents of the documents with each query, a strategy called \"serial " +
                "scanning\". This is what some tools, such as grep, do when searching.\n" +
                "However, when the number of documents to search is potentially large, or the quantity of search" +
                " queries to perform is substantial, the problem of full-text search is often divided into two tasks:" +
                " indexing and searching. The indexing stage will scan the text of all the documents and build a list" +
                " of search terms (often called an index, but more correctly named a concordance). In the search" +
                " stage, when performing a specific query, only the index is referenced, rather than the text of the" +
                " original documents.[2]\n The indexer will make an entry in the index for each term or word found in" +
                " a document, and possibly note its relative position within the document. Usually the indexer will" +
                " ignore stop words (such as \"the\" and \"and\") that are both common and insufficiently meaningful" +
                " to be useful in searching. Some indexers also employ language-specific stemming on the words being" +
                " indexed. For example, the words \"drives\", \"drove\", and \"driven\" will be recorded in the index" +
                " under the single concept word \"drive\".";
        documentService.putDocument(documentKey, documentText);

        String searchQuery = "search documents index concordance";

        mockMvc.perform(
                get("/docs/search")
                        .param("query", searchQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("searchResult"))
                .andExpect(model().attribute("foundKeys", hasItem(documentKey)));
    }

}
