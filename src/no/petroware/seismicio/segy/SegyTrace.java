package no.petroware.seismicio.segy;

/**
 * Model a SEG-Y trace in terms of its header and data content.
 *
 * @author <a href="mailto:info@petroware.no">Petroware AS</a>
 */
public final class SegyTrace
{
  /** Trace header. Non-null. */
  private final SegyTraceHeader traceHeader_;

  /** Data type of trace values. */
  private final Class<?> dataType_;

  /** Trace values. */
  private final Object[] values_;

  /**
   * Create a new SEG-Y trace.
   *
   * @param traceHeader  Trace header. Non-null.
   * @param dataType     Data type of trace values. Non-null.
   * @param values       The trace values. Non-null.
   * @throws IllegalArgumentException  If any of the arguments are null.
   */
  public SegyTrace(SegyTraceHeader traceHeader, Class<?> dataType, Object[] values)
  {
    if (traceHeader == null)
      throw new IllegalArgumentException("traceHeader cannot be null");

    if (dataType == null)
      throw new IllegalArgumentException("dataType cannot be null");

    if (values == null)
      throw new IllegalArgumentException("values cannot be null");

    traceHeader_ = traceHeader;
    dataType_ = dataType;
    values_ = values.clone();
  }

  /**
   * Return the trace header of this trace.
   *
   * @return  Trace header of this trace. Never null.
   */
  public SegyTraceHeader getHeader()
  {
    return traceHeader_;
  }

  /**
   * Return data type oif the trace values.
   *
   * @return  Data type of the trace values. Never null.
   */
  public Class<?> getDataType()
  {
    return dataType_;
  }

  /**
   * Return number of samples in this trace.
   *
   * @return  Number of samples in this trace. [0,&gt;.
   */
  public int getNSamples()
  {
    return values_.length;
  }

  /**
   * Return the specified value of this trace.
   *
   * @param index  Index of value to get. [0,nValues&gt;.
   * @return       The requested value. Never null.
   * @throws IllegalArgumentException  If index is out of bounds.
   */
  public Object getValue(int index)
  {
    if (index < 0 || index >= values_.length)
      throw new IllegalArgumentException("index is out of bounds: " + index);

    return values_[index];
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return traceHeader_.toString();
  }
}
