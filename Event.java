package edu.ewubd.cse489120251;
public class Event {
    String eventId = "";
    String title = "";
    String venue = "";
    long datetime = 0;
    int numParticipants = 0;
    String description = "";

    public Event(String ID, String title, String venue, long datetime, int capacity, String des){
        this.eventId = ID;
        this.title = title;
        this.venue = venue;
        this.datetime = datetime;
        this.numParticipants = capacity;
        this.description = des;
    }
    public String toString(){
        return this.eventId+", "+this.title+", "+this.venue+", "+this.datetime+", "+this.numParticipants+", "+this.description;
    }
}
