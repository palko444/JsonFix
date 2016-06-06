package jsonFix;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class JsonFiles {

    Set<File> jsons = new HashSet<File>();

    public Set<File> getFiles(File folder) {

        for (File f : folder.listFiles()) {
            if ((!f.toString().endsWith("BACKUP")) && (!f.toString().endsWith("PATCH"))) {
                if (f.isDirectory()) {
                    getFiles(f);
                }
                if (f.toString().endsWith(".json")) {
                    jsons.add(f);
                }
            }
        }

        return jsons;
    }

}
