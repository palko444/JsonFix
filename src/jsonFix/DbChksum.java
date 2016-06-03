package jsonFix;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import runComm.CommandExecutor;
import runComm.CommandResult;

public class DbChksum {

    public static String getPolicyCksum(String name, String type, String version) {

        String[] command = new String[] { "/opt/OV/bin/OpC/utils/opcpolicy", "-list_pols", "pol_type=", type, "pol_name=", name, "version=", version };
        CommandResult cm = null;
        try {
            cm = CommandExecutor.exec(command, 5000);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("cannot execute " + command);
            System.exit(1);
        }

        if (cm.rc != 0) {
            System.out.println("cannot execute " + command);
            System.exit(1);
        }
        return parseCksum(cm.stdout);
    }

    private static String parseCksum(String policyDetails) {
        Pattern p = Pattern.compile("checksum_header:\\s*(\\S*)\\s*\n");
        Matcher mp = p.matcher(policyDetails);
        if (mp.find()) {
            return mp.group(1);
        }
        System.out.println("unable to parse cksum header");
        System.exit(1);
        return null;
    }
}