package oaa.webcrowler.similarityChecker;

import org.jsoup.nodes.Element;

/**
 * Any <code>SimilarityChecker</code> implements the following functions:
 * 1) calculating similarity index (some long value) between two <code>Elements</code>.
 * 2) building detailed report about similarity between two <code>Elements</code>.
 */
public interface SimilarityChecker {

    long calculateSimilarityIndex(Element original, Element tested);

    String buildSimilarityDetails(Element original, Element tested);
}
