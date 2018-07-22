package oaa.webcrowler;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ElementPathBuilder {

    protected static final String PATH_SEPARATOR = " > ";

    /**
     * Build element's path
     *
     * @param element
     * @return string representation of element's path in DOM model
     */
    public static String buildElementPath(Element element) {
        StringBuilder elementPath = new StringBuilder();
        Elements parents = element.parents();
        Collections.reverse(parents);
        parents.forEach(p -> elementPath
                .append(buildElementPosition(p))
                .append(PATH_SEPARATOR));
        return elementPath
                .append(buildElementPosition(element))
                .toString();
    }

    /**
     * Build element's position in formats:
     * <TAG> in case parent has only one element with such tag or
     * <TAG>[N] in case parent has more than one element with such tag, where N is index of element among other
     * elements with same tag
     *
     * @param element
     * @return string representation of element's position
     */
    private static String buildElementPosition(Element element) {
        String result = element.tagName();
        if (element.hasParent()) {
            List<Element> parentsChildrenSameByTag = element
                    .parent()
                    .children()
                    .stream()
                    .filter(e -> e.tagName().equals(element.tagName()))
                    .collect(Collectors.toList());

            if (parentsChildrenSameByTag.size() > 1) {
                result += "[" + parentsChildrenSameByTag.indexOf(element) + "]";
            }
        }
        return result;
    }
}
