package jsonFix;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Main {

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        // TODO Auto-generated method stub
        File baseFolder = new File("/var/opt/OpC_local/SALT/store");
        JsonFiles js = new JsonFiles();
        JsonTest jt = new JsonTest();
        js.getFiles(baseFolder);

        for (File f : js.getFiles(baseFolder)) {
            jt.correctJson(f);
        }
    }

}
