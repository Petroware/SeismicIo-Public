package no.petroware.seismicio.segy;

/**
 * Model the content of the 240 byte SEG Y binary file header.
 * <p>
 * From the documentation:
 * <p>
 * <em>
 * The 400-byte Binary File Header record
 * contains binary values that affect the whole
 * SEG Y file.  The values in the Binary File
 * Header are defined as two-byte or four-byte,
 * two's complement or unsigned integers,
 * with the exception of optional IEEE double
 * precision sample intervals.  Certain values
 * in this header are crucial for the processing
 * of the data in the file, particularly the sample
 * interval, trace length and format code.
 * Revision 2.0 defines a few additional fields
 * in the optional portion, as well as providing
 * some clarification on the use of some
 * existing entries.
 * </em>
 *
 * @author <a href="mailto:info@petroware.no">Petroware AS</a>
 */
public final class SegyFileHeader
{
  /**
   * Enumeration of the possible trace value data formats.
   */
  public enum DataFormat
  {
    /** Unknown. */
    UNKNOWN(0, 1, Byte.class, "Unknown"),

    /** 4-byte IBM floating-point. */
    FLOAT4(1, 4, Float.class, "4-byte IBM floating-point"),

    /** 4-byte, two's complement integer. */
    INT4(2, 4, Integer.class, "4-byte, two's complement integer"),

    /** 2-byte, two's complement integer. */
    INT2(3, 2, Short.class, "2-byte, two's complement integer"),

    /** 4-byte fixed point with gain. */
    INT4_GAIN(4, 4, Integer.class, "4-byte fixed point with gain"),

    /** 4-byte IEEE floating point. */
    FLOAT4_IEEE(5, 4, Float.class, "4-byte IEEE floating point"),

    /** 1-byte, two's complement integer. */
    BYTE(8, 1, Byte.class, "1-byte, two's complement integer");

    private final int tag_;

    private final int size_;

    private final Class<?> dataType_;

    private final String description_;

    private DataFormat(int tag, int size, Class<?> dataType, String description)
    {
      assert size > 0 : "Invalid size: " + size;
      assert description != null : "description cannot be null";

      tag_ = tag;
      size_ = size;
      dataType_ = dataType;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

    /**
     * Return size in bytes of this data type.
     *
     * @return  Size in bytes of this data type. &lt;0,&gt;.
     */
    public int getSize()
    {
      return size_;
    }

    /**
     * Return the equivalent Java class of this data type.
     *
     * @return  The equivalent Java class of this data type. Never null.
     */
    public Class<?> getDataType()
    {
      return dataType_;
    }

    /**
     * Return a textual description of this enumeration entry.
     *
     * @return  A textual description of this enumeration entry.
     *          Never null.
     */
    public String getDescription()
    {
      return description_;
    }

    /**
     * Return enumeration value based on the disk file tag
     * representing the entry.
     *
     * @param tag  Tag representing the entry.
     * @return     Associated enumeration entry, or null if not found.
     */
    public static DataFormat get(int tag)
    {
      for (DataFormat dataFormat : DataFormat.values()) {
        if (dataFormat.tag_ == tag)
          return dataFormat;
      }

      // Not found
      return null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
      return description_ + " (" + tag_ + ")";
    }
  }

  public enum Sorting
  {
    OTHER(-1, "Other"),
    UNKNOWN(0, "Unknown"),
    ASRECORDED(1, "As recorded"),
    CDP_ENSAMBLE(2, "CDP ensable"),
    SINGLE_FOLD(3, "Single fold"),
    HORIZONTALLY_STACKED(4, "Horizontally stacked"),
    COMMON_SOURCE_POINT(5, "Common source point"),
    COMMON_RECEIVER_POINT(6, "Common receiver point"),
    COMMON_OFFSET_POINT(7, "Common offest point"),
    COMMON_MID_POINT(8, "Common mid point"),
    COMMON_CONVERSION_POINT(9, "Common conversion point");

    private final int tag_;

    private final String description_;

