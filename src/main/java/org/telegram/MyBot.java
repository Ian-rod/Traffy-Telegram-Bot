package org.telegram;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.google.maps.*;
import com.google.maps.model.DirectionsResult;

public class MyBot extends TelegramLongPollingBot{
    @Override
    public String getBotToken() {
        return "6014395396:AAEtyiY30jDwR-sqaIG80-0W5eXaBqhdDhs";
    }
    @Override
    public String getBotUsername() {
        return "Veriphone";
    }
    SendMessage message = new SendMessage();
    @Override
    public void onUpdateReceived(Update update) {
        //Customize greetings 
        if (update.hasMessage() && update.getMessage().hasText()) {
      // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            //help
            if(update.getMessage().getText().toString().equalsIgnoreCase("Get traffic information"))
            {
               //obtain traffic data details
               String apiKey = "AIzaSyAqNR-TkPPde7R2jp6vlIOGKasN8CnRT5o";
               GeoApiContext context = new GeoApiContext.Builder()
                       .apiKey(apiKey)
                       .build();
       
               // Define the origin and destination
               String origin = " Mombasa, Kenya";
               String destination = "Kisumu, Kenya";
       
               // Create a DirectionsApiRequest object
               DirectionsApiRequest request = DirectionsApi.getDirections(context, origin, destination);
                request.departureTimeNow();
               // Set the mode of transportation and traffic model
               request.mode(com.google.maps.model.TravelMode.DRIVING);
               request.trafficModel(com.google.maps.model.TrafficModel.BEST_GUESS);
       
               try {
                   // Send the request and wait for the result
                   DirectionsResult result = request.await();
       
                   // Extract relevant traffic data from the result
                   System.out.println("Estimated travel time: " + result.routes[0].legs[0].duration.inSeconds + " seconds");
                   System.out.println("Estimated distance: " + result.routes[0].legs[0].distance.inMeters/1000 + " km");
                   System.out.println("Traffic conditions: " + result.routes[0].legs[0].durationInTraffic.humanReadable);
               } catch (Exception e) {
                   e.printStackTrace();
               }
               //api key origin and destination required 
            }
            else if(update.getMessage().getText().toString().equalsIgnoreCase("help"))
            {
                message.setText("Welcome to traffy used for identifying the traffic time for various destinations");  
            }
            else if (update.getMessage().getText().toString().equalsIgnoreCase("hi")){
                //When Hi is typed
                ArrayList<String> responses=new ArrayList<String>(20);
                responses.add("Hello young padowan");
                responses.add("Howdy!");
                responses.add("Yo!");
                responses.add("Sate sate sate");
                responses.add("It’s a pleasure to meet you");
                responses.add("Beautiful weather we have today");
                responses.add("Alright, mate?");
                responses.add(" Hiya!");   
                responses.add("What’s the craic?");
                responses.add("Ahoy!");
                responses.add("Hello stranger!");
                responses.add("What’s crackin’?");
                responses.add("What’s up buttercup?");
                responses.add("Bonjour");
                responses.add("idhi nade");
                responses.add("How do you do?");
                responses.add("Whazzup?");
                responses.add("Sup?");
                responses.add("G’day mate!");
                responses.add("Hiya!");
                Random random=new Random();
                message.setText(responses.get(random.nextInt(19)));
            
            }
           
            setButtons(message);
            try {
                if(message!=null)
                {
                    execute(message);
                     
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //handle number verification
        System.out.println(update.getMessage().getText());
        
    }
    
    public synchronized void setButtons(SendMessage sendMessage) {
        // Create a keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Create a list of keyboard rows
        List<KeyboardRow> keyboard = new ArrayList<>();

        // First keyboard row
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Add buttons to the first keyboard row
        keyboardFirstRow.add(new KeyboardButton("Hi"));

        // Second keyboard row
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow.add(new KeyboardButton("Help"));
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardThirdRow.add(new KeyboardButton("Get traffic information"));

        // Add all of the keyboard rows to the list
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        // and assign this list to our keyboard
    //read telegram bot documentation
    }
}
