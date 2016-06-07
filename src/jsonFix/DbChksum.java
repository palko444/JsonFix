package jsonFix;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import runComm.CommandExecutor;
import runComm.CommandResult;

public class DbChksum {

    public static HashMap<String, String> policies = new HashMap<String, String>();

    public static String getPolicyCksum(String name, String type, String version) {

        String pt = "pol_type=" + type.replaceAll("\"", "");
        String pn = "pol_name=" + name.replaceAll("\"", "");
        String pv = "version=" + version.replaceAll("\"", "");
        String key = name + "++" + type + " ++" + version;

        if (policies.containsKey(key)) {
            return policies.get(key);
        }
        String[] command = new String[] { "/opt/OV/bin/OpC/utils/opcpolicy", "-list_pols", pt, pn, pv };
        CommandResult cm = null;
        try {
            cm = CommandExecutor.exec(command, 5000);
        } catch (IOException e) {
            System.out.println("cannot execute command");
            System.exit(1);
        }

        if (cm.rc != 0) {
            System.out.println("Non 0 exit code: " + cm.rc);
            System.exit(1);
        }
        String cksum = parseCksum(cm.stdout);
        policies.put(key, cksum);
        return cksum;
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