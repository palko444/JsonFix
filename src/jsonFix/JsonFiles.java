package jsonFix;

import java.io.File;
import java.util.ArrayList;

public class JsonFiles {

    ArrayList<File> jsons = new ArrayList<File>();

    public ArrayList<File> getFiles(File folder) {

        for (File f : folder.listFiles()) {
            if ((!f.toString().endsWith("BACKUP")) || (!f.toString().endsWith("PATCH"))) {
                if (f.isDirectory()) {
                    getFiles(f);
                }
                if (f.toString().endsWith(".json")) {
                    // System.out.println(f);
                    jsons.add(f);
                }
            }
        }

        return jsons;
    }

}
