package tcl;

import org.jetbrains.annotations.NotNull;

public final class TclDecoder implements TclBase {
    private static byte[] trace_data_preamble = new String("type target_trace data ").getBytes();

    public static byte[] parseTclData(@NotNull byte[] trace_data) {
        if (trace_data.length >= trace_data_preamble.length) {
            for (int i = 0; i < trace_data_preamble.length; i++) {
                if (trace_data[i] != trace_data_preamble[i]) {
                    return null;
                }
            }
            if(trace_data[trace_data.length - 1] != field_terminator) {
                return null;
            }
            byte[] content = new byte[trace_data.length - trace_data_preamble.length - 1];
            System.arraycopy(trace_data, trace_data_preamble.length, content, 0, content.length);
            return content;
        }
        return null;
    }

}
