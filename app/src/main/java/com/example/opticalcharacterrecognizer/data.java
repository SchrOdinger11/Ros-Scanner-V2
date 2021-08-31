package com.example.opticalcharacterrecognizer;

public class data {
    String query;
    String email;

    String id;

    public data(String id, String datafieldEmail, String datafieldQuery) {
        this.email=email;
        this.query=query;
        this.id=id;
    }


    public String getEmail(){
        return email;
    }
    public String getQuery(){
        return query;
    }
    public String getId(){
        return id;
    }


}
