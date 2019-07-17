package no.petroware.seismicio.segy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract representation of the content of a SEG Y seismic file.
 *
 * @author <a href="mailto:info@petroware.no">Petroware AS</a>
 */
public class SegyFile
{
  /** SEG Y file name. */
  private final String name_;

  /** SEG Y tape label. Null if not present. */
  private final SegyTapeLabel tapeLabel_;

  /** Text headers including the mandatory EBCDIC header. */
  private final List<SegyTextHeader> textHeaders_ = new ArrayList<>();

  /** FIle header. */
  private final SegyFileHeader fileHeader_;

  /** Seismic traces. */
  private final List<SegyTrace> traces_ = new ArrayList<>();

  /** Min overall trace value. */
  private final double minValue_;

  /** Maximumk overall trace value. */
  private final double maxValue_;

  /**
   * Create a new SEG Y file instance.
   *
   * @param name         File name. Non-null.
   * @param tapeLabel    SEG Y tape label. Null if none.
   * @param textHeaders  Text headers. Non-null. Must contain at least
   *                     one entry, being the mandatory EBCDIC header.
   * @param fileHeader   File header. Non-null.
   * @param traces       File traces. Non-null.
   * @param minValue     Minimum overall trace value.
   * @param maxValue     Maximum overall trace value.
   * @throws IllegalArgumentException  If name, textHeaders, fileHeader or
   *                     traces is null, or if textHeaders is empty.
   */
  public SegyFile(String name,
                  SegyTapeLabel tapeLabel,
                  List<SegyTextHeader> textHeaders,
                  SegyFileHeader fileHeader,
                  List<SegyTrace> traces,
                  double minValue, double maxValue)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    if (textHeaders == null)
      throw new IllegalArgumentException("textHeaders cannot be null");

    if (textHeaders.isEmpty())
      throw new IllegalArgumentException("textHeader cannot be empty");

    if (fileHeader == null)
      throw new IllegalArgumentException("fileHEader cannot be null");

    if (traces == null)
      throw new IllegalArgumentException("traces cannot be null");

    name_ = name;
    tapeLabel_ = tapeLabel;
    textHeaders_.addAll(textHeaders);
    fileHeader_ = fileHeader;
    traces_.addAll(traces);
    minValue_ = minValue;
    maxValue_ = maxValue;
  }

  /**
   * Return the name of this instance. Typically (but not necesserily)
   * the name of the backing disk file.
   *
   * @return  Name of this instance. Never null.
   */
  public String getName()
  {
    return name_;
  }

  /**
   * Return the SEG Y tape label of this SEG Y file.
   *
   * @return  The SEG Y tape label of this SEG Y file.
   *          Null if not present.
   */
  public SegyTapeLabel getTapeLabel()
  {
    return tapeLabel_;
  }

  /**
   * Return all textual headers of this SEG Y file.
   * Includes at least one entry, being the mandatory EBCDIC header.
   *
   * @return All text headers of this SEG Y file.
   */
  public List<SegyTextHeader> getTextHeaders()
  {
    return Collections.unmodifiableList(textHeaders_);
  }

  /**
   * Return the first text header of this SEG Y file.
   *
   * @return  The first text header of this SEG Y file. Never null.
   */
  public SegyTextHeader getTextHeader()
  {
    return textHeaders_.get(0);
  }

  /**
   * Return the file header of this SEG Y file.
   *
   * @return  The file header of this SEG Y file. Never null.
   */
  public SegyFileHeader getFileHeader()
  {
    return fileHeader_;
  }

  /**
   * Return number of traces in theis SEG Y file.
   *
   * @return  Number of traces in this SEG Y file. [0,&gt;.
   */
  public int getNTraces()
  {
    return traces_.size();
  }

  /**
   * Return number of samples per trace.
   *
   * @return  Number of samples per trace. [0,&gt;.
   */
  public int getNSamplesPerTrace()
  {
    return traces_.isEmpty() ? 0 : traces_.get(0).getNSamples();
  }

  /**
   * Return the traces of the SEG Y file.
   *
   * @return  The traces of this SEG Y file. Never null.
   */
  public List<SegyTrace> getTraces()
  {
    return Collections.unmodifiableList(traces_);
  }

  /**
   * Return the specified trace of this SEG Y file.
   *
   * @param traceNo  Trace number to get. [0,nTraces&gt;.
   * @return         The requested trace. Never null.
   * @throws IllegalArgumentException  If traceNo is illegal.
   */
  public SegyTrace getTrace(int traceNo)
  {
    if (traceNo < 0 || traceNo >= traces_.size())
      throw new IllegalArgumentException("Invalid traceNo: " + traceNo);

    return traces_.get(traceNo);
  }

  /**
   * Return minimum overall trace value.
   *
   * @return  Minimum overall trace value.
   */
  public double getMinValue()
  {
    return minValue_;
  }

  /**
   * Return minimum overall trace value.
   *
   * @return  Minimum overall trace value.
   */
  public double getMaxValue()
  {
    return maxValue_;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return name_;
  }
}
