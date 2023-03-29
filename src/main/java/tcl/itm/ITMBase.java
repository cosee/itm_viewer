package tcl.itm;

/**
 * for ITM Trace packet format see  <a href="https://developer.arm.com/documentation/ddi0314/h/Instrumentation-Trace-Macrocell/About-the-Instrumentation-Trace-Macrocell/Timestamp-packet">SWIT packet layout</a>
 */
public interface ITMBase {
    int header_size_bitmask = 0x3;
    int header_mandatory_field_bitmask = 0x4;
    int header_mandatory_field_value = 0x0;
    int header_source_address_bitmask =  0xF8;
    int header_source_address_shift_offset = 3;

}
