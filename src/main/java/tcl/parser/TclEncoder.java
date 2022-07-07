package tcl.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TclEncoder implements TclBase {

    public static byte[] generateTclCommand(@NotNull String command) {
        byte[] res = command.getBytes();
        return Common.concatByte(res, field_terminator);
    }
}
