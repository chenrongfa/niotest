package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

public class TestNonBlockingNio {
	
	

	@Test
	public void socket() throws IOException{
		SocketChannel schannel=SocketChannel.open(new InetSocketAddress("127.0.0.1", 8787));
		schannel.configureBlocking(false);
		ByteBuffer bf=ByteBuffer.allocate(1024);
		Scanner sc=new Scanner(System.in);
		while(sc.hasNext()){
			
		String line=sc.nextLine();
		bf.put((line+"你好呀").getBytes());
		bf.flip();
		schannel.write(bf);
		bf.clear();
		}
		schannel.close();
		
		
	}
	@Test
	public void serverSocket() throws IOException{
	
		ServerSocketChannel sschannel=ServerSocketChannel.open();
		sschannel.configureBlocking(false);
		sschannel.bind(new InetSocketAddress(8787));
		Selector open = Selector.open();
		ByteBuffer bf=ByteBuffer.allocate(1024);
		//监听
		sschannel.register(open, SelectionKey.OP_ACCEPT);
		
		//便利
		while(open.select()>0){
			
			Iterator<SelectionKey> iterator = open.selectedKeys().iterator();
		  
			while(iterator.hasNext()){
				SelectionKey next = iterator.next();
				if(next.isAcceptable()){
					SocketChannel accept	= sschannel.accept();
					accept.configureBlocking(false);
					accept.register(open,SelectionKey.OP_READ);
					System.out.println("1");
				}
				else if(next.isReadable()){
					
					SocketChannel channel = (SocketChannel) next.channel();
					int len=0;
					while((len=channel.read(bf))>0){
					
					bf.flip();
					System.out.println(new String(bf.array(),0,bf.limit()));
	
					bf.clear();
					}
					
					
				}
				
				
				
				
				System.out.println(open.select());
			}
			
			iterator.remove();
		
			
		}
		
		
	}

}