    Sorting(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

    /**
     * Return a textual description of this enumeration entry.
     *
     * @return  A textual description of this enumeration entry.
     *          Never null.
     */
    public String getDescription()
    {
      return description_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
      return description_ + " (" + tag_ + ")";
    }

    /**
     * Return enumeration value based on the disk file tag
     * representing the entry.
     *
     * @param tag  Tag representing the entry.
     * @return     Associated enumeration entry, or null if not found.
     */
    public static Sorting get(int tag)
    {
      for (Sorting sorting : Sorting.values()) {
        if (sorting.tag_ == tag)
          return sorting;
      }

      // Not found
      return null;
    }
  }

  public enum SweepType
  {
    UNKNOWN(0, "Unknown"),
    LINEAR(1, "Linear"),
    PARABOLIC(2, "Parabolic"),
    EXPONENTIAL(3, "Exponential"),
    OTHER(4, "Other");

    private final int tag_;

    private final String description_;

    SweepType(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

    /**
     * Return a textual description of this enumeration entry.
     *
     * @return  A textual description of this enumeration entry.
     *          Never null.
     */
    public String getDescription()
    {
      return description_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
      return description_ + " (" + tag_ + ")";
    }

    /**
     * Return enumeration value based on the disk file tag
     * representing the entry.
     *
     * @param tag  Tag representing the entry.
     * @return     Associated enumeration entry, or null if not found.
     */
    public static SweepType get(int tag)
    {
      for (SweepType sweepType : SweepType.values()) {
        if (sweepType.tag_ == tag)
          return sweepType;
      }

      // Not found
      return null;
    }
  }

  public enum SweepTaperType
  {
    UNKNOWN(0, "Unknown"),
    LINEAR(1, "Linear"),
    COS2(2, "Cos2"),
    OTHER(3, "Other");

    private final int tag_;

    private final String description_;

    private SweepTaperType(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

    /**
     * Return a textual description of this enumeration entry.
     *
     * @return  A textual description of this enumeration entry.
     *          Never null.
     */
    public String getDescription()
    {
      return description_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
      return description_ + " (" + tag_ + ")";
    }

    /**
     * Return enumeration value based on the disk file tag
     * representing the entry.
     *
     * @param tag  Tag representing the entry.
     * @return     Associated enumeration entry, or null if not found.
     */
    public static SweepTaperType get(int tag)
    {
      for (SweepTaperType sweepTaperType : SweepTaperType.values()) {
        if (sweepTaperType.tag_ == tag)
          return sweepTaperType;
      }

      // Not found
      return null;
    }
  }

  public enum AmplitudeRecoveryType
  {
    UNKNOWN(0, "Unknown"),
    NONE(1, "None"),
    SPHERICAL_DIVERGENCE(2, "Spherical divergence"),
    AGC(3, "AGC"),
    OTHER(4, "Other");

    private final int tag_;

    private final String description_;

    private AmplitudeRecoveryType(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

    /**
     * Return a textual description of this enumeration entry.
     *
     * @return  A textual description of this enumeration entry.
     *          Never null.
     */
    public String getDescription()
    {
      return description_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
      return description_ + " (" + tag_ + ")";
    }

    /**
     * Return enumeration value based on the disk file tag
     * representing the entry.
     *
     * @param tag  Tag representing the entry.
     * @return     Associated enumeration entry, or null if not found.
     */
    public static AmplitudeRecoveryType get(int tag)
    {
      for (AmplitudeRecoveryType amplitudeRecoveryType : AmplitudeRecoveryType.values()) {
        if (amplitudeRecoveryType.tag_ == tag)
          return amplitudeRecoveryType;
      }

      // Not found
      return null;
    }
  }

  public enum LengthUnit
  {
    UNKNOWN(0, "Unknown", ""),
      METER(1, "Meter", "m"),
      FEET(2, "Feet", "ft");

    private final int tag_;

    private final String description_;

    private final String unitSymbol_;

    private LengthUnit(int tag, String description, String unitSymbol)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
      unitSymbol_ = unitSymbol;
    }

    int getTag()
    {
      return tag_;
    }

    /**
     * Return a textual description of this enumeration entry.
     *
     * @return  A textual description of this enumeration entry.
     *          Never null.
     */
    public String getDescription()
    {
      return description_;
    }

    public String getUnitSymbol()
    {
      return unitSymbol_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
      return description_ + " (" + tag_ + ")";
    }

