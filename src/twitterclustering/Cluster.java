/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;

/**
 *
 * @author sreesha.n
 */
public class Cluster {

    private int id;
    private Tweet centroid;
    private List<Tweet> members;

    public Cluster(int id, Tweet centroid, List<Tweet> members) {
        this.id = id;
        this.centroid = centroid;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void addPoint(Tweet tweet) {
        members.add(tweet);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tweet getCentroid() {
        return centroid;
    }

    public void setCentroid(Tweet centroid) {
        this.centroid = centroid;
    }

    public List<Tweet> getMembers() {
        return members;
    }

    public void setMembers(List<Tweet> members) {
        this.members = members;
    }

    @Override
    public String toString() {
       // return "Cluster{" + "centroid=" + centroid + ", members=" + members + '}';

        String ret = "[Cluster: " + id + "]" + "[Centroid: " + centroid + "]" + "[Tweetids: \n";

        for (Tweet t : members) {
            ret = ret + t.getTweetID();
        }
        ret = ret + "]";

        return ret;
    }

}
