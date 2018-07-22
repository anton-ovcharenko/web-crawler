package oaa.webcrowler;

import oaa.webcrowler.similarityChecker.SimilarityChecker;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebCrawlerTest {

    SimilarityChecker similarityChecker = mock(SimilarityChecker.class);
    WebCrawler crawler = new WebCrawler(similarityChecker);

    Element element1 = new Element("a");
    Element element2 = new Element("div");
    Element element3 = new Element("span");

    @Test
    public void shouldFindSimilarElementWithMaxSimilarityLevel() {
        //given
        Document document = new Document("oaa");
        Element element = new Element("body");
        document.appendChild(element1);
        document.appendChild(element1);
        element2.appendChild(element3);
        document.appendChild(element2);
        when(similarityChecker.calculateSimilarityIndex(eq(element), eq(element1))).thenReturn(1L);
        when(similarityChecker.calculateSimilarityIndex(eq(element), eq(element2))).thenReturn(2L);
        when(similarityChecker.calculateSimilarityIndex(eq(element), eq(element3))).thenReturn(3L);

        //when
        Optional<Element> result = crawler.findSimilarElement(element, document);

        //then
        assert result.isPresent();
        assert element3.equals(result.get()) : "wrong similar element";
    }

    @Test
    public void shouldNotFindAnySimilarElementInEmptyDocument() {
        //when
        Optional<Element> result = crawler.findSimilarElement(new Element("div"), new Document("oaa"));

        //then
        assert !result.isPresent() : "element was found";
    }

    @Test
    public void getSimilarityDetails() {
        //given
        String expectedResult = "some value";
        when(similarityChecker.buildSimilarityDetails(eq(element1), eq(element2))).thenReturn(expectedResult);

        //when
        String result = crawler.getSimilarityDetails(element1, element2);

        //then
        assert expectedResult.equals(result) : "wrong similarity details";
    }
}