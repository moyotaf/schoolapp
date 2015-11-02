/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.innate.cresterp.school;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.innate.cresterp.homework.Homework;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author user
 */
public class StudentLogin {
    private final List<Homework> studNames = new ArrayList<Homework>();
         public List<Homework> findLoginDetails() {
        // convert the object to a JSON document
        studNames.clear();
        NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.start();
        networkManager.addErrorListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                NetworkEvent n = (NetworkEvent) evt;
                n.getError().printStackTrace();
                System.out.println(n.getError());
            }
        });

        ConnectionRequest request;
        request = new ConnectionRequest() {

            int chr;
            StringBuffer sb = new StringBuffer();
            String response = "";

            @Override
            protected void readResponse(InputStream input) throws IOException {

                JSONParser parser = new JSONParser();
                Hashtable hm = parser.parse(new InputStreamReader(input));

                Vector vector = new Vector();
                vector = (Vector) hm.get("homework");

                if (vector.size() > 0) {
                    for (int i = 0; i < vector.size(); i++) {

                        Hashtable h = (Hashtable) vector.get(i);
                        Homework homework = new Homework();
                        homework.setUsername(h.get("username").toString());
                        homework.setPassword(h.get("password").toString());
                        studNames.add(homework);

                    }
                } else {
                    Homework homework = new Homework();
                    homework.setUsername("Sorry...");
                    homework.setDescription("No messages today");
                    studNames.add(homework);
                }
            }

            @Override
            protected void handleException(Exception err) {
                System.err.println(err);
                 Homework homework = new Homework();
                    homework.setUsername("Ooops...");
                    homework.setDescription("Please check your internet connection ");
                    studNames.add(homework);

            }

        };

        String URL = "http://localhost/school/verify.php";
        request.setUrl(URL);
        request.setPost(true);
         
        networkManager.addToQueueAndWait(request);

        return studNames;
    }

       
    
    public boolean getResponse() throws IOException {
        ConnectionRequest req = new ConnectionRequest();
        boolean result = false;
        if (req.getResponseCode() == 200) {
            if (new String(req.getResponseData(), "UTF-8").equals("Login Successful")){
                result = true;
            }
        }
        return result;
    }
}
    
