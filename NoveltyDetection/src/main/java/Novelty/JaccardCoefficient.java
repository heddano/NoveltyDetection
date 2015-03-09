package Novelty;

import java.util.*;

public class JaccardCoefficient {

    public JaccardCoefficient() { }

    public Hashtable<String, Double> getJaccardFromHashtable(Hashtable documents){
        Hashtable<String, Double> jaccard = new Hashtable<String, Double>();
        List<String> keyList = getKeysAsList(documents);

        System.out.println(keyList.size());

        int k=0;

        for (int i = 0; i < keyList.size(); i++) {
            for (int j = i+1; j < keyList.size(); j++) {
                List<String> content1 = (List<String>) documents.get(keyList.get(i));
                List<String> content2 = (List<String>) documents.get(keyList.get(j));


                double jaccardCoefficient = getJaccardCoefficient(content1, content2);
                if(jaccardCoefficient<0.002){
                    jaccard.put(keyList.get(i) + "+" + keyList.get(j), jaccardCoefficient);
                    System.out.println(k);
                    k++;
                }
            }
        }

        System.out.println(jaccard.size());
        System.out.println(jaccard);
        return jaccard;
    }

    private List<String> getKeysAsList(Hashtable keys){
        List<String> listOfKeys = new ArrayList<String>();

        Enumeration i = keys.keys();

        while (i.hasMoreElements()){
            listOfKeys.add((String) i.nextElement());
        }

        return listOfKeys;
    }

    private double  getJaccardCoefficient(List<String> content1, List<String> content2){
        List<String> intersection = getIntersection(content1, content2);
        List<String> union = getUnion(content1, content2);

        return (double) intersection.size() / (double) union.size();
    }

    private List<String> getIntersection(List<String> list1, List<String> list2) {
        List<String> list = new ArrayList<String>();

        for (String t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
    return list;
    }

    private List<String> getUnion(List<String> list1, List<String> list2) {
        Set<String> set = new HashSet<String>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<String>(set);
    }

}
