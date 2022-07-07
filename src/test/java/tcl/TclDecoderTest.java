package tcl;

import org.junit.Test;
import tcl.parser.TclBase;
import tcl.parser.TclDecoder;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TclDecoderTest {

    @Test
    public void sampleTclCommand_validInput() {
        String payload = "01480165016c016c016f01200157016f0172016c01640121"; // Hello World!
        String sample_data_str = "type target_trace data "+payload+"XXX";
        byte[] sample_data = sample_data_str.getBytes();
        sample_data[sample_data.length - 1] = TclBase.field_terminator;
        sample_data[sample_data.length - 2] = '\n';
        sample_data[sample_data.length - 3] = '\r';

        byte[] expected_result = DatatypeConverter.parseHexBinary(payload);
        byte[] result = TclDecoder.parseTclData(sample_data);
        assert(Arrays.equals(expected_result, result));
    }

    @Test
    public void sampleTclCommand_corruptedInput() {
        String sample_data_str = "type target_trace dat";
        byte[] sample_data = sample_data_str.getBytes();

        byte[] result = TclDecoder.parseTclData(sample_data);
        assertEquals(0, result.length);
    }

    @Test
    public void sampleTclCommand_emptyPayload() {
        String sample_data_str = "type target_trace data XXX";
        byte[] sample_data = sample_data_str.getBytes();
        sample_data[sample_data.length - 1] = TclBase.field_terminator;
        sample_data[sample_data.length - 2] = '\n';
        sample_data[sample_data.length - 3] = '\r';

        byte[] result = TclDecoder.parseTclData(sample_data);
        System.out.println(Arrays.toString(result));
        assert(result != null);
        assertEquals(0, result.length);
    }

    @Test
    public void rewindTraceHeader_validData() {
        String payload = "01480165";
        String noise = "abasjbdiasubdiua";
        String sample_data_str = "type target_trace data " + payload;
        String sample_data_str_noised = noise + sample_data_str + "xxx";
        ByteBuffer sample_data = ByteBuffer.wrap(sample_data_str_noised.getBytes());
        sample_data.position(sample_data.limit() - 3);
        sample_data.put((byte) '\r');
        sample_data.put((byte) '\n');
        sample_data.put(TclBase.field_terminator);
        boolean rewindSuccess = TclDecoder.rewindToTraceDataHeader(sample_data);
        assertTrue(rewindSuccess);
        assertTrue(new String(sample_data.array()).contains(sample_data_str));
    }

    @Test
    public void rewindTraceHeader_invalidData_didNotChangeBuffer() {
        String payload = "01480165";
        String noise = "abasjbdianoise" + payload + "XXX";
        ByteBuffer sample_data = ByteBuffer.wrap(noise.getBytes());
        ByteBuffer exceptedData = sample_data.duplicate();
        sample_data.position(sample_data.limit() - 3);
        sample_data.put((byte) '\r');
        sample_data.put((byte) '\n');
        sample_data.put(TclBase.field_terminator);

        boolean rewindSuccess = TclDecoder.rewindToTraceDataHeader(sample_data);
        assertFalse(rewindSuccess);
        assertTrue(Arrays.equals(sample_data.array(), exceptedData.array()));
    }
}
