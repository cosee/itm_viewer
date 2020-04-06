package tcl;

import org.jetbrains.annotations.NotNull;

public final class Encoder {
    private static final byte field_terminator = 0x1A;

    @NotNull
    public static byte[] generate_tcl_command(
            @NotNull String command) {
        byte[] res = command.getBytes();
        return Common.concatByte(res, field_terminator);
    }
}
