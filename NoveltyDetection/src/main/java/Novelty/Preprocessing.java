package Novelty;

import weka.core.stemmers.SnowballStemmer;
import weka.core.Stopwords;

import java.util.*;

public class Preprocessing {
    Stopwords stopwords = new Stopwords();
    SnowballStemmer stemmer = new SnowballStemmer("english");
    Hashtable<String, List<String>> preprocessedDocuments = new Hashtable<String, List<String>>();

    public Preprocessing(){ }

    public Hashtable preprocess(Hashtable document, boolean isJaccard){
        preprocessedDocuments = removeStopwords(document, isJaccard);
        return preprocessedDocuments;
    }

    private Hashtable removeStopwords(Hashtable documents, boolean isJaccard) {
        Hashtable<String, List<String>> listDocuments = new Hashtable<String, List<String>>();

        Enumeration e = documents.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            listDocuments.put(key, splitSentence(documents.get(key).toString(), isJaccard));
        }
        return listDocuments;
    }

    private List<String> splitSentence(String sentence, boolean isJaccard){
        List<String> sentenceList = new ArrayList<String>();

        String[] contentSplit = sentence
                .toString().replaceAll("[-/(){},.;!?%`´'_]", "")
                .replaceAll("[<>]", " ")
                .replaceAll("\n", " ")
                .replaceAll("     ", " ")
                .replaceAll("    ", " ")
                .replaceAll("   ", " ")
                .replaceAll("  ", " ")
                .toLowerCase().split(" ");
        for (int i = 0; i < contentSplit.length; i++) {
            sentenceList.add(contentSplit[i]);
        }
        sentenceList = removeStopwords(sentenceList);
        sentenceList = porterStemmer(sentenceList);

        if(isJaccard == true){
            sentenceList = removeDuplicates(sentenceList);
        }
        sentenceList.remove("");
        sentenceList.remove(" ");
        return sentenceList;
    }

    private List<String> removeStopwords(List<String> removeStopWords) {
        stopwords.add("doc");
        stopwords.add("docno");
        stopwords.add("fileId");
        stopwords.add("text");
        stopwords.add("first");
        stopwords.add("second");
        stopwords.add("head");
        stopwords.add("byline");
        stopwords.add("dateline");
        stopwords.add("note");
        stopwords.add("so");
        stopwords.add("co");
        stopwords.add("g");

        List<String> stopList = new ArrayList<String>();
        String[] stopSplit = stopwords.toString().split(",");
        for (int i = 0; i < stopSplit.length; i++) {
            stopList.add(stopSplit[i]);
        }

        List<Integer> numberToRemove = new ArrayList();

        for (int i = 0; i < removeStopWords.size(); i++) {
            for (int j = 0; j < stopList.size(); j++) {
                if (removeStopWords.get(i).equals(stopList.get(j))) {
                    numberToRemove.add(i);
                }
            }
        }

        for (int i = numberToRemove.size() - 1; i >= 0; i--) {
            int j = numberToRemove.get(i);
            removeStopWords.remove(j);
        }

        return removeStopWords;
    }

    private List<String> getValuesAsList(Hashtable table){
        List<String> value = new ArrayList<String>();

        Enumeration e = table.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            value = ((List<String>) table.get(key));
            System.out.println("Key: " + key + " value: " + ((List<String>) table.get(key)));
            System.out.println(value);
        }
        return value;

    }

    private List<String> removeDuplicates(List<String> content){
        HashSet hashSet = new HashSet();
        hashSet.addAll(content);
        content.clear();
        content.addAll(hashSet);

        return content;
    }

    private List<String> snowballStemmer(List<String> content){
        List<String> stemmedContent = new ArrayList<String>();

        for (int i = 0; i < content.size(); i++) {
            String stemmedWord = stemmer.stem(content.get(i));
            stemmedContent.add(stemmedWord);
        }
        return stemmedContent;
    }

    private List<String> porterStemmer(List<String> words){
        List<String> stemmedWords = new ArrayList<String>();

        for(String word : words){
            Porterstemmer porterstemmer = new Porterstemmer();
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                porterstemmer.add(c);
            }
            porterstemmer.stem();
            stemmedWords.add(porterstemmer.toString());
        }

        return stemmedWords;
    }
}
