package org.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main 
{
    public static void main( String[] args )
    { 
		try {
            TelegramBotsApi botsApi=new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(new MyBot());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
}
