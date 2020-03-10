package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.*;
import javax.xml.parsers.*;


import java.net.URL;
import java.util.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class Ranking1 extends AppCompatActivity {
    private Button button;
    private TextView text1;
    private DatabaseHelper db1;
    private Vector<DiningHallPair> data;

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
            current_hour = 1200;
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

                    ranking.get(dining_hall_index).dining_hall_points = dining_hall_points;
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
        data = new Vector<>();
        try {
            data = d.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        text1 = (TextView) findViewById(R.id.textView3);
        text1.setMovementMethod(new ScrollingMovementMethod());
        text1.setText("");
        for (int i = 0; i < data.size(); i++) {
            Spannable word = new SpannableString(String.valueOf(i + 1) + ". " + data.get(i).dining_hall_name);
            word.setSpan(new ForegroundColorSpan(Color.parseColor("#f2c649")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            word.setSpan(new RelativeSizeSpan(6f), 0,word.length(), 0);
            text1.append(word);
            text1.append("\n");
            Spannable word1 = new SpannableString("  Total Points: ");
            word1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, word1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            word1.setSpan(new RelativeSizeSpan(2.5f), 0,word1.length(), 0);

            Spannable word6 = new SpannableString(String.valueOf(data.get(i).dining_hall_points));
            word6.setSpan(new ForegroundColorSpan(Color.WHITE), 0, word6.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            word6.setSpan(new RelativeSizeSpan(3.5f), 0,word6.length(), 0);
            text1.append(word1);
            text1.append(word6);
            text1.append("\n");
            Spannable word5 = new SpannableString("  Your Favourites:");
            word5.setSpan(new ForegroundColorSpan(Color.parseColor("#f2c649")), 0, word5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            word5.setSpan(new RelativeSizeSpan(4f), 0,word5.length(), 0);
            text1.append(word5);
            text1.append("\n");
            for (int j = 0; j < 3 && j < data.get(i).top_dishes.size(); j++) {
                Spannable word3 = new SpannableString(data.get(i).top_dishes.get(j).dish_name);
                word3.setSpan(new ForegroundColorSpan(Color.WHITE), 0, word3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                word3.setSpan(new RelativeSizeSpan(2.5f), 0,word3.length(), 0);
                text1.append(word3);
                Spannable word4 = new SpannableString("\t\t" + data.get(i).top_dishes.get(j).dish_rating);
                word4.setSpan(new ForegroundColorSpan(Color.parseColor("#f2c649")), 0, word4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                word4.setSpan(new RelativeSizeSpan(3f), 0,word4.length(), 0);
                text1.append(word4);
                text1.append("\n");
            }
            text1.append("\n-------------------------------------------------------------------------------------\n");
        }
    }
}
