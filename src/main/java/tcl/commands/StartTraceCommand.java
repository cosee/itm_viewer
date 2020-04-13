package tcl.commands;

import tcl.parser.TclEncoder;

public final class StartTraceCommand implements BaseCommand {
    public final static String startTclTraceServer = "tcl_trace on";

    @Override
    public byte[] generateCommand() {
        return TclEncoder.generate_tcl_command(startTclTraceServer);
    }

}
