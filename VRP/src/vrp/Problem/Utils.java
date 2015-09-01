
package vrp.Problem;
 

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * -----------------------------------------------------------------------------
 *
 * @author ASAP Automated Scheduling, Optimisation and Planing Research Group
 * (ASAP) School of Computer Science University of Nottingham United Kingdom
 * April, 2013
 * -----------------------------------------------------------------------------
 * Please email any bugs to Jose.Ortiz_Bayliss@nottingham.ac.uk
 * -----------------------------------------------------------------------------
 */

class Utils {
    
    /**
     * Saves the string provided into a text file.
     * <p>
     * @param fileName The name of the file where the data will be saved to.
     * @param string The text to be saved to the file.
     */
    public static void writeToFile(String fileName, String string) {
        File f;
        FileWriter fw;
        try {
            f = new File(fileName);
            fw = new FileWriter(f);
            fw.write(string);
            fw.close();
        } catch (IOException e) {
            System.err.println("An error occurred when attemptin to save the file \'" + fileName + "\'.");
            System.err.println("The system will halt.");
            System.exit(1);
        }
    }    
    
    /**
     * Loads the contents of a text file.
     * <p>
     * @param fileName The name of the file where the text is stored.
     * @return The contents of the text file.
     */
    public static String readFromFile(String fileName) {
        File file = new File(fileName);
        char[] data;
        int size = (int) file.length(), chars_read = 0;
        FileReader in;
        try {
            in = new FileReader(file);
            data = new char[size];
            while (in.ready()) {
                chars_read += in.read(data, chars_read, size - chars_read);
            }
            in.close();
            return (new String(data, 0, chars_read));
        } catch (Exception e) {
            System.out.println("An error occurred while attempting to read the file \'" + fileName + "\'.");
            System.out.println("The system will halt.");
            System.exit(1);
        }
        return null;
    }                    
}
