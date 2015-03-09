package Novelty;

import java.util.*;

public class CosineSimilarity {

    public CosineSimilarity() { }

    public Hashtable<String, Double> getCosineFromHashtable(Hashtable documents){
        Hashtable<String, Double> cosineSimilarity = new Hashtable<String, Double>();

        List<String> keyList = getKeysAsList(documents);

        for (int i = 0; i < keyList.size(); i++) {
            for (int j = i+1; j < keyList.size(); j++) {
                List<String> content1 = (List<String>) documents.get(keyList.get(i));

                List<String> content2 = (List<String>) documents.get(keyList.get(j));

                List<String> terms = getTerms(content1, content2);
                Hashtable<String, Integer> TF1 = getTermfrequency(terms, content1);
                Hashtable<String, Integer> TF2 = getTermfrequency(terms, content2);

                double cosine = getCosine(TF1, TF2);

                if (cosine < 0.6){
                    cosineSimilarity.put(keyList.get(i) + "+" + keyList.get(j), cosine);
                }

            }
        }

        System.out.println(cosineSimilarity.size());
        System.out.println(cosineSimilarity);
        return cosineSimilarity;
    }

    private List<String> getTerms(List<String> content1, List<String> content2){
        Set<String> set = new HashSet<String>();

        set.addAll(content1);
        set.addAll(content2);

        return new ArrayList<String>(set);
    }

    private Hashtable<String, Integer> getTermfrequency(List<String> terms, List<String> content){
        Hashtable<String, Integer> termFrequency = new Hashtable<String, Integer>();

        for (String term : terms) {
            termFrequency.put(term, 0);
        }


        for (String term : terms){
            for (String cont : content){
                if (term.equals(cont)){
                    int i = termFrequency.get(term);
                    termFrequency.put(term, i+1);
                }
            }
        }
        return termFrequency;
    }

    private List<String> getKeysAsList(Hashtable keys){
        List<String> listOfKeys = new ArrayList<String>();

        Enumeration i = keys.keys();

        while (i.hasMoreElements()){
            listOfKeys.add((String) i.nextElement());
        }

        return listOfKeys;
    }

    private double getCosine(Hashtable<String, Integer> firstVector, Hashtable<String, Integer> secondVector){
        double similarity;
        double sumDotProduct = 0.0;
        double norm1;
        double norm2;
        List<Integer> vector1 = new ArrayList<Integer>();
        List<Integer> vector2 = new ArrayList<Integer>();

        Enumeration e = firstVector.elements();
        while (e.hasMoreElements()){
            vector1.add((Integer) e.nextElement());
        }

        Enumeration i = secondVector.elements();
        while (i.hasMoreElements()){
            vector2.add((Integer) i.nextElement());
        }

        for (int j = 0; j < vector1.size(); j++) {
            double product = vector1.get(j) * vector2.get(j);
            sumDotProduct = sumDotProduct + product;
        }

        norm1 = getNorm(vector1);
        norm2 = getNorm(vector2);

        similarity = sumDotProduct/(norm1*norm2);

        return similarity;
    }

    private double getNorm(List<Integer> vector){
        double norm = 0.0;
        for (int i : vector){
            double product = i*i;
            norm += product;
        }
        return Math.sqrt(norm);
    }
}
