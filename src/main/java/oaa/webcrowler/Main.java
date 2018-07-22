package oaa.webcrowler;

import lombok.extern.log4j.Log4j2;
import oaa.webcrowler.dto.InputParameters;
import oaa.webcrowler.similarityChecker.SimilarityChecker;
import oaa.webcrowler.similarityChecker.WeightedSimilarityChecker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static oaa.webcrowler.ElementPathBuilder.buildElementPath;

@Log4j2
public class Main {

    private static final String CHARSET_NAME = "utf8";

    public static void main(String[] args) throws RuntimeException {

        InputParameters parameters = InputParameters
                .parseInputParameters(args)
                .orElseThrow(() -> new RuntimeException("Can not parse parameters from args."));

        Document originalDocument = parseDocument(parameters.getOriginalFilePath())
                .orElseThrow(() -> new RuntimeException("Can not parse original document."));

        Element originalElement = originalDocument.getElementById(parameters.getElementId());
        if (Objects.isNull(originalElement)) {
            throw new RuntimeException(format("Can not find element with id: [%s] inside original document.", parameters.getElementId()));
        }

        Document diffDocument = parseDocument(parameters.getDiffFilePath())
                .orElseThrow(() -> new RuntimeException("Can not parse diff document."));

        WebCrawler crawler = buildWeightedWebCrawler();

        Element similarElement = crawler
                .findSimilarElement(originalElement, diffDocument)
                .orElseThrow(() -> new RuntimeException(
                        format("Can not find element similar to [%s] into diff document.", originalElement.toString())));

        System.out.println(format("Path to similar element: [%s]", buildElementPath(similarElement)));
        System.out.println(format("\nSimilar element: [%s]", similarElement));
        System.out.println(format("\nContribution details: [\n%s]", crawler.getSimilarityDetails(originalElement, similarElement)));
    }

    /**
     * Build <code>WebCrawler</code> with <code>WeightedSimilarityChecker</code> that has:
     * specified weights for attributes "id" and "class"
     * specified weights for functions "Element.tagName()" and "Element.text()"
     *
     * @return <code>WebCrawler</code> instance
     */
    private static WebCrawler buildWeightedWebCrawler() {
        SimilarityChecker similarityChecker = WeightedSimilarityChecker
                .builder()
                .attributeWeight("ID", 2)
                .attributeWeight("CLASS", 2)
                .functionWeight(Element::tagName, 3)
                .functionWeight(Element::text, 3)
                .build();
        return new WebCrawler(similarityChecker);
    }

    /**
     * Parse HTML document with Jsoup
     *
     * @param filePath path to the file
     * @return <code>Optional<Document></code> parsed by Jsoup
     */
    private static Optional<Document> parseDocument(String filePath) {
        try {
            Document doc = Jsoup.parse(new File(filePath), CHARSET_NAME);
            return Optional.of(doc);
        } catch (IOException e) {
            log.error("Error reading [{}] file", filePath, e);
            return Optional.empty();
        }
    }
}
