package no.petroware.seismicio.segy;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Model the content of the 3200-byte free-form text header
 * of a SEG Y file.
 * <p>
 * A SEG Y file contains at least one such header, but may optionally
 * contain more than one, according to the number of extended records
 * indicated in the binary file header.
 *
 * @author <a href="mailto:info@petroware.no">Petroware AS</a>
 */
public final class SegyTextHeader
{
  /** The fixed size length of a tect header. */
  public static final int LENGTH = 3200;

  /** Text of the header. Never null, always LENGTH length. */
  private String text_;

  /**
   * Create a text header with the specified content.
   *
   * @param text  Text content of header. Will be clipped or
   *              space padded as necessary to make it the
   *              fixed size according to the SEG Y specification.
   *              Not null.
   * @throws IllegalArgumentException  If text is null.
   */
  public SegyTextHeader(String text)
  {
    if (text == null)
      throw new IllegalArgumentException("text cannot be null");

    setText(text);
  }

  /**
   * Return the text of this header.
   *
   * @return  Text of this header. Never null. Always LENGTH of length.
   */
  public String getText()
  {
    return text_;
  }

  /**
   * Set text of this header.
   *
   * @param text  Text to set. Non-null. The text will be padded or cut
   *              as necessary in order to fulfill the length requirement.
   */
  public void setText(String text)
  {
    if (text == null)
      throw new IllegalArgumentException("text cannot be null");

    String s = text.substring(0, Math.min(text.length(), LENGTH));
    String formatString = "%-" + LENGTH + "s";
    text_ = String.format(formatString, s);
  }

  /**
   * Return the text of this header encoded according to EBCDIC.
   *
   * @return  The bytes of this header as EBCDIC. Never null.
   *          Always length LENGTH.
   */
  public byte[] asEbcdic()
  {
    try  {
      return text_.getBytes("IBM500");
    }
    catch (UnsupportedEncodingException exception) {
      assert false : "Programming error";
      return text_.getBytes();
    }
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder(text_);

    // The text header is normally without newlines
    // so we insert every 80 chcracter to improve readability
    int p = 80;
    while (p < s.length()) {
      s.insert(p, "\n");
      p += 81;
    }

    return s.toString();
  }
}