    /**
     * Return enumeration value based on the disk file tag
     * representing the entry.
     *
     * @param tag  Tag representing the entry.
     * @return     Associated enumeration entry, or null if not found.
     */
    public static LengthUnit get(int tag)
    {
      for (LengthUnit lengthUnit : LengthUnit.values()) {
        if (lengthUnit.tag_ == tag)
          return lengthUnit;
      }

      // Not found
      return null;
    }
  }

  public enum ImpulseSignalPolarity
  {
    UNKNOWN(0, "Unknown"),
    NEGATIVE(1, "Negative"),
    POSITIVE(2, "Positive");

    private final int tag_;

    private final String description_;

    private ImpulseSignalPolarity(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

    /**
     * Return a textual description of this enumeration entry.
     *
     * @return  A textual description of this enumeration entry.
     *          Never null.
     */
    public String getDescription()
    {
      return description_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
      return description_ + " (" + tag_ + ")";
    }

    /**
     * Return enumeration value based on the disk file tag
     * representing the entry.
     *
     * @param tag  Tag representing the entry.
     * @return     Associated enumeration entry, or null if not found.
     */
    public static ImpulseSignalPolarity get(int tag)
    {
      for (ImpulseSignalPolarity impulseSignalPolarity : ImpulseSignalPolarity.values()) {
        if (impulseSignalPolarity.tag_ == tag)
          return impulseSignalPolarity;
      }

      // Not found
      return null;
    }
  }

  public enum VibratoryPolarity
  {
    SECTOR1(1, 337.5, 22.5),
    SECTOR2(2, 22.5, 67.5),
    SECTOR3(3, 67.5, 112.5),
    SECTOR4(4, 112.5, 157.5),
    SECTOR5(5, 157.5, 202.5),
    SECTOR6(6, 202.5, 247.5),
    SECTOR7(7, 247.5, 292.5),
    SECTOR8(8, 292.5, 337.5);

    private final int tag_;

    private final double startAngle_;

    private final double endAngle_;

    private VibratoryPolarity(int tag, double startAngle, double endAngle)
    {
      tag_ = tag;
      startAngle_ = startAngle;
      endAngle_ = endAngle;
    }

    int getTag()
    {
      return tag_;
    }

    public double[] getAngleInterval()
    {
      return new double[] {startAngle_, endAngle_};
    }

    /**
     * Return a textual description of this enumeration entry.
     *
     * @return  A textual description of this enumeration entry.
     *          Never null.
     */
    public String getDescription()
    {
      return startAngle_ + " - " + endAngle_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
      return getDescription() + " (" + tag_ + ")";
    }

    /**
     * Return enumeration value based on the disk file tag
     * representing the entry.
     *
     * @param tag  Tag representing the entry.
     * @return     Associated enumeration entry, or null if not found.
     */
    public static VibratoryPolarity get(int tag)
    {
      for (VibratoryPolarity vibratoryPolarity : VibratoryPolarity.values()) {
        if (vibratoryPolarity.tag_ == tag)
          return vibratoryPolarity;
      }

      // Not found
      return null;
    }

    /**
     * Return the vibratory polarity interval based on the
     * specified angle.
     *
     * @param angle  Angle (in degrees) to get interval of.
     * @return       Associated interval. Never null.
     */
    public static VibratoryPolarity get(double angle)
    {
      // Make sure angle is within [0,360>
      angle += Math.ceil(-angle / 360.0) * 360.0;

      for (VibratoryPolarity vibratoryPolarity : VibratoryPolarity.values()) {
        if (angle < vibratoryPolarity.endAngle_)
          return vibratoryPolarity;
      }

      // Not found
      assert false : "Programming error";
      return null;
    }
  }

  private final int jobNo_;
  private final int lineNo_;
  private final int reelNo_;
  private final int nTracesPerShot_;
  private final int maxCmpFold_;
  private final int nAuxillaryTraces_;
  private final int sampleRate_;      // [micro seconds]
  private final int sampleRateOriginal_;
  private final int nSamples_;
  private final int nSamplesOriginal_;
  private final DataFormat dataFormat_;
  private final int cmpFold_;
  private final Sorting sorting_;
  private final int verticalSum_;
  private final int sweepFrequencyAtStart_;
  private final int sweepFrequencyAtEnd_;
  private final int sweepLength_;
  private final SweepType sweepType_;
  private final int sweepChannelNo_;
  private final int sweepTaperLengthAtStart_;
  private final int sweepTaperLengthAtEnd_;
  private final SweepTaperType sweepTaperType_;
  private final boolean isTracesCorrelated_;
  private final boolean isBinaryGainRecovery_;
  private final AmplitudeRecoveryType amplitudeRecoveryType_;
  private final LengthUnit lengthUnit_;
  private final ImpulseSignalPolarity impulseSignalPolarity_;
  private final VibratoryPolarity vibratoryPolarity_;
  private final SegyVersion segyVersion_;
  private final boolean isTraceFixedLength_;
  private final int nExtendedRecords_;

