package pipe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <p>
 * <B>SocketFactory: </B>Socket 工厂，可以监听本地端口或者连接远程端口。
 * </p>
 * <p>
 * 该类主要是为了屏蔽获取 Socket 的细节，简化主程序的编码。
 * </p>
 * 
 * @author redraiment
 * @version 0.1.0
 * @category Factory
 */
public class SocketFactory
{
	private Verboser v = Verboser.getVerboser ();
	private ServerSocket ss = null;
	private String ip = null;
	private int port;

	/** 通过制定端口来获取 Socket。 */
	public SocketFactory ( int port )
	{
		try
		{
			ss = new ServerSocket ( port );
		}
		catch ( IOException e )
		{
			ss = null;
		}
	}

	/** 通过制定远程地址和端口来获取 Socket。 */
	public SocketFactory ( String ip, int port )
	{
		this.ip = ip;
		this.port = port;
	}

	/** 获取 Socket。 */
	public Socket getSocket ()
	{
		try
		{
			return ss == null ? new Socket ( ip, port ) : ss.accept ();
		}
		catch ( UnknownHostException e )
		{
			v.bail ( "Unknown Host: " + ip );
		}
		catch ( IOException e )
		{
			v.bail ( "Are you sure the remote host open?" );
		}

		return null;
	}
}
