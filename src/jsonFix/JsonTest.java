package jsonFix;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {
    
    public void correctJson (File json) throws JsonParseException, JsonMappingException, IOException {
        
        ObjectMapper om = new ObjectMapper();
         JsonData44 value = om.readValue(json, JsonData44.class);
         System.out.println(value);
//        INSTALLCHECKS
//        "action" : "check_policy_exists
//        "policy" : "UXMON_actmon_PRE",
//        "policy_hdr_checksum" : "fe8245ee5a9125bed3911661588f0f514a297058",
//        "policy_type" : "Logfile_Entry",
//        "policy_version" : "0001.0000"
    }
    
}
