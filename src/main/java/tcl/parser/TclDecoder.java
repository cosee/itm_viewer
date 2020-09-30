package tcl.parser;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;

public final class TclDecoder implements TclBase {
    private static final byte[] trace_data_preamble = "type target_trace data ".getBytes();
    private static final byte[] trace_data_termination = new byte[]{0x0d, 0x0a, 0x1a};


    public static boolean rewindToTraceDataHeader(@NotNull ByteBuffer bb) {
        bb.rewind();
        while(bb.hasRemaining()) {
            boolean found = true;
            int i = 0;
            for (; i < trace_data_preamble.length; i++) {
                if (bb.get() != trace_data_preamble[i]) {
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
        if(!rewindToTraceDataHeader(bb)) {
            return null;
        }
        while(bb.hasRemaining()) {
            byte temp = bb.get();
            if(temp == field_terminator) {
                byte[] tmp = new byte[bb.limit()];
                bb.get(tmp,0, bb.limit());
                bb.position(0);
                return tmp;
            }
        }
        return null;
    }

    public static byte[] parseTclData(@NotNull byte[] trace_data) {
        if (trace_data.length >= (trace_data_preamble.length + trace_data_termination.length)) {
            for (int i = 0; i < trace_data_preamble.length; i++) {
                if (trace_data[i] != trace_data_preamble[i]) {
                    return null;
                }
            }
            int termination_index_offset = trace_data.length - trace_data_termination.length;
            for (int i = 0; i < trace_data_termination.length; i++) {
                if (trace_data[termination_index_offset + i] != trace_data_termination[i]) {
                    return null;
                }
            }
            if(trace_data[trace_data.length - 1] != field_terminator) {
                return null;
            }
            //TODO: add error handling if payload is not byte aligned
            byte[] content_raw = new byte[trace_data.length - trace_data_preamble.length - trace_data_termination.length];
            System.arraycopy(trace_data, trace_data_preamble.length, content_raw, 0, content_raw.length);
            String content = new String(content_raw);
            return DatatypeConverter.parseHexBinary(content);
        }
        return null;
    }

}
