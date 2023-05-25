package org.telegram;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.google.maps.*;
import com.google.maps.model.DirectionsResult;

public class MyBot extends TelegramLongPollingBot{
    @Override
    public String getBotToken() {
        return Keys.BotToken;
    }
    @Override
    public String getBotUsername() {
        return "Traffy";
    }
    Boolean onsetDestination=false;
    Boolean onsetOrigin=false;
    SendMessage message = new SendMessage();
    String origin="";
    String destination="";
    @Override
    public void onUpdateReceived(Update update) {
        if(onsetDestination)
        {
            //messages following will be of these flow
            destination=update.getMessage().getText();
            message.setText("Destination Set as "+destination);
            sendreply(message);
            onsetDestination=false;
            setResponse(origin,destination);
        }
        if(onsetOrigin)
        {
            //set the origin
                //try to prompt user for geo locatio
            // reset reply keyboard
            origin=update.getMessage().getText();
            message.setText("Origin Set as "+origin);
            sendreply(message);
            onsetOrigin=false;
            SetDestination();
        }
        //Customize greetings 
        if (update.hasMessage() && update.getMessage().hasText()) {
      // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            //help
            if(update.getMessage().getText().toString().equalsIgnoreCase("Get traffic information"))
            {
              SetOrigin();
               
            }
            else if(update.getMessage().getText().toString().equalsIgnoreCase("help"))
            {
                message.setText("Welcome to traffy used for identifying the traffic time for various destinations when specifying location please be as specific as possible eg Donholm,Nairobi for the best results"); 
                sendreply(message); 
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
                sendreply(message);
            
            }
           
            setButtons(message);
            //sendreply(message);

        }
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
        // Add the buttons to the third keyboard row
        keyboardThirdRow.add(new KeyboardButton("Get traffic information"));
        KeyboardRow keyboardForthRow = new KeyboardRow();
        // Add the buttons to the third keyboard row
        // KeyboardButton location=new KeyboardButton("Use my Location");
        // location.getRequestLocation();
        // keyboardForthRow.add(location);
        // Add all of the keyboard rows to the list
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardForthRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        // and assign this list to our keyboard
    //read telegram bot documentation
    }

    public void SetOrigin()
    {
      message.setText("Enter your Origin");
      sendreply(message);
      onsetOrigin=true;
    }
    public void SetDestination()
    {
      message.setText("Enter your Destination");
      sendreply(message);
      onsetDestination=true;
    }
    public void setResponse(String origin,String destination)
    {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(Keys.apiKey)
                .build();

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
          //getting delay
           long delay=result.routes[0].legs[0].durationInTraffic.inSeconds-result.routes[0].legs[0].duration.inSeconds;
           //add time based on delay calculation
           delay=Math.abs(delay);
           Calendar now = Calendar.getInstance();
           now.add(Calendar.SECOND, Integer.valueOf(String.valueOf(result.routes[0].legs[0].durationInTraffic.inSeconds)));
            message.setText("Estimated travel time: " + result.routes[0].legs[0].duration.humanReadable+"\nEstimated distance in metres: " + result.routes[0].legs[0].distance.inMeters + " m"+"\nEstimated distance in Kilometres: " + result.routes[0].legs[0].distance +"\nTotal time to travel including traffic conditions : " + result.routes[0].legs[0].durationInTraffic.humanReadable+"\nEstimated traffic delay: "+ String.valueOf(delay>60?TimeUnit.SECONDS.toMinutes(delay)+" Minute(s)":delay+" seconds")+"\nEstimated Arrival time: "+ new SimpleDateFormat("HH:mm aa").format(now.getTime()));
        
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
        sendreply(message);
    }
    public void sendreply(SendMessage mes)
    {
        try {
            if(mes!=null)
            {
                execute(mes);
                 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
