package oaa.webcrowler.similarityChecker;

import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * <code>WeightedSimilarityChecker</code> uses <code>Element</code>'s attributes and functions to implement logic
 * of calculating similarity index and building similarity details.
 * <p>
 * There are possibilities to specify weights for specific attributes or use default weight (value: 1)
 * OR/AND specify functions with their weights as well.
 */
@Builder
public class WeightedSimilarityChecker implements SimilarityChecker {

    private static final Integer DEFAULT_ATTRIBUTE_WEIGHT = 1;

    @Singular
    Map<String, Integer> attributeWeights;
    @Singular
    Map<Function<Element, String>, Integer> functionWeights;

    /**
     * Calculate similarity level between two <code>Element</code> based on their attributes and functions values.
     * <p>
     * In case attribute's weight is not found will be used default weight (=1)
     * Each attribute contributes (0) to similarity level in case inequality of values for origin and tested.
     * Each attribute contributes (1 * weight) to similarity level in case equality of values for origin and tested.
     * Each specified function contributes (0) to similarity level in case inequality of values for origin and tested.
     * Each specified function contributes (1 * weight) to similarity level in case equality of values for origin and tested.
     *
     * @param original base <code>Element</code> for similarity checking
     * @param tested   <code>Element</code> which will be compared with original on similarity
     * @return int value (higher value means more similarity) that represents level of similarity between original
     * and testes <code>Element</code>.
     */
    @Override
    public long calculateSimilarityIndex(Element original, Element tested) {
        //calculate similarityIndex by equality of attributes
        int similarityIndexByAttributes = original
                .attributes()
                .asList()
                .stream()
                .mapToInt(attribute -> calculateAttributeContribution(tested.attributes(), attribute))
                .sum();

        //calculate similarityIndex by equality of functions
        int similarityIndexByFunctions = functionWeights
                .entrySet()
                .stream()
                .mapToInt(entry -> calculateFunctionContribution(original, tested, entry))
                .sum();

        return similarityIndexByAttributes + similarityIndexByFunctions;
    }

    private int calculateFunctionContribution(Element original,
                                              Element tested,
                                              Entry<Function<Element, String>, Integer> entry) {
        Function<Element, String> function = entry.getKey();
        boolean functionsHaveEqualValues = StringUtils
                .equalsIgnoreCase(function.apply(original), function.apply(tested));
        return (functionsHaveEqualValues ? 1 : 0) * entry.getValue();
    }

    private int calculateAttributeContribution(Attributes testedElementAttributes, Attribute attribute) {
        String attributeKey = attribute.getKey();
        boolean isTestedElementHasEqualAttribute = attribute
                .getValue()
                .equalsIgnoreCase(testedElementAttributes.getIgnoreCase(attributeKey));
        Integer attributeWeight = attributeWeights
                .getOrDefault(attributeKey.toUpperCase(), DEFAULT_ATTRIBUTE_WEIGHT);
        return (isTestedElementHasEqualAttribute ? 1 : 0) * attributeWeight;
    }

    /**
     * Build similarity report for two <code>Element</code>s with description of each attribute and function
     * and their contribution to similarity level value.
     *
     * @param original base <code>Element</code> for similarity checking
     * @param tested   <code>Element</code> which will be compared with original on similarity
     * @return multiline string with detailed report about similarity of two <code>Element</code>s
     */
    @Override
    public String buildSimilarityDetails(Element original, Element tested) {
        StringBuilder sb = new StringBuilder();
        original
                .attributes()
                .asList()
                .forEach(attribute -> sb.append(buildAttributeContributionDetails(original, tested, attribute)));
        functionWeights
                .entrySet()
                .forEach(entry -> sb.append(buildFunctionContributionDetails(original, tested, entry)));

        sb.append("Total similarity index: ").append(calculateSimilarityIndex(original, tested));
        return sb.toString();
    }

    private String buildAttributeContributionDetails(Element original, Element tested, Attribute attribute) {
        StringBuilder sb = new StringBuilder();
        String attributeName = attribute.getKey();
        Attributes testedAttributes = tested.attributes();
        sb.append("Attribute: [").append(attributeName).append("] ")
                .append("\n\toriginal element value: [").append(original.attributes().getIgnoreCase(attributeName)).append("], ")
                .append("\n\tsimilar element value: [").append(testedAttributes.getIgnoreCase(attributeName)).append("], ")
                .append("\n\tcontribution: ").append(calculateAttributeContribution(testedAttributes, attribute))
                .append("\n");
        return sb.toString();
    }

    private String buildFunctionContributionDetails(Element original,
                                                    Element tested,
                                                    Entry<Function<Element, String>, Integer> entry) {
        StringBuilder sb = new StringBuilder();
        String functionName = entry.getKey().toString();
        sb.append("Function: [").append(functionName).append("] ")
                .append("\n\toriginal element value: [").append(entry.getKey().apply(original)).append("], ")
                .append("\n\tsimilar element value: [").append(entry.getKey().apply(tested)).append("], ")
                .append("\n\tcontribution: ").append(calculateFunctionContribution(original, tested, entry))
                .append("\n");
        return sb.toString();
    }
}
