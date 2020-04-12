package tcl;

import org.junit.Test;
import tcl.itm.ITMDecoder;
import tcl.parser.TclEntity;

import javax.xml.bind.DatatypeConverter;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ITMDecoderTest {

    public String joinChannelData(List<TclEntity> data) {
        return data.stream().map(TclEntity::getContent).collect(Collectors.joining());
    }
    @Test
    public void sampleITMCommand_validInput() {
        String payload = "01480165016c016c016f01200157016f0172016c01640121";
        byte[] sample_data = DatatypeConverter.parseHexBinary(payload);

        List<TclEntity> result = ITMDecoder.parseITMData(sample_data);
        assertNotNull(result);
        assertEquals("Hello World!", joinChannelData(result));
    }

    @Test
    public void sampleITMCommand_validInput_2BytePayload() {
        String payload = "024865016c016c016f01200157016f0172016c01640121";
        byte[] sample_data = DatatypeConverter.parseHexBinary(payload);

        List<TclEntity> result = ITMDecoder.parseITMData(sample_data);
        assertNotNull(result);
        assertEquals("Hello World!", joinChannelData(result));
    }

    @Test
    public void sampleITMCommand_validInput_4BytePayload() {
        String payload = "0348656c6c016f01200157016f0172016c01640121";
        byte[] sample_data = DatatypeConverter.parseHexBinary(payload);

        List<TclEntity> result = ITMDecoder.parseITMData(sample_data);
        assertNotNull(result);
        assertEquals("Hello World!", joinChannelData(result));
    }

    @Test
    public void sampleITMCommand_corruptedInput() {
        String payload = "0148656c6c016f01200157016f0172016c01640121";
        byte[] sample_data = DatatypeConverter.parseHexBinary(payload);

        List<TclEntity> result = ITMDecoder.parseITMData(sample_data);
        assertNull(result);
    }
    @Test
    public void sampleITMCommand_unevenInput() {
        byte[] sample_data = new byte[] {1, 72, 101, 108, 108, 1, 111, 1, 32, 1, 87, 1, 111, 1, 114, 1, 108, 1, 100, 1};
        List<TclEntity> result = ITMDecoder.parseITMData(sample_data);
        assertNull(result);
    }

}
