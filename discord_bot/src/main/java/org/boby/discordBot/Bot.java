package org.boby.discordBot;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
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
	
	public static void main(String[] args) throws LoginException {

		api = JDABuilder.createDefault("Njg4MTIzMzYzODg0OTI1MTAz.Xmvu0g.J83EBmLxeFhJuQoNPplCK-GRl0A")
				.addEventListeners(new Bot()).setActivity(Activity.playing("Minecraft 2")).build();

		User user = api.getUsers().get(0);
		userId = user.getIdLong();
		System.out.println(user.getId());
		
		music = ByteBuffer.allocate(960);
		for(int i = 0; i < 960; i++) {
			byte b = (byte)( 255 * Math.sin(i * Math.PI / 48000 * 440) - 128);
			music.put(b);
		}
		music.flip();
		
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
					return music;
				}
				
				@Override
				public boolean canProvide() {
					return false;
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
			
			MessageBuilder mb = new MessageBuilder("Adios, Amigos!! <:snek:742735318805119127>");
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