  SegyFileHeader(int jobNo,
                 int lineNo,
                 int reelNo,
                 int nTracesPerShot,
                 int nAuxillaryTraces,
                 int sampleRate,
                 int sampleRateOriginal,
                 int nSamples,
                 int nSamplesOriginal,
                 DataFormat dataFormat,
                 int cmpFold,
                 Sorting sorting,
                 int verticalSum,
                 int sweepFrequencyAtStart,
                 int sweepFrequencyAtEnd,
                 int sweepLength,
                 SweepType sweepType,
                 int sweepChannelNo,
                 int sweepTaperLengthAtStart,
                 int sweepTaperLengthAtEnd,
                 SweepTaperType sweepTaperType,
                 boolean isBinaryGainRecovery,
                 AmplitudeRecoveryType amplitudeRecoveryType,
                 LengthUnit lengthUnit,
                 ImpulseSignalPolarity impulseSignalPolarity,
                 VibratoryPolarity vibratoryPolarity,
                 SegyVersion segyVersion,
                 boolean isTraceFixedLength,
                 int nExtendedRecords)
  {
    jobNo_ = jobNo;
    lineNo_ = lineNo;
    reelNo_ = reelNo;
    nTracesPerShot_ = nTracesPerShot;
    nAuxillaryTraces_ = nAuxillaryTraces;
    sampleRate_ = sampleRate;
    sampleRateOriginal_ = sampleRateOriginal;
    nSamples_ = nSamples;
    nSamplesOriginal_ = nSamplesOriginal;
    dataFormat_ = dataFormat;
    cmpFold_ = cmpFold;
    sorting_ = sorting;
    verticalSum_ = verticalSum;
    sweepFrequencyAtStart_ = sweepFrequencyAtStart;
    sweepFrequencyAtEnd_ = sweepFrequencyAtEnd;
    sweepLength_ = sweepLength;
    sweepType_ = sweepType;
    sweepChannelNo_ = sweepChannelNo;
    sweepTaperLengthAtStart_ = sweepTaperLengthAtStart;
    sweepTaperLengthAtEnd_ = sweepTaperLengthAtEnd;
    sweepTaperType_ = sweepTaperType;
    isBinaryGainRecovery_ = isBinaryGainRecovery;
    amplitudeRecoveryType_ = amplitudeRecoveryType;
    lengthUnit_ = lengthUnit;
    impulseSignalPolarity_ = impulseSignalPolarity;
    vibratoryPolarity_ = vibratoryPolarity;
    segyVersion_ = segyVersion;
    isTraceFixedLength_ = isTraceFixedLength;
    nExtendedRecords_ = nExtendedRecords;

    // TODO
    maxCmpFold_ = -1;
    isTracesCorrelated_ = false;
  }

  /**
   * Return the job identification number of the SEG Y file.
   *
   * @return  The job identification number of the SEG Y file.
   */
  public int getJobNo()
  {
    return jobNo_;
  }

  /**
   * Return the line number of the SEG Y file.
   * <p>
   * For 3-D poststack data, this will typically contain the in-line
   * number.
   *
   * @return  The line number of the SEG Y file.
   */
  public int getLineNo()
  {
    return lineNo_;
  }

  /**
   * Return the reel number of the SEG Y file.
   *
   * @return  The reel number of the SEG Y file.
   */
  public int getReelNo()
  {
    return reelNo_;
  }

  /**
   * Return the number of traces per ensamble of the SEG Y file.
   * <p>
   * Mandatory for prestack data.
   *
   * @return  The number opf traces per ensamble of the SEG Y file.
   */
  public int getNTracesPerShot()
  {
    return nTracesPerShot_;
  }

