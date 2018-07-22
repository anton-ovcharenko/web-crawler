package oaa.webcrowler;

import oaa.webcrowler.dto.InputParameters;
import org.junit.Test;

import java.util.Optional;

import static oaa.webcrowler.dto.InputParameters.MAKE_EVERYTHING_OK_BUTTON;

public class InputParametersTest {

    @Test
    public void shouldParseTwoParameters() {
        //given
        String originalFilePath = "A";
        String diffFilePath = "B";

        //when
        Optional<InputParameters> result = InputParameters
                .parseInputParameters(new String[]{originalFilePath, diffFilePath});

        //then
        assert result.isPresent();
        assert result.get().getOriginalFilePath().equals(originalFilePath) : "originalFilePath has wrong value";
        assert result.get().getDiffFilePath().equals(diffFilePath) : "diffFilePath has wrong value";
        assert result.get().getElementId().equals(MAKE_EVERYTHING_OK_BUTTON) : "elementId has wrong value";
    }

    @Test
    public void shouldParseThreeParameters() {
        //given
        String originalFilePath = "A";
        String diffFilePath = "B";
        String elementId = "C";

        //when
        Optional<InputParameters> result = InputParameters
                .parseInputParameters(new String[]{originalFilePath, diffFilePath, elementId});

        //then
        assert result.isPresent();
        assert result.get().getOriginalFilePath().equals(originalFilePath) : "originalFilePath has wrong value";
        assert result.get().getDiffFilePath().equals(diffFilePath) : "diffFilePath has wrong value";
        assert result.get().getElementId().equals(elementId) : "elementId has wrong value";
    }

    @Test
    public void shouldNotParseOneParameter() {
        //when
        Optional<InputParameters> result = InputParameters
                .parseInputParameters(new String[]{"1"});

        //then
        assert !result.isPresent();
    }

    @Test
    public void shouldNotParseForParameter() {
        //when
        Optional<InputParameters> result = InputParameters
                .parseInputParameters(new String[]{"1", "2", "3", "4"});

        //then
        assert !result.isPresent();
    }
}