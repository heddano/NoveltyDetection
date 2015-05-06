package Novelty;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CosineSimilarity {
    private List<String> allTerms = new ArrayList(); //to hold all terms
    private Hashtable<String, Double> inverseDocumentFrequency = new Hashtable<String, Double>();

    public CosineSimilarity() { }

    public Hashtable<String, Double> getCosineFromHashtable(Hashtable documents) throws ParseException {
        Hashtable<String, Double> cosineSimilarity = new Hashtable<String, Double>();
        createListOfAllTerms(documents);
        getAllIdfs(documents, allTerms);

        System.out.println(documents.size());

        List<String> keyList = getKeysAsList(documents);

        int k = 0;
        for (int i = 0; i < keyList.size(); i++) {
            for (int j = i + 1; j < keyList.size(); j++) {
                k++;
                System.out.println(k);

                List<String> content1 = (List<String>) documents.get(keyList.get(i));
                List<String> content2 = (List<String>) documents.get(keyList.get(j));

                Hashtable<String, Double> TF1 = getTermfrequency(content1);
                Hashtable<String, Double> TF2 = getTermfrequency(content2);

                Hashtable<String, Double> sortedTF1 = sortOnTerms(TF1, TF2, 1);
                Hashtable<String, Double> sortedTF2 = sortOnTerms(TF1, TF2, 2);

                Hashtable<String, Double> tfIdf1 = getTfIdf(sortedTF1);
                Hashtable<String, Double> tfIdf2 = getTfIdf(sortedTF2);

                double dateWeight = getDateWeight(keyList.get(i), keyList.get(j));
                double cosine = getCosine(tfIdf1, tfIdf2);

                double score = dateWeight + cosine;

/*                if (score <= 0.55){
                    cosineSimilarity.put(keyList.get(i) + "+" + keyList.get(j), cosine);
                    String result = keyList.get(i).replaceAll("[^A-Za-z0-9-]", "") + " + " + keyList.get(j).
                            replaceAll("[^A-Za-z0-9-]", "");
                    writeToFile(result);
                    String result2 = keyList.get(j).replaceAll("[^A-Za-z0-9-]", "") + " + " +  keyList.get(i).
                            replaceAll("[^A-Za-z0-9-]", "");
                    writeToFile(result2);
                }*/

            }
        }

        System.out.println(cosineSimilarity.size());
        System.out.println(cosineSimilarity);
        return cosineSimilarity;
    }

    private Hashtable<String, Double> getTfIdf(Hashtable<String, Double> tf) {
        Hashtable<String, Double> tfIdfWeights = new Hashtable<String, Double>();

        Enumeration e = tf.keys();

        while (e.hasMoreElements()){
            String key = (String) e.nextElement();
            double termFrequency = tf.get(key);
            double idf = inverseDocumentFrequency.get(key);
            double tfidf = termFrequency*idf;

            tfIdfWeights.put(key, tfidf);
        }

        return tfIdfWeights;
    }

    private Hashtable<String, Double> getTermfrequency(List<String> terms){
        Hashtable<String, Integer> initialTermFrequency = new Hashtable<String, Integer>();
        Hashtable<String, Double> termFrequency = new Hashtable<String, Double>();

        for (String term : terms) {
            initialTermFrequency.put(term, 0);
        }

        for (String term : terms){
                    int i = initialTermFrequency.get(term);
                    initialTermFrequency.put(term, i + 1);
        }

        Enumeration e = initialTermFrequency.keys();

        while (e.hasMoreElements()){
            String key = (String) e.nextElement();
            int value = initialTermFrequency.get(key);
            double tf = (1 + (Math.log(value))/(Math.log(2)));
            termFrequency.put(key, tf);
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

    private Hashtable<String, Double> sortOnTerms(
            Hashtable<String, Double> firstVector,
            Hashtable<String, Double> secondVector,
            int firstOrSecond){
        List<String> terms = new ArrayList<String>();
        Hashtable<String, Double> newVector = new Hashtable<String, Double>();

        Enumeration e = firstVector.keys();
        while (e.hasMoreElements()){
            String key = (String) e.nextElement();
            terms.add(key);
        }

        Enumeration i = secondVector.keys();
        while (i.hasMoreElements()){
            String key = (String) i.nextElement();
            terms.add(key);
        }


        Set<String> set = new HashSet<String>();
        set.addAll(terms);

        terms = new ArrayList<String>(set);


        if (firstOrSecond == 1){
            for (String term: terms){
                Double weight = firstVector.get(term);
                if (weight == null){
                    weight = 0.0;
                }
                newVector.put(term, weight);
            }
        }

        if (firstOrSecond == 2){
            for (String term: terms){
                Double weight = secondVector.get(term);
                if (weight == null){
                    weight = 0.0;
                }
                newVector.put(term, weight);
            }
        }

        return newVector;
    }

    private double getCosine(Hashtable<String, Double> firstVector, Hashtable<String, Double> secondVector){
        double similarity;
        double sumDotProduct = 0.0;
        double norm1;
        double norm2;
        List<Double> vector1 = new ArrayList<Double>();
        List<Double> vector2 = new ArrayList<Double>();

        Enumeration e = firstVector.elements();
        while (e.hasMoreElements()){
            vector1.add((Double) e.nextElement());
        }

        Enumeration i = secondVector.elements();
        while (i.hasMoreElements()){
            vector2.add((Double) i.nextElement());
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

    private double getNorm(List<Double> vector){
        double norm = 0.0;
        for (double i : vector){
            double product = i*i;
            norm += product;
        }
        return Math.sqrt(norm);
    }

    private void createListOfAllTerms(Hashtable<String, List<String>> content) {
        Enumeration e = content.keys();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            List<String> value = content.get(key);
            for (String term : value) {
                allTerms.add(term);
            }
        }
    }

    private void getAllIdfs(Hashtable<String, List<String>> allDocuments, List<String> allTerms) {
        for (String term : allTerms) {
            inverseDocumentFrequency.put(term, idfCaluculator(allDocuments, term));
        }
    }


    private double idfCaluculator(Hashtable<String, List<String>> allDocuments, String termToCheck){
        Enumeration e = allDocuments.keys();

        int count = 0;

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            List<String> value = allDocuments.get(key);

            for (String term : value) {
                if(term.equalsIgnoreCase(termToCheck)){
                    count++;
                    break;
                }
            }
        }
        return (Math.log((float) allDocuments.size() / count) / (Math.log(2)));
    }

    private double getDateWeight(String docno1, String docno2) throws ParseException {
        String date1string = getPublicationDate(docno1);
        String date2string = getPublicationDate(docno2);

        System.out.println(date1string + " + " + date2string);
        DateFormat format = new SimpleDateFormat("yyMMdd");
        Date date1 = format.parse(date1string);
        Date date2 = format.parse(date2string);


        long difference = date2.getTime() - date1.getTime();
        double days = Math.abs(TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));

        if (days >= 0.0 && days <= 5.0){
            return 0.4;
        }
        else if (days > 5.0 && days <= 10.0){
            return 0.2;
        }
        else if (days > 10.0 && days <= 15.0){
            return 0.1;
        }
        else if (days > 15.0 && days <= 20.0){
            return 0.05;
        }
        else if (days > 20.0 && days <= 25.0){
            return 0.025;
        }
        else if (days > 25.0 && days <= 31.0){
            return 0.01;
        }
        else return 0.0;
    }

    private String getPublicationDate (String docno){
        String date = null;
        if (docno.contains("AP")){
            date = StringUtils.substringBetween(docno, "AP", "-");
        }
        if (docno.contains("WSJ")){
            date = StringUtils.substringBetween(docno,"WSJ", "-");
        }
        return date;
    }

    private static void writeToFile(String text){
        PrintWriter out = null;
        try{
            out = new PrintWriter(new BufferedWriter(new FileWriter(
                    "/Users/martehallan/Documents/queries/resultsFrom045/resultLessThan055q112",true)));
            out.println(text);
            System.out.println("printing");
        }catch (IOException e){
            System.out.println(e);
        }finally {
            if (out!= null){
                out.close();
            }
        }
    }
}
