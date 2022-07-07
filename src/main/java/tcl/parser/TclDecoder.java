package tcl.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TclDecoder implements TclBase {
    private static final byte[] TRACE_DATA_PREAMBLE = "type target_trace data ".getBytes();
    private static final byte[] TRACE_DATA_TERMINATION = new byte[]{0x0d, 0x0a, 0x1a};
    private static final byte[] EMPTY_ARRAY = new byte[0];


    public static boolean rewindToTraceDataHeader(@NotNull ByteBuffer bb) {
        bb.rewind();
        while (bb.hasRemaining()) {
            boolean found = true;
            int i = 0;
            for (; i < TRACE_DATA_PREAMBLE.length; i++) {
                if (bb.get() != TRACE_DATA_PREAMBLE[i]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                bb.compact().flip();
                return true;
            }
        }
        return false;
    }

    public static byte[] stripTclData(@NotNull ByteBuffer bb) {
        if (!rewindToTraceDataHeader(bb)) {
            return EMPTY_ARRAY;
        }
        while (bb.hasRemaining()) {
            byte temp = bb.get();
            if (temp == field_terminator) {
                byte[] tmp = new byte[bb.limit()];
                bb.get(tmp, 0, bb.limit());
                bb.position(0);
                return tmp;
            }
        }
        return EMPTY_ARRAY;
    }

    public static byte[] parseTclData(byte[] traceData) {
        if (traceData.length >= (TRACE_DATA_PREAMBLE.length + TRACE_DATA_TERMINATION.length)) {
            for (int i = 0; i < TRACE_DATA_PREAMBLE.length; i++) {
                if (traceData[i] != TRACE_DATA_PREAMBLE[i]) {
                    return EMPTY_ARRAY;
                }
            }
            int terminationIndexOffset = traceData.length - TRACE_DATA_TERMINATION.length;
            for (int i = 0; i < TRACE_DATA_TERMINATION.length; i++) {
                if (traceData[terminationIndexOffset + i] != TRACE_DATA_TERMINATION[i]) {
                    return EMPTY_ARRAY;
                }
            }
            if (traceData[traceData.length - 1] != field_terminator) {
                return EMPTY_ARRAY;
            }
            //TODO: add error handling if payload is not byte aligned
            byte[] contentRaw = new byte[traceData.length - TRACE_DATA_PREAMBLE.length - TRACE_DATA_TERMINATION.length];
            System.arraycopy(traceData, TRACE_DATA_PREAMBLE.length, contentRaw, 0, contentRaw.length);
            String content = new String(contentRaw);
            return DatatypeConverter.parseHexBinary(content);
        }
        return EMPTY_ARRAY;
    }

}
