package tcl.commands;

import tcl.parser.TclEntity;

import java.nio.ByteBuffer;

public interface BaseCommand {
    public byte[] generateCommand();
}
