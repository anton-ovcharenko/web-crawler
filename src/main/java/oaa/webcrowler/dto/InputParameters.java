package oaa.webcrowler.dto;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

/**
 * Class for parsing and holding input parameters
 */
@Value
@Builder
@Log4j2
public class InputParameters {
    public static final String MAKE_EVERYTHING_OK_BUTTON = "make-everything-ok-button";

    String originalFilePath;
    String diffFilePath;
    @Default
    String elementId = MAKE_EVERYTHING_OK_BUTTON;

    /**
     * Parse input parameter of application and build optional InputParameters object
     *
     * @param args input parameters of application
     * @return optional of <code>InputParameters</code> object that contains all required parameters
     * or empty() in case some error during parsing
     */
    public static Optional<InputParameters> parseInputParameters(String[] args) {
        if (args.length < 2 || args.length > 3) {
            log.error("Wrong parameters amount: {}, must be 2 or 3.", args.length);
            return Optional.empty();
        }

        InputParametersBuilder inputParametersBuilder = InputParameters
                .builder()
                .originalFilePath(args[0])
                .diffFilePath(args[1]);

        if (args.length == 3) {
            inputParametersBuilder.elementId(args[2]);
        }

        InputParameters inputParameters = inputParametersBuilder.build();
        log.info("Input parameters after parsing: {}", inputParameters);
        return Optional.of(inputParameters);
    }
}
