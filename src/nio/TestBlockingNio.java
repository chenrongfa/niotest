package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class TestBlockingNio {

	
	@Test
	public void socket() throws IOException{
		InetSocketAddress address=new InetSocketAddress("127.0.0.1", 8787);
		SocketChannel schannel=SocketChannel.open(address);
		FileChannel file=FileChannel.open(Paths.get("1.exe"),
				StandardOpenOption.READ,StandardOpenOption.WRITE);
		ByteBuffer bf=ByteBuffer.allocate(1024);
		int len=0;
		while((len=file.read(bf))!=-1){
		bf.flip();
		schannel.write(bf);
		bf.clear();
			
		}
		//告诉服务器我已经写完了,可以发送数据了
		schannel.shutdownOutput();
		
		
		len=schannel.read(bf);
			bf.flip();
			System.out.println(new String(bf.array(),0,len));
		
		schannel.close();
		file.close();
		
	}
	@Test
	public void serverSocket() throws IOException{
		ServerSocketChannel sschannel=ServerSocketChannel.open();
		sschannel.bind(new InetSocketAddress(8787));
	

		SocketChannel accept = sschannel.accept();
		FileChannel file=FileChannel.open(Paths.get("2.exe"), 
				StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		ByteBuffer bf=ByteBuffer.allocate(1024);
		int len;
		while((len=accept.read(bf))!=-1){
			bf.flip();
			file.write(bf);
			bf.clear();	
		}
		accept.shutdownInput();
		bf.clear();
		bf.put("街道数据".getBytes());
		bf.flip();
		accept.write(bf);
		file.close();
		sschannel.close();
		
		
	}
}