  /**
   * Return the number of auxilliary traces per ensamble of the SEG Y file.
   * <p>
   * Mandatory for prestack data.
   *
   * @return  The number of auxilliary traces per ensamble of the SEG Y file.
   */
  public int getNAuxillaryTraces()
  {
    return nAuxillaryTraces_;
  }

  /**
   * Return the sample interval of the SEG Y file.
   * <p>
   * Sample interval. Microseconds (us) for time data, Hertz
   * (Hz) for frequency data, meters (m) or feet (ft) for depth data.
   *
   * @return  The sample interval of the SEG Y file.
   */
  public int getSampleRate()
  {
    return sampleRate_;  // micro seconds
  }

  /**
   * Return sample interval of original field recording of the SEG Y file.
   *
   * @return  The sample interval of original field recording of the SEG Y file.
   */
  public int getSampleRateOriginal()
  {
    return sampleRateOriginal_;
  }

  /**
   * Return the number of samples per data trace of the SEG Y file.
   * <p>
   * Note: The sample interval and number of samples in the
   * Binary File Header
   * should be for the primary set of seismic data traces in the file.
   *
   * @return  The number of samples per data trace of the SEG Y file.
   */
  public int getNSamples()
  {
    return nSamples_;
  }

  /**
   * Return the number of samples per data trace for original field recording
   * of the SEG Y file.
   *
   * @return  The number of samples per data trace for original field recordingof
   * the SEG Y file.
   */
  public int getNSamplesOriginal()
  {
    return nSamplesOriginal_;
  }

  /**
   * Return the data format of the SEG Y file.
   * <p>
   * Mandatory for all data.
   *
   * @return  The data format of the SEG Y file.
   */
  public DataFormat getDataFormat()
  {
    return dataFormat_;
  }

  /**
   * Return the ensamble fold of the SEG Y file.
   * <p>
   * The expected number of data traces per trace ensemble
   * (e.g. the CMP fold).
   *
   * @return  The ensamble fold of the SEG Y file.
   */
  public int getCmpFold()
  {
    return cmpFold_;
  }

  /**
   * Return the trace sorting of the SEG Y file.
   * <p>
   * I.e. type of ensemble.
   *
   * @return  The trac sorting of the SEG Y file.
   */
  public Sorting getSorting()
  {
    return sorting_;
  }

  /**
   * Return the vertical sum code of the SEG Y file.
   * <br>
   * <pre>
   *   1 = no sum,
   *   2 = two sum,
   *   ...,
   *   N = M-1 sum  (M = 2 to 32,767)
   * </pre>
   *
   * @return  The vertical sum code of the SEG Y file.
   */
  public int getVerticalSum()
  {
    return verticalSum_;
  }

  /**
   * Return the sweep frequency at start of the SEG Y file.
   *
   * @return  The sweep frequency at start of the SEG Y file. In Hz.
   */
  public int getSweepFrequencyAtStart()
  {
    return sweepFrequencyAtStart_;
  }

  /**
   * Return the sweep frequency at end of the SEG Y file.
   *
   * @return  The sweep frequency at end of the SEG Y file. In Hz.
   */
  public int getSweepFrequencyAtEnd()
  {
    return sweepFrequencyAtEnd_;
  }

  /**
   * Return the sweep length of the SEG Y file.
   *
   * @return  The sweep length of the SEG Y file. In ms.
   */
  public int getSweepLength()
  {
    return sweepLength_;
  }

  /**
   * Return the sweep type of the SEG Y file.
   *
   * @return  The sweep type of the SEG Y file.
   */
  public SweepType getSweepType()
  {
    return sweepType_;
  }

  /**
   * Return the trace number of sweep channel of the SEG Y file.
   *
   *
   * @return  The trace number of sweep channel of the SEG Y file.
   */
  public int getSweepChannelNo()
  {
    return sweepChannelNo_;
  }

  /**
   * Return the sweep taper length at start of the SEG Y file.
   * <p>
   * Sweep trace taper length in milliseconds at start if tapered
   * (the taper starts at zero time and is effective for this length).
   *
   * @return  The sweep taper length at start of of the SEG Y file. In ms.
   */
  public int getSweepTaperLengthAtStart()
  {
    return sweepTaperLengthAtStart_;
  }

