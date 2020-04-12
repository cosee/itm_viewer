package tcl.commands;

import tcl.parser.TclEncoder;

public final class StartTraceCommand implements BaseCommand {
    public final static String startTclTraceServer = "";

    @Override
    public byte[] generateCommand() {
        return TclEncoder.generate_tcl_command(startTclTraceServer);
    }

}
