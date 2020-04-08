package tcl;

import org.jetbrains.annotations.NotNull;

public class TclDecoder implements TclBase {
    private byte[] trace_data_preamble = new String("type target_trace data ").getBytes();

    private TclEntity getHeader(@NotNull byte[] trace_data) {
        if (trace_data.length >= trace_data_preamble.length) {
            for (int i = 0; i < trace_data_preamble.length; i++) {
                if (trace_data[i] != trace_data_preamble[i]) {
                    return null;
                }
            }
        }
        return null;
    }


}
