package tcl;

import org.jetbrains.annotations.NotNull;

public class Common {
    @NotNull
    public static byte[] concatByte(
            @NotNull byte[] barr,
            @NotNull byte b) {
        byte[] res = new byte[barr.length + 1];
        System.arraycopy(barr,0, res, 0, barr.length);
        res[res.length - 1] = b;
        return res;
    }

    @NotNull
    public static byte[] concatByteArrays(
            @NotNull byte[] barray1,
            @NotNull byte[] barray2) {
        byte[] res = new byte[barray1.length + barray2.length];
        System.arraycopy(barray1,0, res, 0, barray1.length);
        System.arraycopy(barray2,0, res, barray1.length, barray2.length);
        return res;
    }
}
