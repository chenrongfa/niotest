package nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.SortedMap;

import org.junit.Test;

public class Niotest {



	//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		ByteBuffer bf=ByteBuffer.allocate(1024);
//		System.out.println("创建 byteBuffer");
//		System.out.println("位置"+bf.position());
//		System.out.println("容量"+bf.capacity());
//		System.out.println("剩余"+bf.remaining());
//		System.out.println("可操作的容量"+bf.limit());
//		String name="abc";
//		System.out.println(name.getBytes().length);
//		bf.put(name.getBytes());
//		System.out.println("put byteBuffer");
//		System.out.println("位置"+bf.position());
//		System.out.println("容量"+bf.capacity());
//		System.out.println("剩余"+bf.remaining());
//		System.out.println("可操作的容量"+bf.limit());
//		bf.flip();
//		System.out.println("flip byteBuffer");
//		System.out.println("位置"+bf.position());
//		System.out.println("容量"+bf.capacity());
//		System.out.println("剩余"+bf.remaining());
//		System.out.println("可操作的容量"+bf.limit());
//		byte bt[]=new byte[bf.limit()];
//		bf.get(bt,0,1);
//		ByteBuffer wrap = bf.wrap(bt);
//		System.out.println("wrap byteBuffer");
//		System.out.println("位置"+wrap.position());
//		System.out.println("容量"+wrap.capacity());
//		System.out.println("剩余"+wrap.remaining());
//		System.out.println("可操作的容量"+wrap.limit());
//		bf.clear();
//		System.out.println("clear byteBuffer");
//		System.out.println("位置"+bf.position());
//		System.out.println("容量"+bf.capacity());
//		System.out.println("剩余"+bf.remaining());
//		System.out.println("可操作的容量"+bf.limit());
//		byte[] array = bf.array();
////		bf.limit(30);
//		System.out.println(new String(array)); // clear只能改变为位置并不能删除数据
//		System.out.println(bf.get(3));
//
//	}
	@Test
	public void testNio(){
		//非直接缓冲区
		FileInputStream fis=null;
		FileOutputStream fos=null;
		FileChannel channel=null;
		FileChannel channel2=null;
		try {
			fis = new FileInputStream("1.exe");
			fos=new FileOutputStream("2.exe");
			//获取通道
			 channel = fis.getChannel();
			channel2 = fos.getChannel();
			System.out.println(channel.size());
			ByteBuffer bf=ByteBuffer.allocate(1024);
			int len=0;
			long start = System.currentTimeMillis();
			while ((len=channel.read(bf))!=-1) {
				bf.flip();//改变位置进行存
				channel2.write(bf);
				bf.clear();//改变位置进行读
				
			}
			long end = System.currentTimeMillis();
			System.out.println("count time"+(end-start));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(fis!=null){
					fis.close();
					
				}
				if(channel!=null){
					channel.close();
				}
				if(fos!=null){
					fos.close();
					
				}
				if(channel2!=null){
					channel2.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	@Test
	public void testIo(){
		FileInputStream fis=null;
		FileOutputStream fos=null;
		FileChannel channel=null;
		FileChannel channel2=null;
		try {
			fis = new FileInputStream("1.exe");
			fos=new FileOutputStream("2.exe");
			//获取通道
			
//			System.out.println(channel.size());
			byte bf[]=new byte[104];
			int len=0;
			long start = System.currentTimeMillis();
			while ((len=fis.read(bf))!=-1) {
			
				fos.write(bf, 0, len);
				
			}
			long end = System.currentTimeMillis();
			System.out.println("count time"+(end-start));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(fis!=null){
					fis.close();
					
				}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	
	}
	@Test
	public void testNioDirect(){
		//直接缓冲区  (对数据不能直接控制,并且对内存资源开销大,不稳定,效果传输效率高)
		FileChannel channel=null;
		FileChannel channel2=null;
		MappedByteBuffer in=null;
		MappedByteBuffer out=null;
		try {
			//获取通道
			
			 channel = FileChannel.open( Paths.get("1.exe"), StandardOpenOption.READ);
			channel2 = FileChannel.open(Paths.get("2.exe"),StandardOpenOption.READ,StandardOpenOption
					.WRITE,StandardOpenOption.CREATE);
			System.out.println(channel.size());
			long start = System.currentTimeMillis();
			 in = channel.map(MapMode.READ_ONLY, 0, channel.size());
			out = channel2.map(MapMode.READ_WRITE, 0, channel.size());
//			in.put(out);
			byte by[]=new byte[in.limit()];
			in.get(by);
			out.put(by);
			
			long end = System.currentTimeMillis();
			System.out.println("count time"+(end-start));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
			
				
				if(channel!=null){
					channel.close();
				}
			
				if(channel2!=null){
					channel2.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	@Test
	public void testNioDirect1(){
		//直接缓冲区  (对数据不能直接控制,并且对内存资源开销大,不稳定,效果传输效率高)
		FileChannel channel=null;
		FileChannel channel2=null;
		try {
			//获取通道
			
			channel = FileChannel.open( Paths.get("1.exe"), StandardOpenOption.READ);
			channel2 = FileChannel.open(Paths.get("2.exe"),StandardOpenOption.READ,StandardOpenOption
					.WRITE,StandardOpenOption.CREATE);
			System.out.println(channel.size());
			long start = System.currentTimeMillis();
			ByteBuffer bf=ByteBuffer.allocateDirect(1024);
//			in.put(out);
			byte by[]=new byte[1024];
			int len=0;
			while((len=channel.read(bf))!=-1){
				bf.flip();
				channel2.write(bf);
				bf.clear();
		
			}	
			
			long end = System.currentTimeMillis();
			System.out.println("count time"+(end-start));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				
				if(channel!=null){
					channel.close();
				}
				
				if(channel2!=null){
					channel2.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	@Test
	public void testNio1(){
		//非直接缓冲区  (对数据不能直接控制,并且对内存资源开销大,不稳定,效果传输效率高)
		FileChannel channel=null;
		FileChannel channel2=null;
		try {
			//获取通道
			
			channel = FileChannel.open( Paths.get("1.exe"), StandardOpenOption.READ);
			channel2 = FileChannel.open(Paths.get("2.exe"),StandardOpenOption.READ,StandardOpenOption
					.WRITE,StandardOpenOption.CREATE);
			System.out.println(channel.size());
			long start = System.currentTimeMillis();
		//通道传输
//			in.put(out);
			channel.transferTo(0, channel.size(), channel2);
		
			long end = System.currentTimeMillis();
			System.out.println("count time"+(end-start));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				
				if(channel!=null){
					channel.close();
				}
				
				if(channel2!=null){
					channel2.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	@Test
	public void testNio2(){
		//非直接缓冲区  (对数据不能直接控制,并且对内存资源开销大,不稳定,效果传输效率高)
		FileChannel channel=null;
		FileChannel channel2=null;
		try {
			//获取通道
			RandomAccessFile inrf=new RandomAccessFile("1.exe", "r");
			RandomAccessFile outrf=new RandomAccessFile("2.exe", "rw");
			channel = inrf.getChannel();
			channel2 = outrf.getChannel();
			System.out.println(channel.size());
			long start = System.currentTimeMillis();
			
			ByteBuffer bt1=ByteBuffer.allocate(1024);
			ByteBuffer bt2=ByteBuffer.allocate(1024);
			ByteBuffer bt3[]=new ByteBuffer[]{bt1,bt2};
			long len=0;
			while((len=channel.read(bt3))!=-1){
				bt1.flip();
				bt2.flip();
				channel2.write(bt3);
				bt1.clear();
				bt2.clear();
			}
			
			long end = System.currentTimeMillis();
			System.out.println("count time"+(end-start));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				
				if(channel!=null){
					channel.close();
				}
				
				if(channel2!=null){
					channel2.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	@Test
	public void testCharSet(){
		//所支持的字符编码
		SortedMap<String,Charset> map = Charset.availableCharsets();
		Set<String> keySet = map.keySet();
		for(String key:keySet){
			System.out.println(key+"=="+map.get(key));
			
		}
	
	}
	@Test
	public void testCharSetDe(){
		//字符编码
		if(Charset.isSupported("utf-8")){
			System.out.println("ture");
			Charset charset = Charset.forName("utf-8");
			Charset charset2 = Charset.forName("GBK");
			CharsetEncoder encoder = charset.newEncoder();
			CharsetDecoder newDecoder = charset.newDecoder();
			CharBuffer ch=CharBuffer.allocate(1024);
			String name="陈荣发";
			char[] array = name.toCharArray();
			ch.put(array);
			ch.flip();
			try {
				ByteBuffer encode = encoder.encode(ch);
				System.out.println(encode.position());
				System.out.println(encode.limit());
				System.out.println(encode.capacity());
				System.out.println(encode.remaining());
				for(int i=0;i<encode.limit();i++){
					System.out.println(encode.get(i));
				}
				CharBuffer decode = charset2.decode(encode);
				System.out.println(decode.toString());
				
			} catch (CharacterCodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
