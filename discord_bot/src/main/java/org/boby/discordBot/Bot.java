package org.boby.discordBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class Bot extends ListenerAdapter {

	static JDA api;
	static long userId;

	static String prefix = "?";
	
	static ByteBuffer music;
	
	static double time = 0;
	
	static int sampleRate = 48000;
	static double samplePeriod = 1.0/sampleRate;
	
	static double duration = 0.02;
	static int durationInSamples = (int) Math.ceil(duration * sampleRate);
	
	static int frequency = 440;
	
	static int tone_counter = 0;
	
	static int letterToId(char a) {
		switch(a) {
		case 'a': return 0;
		case 'b': return 2;
		case 'c': return 3;
		case 'd': return 5;
		case 'e': return 7;
		case 'f': return 8;
		case 'g': return 10;
		}
		
		return -1;
	}
	
	public static void main(String[] args) throws LoginException {

		BufferedReader reader;
		String firstLine = null;
		try {
			reader = new BufferedReader(new FileReader(new File("./res/bot_token.txt")));
			firstLine = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		
		api = JDABuilder.createDefault(firstLine)
				.addEventListeners(new Bot()).setActivity(Activity.playing("Minecraft 2")).build();

		User user = api.getUsers().get(0);
		userId = user.getIdLong();
		
		
		
		music = ByteBuffer.allocate(durationInSamples * 4);
		
		int array[] = {
				letterToId('d'),
				letterToId('d'),
				letterToId('d') + 12,
				-100,
				letterToId('a'),
				-100,
				-100,
				letterToId('g') + 1,
				-100,
				letterToId('g'),
				-100,
				letterToId('f'),
				-100,
				letterToId('d'),
				letterToId('f'),
				letterToId('g'),
		};
		
		new java.util.Timer().scheduleAtFixedRate( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		                frequency = (int)(440.0 * Math.pow(2, array[tone_counter]/12.0));
		                if(array[tone_counter] == -100) frequency = 0;
		                tone_counter++;
		                tone_counter %= array.length;
		            }
		        },
		        0,
		        125
		);
		
		new java.util.Timer().scheduleAtFixedRate( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		                frequency = 0;
		            }
		        },
		        100,
		        125
		);
		
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message msg = event.getMessage();
		
		if(!msg.isFromGuild()) return;
		
		User author = msg.getAuthor();
		MessageChannel channel = event.getChannel();
		
		String text = msg.getContentRaw();
		
		if(author.isBot()) return;
		
		
		Guild guild = msg.getGuild();
		System.out.println("recieved message from " + author.getName() + " in channel: " + channel.getName() + " in server: " + guild.getName() + ": " + msg.getContentDisplay());
	
		
		if (text.equals(prefix + "ping")) {
			long time = System.currentTimeMillis();
			channel.sendMessage("Pong!") /* => RestAction<Message> */
					.queue(response /* => Message */ -> {
						response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
					});
		}
		else if(text.equals(prefix + "asdf")) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(text.equals(prefix + "join")) {
			
			VoiceChannel v_channel = event.getMember().getVoiceState().getChannel();
			if(v_channel == null) {
				channel.sendMessage("youre not connected to a voice channel you dumbass!").queue();
				return;
			}
			
			if(!guild.getSelfMember().hasPermission(v_channel, Permission.VOICE_CONNECT)) {
				
				
				channel.sendMessage("I don't have permission to join a voice channel!").queue();
				return;
			}
			
			AudioManager manager = guild.getAudioManager();
			if(manager.isAttemptingToConnect()) {
				channel.sendMessage("stop, im trying!!!").queue();
				return;
			}
			
			AudioSendHandler sendHandler = new AudioSendHandler() {
				
				@Override
				public ByteBuffer provide20MsAudio() {
					//double time = 0;
					for(int i = 0; i < durationInSamples; i ++) {
						
						if(frequency != 0) {
						float val = (float) (Math.sin(2 * Math.PI * frequency * time) * 0.1);
						
						short bits = (short)(32768 * val * 0.5);
						
						music.put((byte)(bits >> 8));
						music.put((byte)(bits & 0x0F));
						music.put((byte)(bits >> 8));
						music.put((byte)(bits & 0x0F));
						}
						else {
							music.put((byte)0);
							music.put((byte)0);
							music.put((byte)0);
							music.put((byte)0);
						}
						time += samplePeriod;
					}
					
					music.flip();
					return music;
				}
				
				@Override
				public boolean canProvide() {
					return true;
				}
				
				@Override
				public boolean isOpus() {
					return false;
				}
			};
			
			
			manager.setSendingHandler(sendHandler);
			manager.openAudioConnection(v_channel);
			channel.sendMessage("i connected!!").queue();
			
			return;
		}
		else if(text.equals(prefix + "leave")) {
			VoiceChannel vc =  event.getMember().getVoiceState().getChannel();
			if(vc == null) {
				channel.sendMessage("im not in a voice channel you buffoon!").queue();;
				return;
			}
			guild.getAudioManager().closeAudioConnection();
			channel.sendMessage("disconnected from voice chat!").queue();
			return;
		}
		else if(text.equals(prefix + "permissions")) {
			VoiceChannel v_channel = event.getMember().getVoiceState().getChannel();
			if(v_channel == null) {
				channel.sendMessage("youre not connected to a voice channel you dumbass!").queue();
				return;
			}
			
			EnumSet<Permission> permissions = guild.getSelfMember().getPermissions(v_channel);
			channel.sendMessage("permissions:").queue();
			for(Permission p: permissions) {
				channel.sendMessage(p.getName()).queue();
			}
			return;
		}
		else if(text.equals(prefix + "stop")) {
			
			MessageBuilder mb = new MessageBuilder("Adios, Amigos!! <a:pop:782923200605847562>");
			Message mess = mb.build();
			
			channel.sendMessage(mess).queue();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
	}
	
}
