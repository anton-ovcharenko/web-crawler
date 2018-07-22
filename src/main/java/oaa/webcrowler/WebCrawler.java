package oaa.webcrowler;

import lombok.AllArgsConstructor;
import oaa.webcrowler.similarityChecker.SimilarityChecker;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.Optional;

/**
 * Web crawler that is used for search of most similar <code>Element</code> to original one into some document and
 * building similarity detail based on <code>SimilarityChecker</code>
 */
@AllArgsConstructor
public class WebCrawler {

    private SimilarityChecker similarityChecker;

    /**
     * Search for most similar <code>Element</code> based on <code>SimilarityChecker.calculateSimilarityIndex(...)</code>
     * method
     *
     * @param original
     * @param document
     * @return <code>Optional<Element></code> that has biggest similarity index among all element of document
     */
    public Optional<Element> findSimilarElement(Element original, Document document) {
        return document
                .getAllElements()
                .stream()
                .filter(element -> !element.equals(document)) // exclude document itself
                .map(element -> new SimpleEntry<>(element, similarityChecker.calculateSimilarityIndex(original, element)))
                .max(Comparator.comparingLong(SimpleEntry::getValue))
                .map(SimpleEntry::getKey);
    }

    public String getSimilarityDetails(Element original, Element similar) {
        return similarityChecker.buildSimilarityDetails(original, similar);
    }
}
