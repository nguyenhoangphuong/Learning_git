import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.misfit.ta.utils.Files;


public class ELBLogParser {

    String filename= "/Users/thinh/Downloads/elb_logs/file1.txt";
    Map<String, Integer> ips = new HashMap<String, Integer>();
    public ELBLogParser() {
        try {
            File file = Files.getFile(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (ips.containsKey(line)) {
                    Integer count = ips.get(line);
                    ips.put(line, count+1); 
                } else {
                    ips.put(line, 1);
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String line: ips.keySet()) {
            System.out.println(line + "\t" + ips.get(line));
        }
    }
    
    public static void main (String[] args) {
        ELBLogParser parser = new ELBLogParser();
    }

}
