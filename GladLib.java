import edu.duke.*;
import java.util.*;

public class GladLib {
    private HashMap<String, ArrayList<String>> map;
    private ArrayList<String> tempList;
    private int countReplaced;
    private Random myRandom;
    
    private static String dataSourceURL = "http://dukelearntoprogram.com/course3/data";
    private static String dataSourceDirectory = "data";
    
    public GladLib(){
        map = new HashMap<String, ArrayList<String>>();
        tempList = new ArrayList<String>();
        myRandom = new Random();
        initializeFromSource(dataSourceDirectory);
    }
    
    public GladLib(String source){
        initializeFromSource(source);
        myRandom = new Random();
    }
    
    private void initializeFromSource(String source) {
        String[] labels = {"country", "noun", "animal",
                           "adjective", "name", "color",
                           "timeframe", "verb", "fruit"};
        for (String s : labels) {
            ArrayList<String> list = readIt(source+"/" + s + ".txt");
            map.put(s, list);
        }
        countReplaced = 0;
    }
    
    private String randomFrom(ArrayList<String> source){
        int index = myRandom.nextInt(source.size());
        return source.get(index);
    }
    
    private String getSubstitute(String label) {
        if (label.equals("number")) {
            return ""+myRandom.nextInt(50) + 5;
        }
        return randomFrom(map.get(label));
    }
    
    private int totalWordsInMap() {
        int totalWords = 0;
        for (ArrayList<String> s : map.values()) {
            totalWords += s.size();
        }
        return totalWords;
    }
    
    private int totalWordsConsidered() {
        
        return 1;
    }
    private String processWord(String w){
        int first = w.indexOf("<");
        int last = w.indexOf(">",first);
        if (first == -1 || last == -1){
            return w;
        }
        String prefix = w.substring(0,first);
        String suffix = w.substring(last+1);
        String sub = getSubstitute(w.substring(first+1,last));
        if (!tempList.contains(sub)) {
            tempList.add(sub);
        }
        else {
            countReplaced += 1;
            return processWord(w);
        }
        return prefix+sub+suffix;
    }
    
    private void printOut(String s, int lineWidth){
        int charsWritten = 0;
        for(String w : s.split("\\s+")){
            if (charsWritten + w.length() > lineWidth){
                System.out.println();
                charsWritten = 0;
            }
            System.out.print(w+" ");
            charsWritten += w.length() + 1;
        }
    }
    
    private String fromTemplate(String source){
        String story = "";
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for(String word : resource.words()){
                story = story + processWord(word) + " ";
            }
        }
        else {
            FileResource resource = new FileResource(source);
            for(String word : resource.words()){
                story = story + processWord(word) + " ";
            }
        }
        return story;
    }
    
    private ArrayList<String> readIt(String source){
        ArrayList<String> list = new ArrayList<String>();
        if (source.startsWith("http")) {
            URLResource resource = new URLResource(source);
            for(String line : resource.lines()){
                list.add(line);
            }
        }
        else {
            FileResource resource = new FileResource(source);
            for(String line : resource.lines()){
                list.add(line);
            }
        }
        return list;
    }
    
    public void makeStory(){
        tempList.clear();
        System.out.println("\n");
        String story = fromTemplate("data/madtemplate2.txt");
        printOut(story, 60);
        System.out.println("\n");
        System.out.println("Total words replaced = " + countReplaced);
        totalWordsInMap();
        System.out.println("Number of words in all the Arraylists in the HashMap = " + totalWordsInMap());
        System.out.println(tempList.size());
    }
}
