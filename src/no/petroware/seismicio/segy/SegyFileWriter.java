package no.petroware.seismicio.segy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A writer for SEG Y files.
 *
 * @author <a href="mailto:info@petroware.no">Petroware AS</a>
 */
public class SegyFileWriter
{
  /** The logger instance. */
  private static final Logger logger_ = Logger.getLogger(SegyFileWriter.class.getName());

  /** The SEG Y physical file instance. */
  private final File file_;

  /**
   * Create a SEGY writer for the specified file instance.
   * The actual writing is done with the readFile() method.
   *
   * @param file  File to read. Non null.
   * @throws IllegalArgumentException  If file is null.
   */
  public SegyFileWriter(File file)
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    file_ = file;
  }

  /**
   * Write the specified SEG Y file to disk.
   *
   * @param segyFile  SEG Y file to write. Non-null.
   * @throws IllegalArgumentException  iF segyFile is null.
   * @throws IOException  If the write operation fails for some reason.
   */
  public void write(SegyFile segyFile)
    throws IOException
  {
    if (segyFile == null)
      throw new IllegalArgumentException("segyFile cannot be null");

    FileOutputStream stream = new FileOutputStream(file_, true);

    try {
      // TODO
    }
    finally {
      stream.close();
    }
  }

  /**
   * Inject the specified text header into the EBCDIC section of
   * the disk file.
   *
   * @param textHeader  Text header to write.
   * @throws IllegalArgumentException  If textHeader is null.
   * @throws IOException  If the write operation failes for some reason.
   */
  public void writeTextHeader(SegyTextHeader textHeader)
    throws IOException
  {
    if (textHeader == null)
      throw new IllegalArgumentException("textHeader cannot be null");

    byte[] header = textHeader.asEbcdic();

    RandomAccessFile file = null;

    try {
      file = new RandomAccessFile(file_, "rw");

      logger_.log(Level.INFO, "Injecting text header in " + file_);

      file.seek(0L);
      file.write(header);

      logger_.log(Level.INFO, "Write operation complete");
    }
    catch (IOException exception) {
      logger_.log(Level.INFO, "Unable to write header of " + file_, exception);
    }
    finally {
      if (file != null)
        file.close();
    }
  }
}
