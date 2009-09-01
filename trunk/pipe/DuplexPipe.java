package pipe;

import java.io.IOException;
import java.net.Socket;

/**
 * <p>
 * <B>DuplexPipe: </B>双向管道！
 * </p>
 * <p>
 * 本程序是一个双向管道。传统的管道只能从一端输入、一端输出。双向管道不仅可以<br>
 * 让进程 A 的输出作为进程 B 的输入，也会让进程 B 的输出作为进程 A 的输入。<br>
 * 这样就可以让两个进程实现交流。
 * </p>
 * <p>
 * 本程序主要是 TCP 转发工具。允许监听本地端口，也可以主动连接远程端口。<br>
 * 如果和瑞士军刀“nc -e”配合使用， 就能实现本地进程和网络进程任意沟通。
 * </p>
 * <p>
 * 程序的具体使用方法可以参见 help()。
 * </p>
 * 
 * @author redraiment
 * @version 0.1.0
 * @category Main
 */
public class DuplexPipe
{
	public static void main ( String[] args )
	{
		SocketFactory[] sf = new SocketFactory[2];
		Socket[] s = new Socket[2];
		PipeIO[] p = new PipeIO[2];
		Verboser v = Verboser.getVerboser ();
		String ip;
		int port;

		int ac = 0;
		for ( int i = 0; i < args.length; i++ )
		{
			if ( args[i].charAt ( 0 ) != '-' )
			{
				help ();
			}

			switch ( args[i].charAt ( 1 ) )
			{
			case 'c':
				ip = args[++i];
				port = Integer.parseInt ( args[++i] );
				sf[ac++] = new SocketFactory ( ip, port );
				break;
			case 'l':
				port = Integer.parseInt ( args[++i] );
				sf[ac++] = new SocketFactory ( port );
				break;
			case 'v':
				v.setVerbose ( true );
				break;
			default :
				help ();
				break;
			}
		}

		while ( true )
		{
			s[0] = sf[0].getSocket ();
			s[1] = sf[1].getSocket ();

			try
			{
				p[0] = new PipeIO ( s[0].getInputStream (), s[1]
						.getOutputStream () );
				p[1] = new PipeIO ( s[1].getInputStream (), s[0]
						.getOutputStream () );
			}
			catch ( IOException e )
			{
				v.holler ( "Can't get IO-stream, why??" );
			}

			p[0].start ();
			p[1].start ();
			try
			{
				p[0].join ();
				p[1].join ();
			}
			catch ( InterruptedException e )
			{
				v.holler ( "Don't ask me!" );
				v.holler ( "I nerver interrupted it!" );
			}

			try
			{
				if ( s[0] != null )
				{
					s[0].close ();
					s[0] = null;
				}
				if ( s[1] != null )
				{
					s[1].close ();
					s[1] = null;
				}
			}
			catch ( IOException e )
			{
				v.holler ( "I can't belive it!" );
				v.holler ( "Socket can not close?" );
			}
		}
	}

	/** 帮助函数！完善中... */
	public static void help ()
	{
		Verboser v = Verboser.getVerboser ();
		v.setVerbose ( true );
		v.holler ( "[v0.1.0 in Java]" );
		v.holler ( "作者: redraiment" );
		v.holler ( "有任何建议，欢迎联系 redraiment@gmail.com" );
		v.holler ( "博客：http://blog.csdn.net/redraiment\n" );
		v.holler ( "用法: java pipe.DuplexPipe [-vh] model model" );
		v.holler ( "选项:" );
		v.holler ( "\t-v\t\t输出一些提示信息" );
		v.holler ( "\t-h\t\t显示本帮助文档" );
		v.holler ( "模式:" );
		v.holler ( "\t-l port\t\t监听本地端口" );
		v.holler ( "\t-c host port\t\t连接远程端口" );
		v.holler ( "示例:" );
		v.holler ( "\tjava pipe.DuplexPipe -c 192.168.1.100 3389 -l 1234" );
		v.bail ( "\t将本地 1234 号端口上的信息转发给 192.168.1.100 的 3389端口" );
	}
}
