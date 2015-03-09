package Novelty;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        Hashtable<String, String> documents;
        Hashtable<String, List<String>> preprocessedDocuments;
        boolean isJaccard = false;


        File folder = new File("/TREC_Volume_1_Decompressed/AP/");
        File[] listOfFiles = folder.listFiles();

        Reader reader = new Reader();
        documents = reader.getFiles(listOfFiles);

        Preprocessing preprocessing = new Preprocessing();
        preprocessedDocuments = preprocessing.preprocess(documents, isJaccard);

        JaccardCoefficient jaccardCoefficient = new JaccardCoefficient();
        //jaccardCoefficient.getJaccardFromHashtable(preprocessedDocuments);

        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        cosineSimilarity.getCosineFromHashtable(preprocessedDocuments);
	}
}
