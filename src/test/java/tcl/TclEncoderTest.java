package tcl;

import org.junit.Test;
import tcl.parser.TclEncoder;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TclEncoderTest {

    @Test
    public void sampleTclCommand_validInput() {
        String test_command = "abc";
        byte[] expected_result = new byte[] { 0x61, 0x62, 0x63, 0x1A };
        byte[] result = TclEncoder.generateTclCommand(test_command);
        assertArrayEquals(expected_result, result);
    }

    @Test
    public void sampleTclCommand_invalidInput() {
        String test_command = "abc";
        byte[] expected_result = new byte[] { 0x61, 0x62, 0x63 };
        byte[] result = TclEncoder.generateTclCommand(test_command);
        assertFalse(Arrays.equals(expected_result, result));
    }
}
