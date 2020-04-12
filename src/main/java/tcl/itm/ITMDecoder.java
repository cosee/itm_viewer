package tcl.itm;

import org.jetbrains.annotations.NotNull;
import tcl.parser.TclEntity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ITMDecoder implements ITMBase {

    /**
     * The calculation of the size field can be simplified by 2^(size_field - 1) but I decided against it to make it
     * easier to comprehend it for others (and of course for me later)
     * For reference see SWIT packet layout: http://infocenter.arm.com/help/index.jsp?topic=/com.arm.doc.ddi0314h/Chdbicbg.html
     * @param size_field: ITM packet size bit field
     * @return size in bytes
     */
    private static int getITMSize(byte size_field) {
        if(size_field == 0x1) {
            return 1;
        } else if(size_field == 0x2) {
            return 2;
        } else if(size_field == 0x3) {
            return 4;
        }
        return 0;
    }


    public static List<TclEntity> parseITMData(@NotNull byte[] itm_data) {
        if(itm_data.length == 0) {
            return null;
        }
        List<TclEntity> itmPackages = new ArrayList<TclEntity>();
        for(int i =0; i<itm_data.length;) {

            byte header = itm_data[i];
            byte mandatory_field = (byte) (header & header_mandatory_field_bitmask);
            byte source = (byte) (header & header_source_address_bitmask);
            source = (byte) (source >> header_source_address_shift_offset);

            byte size_field = (byte) (header & header_size_bitmask);
            if(size_field != 0 && mandatory_field == header_mandatory_field_value) {
                int size = getITMSize(size_field);
                if(size == 0) {
                    return null;
                }
                int start = i+1;
                int end = start + size;
                byte[] payload = Arrays.copyOfRange(itm_data, start, end);
                String content = new String(payload, StandardCharsets.US_ASCII);

                TclEntity entity = new TclEntity();
                entity.setChannel(source);
                entity.setContent(content);
                itmPackages.add(entity);
                i=end;
            } else {
                return null;
            }

        }
        return itmPackages;
    }
    
}
