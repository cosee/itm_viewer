package tcl.commands;

import tcl.parser.TclEncoder;

public final class StartTraceCommand implements BaseCommand {

    public static final String START_TCL_TRACE_SERVER = "tcl_trace on";

    @Override
    public byte[] generateCommand() {
        return TclEncoder.generateTclCommand(START_TCL_TRACE_SERVER);
    }

}
