/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Objects;
import java.util.Set;

/**
 *
 * @author sreesha.n
 */
public class Tweet {
    
    private long attributeValue; 

    public long getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(long attributeValue) {
        this.attributeValue = attributeValue;
    }
    private Set text;
    private String tweetID;
    
    public Set getText() {
        return text;
    }

    public void setText(Set text) {
        this.text = text;
    }

    public String getTweetID() {
        return tweetID;
    }

    public void setTweetID(String tweetID) {
        this.tweetID = tweetID;
    }
   
}
