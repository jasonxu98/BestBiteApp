package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.*;
import javax.xml.parsers.*;


import java.net.URL;
import java.util.*;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Ranking1 extends AppCompatActivity {
    private Button button;
    private TextView text1;
    private DatabaseHelper db1;

    class DiningHallPair implements Comparable<DiningHallPair> {
        String dining_hall_name;
        int dining_hall_points;
        Vector<DishPair> top_dishes = new Vector<>();

        public int compareTo(DiningHallPair another_pair) {
            return another_pair.dining_hall_points - this.dining_hall_points;
        }

        public DiningHallPair(String a, int b) {
            dining_hall_name = a;
            dining_hall_points = b;
        }
    }

    class DishPair implements Comparable<DishPair> {
        String dish_name;
        int dish_rating;

        public int compareTo(DishPair AnotherDish) {
            return AnotherDish.dish_rating - this.dish_rating;
        }

        public DishPair(String a, int b) {
            dish_name = a;
            dish_rating = b;
        }

    }

    class Downloader extends AsyncTask<String, Void, Vector<DiningHallPair> > {
        public Vector<DiningHallPair> doInBackground(String... urls) {
            // codes under review
            SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
            int current_hour = Integer.parseInt(sdf.format(new Date()));
            System.out.println("Current hour is: " + current_hour);

            Vector<DiningHallPair> ranking = new Vector<>();

            try{
                String url = "http://umtri.org/BestBiteServer/todaymenu/Menu.xml";
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new URL(url).openStream());
                doc.getDocumentElement().normalize();
                System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                NodeList dining_halls = doc.getElementsByTagName("dininghall");
                System.out.println("----------------------------");
                for (int dining_hall_index = 0; dining_hall_index < dining_halls.getLength(); dining_hall_index++) {
                    Node dining_hall = dining_halls.item(dining_hall_index);
                    Element dining_hall_element = (Element) dining_hall;
                    String dining_hall_name = dining_hall_element.getElementsByTagName("name").item(0).getTextContent();
                    System.out.println("Name: " + dining_hall_name);
                    NodeList meals = dining_hall_element.getElementsByTagName("meal");
                    int dining_hall_points = 0;
                    ranking.add(new DiningHallPair(dining_hall_name, dining_hall_points));


                    for (int meal_index = 0; meal_index < meals.getLength(); meal_index++) {
                        Node meal = meals.item(meal_index);
                        Element meal_element = (Element) meal;
                        int meal_start = Integer.parseInt(meal_element.getElementsByTagName("start").item(0).getTextContent());
                        int meal_end = Integer.parseInt(meal_element.getElementsByTagName("end").item(0).getTextContent());
                        if (current_hour >= meal_start && current_hour < meal_end) {
                            System.out.println("    Meal Duration: " + meal_start + " " + meal_end);
                            NodeList dishes = meal_element.getElementsByTagName("dish");
                            for (int dish_index = 0; dish_index < dishes.getLength(); dish_index++) {
                                Node dish = dishes.item(dish_index);
                                Element dish_element = (Element) dish;
                                String dish_name = dish_element.getTextContent();
                                int dish_point;

                                try {
                                    dish_point = db1.checkRating(dish_name);
                                } catch (Exception e) {
                                    System.out.println("Gaycob Database Bad");
                                    dish_point = 0;
                                }

                                dining_hall_points += dish_point;
                                ranking.get(dining_hall_index).top_dishes.add(new DishPair(dish_name, dish_point));

                                System.out.println("        " + dish_point);
                            }
                        }
                    }

                    ranking.get(1).dining_hall_points = dining_hall_points;
                }

                for (int i = 0; i < 7; i++) {
                    System.out.println(ranking.get(i).dining_hall_name + " " + ranking.get(i).dining_hall_points);
                }

                Collections.sort(ranking);

                System.out.println("----------------------------");

                // Ranking現在按照points從高到低排序
                for (int i = 0; i < 7; i++) {
                    Collections.sort(ranking.get(i).top_dishes);
                    System.out.println(ranking.get(i).dining_hall_name + " " + ranking.get(i).dining_hall_points);
                    for (int j = 0; j < ranking.get(i).top_dishes.size(); j++) {
                        System.out.println("    " + ranking.get(1).top_dishes.get(j).dish_name + " " + ranking.get(i).top_dishes.get(j).dish_rating);
                    }
                }


            } catch(Exception e) {
                System.out.println("Network Bad");
                System.out.println("or");
                System.out.println("Server Fucked");
                System.out.println(e);
            }

            return ranking;
        }
//
//        protected void onPostExecute (ArrayList<String> result) {
//            src.setText(result.get(0));
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking2);
        getSupportActionBar().hide();

        db1 = new DatabaseHelper(this);

        Downloader d = new Downloader();
        d.execute();



        final Button button8416 = (Button) findViewById(R.id.button8416);
        text1 = (TextView) findViewById(R.id.textView3);

        button8416.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                text1.setText("this is text ");
            }
        });

    }
}
