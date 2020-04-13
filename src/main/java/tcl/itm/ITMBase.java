package tcl.itm;

/**
 * for ITM Trace packet format see  http://infocenter.arm.com/help/index.jsp?topic=/com.arm.doc.ddi0314h/Chdbicbg.html
 */
public interface ITMBase {
    byte header_size_bitmask = 0x3;
    byte header_mandatory_field_bitmask = 0x4;
    byte header_mandatory_field_value = 0x0;
    byte header_source_address_bitmask = (byte) 0xF8;
    byte header_source_address_shift_offset = 3;

}
