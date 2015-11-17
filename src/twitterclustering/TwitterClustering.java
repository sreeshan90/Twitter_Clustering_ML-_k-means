package twitterclustering;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author sreesha.n
 */
public class TwitterClustering {

    public static float jaccardDistance(Set<String> a, Set<String> b) {

        if (a.size() == 0 || b.size() == 0) {
            return 0;
        }

        Set<String> unionXY = new HashSet<String>(a);
        unionXY.addAll(b);

        Set<String> intersectionXY = new HashSet<String>(a);
        intersectionXY.retainAll(b);
        float retValue = (float) intersectionXY.size() / (float) unionXY.size();
        return retValue;

    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
            sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Comparator.comparing(e -> e.getValue()))
                .forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here

        Scanner s = new Scanner(new File("C:\\Users\\sreesha.n\\Documents\\NetBeansProjects\\TwitterClustering\\InitialSeeds.txt")).useDelimiter(",");
        JSONParser parser = new JSONParser();
        Set<Cluster> clusterSet = new HashSet<Cluster>();
        HashMap<String, Tweet> tweets = new HashMap();

        //init
        try {

            Object obj = parser.parse(new FileReader("C:\\Users\\sreesha.n\\Documents\\NetBeansProjects\\TwitterClustering\\Tweets.json"));

            JSONArray jsonArray = (JSONArray) obj;

            for (int i = 0; i < jsonArray.size(); i++) {

                Tweet twt = new Tweet();
                JSONObject jObj = (JSONObject) jsonArray.get(i);
                String text = jObj.get("text").toString();

                long sum = 0;
                for (int y = 0; y < text.toCharArray().length; y++) {

                    sum += (int) text.toCharArray()[y];
                }

                // System.out.println(sum);
                String[] token = text.split(" ");
                String tID = jObj.get("id").toString();

                Set<String> mySet = new HashSet<String>(Arrays.asList(token));
                twt.setAttributeValue(sum);
                twt.setText(mySet);
                twt.setTweetID(tID);
                tweets.put(tID, twt);

            }

            //preparing initial clusters
            int i = 0;
            while (s.hasNext()) {
                String id = s.next();//id
                Tweet t = tweets.get(id.trim());
                clusterSet.add(new Cluster(i + 1, t, new LinkedList()));
                i++;
            }

            Iterator it = tweets.entrySet().iterator();

            for (int l = 0; l < 25; l++) { //limit to 25 iterations

                while (it.hasNext()) {
                    Map.Entry me = (Map.Entry) it.next();

                    //calculate distance to each centroid
                    Tweet p = (Tweet) me.getValue();
                    HashMap<Cluster, Float> distMap = new HashMap();

                    for (Cluster clust : clusterSet) {

                        distMap.put(clust, jaccardDistance(p.getText(), clust.getCentroid().getText()));
                    }

                    HashMap<Cluster, Float> sorted = (HashMap<Cluster, Float>) sortByValue(distMap);

                    sorted.keySet().iterator().next().getMembers().add(p);

                }

                //calculate new centroid and update Clusterset
                for (Cluster clust : clusterSet) {

                    TreeMap<String, Long> tDistMap = new TreeMap();

                    Tweet newCentroid = null;
                    Long avgSumDist = new Long(0);
                    for (int j = 0; j < clust.getMembers().size(); j++) {

                        avgSumDist += clust.getMembers().get(j).getAttributeValue();
                        tDistMap.put(clust.getMembers().get(j).getTweetID(), clust.getMembers().get(j).getAttributeValue());
                    }
                    if (clust.getMembers().size() != 0) {
                        avgSumDist /= (clust.getMembers().size());
                    }

                    ArrayList<Long> listValues = new ArrayList<Long>(tDistMap.values());

                    if (tDistMap.containsValue(findClosestNumber(listValues, avgSumDist))) {
                        //found closest
                        newCentroid = tweets.get(getKeyByValue(tDistMap, findClosestNumber(listValues, avgSumDist)));
                        clust.setCentroid(newCentroid);
                    }

                }

            }
            // create an iterator
            Iterator iterator = clusterSet.iterator();

            // check values
            while (iterator.hasNext()) {

                Cluster c = (Cluster) iterator.next();

                System.out.print(c.getId() + "\t");
                
                for(Tweet t : c.getMembers()){
                  //  System.out.print(t.getTweetID()+ ",");
                      System.out.println("\t"+t.getTweetID()+ "\t"+t.getText().toString());
                }
                         System.out.println("");         
            }
             System.out.println("");

            System.out.println("SSE " + sumSquaredErrror(clusterSet));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double sumSquaredErrror(Set<Cluster> clusterSet) {

        double sse = 0;

        for (Cluster clust : clusterSet) {

            for (Tweet p : clust.getMembers()) {

                double dist = jaccardDistance(p.getText(), clust.getCentroid().getText());
                sse += dist * dist;
            }

        }
        return sse;
    }

    public static Long findClosestNumber(List list, Long num) {
        if (list.size() > 0) { // Check list does not empty
            Long smaller
                    = (Long) Collections.min(list); // get min number from the list
            Long larger
                    = (Long) Collections.max(list); // get max number from the list

            for (int i = 0; i < list.size(); i++) { //Traverse list
                if (num == (Long) list.get(i)) //if find the passed number in the list
                {
                    return num;                     //than return num
                }
                if (num > (Long) list.get(i) && smaller < (Long) list.get(i)) // find nearest smaller
                {
                    smaller = (Long) list.get(i);
                }
                if (num < (Long) list.get(i) && larger > (Long) list.get(i)) // find nearest larger
                {
                    larger = (Long) list.get(i);
                }
            }
            return (num - smaller < larger - num ? smaller : larger); // return closest number
        }
        return new Long(0);
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
