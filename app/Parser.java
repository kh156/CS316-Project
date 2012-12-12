import java.util.*;
import java.io.*;

public class Parser {
    public static void main(String[] args) throws IOException {
        Scanner s = new Scanner(new File("/Users/kuanghan/Desktop/play-1.2.5/samples-and-tests/forum/app/2007TextbookInventory.csv"));
        FileWriter f = new FileWriter("/Users/kuanghan/desktop/new.yml");
        PrintWriter out = new PrintWriter(f);
        /**
         * ;;;;;;;;;;;;;;;;;;;;
State Approved Textbook List 2007;;;;;;;;;;;;;;;;;;;;
3/25/2008;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;
DistrictName;;;;SubjectDesc;;ISBN10;;Title;;;;;;Author;;;Publisher;;;PublicationYear
Barrington;;;;English/Lang;;0321010337;;Call to Write (The);;;;;;Trimbur;;;Longman;;;98
         */
        for (int i = 0; i < 6; ++i) {
            s.nextLine();
        }
        int cnt = 1;
        while (s.hasNextLine()) {
            String[] words = s.nextLine().split(";+");
            if (words.length < 7) continue;
            out.write("Textbook(f" + cnt + "):\n");
            out.write("    name: \"" + words[3].replaceAll("\"", "") + "\"\n");
            out.write("    ISBN: \"" + words[2].replaceAll("\"", "") + "\"\n");
            out.write("    author: \"" + words[4].replaceAll("\"", "") + "\"\n");
            out.write("    description: test description\n\n");
            ++cnt;
        }
        
        
    }
    
    
}
