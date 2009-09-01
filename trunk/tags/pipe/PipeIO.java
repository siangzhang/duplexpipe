package pipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * <B>PipeIO: </B>管道的输入输出操作。
 * </p>
 * <p>
 * 通过制定管道的输入输出，来实现进程通信。<br>
 * 因为请求和相应是异步的，因此采用多线程。
 * </p>
 * 
 * @author redraiment
 * @version 0.1.0
 * @category Thread
 */
public class PipeIO extends Thread
{
	private Verboser v = Verboser.getVerboser ();
	private InputStream in = null;
	private OutputStream out = null;

	/** 需要制定管道的输入流和输出流。 */
	public PipeIO ( InputStream in, OutputStream out )
	{
		this.in = in;
		this.out = out;
	}

	/** 从输入流获取数据，完整地写到输出流。 */
	public void run ()
	{
		if ( in == null || out == null )
			return;

		try
		{
			byte[] buffer = new byte[1024];
			int len;

			// 核心：读写数据
			while ( ( len = in.read ( buffer ) ) != -1 )
			{
				out.write ( buffer, 0, len );
				out.flush ();
			}
		}
		catch ( IOException e )
		{
			v.holler ( "Maybe connect is closed." );
		}

		try
		{
			// 无论如何都关闭IO stream。
			if ( in != null )
			{
				in.close ();
			}
			if ( out != null )
			{
				out.close ();
			}
		}
		catch ( IOException e )
		{
			v.holler ( "I can't belive it!" );
			v.holler ( "IO stream can not close?" );
		}
	}
}
