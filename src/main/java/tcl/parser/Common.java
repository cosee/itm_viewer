package tcl.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Common {

    public static byte[] concatByte(byte[] barr, byte b) {
        byte[] res = new byte[barr.length + 1];
        System.arraycopy(barr, 0, res, 0, barr.length);
        res[res.length - 1] = b;
        return res;
    }

    public static byte[] concatByteArrays(byte[] barray1, byte[] barray2) {
        byte[] res = new byte[barray1.length + barray2.length];
        System.arraycopy(barray1, 0, res, 0, barray1.length);
        System.arraycopy(barray2, 0, res, barray1.length, barray2.length);
        return res;
    }
}
