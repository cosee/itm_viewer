package tcl;

import org.junit.Test;
import tcl.parser.Common;

import static org.junit.Assert.*;
import java.util.Arrays;

public class CommonTest {
    @Test
    public void concatArrays_validInput() {
        byte[] a = new byte[] { 0x1, 0x2};
        byte[] b = new byte[] { 0x3, 0x4};
        byte[] expected_res = new byte[] { 0x1, 0x2, 0x3, 0x4};
        byte[] res = Common.concatByteArrays(a, b);
        assert(Arrays.equals(expected_res, res));
    }

    @Test
    public void concatArrays_invalidInput() {
        byte[] a = new byte[] { 0x1, 0x2};
        byte[] b = new byte[] { 0x3, 0x4};
        byte[] fail_res = new byte[] { 0x2, 0x2, 0x3, 0x4};
        byte[] res = Common.concatByteArrays(a, b);
        assertFalse(Arrays.equals(fail_res, res));
    }

    @Test
    public void concatByte_validInput() {
        byte[] a = new byte[] { 0x1, 0x2};
        byte b = 0x3;
        byte[] expected_res = new byte[] { 0x1, 0x2, 0x3};
        byte[] res = Common.concatByte(a, b);
        assert(Arrays.equals(expected_res, res));
    }

    @Test
    public void concatByte_invalidInput() {
        byte[] a = new byte[] { 0x1, 0x2};
        byte b = 0x4;
        byte[] expected_res = new byte[] { 0x1, 0x2, 0x3};
        byte[] res = Common.concatByte(a, b);
        assertFalse(Arrays.equals(expected_res, res));
    }
}
