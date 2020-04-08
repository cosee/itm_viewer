package tcl;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNull;

public class TclDecoderTest {
    @Test
    public void sampleTclCommand_validInput() {
        String payload = "01480165016c016c016f01200";
        String sample_data_str = "type target_trace data "+payload+"X";
        byte[] sample_data = sample_data_str.getBytes();
        sample_data[sample_data.length - 1] = TclBase.field_terminator;

        byte[] expected_result = payload.getBytes();
        byte[] result = TclDecoder.parseTclData(sample_data);
        assert(Arrays.equals(expected_result, result));
    }

    @Test
    public void sampleTclCommand_corruptedInput() {
        String sample_data_str = "type target_trace dat";
        byte[] sample_data = sample_data_str.getBytes();

        byte[] result = TclDecoder.parseTclData(sample_data);
        assertNull(result);
    }

    @Test
    public void sampleTclCommand_emptyPayload() {
        String sample_data_str = "type target_trace data X";
        byte[] sample_data = sample_data_str.getBytes();
        sample_data[sample_data.length - 1] = TclBase.field_terminator;

        byte[] result = TclDecoder.parseTclData(sample_data);
        assertNull(result);
    }
}
