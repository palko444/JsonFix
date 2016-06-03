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
        Iterator<JsonNode> installSections = installChecks.elements();
        String dbCksum = null;

        while (installSections.hasNext()) {
            JsonNode section = installSections.next();
            if (section.path("action").toString().replace("\"", "").equals("check_policy_exists")) {
                String policy_name = section.path("policy").toString();
                String policy_hdr_checksum = section.path("policy_hdr_checksum").toString();
                String policy_type = section.path("policy_type").toString();
                String policy_version = section.path("policy_version").toString();
                dbCksum = DbChksum.getPolicyCksum(policy_name, policy_type, policy_version);

                if (policy_hdr_checksum.replace("\"", "").equals(dbCksum)) {
                    System.out.println("tu " + policy_name);

                }
            }
        }

        JsonNode moduleChecks = node.path("MODULES");
        Iterator<JsonNode> moduleSections = moduleChecks.elements();

        while (moduleSections.hasNext()) {
            JsonNode sections = moduleSections.next();
            JsonNode modeChecks = sections.path("MODES");
            Iterator<JsonNode> modeSections = modeChecks.elements();

            while (modeSections.hasNext()) {
                JsonNode section = modeSections.next();
                if (section.path("MODE_NAME").toString().replace("\"", "").matches("postassigncheck|predeassigncheck")) {
                    JsonNode actionChecks = section.path("ACTIONS");
                    Iterator<JsonNode> actionSection = actionChecks.elements();

                    while (actionSection.hasNext()) {
                        JsonNode aSection = actionSection.next();
                        if (aSection.path("action").toString().replace("\"", "").equals("check_policy_on_node")) {
                            if (aSection.path("policy_hdr_checksum").toString().replace("\"", "").equals(dbCksum)) {
                            System.out.println("tam " + aSection.path("policy").toString());
                            }
                        }
                    }
                }
            }
        }
    }
}
