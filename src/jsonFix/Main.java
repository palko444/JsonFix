package jsonFix;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Main {

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        // TODO Auto-generated method stub
        // File baseFolder = new File ("/var/opt/OpC_local/SALT/store");
        File baseFolder = new File("/home/pala/json");
        File tj = new File("/home/pala/json/WW_UXMON/11.00/WW_UXMON.json");
        // JsonFiles js = new JsonFiles();
        // System.out.println(js.getFiles(baseFolder));
        JsonTest jt = new JsonTest();
        jt.correctJson(tj);

    }

}
