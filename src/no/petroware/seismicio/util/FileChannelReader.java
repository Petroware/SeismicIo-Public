package no.petroware.seismicio.util;

import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.io.IOException;

/**
 * A file channel reader capable of reading larger than 2 GB files
 * (Integer.MAX_VALUE being the limit for memory mapped reading).
 * <p>
 * Inspired by the following article:
 * http://www.snippetit.com/2009/12/java-continuously-read-data-from-filechannel-without-mappedbytebuffer/
 *
 * @author <a href="mailto:info@petroware.no">Petroware AS</a>
 */
public final class FileChannelReader
{
  /**
   * The buffer size we are using. Experiments indicates that a good value
   * for performance for large files may be around 8 MB.
   */
  private final int bufferSize_;

  /** The file channel we are reading. */
  private final FileChannel fileChannel_;

  /** The data buffer. */
  private final ByteBuffer buffer_;

  /**
   * Create a reader for the specified channel.
   *
   * @param fileChannel  File channel to read. Non-null.
   * @param bufferSize   Buffer size. &lt;0,&gt;.
   * @throws IllegalArgumentException  If fileChannel is null.
   * @throws IllegalArgumentException  If bufferSize &lt;= 0.
   */
  public FileChannelReader(FileChannel fileChannel, int bufferSize)
  {
    if (fileChannel == null)
      throw new IllegalArgumentException("fileChannel cannot be null");

    if (bufferSize <= 0)
      throw new IllegalArgumentException("Invalid bufferSize: " + bufferSize);

    fileChannel_ = fileChannel;
    bufferSize_ = bufferSize;

    buffer_ = ByteBuffer.allocate(bufferSize_);
    buffer_.clear();
    buffer_.flip();
  }

  /**
   * Make sure there are at least size bytes available in the buffer.
   * If there is currently not, read a new portion from the backing file.
   *
   * @param size  Size to check. &lt;0,&gt;.
   */
  private void ensureData(int size)
    throws IOException
  {
    assert size > 0 : "Invalid size: " + size;

    if (buffer_.remaining() < size) {
      buffer_.compact();
      if (fileChannel_.read(buffer_) <= 0)
        throw new IOException("Unexpected end of stream");

      buffer_.flip();
    }
  }

  /**
   * Check how much data remains in the specified file channel.
   *
   * @param fileChannel  File channel to check. Non-null.
   * @return             Number of bytes left to read in the file channel. &lt;0,&gt;
   */
  private static long remaining(FileChannel fileChannel)
    throws IOException
  {
    assert fileChannel != null : "fileChannel cannot be null";

    return fileChannel.size() - fileChannel.position();
  }

  /**
   * Check if there is more data to read.
   *
   * @return  True if there is more data to read, false otherwise.
   * @throws IOException  If the check fails for some reason.
   */
  public boolean hasRemaining()
    throws IOException
  {
    return buffer_.hasRemaining() || remaining(fileChannel_) > 0;
  }

  /**
   * Check how many bytes are left for reading.
   *
   * @return  How many bytes are left for reading. &lt;0,&gt;
   * @throws IOException  If the check fails for some reason.
   */
  public long remaining()
    throws IOException
  {
    return buffer_.remaining() + remaining(fileChannel_);
  }

  /**
   * Return the current read position.
   *
   * @return Current read position. [0,size].
   * @throws IOException  If the check fails for some reason.
   */
  public long position()
    throws IOException
  {
    return fileChannel_.position() - buffer_.remaining();
  }

  /**
   * Reposition the current read location.
   *
   * @param position  Position to set. [0,&gt;.
   * @throws IllegalArgumentException  If position &lt; 0.
   * @throws IOException  If the operation fails for some reason.
   */
  public void position(long position)
    throws IOException
  {
    if (position < 0)
      throw new IllegalArgumentException("Invalid position: " + position);

    fileChannel_.position(position);
    buffer_.clear();
    buffer_.flip();
  }

  /**
   * Skip the speified number of bytes in the file.
   *
   * @param nBytes  Number of bytes to skip. May be negative to
   *                jump backwards.
   */
  public void skip(long nBytes)
    throws IOException
  {
    position(position() + nBytes);
  }

  /**
   * @see ByteBuffer.getInt()
   *
   * @return Read int.
   * @throws IOException  If the operation fails for some reason.
   */
  public int getInt()
    throws IOException
  {
    ensureData(Integer.SIZE / 8);
    return buffer_.getInt();
  }

  /**
   * @see ByteBuffer.getShort()
   *
   * @return Read short.
   * @throws IOException  If the operation fails for some reason.
   */
  public int getShort()
    throws IOException
  {
    ensureData(Short.SIZE / 8);
    return buffer_.getShort();
  }

  /**
   * @see ByteBuffer.getFloat()
   *
   * @return Read float.
   * @throws IOException  If the operation fails for some reason.
   */
  public float getFloat()
    throws IOException
  {
    ensureData(Float.SIZE / 8);
    return buffer_.getFloat();
  }

  /**
   * @see ByteBuffer.getDouble()
   *
   * @return Read doublet.
   * @throws IOException  If the operation fails for some reason.
   */
  public double getDouble()
    throws IOException
  {
    ensureData(Double.SIZE / 8);
    return buffer_.getDouble();
  }

  /**
   * @see ByteBuffer.get()
   *
   * @return Read byte.
   * @throws IOException  If the operation fails for some reason.
   */
  public byte get()
    throws IOException
  {
    ensureData(Byte.SIZE / 8);
    return buffer_.get();
  }

  /**
   * @see ByteBuffer.get(byte[])
   *
   * @param  destination  Array of byte to populate. Non-null.
   * @throws IOException  If the operation fails for some reason.
   */
  public void get(byte[] destination)
    throws IOException
  {
    ensureData(destination.length * Byte.SIZE / 8);
    buffer_.get(destination);
  }

  /*
  public static void main(String[] arguments)
  {
    // 1GB, read meta-data in about 30s
    java.io.File inFile = new java.io.File("X:/GDF_SUEZ/Exploration/7218_8-1/data_to_PB/WELL_7218_8-1/WB_1508-L/MWD/BIT_RUNS/WL_RAW_CAL-DEN-GR-NEU-REMP-RLL_MWD_1.DLIS");

    try {
      java.io.FileInputStream fileInputStream = new java.io.FileInputStream(inFile);
      FileChannel fileChannel = fileInputStream.getChannel();

      FileChannelReader reader = new FileChannelReader(fileChannel, 100);

      for (int i = 0; i < 1000; i++)
        System.out.println(reader.getInt());
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  */
}
