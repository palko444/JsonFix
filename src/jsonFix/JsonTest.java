package jsonFix;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonTest {

    public void correctJson(File json) throws JsonParseException, JsonMappingException, IOException {

        System.out.println("#### Starting working on file " + json);
        ObjectMapper om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        JsonNode node = om.readTree(json);
        JsonNode installChecks = node.path("INSTALLCHECKS");
        Iterator<JsonNode> installSections = installChecks.elements();
        String dbCksum = null;
        int numberOfChnages = 0;

        while (installSections.hasNext()) {
            JsonNode section = installSections.next();
            if (section.path("action").toString().replaceAll("\"", "").equals("check_policy_exists")) {
                String policy_name = section.path("policy").toString();
                String policy_hdr_checksum = section.path("policy_hdr_checksum").toString();
                String policy_type = section.path("policy_type").toString();
                String policy_version = section.path("policy_version").toString();
                dbCksum = DbChksum.getPolicyCksum(policy_name, policy_type, policy_version);

                if (!policy_hdr_checksum.replaceAll("\"", "").equals(dbCksum)) {
                    ((ObjectNode) section).put("policy_hdr_checksum", dbCksum);
                    numberOfChnages++;
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
                if (section.path("MODE_NAME").toString().replaceAll("\"", "").matches("postassigncheck|predeassigncheck")) {
                    JsonNode actionChecks = section.path("ACTIONS");
                    Iterator<JsonNode> actionSection = actionChecks.elements();

                    while (actionSection.hasNext()) {
                        JsonNode aSection = actionSection.next();
                        if (aSection.path("action").toString().replaceAll("\"", "").equals("check_policy_on_node")) {
                            String policy_name = aSection.path("policy").toString();
                            String policy_hdr_checksum = aSection.path("policy_hdr_checksum").toString();
                            String policy_type = aSection.path("policy_type").toString();
                            String policy_version = aSection.path("policy_version").toString();
                            dbCksum = DbChksum.getPolicyCksum(policy_name, policy_type, policy_version);
                            if (!policy_hdr_checksum.toString().replaceAll("\"", "").equals(dbCksum)) {
                                ((ObjectNode) aSection).put("policy_hdr_checksum", dbCksum);
                                numberOfChnages++;
                            }
                        }
                    }
                }
            }
        }

        File solusionDir = new File(json.getParent());
        File backupDir = new File(solusionDir + "/BACKUP");
        File backupJson = new File(solusionDir + "/BACKUP/AAM_FIX.json");
        if (!backupDir.exists()) {
            backupDir.mkdir();
        }
        try {
            Files.copy(json.toPath(), backupJson.toPath());
        } catch (FileAlreadyExistsException e) {
            System.out.println("Backup already exist");
        }
        om.writeValue(json, node);
        System.out.println("Number of changes done in file: " + numberOfChnages);
        System.out.println("#### File " + json + " done");
        System.out.println();
    }
}
