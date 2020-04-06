package tcl;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EncoderTest {

    @Test
    public void sampleTclCommand_validInput() {
        String test_command = "abc";
        byte[] expected_result = new byte[] { 0x61, 0x62, 0x63, 0x1A };
        byte[] result = Encoder.generate_tcl_command(test_command);
        assert(Arrays.equals(expected_result, result));
    }

    @Test
    public void sampleTclCommand_invalidInput() {
        String test_command = "abc";
        byte[] expected_result = new byte[] { 0x61, 0x62, 0x63 };
        byte[] result = Encoder.generate_tcl_command(test_command);
        assertFalse(Arrays.equals(expected_result, result));
    }
}
