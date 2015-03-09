package Novelty;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Reader {
    private Hashtable<String, List<String>> documents = new Hashtable<String, List<String>>();
    Hashtable<String, String> document = new Hashtable<String, String>();

    public Reader(){ }

    String content = null;

    public Hashtable getFiles(File[] listOfFiles){

        for (File file : listOfFiles ){
            if(file.isFile()){
                String content = readFile(file);
                documents = getDocumentsAsHashtable(content);
            }
        }

        return documents;
    }

    private String readFile(File file) {
    try {
        FileReader reader = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        reader.read(chars);
        content = new String(chars);
        reader.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
        return content;
    }

    private Hashtable getDocumentsAsHashtable(String content){
        List<String> listOfDocuments = splitBetweenDocs(content);

        //Collections.reverse(listOfDocuments);

        for (int i = 1; i < listOfDocuments.size(); i++) {
            document.put(getDocNo(listOfDocuments.get(i)), listOfDocuments.get(i));
        }
        return document;
    }

    private List splitBetweenDocs(String content){

        List<String> documents = new ArrayList<String>();
        String[] docs = content.replaceAll("</DOC>", "").split("<DOC>");
        for (int i = 0; i < docs.length; i++) {
            documents.add(docs[i]);
        }
        return documents;
    }

    private String getDocNo (String content){
        String substring = StringUtils.substringBetween(content, "<DOCNO>", "</DOCNO>");
        return substring;
    }
}