  /**
   * Return the sweep taper length at end of the SEG Y file.
   * <p>
   * Sweep trace taper length in milliseconds at end (the ending
   * taper starts at sweep length minus the taper length at end).
   *
   * @return  The sweep taper length at end of the SEG Y file. In ms.
   */
  public int getSweepTaperLengthAtEnd()
  {
    return sweepTaperLengthAtEnd_;
  }

  /**
   * Return the sweep taper type of the SEG Y file.
   *
   * @return  The sweep taper type of the SEG Y file.
   */
  public SweepTaperType getSweepTaperType()
  {
    return sweepTaperType_;
  }

  /**
   * Return if binary gain has been recovered in the SEG Y file.
   *
   * @return  Ff binary gain has been recovered in the SEG Y file.
   */
  public boolean isBinaryGainRecovery()
  {
    return isBinaryGainRecovery_;
  }

  /**
   * Return the amplitude recovery method of the SEG Y file.
   *
   * @return  The amplitude recovery method of the SEG Y file.
   */
  public AmplitudeRecoveryType getAmplitudeRecoveryType()
  {
    return amplitudeRecoveryType_;
  }

  /**
   * Return the length unit of the SEG Y file.
   * <p>
   * Measurement system: If Location Data stanzas are included
   * in the file, this entry must agree with the Location Data stanza.
   * If there is a disagreement, the last Location Data stanza is the
   * controlling authority.
   *
   * @return  The length unit of the SEG Y file.
   */
  public LengthUnit getLengthUnit()
  {
    return lengthUnit_;
  }

  /**
   * Return the impulse signal polarity of the SEG Y file.
   * <p>
   *
   *
   * @return  The impulse signal polarity of the SEG Y file.
   */
  public ImpulseSignalPolarity getImpulseSignalPolarity()
  {
    return impulseSignalPolarity_;
  }

  /**
   * Return the vibratory polarity sector of the SEG Y file.
   * <p>
   * This is how much the seismic signal lags pilot signal by.
   *
   * @return  The vibratory polarity sector of the SEG Y file.
   */
  public VibratoryPolarity getVibratoryPolarity()
  {
    return vibratoryPolarity_;
  }

  /**
   * Return the SEG Y format revision number of the SEG Y file.
   * <p>
   * Major SEG Y Format Revision Number. This is an 8-bit unsigned value.
   * Thus for SEG Y Revision 2.0, as defined in this document, this will
   * be recorded as 02<sub>16</sub>.  This field is mandatory for all
   * versions of SEG Y, although a value of zero indicates "traditional"
   * SEG Y conforming to the 1975 standard.
   *
   * @return  The  SEG Y format revision number of the SEG Y file.
   */
  public SegyVersion getSegyVersion()
  {
    return segyVersion_;
  }

  /**
   * Return if the traces are fixed length in the SEG Y file.
   * <p>
   * Fixed length trace flag.  A value of one indicates th
   * at all traces in this SEG Y file are guaranteed to have the same
   * sample interval and number of samples, as specified in Binary File
   * Header bytes 3217-3218 and 3221-3222.  A value of zero indicates that the
   * length of the traces in the file may vary and the number of samples in
   * bytes 115-116 of the Standard SEG Trace  Header must be
   * examined to determine the actual length of each trace. This field is
   * mandatory for all versions of SEG Y, although a value of zero indicates
   * "traditional" SEG Y.
   *
   * @return  If the traces are fixed length in the SEG Y file.
   */
  public boolean isTraceFixedLength()
  {
    return isTraceFixedLength_;
  }

  /**
   * Return the number of 3200-byte extended records of the SEG Y file.
   * <p>
   * Number of 3200-byte, Extended Textual File Header records following the
   * Binary Header.  A value of zero indicates there are no Extended Textual File
   * Header records (i.e. this file has no Extended Textual File Header(s)).  A value
   * of -1 indicates that there are a variable number of Extended Textual File
   * Header records and the end of the Extended Textual File Header is denoted by
   * an ((SEG: EndText)) stanza in the final record.  A positive value indicates that
   * there are exactly that many Extended Textual File Header records.  Note that,
   * although the exact number of Extended Textual File Header records may be a
   * useful piece of information, it will not always be known at the time the Binary
   * Header is written and it is not mandatory that a positive value be recorded
   * here.  This field is mandatory for all versions of SEG Y, although a value of
   * zero indicates "traditional" SEG Y conforming to the 1975 standard.
   *
   * @return  The number of 3200-byte extended records of the SEG Y file.
   */
  public int getNExtendedRecords()
  {
    return nExtendedRecords_;
  }

