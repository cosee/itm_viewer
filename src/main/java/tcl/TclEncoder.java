package tcl;

import org.jetbrains.annotations.NotNull;

public final class TclEncoder implements TclBase {

    @NotNull
    public static byte[] generate_tcl_command(
            @NotNull String command) {
        byte[] res = command.getBytes();
        return Common.concatByte(res, field_terminator);
    }
}
