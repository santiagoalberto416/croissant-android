package com.croissant.croissant;


/**
 * Created by brandon on 08/03/2016.
 */
public class Conference {
    private String name, speaker, datetime, place;
    private int id;

    //methods
    public String getName() {return this.name;}
    public String getSpeaker() {return this.speaker;}
    public String getDatetime() {return this.datetime;}
    public String getPlace() {return this.place;}
    public int getId() {return this.id;}


    //constructor
    public Conference()
    {
        this.id = 0;
        this.name = "";
        this.speaker = "";
        this.datetime = "";
        this.place = "";
    }

    public Conference(int id, String name, String speaker, String datetime, String place)
    {
        this.id = id;
        this.name = name;
        this.speaker = speaker;
        this.datetime = datetime;
        this.place = place;
    }
}