  /**
   * Return the max CMP fold of the SEG Y file.
   * <p>
   *
   *
   * @return  The of max CMP fold the SEG Y file.
   */
  public int getMaxCmpFold()
  {
    return maxCmpFold_;
  }

  /**
   * Return the of the SEG Y file.
   * <p>
   *
   *
   * @return  The of the SEG Y file.
   */
  public boolean isTracesCorrelated()
  {
    return isTracesCorrelated_;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();

    s.append("Job no.....................: ");
    s.append(jobNo_);
    s.append("\n");

    s.append("Line no....................: ");
    s.append(lineNo_);
    s.append("\n");

    s.append("Reel no....................: ");
    s.append(reelNo_);
    s.append("\n");

    s.append("# traces per shot..........: ");
    s.append(nTracesPerShot_);
    s.append("\n");

    s.append("Max CPM fold...............: ");
    s.append(maxCmpFold_);
    s.append("\n");

    s.append("# auxillary traces.........: ");
    s.append(nAuxillaryTraces_);
    s.append("\n");

    s.append("Sample rate................: ");
    s.append(sampleRate_);
    s.append(" \u00b5s");
    s.append("\n");

    s.append("Sample rate original.......: ");
    s.append(sampleRateOriginal_);
    s.append(" \u00b5s");
    s.append("\n");

    s.append("# samples..................: ");
    s.append(nSamples_);
    s.append("\n");

    s.append("# samples original.........: ");
    s.append(nSamplesOriginal_);
    s.append("\n");

    s.append("Data format................: ");
    s.append(dataFormat_);
    s.append("\n");

    s.append("CMP fold...................: ");
    s.append(cmpFold_);
    s.append("\n");

    s.append("Sorting....................: ");
    s.append(sorting_);
    s.append("\n");

    s.append("Vertical sum...............: ");
    s.append(verticalSum_);
    s.append("\n");

    s.append("Sweep frequency at start...: ");
    s.append(sweepFrequencyAtStart_);
    s.append(" Hz");
    s.append("\n");

    s.append("Sweep frequency at end.....: ");
    s.append(sweepFrequencyAtEnd_);
    s.append(" Hz");
    s.append("\n");

    s.append("Sweep length...............: ");
    s.append(sweepLength_);
    s.append(" ms");
    s.append("\n");

    s.append("Sweep type.................: ");
    s.append(sweepType_);
    s.append("\n");

    s.append("Sweep channel no...........: ");
    s.append(sweepChannelNo_);
    s.append("\n");

    s.append("Sweep taper length at start: ");
    s.append(sweepTaperLengthAtStart_);
    s.append(" ms");
    s.append("\n");

    s.append("Sweep taper length at end..: ");
    s.append(sweepTaperLengthAtEnd_);
    s.append(" ms");
    s.append("\n");

    s.append("Sweep taper type...........: ");
    s.append(sweepTaperType_);
    s.append("\n");

    s.append("Are traces correlated......: ");
    s.append(isTracesCorrelated_ ? "Yes" : "No");
    s.append("\n");

    s.append("Binary gain recovered......: ");
    s.append(isBinaryGainRecovery_ ? "Yes" : "No");
    s.append("\n");

    s.append("Amplitude recovery type....: ");
    s.append(amplitudeRecoveryType_);
    s.append("\n");

    s.append("Length unit................: ");
    s.append(lengthUnit_);
    s.append("\n");

    s.append("Impulse signal polarity....: ");
    s.append(impulseSignalPolarity_);
    s.append("\n");

    s.append("Vibratory polarity code.....: ");
    s.append(vibratoryPolarity_);
    s.append("\n");

    s.append("SEGY version...............: ");
    s.append(segyVersion_);
    s.append("\n");

    s.append("Trace fixed length.........: ");
    s.append(isTraceFixedLength_ ? "Yes" : "No");
    s.append("\n");

    s.append("# extended records.........: ");
    s.append(nExtendedRecords_);
    s.append("\n");

    return s.toString();
  }
}
