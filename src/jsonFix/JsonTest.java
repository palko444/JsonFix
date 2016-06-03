package jsonFix;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {

    public void correctJson(File json) throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper om = new ObjectMapper();
        JsonNode node = om.readTree(json);
        JsonNode installChecks = node.path("INSTALLCHECKS");
        Iterator<JsonNode> sections = installChecks.elements();

        while (sections.hasNext()) {
            JsonNode section = sections.next();
            if (section.path("action").toString().equals("\"check_policy_exists\"")) {
                String policy_name = section.path("policy").toString();
                String policy_hdr_checksum = section.path("policy_hdr_checksum").toString();
                String policy_type = section.path("policy_type").toString();
                String policy_version = section.path("policy_version").toString();

                String dbCksum = DbChksum.getPolicyCksum(policy_name, policy_type, policy_version);

                if (!policy_hdr_checksum.equals(dbCksum)) {
                
                }

            }
        }
    }

}