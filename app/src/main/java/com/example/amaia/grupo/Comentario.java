package com.example.amaia.grupo;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by zihara on 8/05/18.
 */

class Comentario {
    private int userID,id;
    private String username;
    private int sitioID;
    private String texto;

    public Comentario(String comentario){
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(comentario);
            id = Integer.valueOf((String)json.get("id"));
            userID = Integer.valueOf((String)json.get("userid"));
            sitioID = Integer.valueOf((String)json.get("sitioid"));
            username = (String)json.get("username");
            texto = (String)json.get("texto");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getUserID(){return userID;}

    public int getSitioID(){return sitioID;}

    public String getUsername(){return username;}

    public String getTexto(){return texto;}

    public int getId(){return id;}

}
