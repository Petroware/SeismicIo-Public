package no.petroware.seismicio.segy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Model a SEG Y trace header.
 *
 * @author <a href="mailto:info@petroware.no">Petroware AS</a>
 */
public final class SegyTraceHeader
{
  public enum TraceType
  {
    OTHER(-1, "Other"),
    UNKNOWN(0, "Unknown"),
    SEISMIC(1, "Seismic data"),
    DEAD(2, "Dead"),
    DUMMY(3, "Dummy"),
    TIMEBREAK(4, "Time break"),
    UPHOLE(5, "Uphole"),
    SWEEP(6, "Sweep"),
    TIMING(7, "Timing"),
    WATERBREAK(8, "Waterbreak"),
    NEARFIELD(9, "Near-field  gun signature"),
    FARFIELD(10, "Far-field gun signature"),
    SEISMIC_PRESSURE(11, "Seismic pressure sensor"),
    SENSOR_VERTICAL(12, "Multicomponent seismic sensor - Vertical component"),
    SENSOR_CROSSLINE(13, "Multicomponent seismic sensor - Cross-line component"),
    SENSOR_INLINE(14, "Multicomponent seismic sensor - In-line component"),
    ROT_SENSOR_VERTICAL(15, "Rotated multicomponent seismic sensor - Vertical component"),
    ROT_SENSOR_CROSSLINE(16, "Rotated multicomponent seismic sensor - Cross-line component"),
    ROT_SENSOR_INLINE(17, "Rotated multicomponent seismic sensor - In-line component"),
    VIBRATOR_MASS(18, "Vibrator reaction mass"),
    VIBRATOR_BASEPLATE(19, "Vibrator baseplate"),
    VIBRATOR_GROUND_FORCE(20, "Vibrator estimated ground force"),
    VIBRATOR_REFERENCE(21, "Vibrator reference"),
    TIME_VELOCITY(22, "Time-velocity pairs");

    private final int tag_;

    private final String description_;

    private TraceType(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

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

    public static TraceType get(int tag)
    {
      for (TraceType traceType : TraceType.values()) {
        if (traceType.tag_ == tag)
          return traceType;
      }

      // Not found
      return null;
    }
  }

  public enum DataUsage
  {
    UNKNOWN(0, "Unknown"),
    PRODUCTION(1, "Production"),
    TEST(2, "Test");

    private final int tag_;

    private final String description_;

    private DataUsage(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

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

    public static DataUsage get(int tag)
    {
      for (DataUsage dataUsage : DataUsage.values()) {
        if (dataUsage.tag_ == tag)
          return dataUsage;
      }

      // Not found
      return null;
    }
  }

  public enum CoordinateUnit
  {
    UNKNOWN(0, "Unknown"),
    LENGTH(1, "Length (meter or feet)"),
    ARCSECONDS(2, "Seconds of arc"),
    DEGREES(3, "Decimal degrees"),
    DMS(4, "DMS");

    private final int tag_;

    private final String description_;

    private CoordinateUnit(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

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

    public static CoordinateUnit get(int tag)
    {
      for (CoordinateUnit coordinateUnit : CoordinateUnit.values()) {
        if (coordinateUnit.tag_ == tag)
          return coordinateUnit;
      }

      // Not found
      return null;
    }
  }

  public enum GainType
  {
    UNKNOWN(0, "Unknown"),
    FIXED(1, "Fixed"),
    BINARY(2, "Binary"),
    FLOAT(3, "Float"),
    AGC(4, "AGC"),
    PGC(5, "PGC"),
    GANG(6, "Gang");

    private final int tag_;

    private final String description_;

    private GainType(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

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

    public static GainType get(int tag)
    {
      for (GainType gainType : GainType.values()) {
        if (gainType.tag_ == tag)
          return gainType;
      }

      // Not found
      return null;
    }
  }

  public enum TimeType
  {
    UNKNOWN(0, "Unknown"),
    LOCAL(1, "Local"),
    GMT(2, "GMT"),
    OTHER(3, "Other"),
    UTC(4, "UTC");

    private final int tag_;

    private final String description_;

    private TimeType(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

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

    public static TimeType get(int tag)
    {
      for (TimeType timeType : TimeType.values()) {
        if (timeType.tag_ == tag)
          return timeType;
      }

      // Not found
      return null;
    }
  }

  public enum Overtravel
  {
    UNKNOWN(0, "Unknown"),
    BEHIND(1, "Behind"),
    AHEAD(2, "Ahead");

    private final int tag_;

    private final String description_;

    private Overtravel(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

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

    public static Overtravel get(int tag)
    {
      for (Overtravel overtravel : Overtravel.values()) {
        if (overtravel.tag_ == tag)
          return overtravel;
      }

      // Not found
      return null;
    }
  }

  public enum TraceValueUnit
  {
    OTHER(-1, "Other"),
    UNKNOWN(0, "Unknown"),
    PASCAL(1, "Pa"),
    VOLTS(2, "V"),
    MILLIVOLTS(3, "mV"),
    AMPERES(4, "A"),
    METERS(5, "m"),
    METERS_PER_SECOND(6, "m/s"),
    METERS_PER_SECOND2(7, "m/s2"),
    NEWTON(8, "N"),
    WATT(9, "W");

    private final int tag_;

    private final String description_;

    private TraceValueUnit(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

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

    public static TraceValueUnit get(int tag)
    {
      for (TraceValueUnit traceValueUnit : TraceValueUnit.values()) {
        if (traceValueUnit.tag_ == tag)
          return traceValueUnit;
      }

      // Not found
      return null;
    }
  }

  public enum SourceUnit
  {
    OTHER(-1, "Other"),
    UNKNOWN(0, "Unknown"),
    PASCAL(1, "Pa"),
    VOLTS(2, "V"),
    MILLIVOLTS(3, "mV"),
    AMPERES(4, "A"),
    METERS(5, "m"),
    METERS_PER_SECOND(6, "m/s"),
    METERS_PER_SECOND2(7, "m/s2"),
    NEWTON(8, "N"),
    WATT(9, "W");

    private final int tag_;

    private final String description_;

    private SourceUnit(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

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

    public static SourceUnit get(int tag)
    {
      for (SourceUnit unit : SourceUnit.values()) {
        if (unit.tag_ == tag)
          return unit;
      }

      // Not found
      return null;
    }
  }

  public enum SourceType
  {
    OTHER(-1, "Other"),
    UNKNOWN(0, "Unknown"),
    VIBRATORY_VERTICAL(1, "Vibratory - Vertical orientation"),
    VIBRATORY_CROSSLINE(2, "Vibratory - Cross-line orientation"),
    VIBRATORY_INLINE(3, "Vibratory - In-line orientation"),
    IMPULSIVE_VERTICAL(4, "Impulsive - Vertical orientation"),
    IMPULSIVE_CROSSLINE(5, "Impulsive - Cross-line orientation"),
    IMPULSIVE_INLINE(6, "Impulsive - In-line orientation"),
    DISTRIBUTED_VERTICAL(7, "Distributed Impulsive - Vertical orientation"),
    DISTRIBUTED_CROSSLINE(8, "Distributed Impulsive - Cross-line orientation"),
    DISTRIBUTED_INLINE(9, "Distributed Impulsive - In-line orientation");

    private final int tag_;

    private final String description_;

    private SourceType(int tag, String description)
    {
      assert description != null : "description cannot be null";

      tag_ = tag;
      description_ = description;
    }

    int getTag()
    {
      return tag_;
    }

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

    public static SourceType get(int tag)
    {
      for (SourceType sourceType : SourceType.values()) {
        if (sourceType.tag_ == tag)
          return sourceType;
      }

      // Not found
      return null;
    }
  }

  private final int traceSequenceNo_;
  private final int traceSequenceNoReel_;
  private final int originalFieldRecordNo_;
  private final int originalFieldRecordTraceNo_;
  private final int energySourcePointNo_;
  private final int cmpEnsamble_;
  private final int ensambleTraceNo_;
  private final TraceType traceType_;
  private final int nVerticallySummedTraces_;
  private final int nHorizontallyStackedTraces_;
  private final DataUsage dataUsage_;
  private final int sourceReceiverDistance_;
  private final double receiverElevation_;
  private final double surfaceElevation_;
  private final double sourceDepth_;
  private final double datumElevationReceiver_;
  private final double datumElevationSource_;
  private final double waterDepthSource_;
  private final double waterDepthReceiver_;
  private final double x_;
  private final double y_;
  private final double xGroup_;
  private final double yGroup_;
  private final CoordinateUnit coordinateUnit_;
  private final int weatheringVelocity_;
  private final int subweatheringVelocity_;
  private final double upholeTimeAtSource_;
  private final double upholeTimeAtReceiver_;
  private final double sourceStaticsCorrection_;
  private final double groupStaticsCorrection_;
  private final double totalStaticsApplied_;
  private final double lagTimeA_;
  private final double lagTimeB_;
  private final double recordingTimeDelay_;
  private final double muteTimeStart_;
  private final double muteTimeEnd_;
  private final int nSamples_;
  private final int sampleInterval_;
  private final GainType fieldGainType_;
  private final int instrumentGainConstant_;
  private final int instrumentEarlyGain_;
  private final boolean isCorrelated_;
  private final int sweepFrequencyAtStart_;
  private final int sweepFrequencyAtEnd_;
  private final int sweepLength_;  // ms
  private final SegyFileHeader.SweepType sweepType_;
  private final int sweepTaperLengthAtStart_;
  private final int sweepTaperLengthAtEnd_;
  private final SegyFileHeader.SweepTaperType sweepTaperType_;
  private final int aliasFilterFrequency_;
  private final int aliasFilterSlope_;
  private final int notchFilterFrequency_;
  private final int notchFilterSlope_;
  private final int lowCutFrequency_;
  private final int highCutFrequency_;
  private final int lowCutSlope_;
  private final int highCutSlope_;
  private final int year_;
  private final int dayOfYear_;
  private final int hour_;
  private final int minute_;
  private final int second_;
  private final TimeType timeType_;
  private final int traceWeightingFactor_;
  private final int geophoneGroupNoOfRollSweepPosition_;
  private final int geophoneGroupNoOfOriginalTraceNo_;
  private final int geophoneGroupNoOfLastTrace_;
  private final int gapSize_;
  private final Overtravel overtravelTaper_;
  private final double xCdp_;
  private final double yCdp_;
  private final int inlineNo_;
  private final int crosslineNo_;
  private final double shotPoint_;
  private final TraceValueUnit traceValueUnit_;
  private final double transductionConstant_;
  private final TraceValueUnit transductionUnit_;
  private final int deviceId_;
  private final SourceType sourceTypeAndOrientation_;
  private final double[] sourceEnergyDirection_;
  private final double sourceMeasurement_;
  private final SourceUnit sourceUnit_;

  public SegyTraceHeader(int traceSequenceNo,
                         int traceSequenceNoReel,
                         int originalFieldRecordNo,
                         int originalFieldRecordTraceNo,
                         int energySourcePointNo,
                         int cmpEnsamble,
                         int ensambleTraceNo,
                         TraceType traceType,
                         int nVerticallySummedTraces,
                         int nHorizontallyStackedTraces,
                         DataUsage dataUsage,
                         int sourceReceiverDistance,
                         double receiverElevation,
                         double surfaceElevation,
                         double sourceDepth,
                         double datumElevationReceiver,
                         double datumElevationSource,
                         double waterDepthSource,
                         double waterDepthReceiver,
                         double x,
                         double y,
                         double xGroup,
                         double yGroup,
                         CoordinateUnit coordinateUnit,
                         int weatheringVelocity,
                         int subweatheringVelocity,
                         double upholeTimeAtSource,
                         double upholeTimeAtReceiver,
                         double sourceStaticsCorrection,
                         double groupStaticsCorrection,
                         double totalStaticsApplied,
                         double lagTimeA,
                         double lagTimeB,
                         double recordingTimeDelay,
                         double muteTimeStart,
                         double muteTimeEnd,
                         int nSamples,
                         int sampleInterval,
                         GainType fieldGainType,
                         int instrumentGainConstant,
                         int instrumentEarlyGain,
                         boolean isCorrelated,
                         int sweepFrequencyAtStart,
                         int sweepFrequencyAtEnd,
                         int sweepLength,
                         SegyFileHeader.SweepType sweepType,
                         int sweepTaperLengthAtStart,
                         int sweepTaperLengthAtEnd,
                         SegyFileHeader.SweepTaperType sweepTaperType,
                         int aliasFilterFrequency,
                         int aliasFilterSlope,
                         int notchFilterFrequency,
                         int notchFilterSlope,
                         int lowCutFrequency,
                         int highCutFrequency,
                         int lowCutSlope,
                         int highCutSlope,
                         int year,
                         int dayOfYear,
                         int hour,
                         int minute,
                         int second,
                         TimeType timeType,
                         int traceWeightingFactor,
                         int geophoneGroupNoOfRollSweepPosition,
                         int geophoneGroupNoOfOriginalTraceNo,
                         int geophoneGroupNoOfLastTrace,
                         int gapSize,
                         Overtravel overtravelTaper,
                         double xCdp,
                         double yCdp,
                         int inlineNo,
                         int crosslineNo,
                         double shotPoint,
                         TraceValueUnit traceValueUnit,
                         double transductionConstant,
                         TraceValueUnit transductionUnit,
                         int deviceId,
                         SourceType sourceTypeAndOrientation,
                         double[] sourceEnergyDirection,
                         double sourceMeasurement,
                         SourceUnit sourceUnit)
  {
    traceSequenceNo_ = traceSequenceNo;
    traceSequenceNoReel_ = traceSequenceNoReel;
    originalFieldRecordNo_ = originalFieldRecordNo;
    originalFieldRecordTraceNo_ = originalFieldRecordTraceNo;
    energySourcePointNo_ = energySourcePointNo;
    cmpEnsamble_ = cmpEnsamble;
    ensambleTraceNo_ = ensambleTraceNo;
    traceType_ = traceType;
    nVerticallySummedTraces_ = nVerticallySummedTraces;
    nHorizontallyStackedTraces_ = nHorizontallyStackedTraces;
    dataUsage_ = dataUsage;
    sourceReceiverDistance_ = sourceReceiverDistance;
    receiverElevation_ = receiverElevation;
    surfaceElevation_ = surfaceElevation;
    sourceDepth_ = sourceDepth;
    datumElevationReceiver_ = datumElevationReceiver;
    datumElevationSource_ = datumElevationSource;
    waterDepthSource_ = waterDepthSource;
    waterDepthReceiver_ = waterDepthReceiver;
    x_ = x;
    y_ = y;
    xGroup_ = xGroup;
    yGroup_ = yGroup;
    coordinateUnit_ = coordinateUnit;
    weatheringVelocity_ = weatheringVelocity;
    subweatheringVelocity_ = subweatheringVelocity;
    upholeTimeAtSource_ = upholeTimeAtSource;
    upholeTimeAtReceiver_ = upholeTimeAtReceiver;
    sourceStaticsCorrection_ = sourceStaticsCorrection;
    groupStaticsCorrection_ = groupStaticsCorrection;
    totalStaticsApplied_ = totalStaticsApplied;
    lagTimeA_ = lagTimeA;
    lagTimeB_ = lagTimeB;
    recordingTimeDelay_ = recordingTimeDelay;
    muteTimeStart_ = muteTimeStart;
    muteTimeEnd_ = muteTimeEnd;
    nSamples_ = nSamples;
    sampleInterval_ = sampleInterval;
    fieldGainType_ = fieldGainType;
    instrumentGainConstant_ = instrumentGainConstant;
    instrumentEarlyGain_ = instrumentEarlyGain;
    isCorrelated_ = isCorrelated;
    sweepFrequencyAtStart_ = sweepFrequencyAtStart;
    sweepFrequencyAtEnd_ = sweepFrequencyAtEnd;
    sweepLength_ = sweepLength;
    sweepType_ = sweepType;
    sweepTaperLengthAtStart_ = sweepTaperLengthAtStart;
    sweepTaperLengthAtEnd_ = sweepTaperLengthAtEnd;
    sweepTaperType_ = sweepTaperType;
    aliasFilterFrequency_ = aliasFilterFrequency;
    aliasFilterSlope_ = aliasFilterSlope;
    notchFilterFrequency_ = notchFilterFrequency;
    notchFilterSlope_ = notchFilterSlope;
    lowCutFrequency_ = lowCutFrequency;
    highCutFrequency_ = highCutFrequency;
    lowCutSlope_ = lowCutSlope;
    highCutSlope_ = highCutSlope;
    year_ = year;
    dayOfYear_ = dayOfYear;
    hour_ = hour;
    minute_ = minute;
    second_ = second;
    timeType_ = timeType;
    traceWeightingFactor_ = traceWeightingFactor;
    geophoneGroupNoOfRollSweepPosition_ = geophoneGroupNoOfRollSweepPosition;
    geophoneGroupNoOfOriginalTraceNo_ = geophoneGroupNoOfOriginalTraceNo;
    geophoneGroupNoOfLastTrace_ = geophoneGroupNoOfLastTrace;
    gapSize_ = gapSize;
    overtravelTaper_ = overtravelTaper;
    xCdp_ = xCdp;
    yCdp_ = yCdp;
    inlineNo_ = inlineNo;
    crosslineNo_ = crosslineNo;
    shotPoint_ = shotPoint;
    traceValueUnit_ = traceValueUnit;
    transductionConstant_ = transductionConstant;
    transductionUnit_ = transductionUnit;
    deviceId_ = deviceId;
    sourceTypeAndOrientation_ = sourceTypeAndOrientation;
    sourceEnergyDirection_ = sourceEnergyDirection.clone();
    sourceMeasurement_ = sourceMeasurement;
    sourceUnit_ = sourceUnit;
  }

  /**
   * Return the sequence number of this trace.
   * <p>
   * Trace sequence number within line - Numbers continue to increase if the same
   * line continues across multiple SEG Y files. Highly recommended for all types
   * of data.
   *
   * @return  The sequence number of this trace.
   */
  public int getTraceSequenceNo()
  {
    return traceSequenceNo_;
  }

  /**
   * Return the trace sequence number within the SEG Y file.
   * <p>
   * Trace sequence number within SEG Y file - Each file starts with trace
   * sequence one.
   *
   * @return  The trace sequence number within the SEG Y file.
   */
  public int getTraceSequenceNoReel()
  {
    return traceSequenceNoReel_;
  }

  /**
   * Return the original file record number of this trace.
   * <p>
   * Highly recommended for all types of data.
   *
   * @return  The original file record number of this trace.
   */
  public int getOriginalFieldRecordNo()
  {
    return originalFieldRecordNo_;
  }

  /**
   * Return the trace number within the original field record.
   * <p>
   * Highly recommended for all types of data.
   *
   * @return  The trace number within the original field record.
   */
  public int getOriginalFieldRecordTraceNo()
  {
    return originalFieldRecordTraceNo_;
  }

  /**
   * Return the energy point source number of this trace.
   * <p>
   * Used when more than one record occurs at the
   * same effective surface location.  It is recommended that
   * the new entry defined in Trace Header bytes 197-202 be used
   * for shotpoint number.
   *
   * @return  The energy point source number of this trace.
   */
  public int getEnergySourcePointNo()
  {
    return energySourcePointNo_;
  }

  /**
   * Return the ensamble number of this trace.
   * <p>
   * I.e. CDP, CMP, CRP, etc.
   *
   * @return  The ensamble number of this trace.
   */
  public int getCmpEnsamble()
  {
    return cmpEnsamble_;
  }

  /**
   * Return the trace number within the ensamble.
   * <p>
   * Each ensemble starts with trace number one.
   *
   * @return  The trace number within the ensamble.
   */
  public int getEnsambleTraceNo()
  {
    return ensambleTraceNo_;
  }

  /**
   * Return the type of this trace.
   * <p>
   * Highly recommended for all types of data.
   *
   * @return  The type of this trace.
   */
  public TraceType getTraceType()
  {
    return traceType_;
  }

  /**
   * Return the number of vertically summed traces yielding this trace.
   * <p>
   * 1 is one trace, 2 is two summed traces, etc.
   *
   * @return  The number of vertically summed traces yielding this trace.
   */
  public int getNVerticallySummedTraces()
  {
    return nVerticallySummedTraces_;
  }

  /**
   * Return the number of horizontally stacked traces yielding this trace.
   * <p>
   * 1 is one trace, 2 is two stacked traces, etc.
   *
   * @return  The number of horizontally stacked traces yielding this trace.
   */
  public int getNHorizontallyStackedTraces()
  {
    return nHorizontallyStackedTraces_;
  }

  /**
   * Return the data usage of this trace.
   *
   * @return  The data usage of this trace.
   */
  public DataUsage getDataUsage()
  {
    return dataUsage_;
  }

  /**
   * Return the source receiver distance of this trace.
   * <p>
   * Distance from center of the source point to the center of the receiver
   * group (negative if opposite to direction in which line is shot).
   *
   * @return  The source receiver distance of this trace.
   */
  public int getSourceReceiverDistance()
  {
    return sourceReceiverDistance_;
  }

  /**
   * Return the receiver elevation of this trace.
   * <p>
   * Receiver group elevation (all elevations above the
   * Vertical datum are positive and below are negative).
   *
   * @return  The receiver elevation of this trace.
   */
  public double getReceiverElevation()
  {
    return receiverElevation_;
  }

  /**
   * Return the surface elevation of this trace.
   * <p>
   * Surface elevation at source.
   *
   * @return  The surface elevation of this trace.
   */
  public double getSurfaceElevation()
  {
    return surfaceElevation_;
  }

  /**
   * Return the source depth of this trace.
   * <p>
   * Source depth below surface (a positive number).
   *
   * @return  The source depth of this trace.
   */
  public double getSourceDepth()
  {
    return sourceDepth_;
  }

  /**
   * Return the data elevation receiver of this trace.
   * <p>
   * This is the datum elevation at receiver group.
   *
   * @return  The datum elevation receiver of this trace.
   */
  public double getDatumElevationReceiver()
  {
    return datumElevationReceiver_;
  }

  /**
   * Return the datum elevation at source of this trace.
   *
   * @return  The datum elevation at source of this trace.
   */
  public double getDatumElevationSource()
  {
    return datumElevationSource_;
  }

  /**
   * Return the water depth at suorce of this trace.
   *
   * @return  The water depth at source of this trace.
   */
  public double getWaterDepthSource()
  {
    return waterDepthSource_;
  }

  /**
   * Return the water depth at receiver group of this trace.
   * <p>
   *
   *
   * @return  The water depth at receiver group of this trace.
   */
  public double getWaterDepthReceiver()
  {
    return waterDepthReceiver_;
  }

  /**
   * Return the source coordinate X of this trace.
   * <p>
   * The coordinate reference system should be
   * identified through an extended header Location
   * Data stanza (see section D-1).
   * <p>
   * If the coordinate units are in seconds of arc,
   * decimal degrees or DMS, the X values represent
   * longitude and the Y values latitude.  A positive
   * value designates east of Greenwich Meridian or
   * north of the equator and a negative value
   * designates south or west.
   *
   * @return  The source coordinate X of this trace.
   */
  public double getX()
  {
    return x_;
  }

  /**
   * Return the source coordinate Y of this trace.
   * <p>
   * The coordinate reference system should be
   * identified through an extended header Location
   * Data stanza (see section D-1).
   * <p>
   * If the coordinate units are in seconds of arc,
   * decimal degrees or DMS, the X values represent
   * longitude and the Y values latitude.  A positive
   * value designates east of Greenwich Meridian or
   * north of the equator and a negative value
   * designates south or west.
   *
   * @return  The source coordinate Y of this trace.
   */
  public double getY()
  {
    return y_;
  }

  /**
   * Return the group coordinate X of this trace.
   * <p>
   * The coordinate reference system should be
   * identified through an extended header Location
   * Data stanza (see section D-1).
   * <p>
   * If the coordinate units are in seconds of arc,
   * decimal degrees or DMS, the X values represent
   * longitude and the Y values latitude.  A positive
   * value designates east of Greenwich Meridian or
   * north of the equator and a negative value
   * designates south or west.
   *
   * @return  The group coordinate X of this trace.
   */
  public double getXGroup()
  {
    return xGroup_;
  }

  /**
   * Return the group coordinate Y of this trace.
   * <p>
   * The coordinate reference system should be
   * identified through an extended header Location
   * Data stanza (see section D-1).
   * <p>
   * If the coordinate units are in seconds of arc,
   * decimal degrees or DMS, the X values represent
   * longitude and the Y values latitude.  A positive
   * value designates east of Greenwich Meridian or
   * north of the equator and a negative value
   * designates south or west.
   *
   * @return  The group coordinate Y of this trace.
   */
  public double getYGroup()
  {
    return yGroup_;
  }

  /**
   * Return the coordinate unit of this trace.
   *
   * @return  The coordinate unit of this trace.
   */
  public CoordinateUnit getCoordinateUnit()
  {
    return coordinateUnit_;
  }

  /**
   * Return the weathering velocity of this trace.
   * <p>
   * Unit is length per second where the length unit
   * is according to SegtFileHEader.getLengthUnit().
   *
   * @return  The weathering velocity of this trace.
   */
  public int getWeatheringVelocity()
  {
    return weatheringVelocity_;
  }

  /**
   * Return the subweathering velocity of this trace.
   * <p>
   * Unit is length per second where the length unit
   * is according to SegtFileHEader.getLengthUnit().
   *
   * @return  The of this trace.
   */
  public int getSubweatheringVelocity()
  {
    return subweatheringVelocity_;
  }

  /**
   * Return the uphole time at source of this trace.
   *
   * @return  The uphole time at source of this trace. Milliseconds.
   */
  public double getUpholeTimeAtSource()
  {
    return upholeTimeAtSource_;
  }

  /**
   * Return the uphole time at group of this trace.
   * <p>
   *
   *
   * @return  The uphole time at group of this trace. Milliseconds.
   */
  public double getUpholeTimeAtReceiver()
  {
    return upholeTimeAtReceiver_;
  }

  /**
   * Return the source statics correction of this trace.
   *
   * @return  The source statics correction of this trace. Milliseconds.
   */
  public double getSourceStaticsCorrection()
  {
    return sourceStaticsCorrection_;
  }

  /**
   * Return the group static correction of this trace.
   *
   * @return  The group static correction of this trace. Milliseconds.
   */
  public double getGroupStaticsCorrection()
  {
    return groupStaticsCorrection_;
  }

  /**
   * Return the total static applied of this trace.
   * <p>
   * 0.0 of no static has been applied,
   *
   * @return  The total static applied of this trace.
   */
  public double getTotalStaticsApplied()
  {
    return totalStaticsApplied_;
  }

  /**
   * Return the lag time A of this trace.
   * <p>
   * Time in milliseconds between end of 240-byte trace identification header
   * and time break. The value is positive if time break occurs after the end
   * of header; negative if time break occurs before the end of header.
   * Time break is defined as the initiation pulse that may be recorded on an
   * auxiliary trace or as otherwise specified by the recording system.
   *
   * @return  The lag time A of this trace. Milliseconds.
   */
  public double getLagTimeA()
  {
    return lagTimeA_;
  }

  /**
   * Return the lag time B of this trace.
   * <p>
   * Time in milliseconds between time break and
   * the initiation time of the energy source.  May be positive or negative.
   *
   * @return  The lag time B of this trace. Milliseconds.
   */
  public double getLagTimeB()
  {
    return lagTimeB_;
  }

  /**
   * Return the recording time delay of this trace.
   * <p>
   * Time in milliseconds between initiation time of energy source and the
   * time when recording of data samples begins.  In SEG Y rev 0 this entry was
   * intended for deep-water work if data recording does not start at zero time.
   * The entry can be negative to accommodate negative start times
   * (i.e. data recorded before time zero, presumably as a result of static
   * application to the data trace).
   * If a non-zero value (negative or positive) is recorded in this
   * entry, a comment to that effect should appear in the Textual File Header.
   *
   * @return  The recording time delay of this trace. Milliseconds.
   */
  public double getRecordingTimeDelay()
  {
    return recordingTimeDelay_;
  }

  /**
   * Return the mute start time of this trace.
   *
   * @return  The mute start time of this trace. Milliseconds.
   */
  public double getMuteTimeStart()
  {
    return muteTimeStart_;
  }

  /**
   * Return the mute end time of this trace.
   *
   * @return  The mute end time of this trace. Milliseconds.
   */
  public double getMuteTimeEnd()
  {
    return muteTimeEnd_;
  }

  /**
   * Return the number of samples in this trace.
   *
   * @return  The number of samples in this trace. [0,&gt;.
   */
  public int getNSamples()
  {
    return nSamples_;
  }

  /**
   * Return the sample interval for this trace.
   * <p>
   * Microseconds for time data, Hertz (Hz) for frequency data,
   * meters (m) or feet (ft) for depth data.
   * The number of bytes in a trace record must be consistent
   * with the number of samples written in the Binary File Header
   * and/or the SEG-defined Trace Header(s). This is important for
   * all recording media; but it is particularly crucial for the
   * correct processing of SEG Y data in disk files.
   * <p>
   * If the fixed length trace flag of the Binary File Header is set,
   * the sample interval and number of samples in every trace in the
   * SEG Y file is assumed to be the same as the values recorded in the
   * Binary File Header and these fields are ignored.
   * If the fixed length trace flag is not set, the sample interval and
   * number of samples may vary from trace to trace.
   * Set this to zero if the value in bytes 97-100 of the optional SEG
   * Trace Header Extension 1 is nonzero.
   *
   * @return  The sample interval for this trace.
   */
  public int getSampleInterval()
  {
    return sampleInterval_;
  }

  /**
   * Return the gain type of field instruments for this trace.
   *
   * @return  The gain type of field instruments for this trace.
   */
  public GainType getFieldGainType()
  {
    return fieldGainType_;
  }

  /**
   * Return the instrument gain constant of this trace.
   *
   * @return  The instrument gain constant of this trace. dB.
   */
  public int getInstrumentGainConstant()
  {
    return instrumentGainConstant_;
  }

  /**
   * Return the instrument early or initial gain of this trace.
   *
   * @return  The instrument early or initial gain of this trace.
   */
  public int getInstrumentEarlyGain()
  {
    return instrumentEarlyGain_;
  }

  /**
   * Return if the trace is correlated the of this trace.
   *
   * @return  True if the trace is correlated, false otherwise.
   */
  public boolean isCorrelated()
  {
    return isCorrelated_;
  }

  /**
   * Return the sweep frequency at start of this trace.
   *
   * @return  The sweep frequency at start of this trace. Hz.
   */
  public int getSweepFrequencyAtStart()
  {
    return sweepFrequencyAtStart_;
  }

  /**
   * Return the sweep frequency at end of this trace.
   *
   * @return  The sweep frequency at end of this trace. Hz.
   */
  public int getSweepFrequencyAtEnd()
  {
    return sweepFrequencyAtEnd_;
  }

  /**
   * Return the sweep length of this trace.
   *
   * @return  The sweep length of this trace. Milliseconds.
   */
  public int getSweepLength()
  {
    return sweepLength_;
  }

  /**
   * Return the of this trace.
   * <p>
   *
   *
   * @return  The of this trace.
   */
  public SegyFileHeader.SweepType getSweepType()
  {
    return sweepType_;
  }

  /**
   * Return the sweep trace taper length at start of this trace.
   *
   * @return  The sweep trace taper length at start of this trace. Milliseconds.
   */
  public int getSweepTaperLengthAtStart()
  {
    return sweepTaperLengthAtStart_;
  }

  /**
   * Return the sweep trace taper length at end of this trace.
   *
   * @return  The sweep trace taper length at end of this trace.
   */
  public int getSweepTaperLengthAtEnd()
  {
    return sweepTaperLengthAtEnd_;
  }

  /**
   * Return the sweep taper type of this trace.
   *
   * @return  The sweep taper type of this trace.
   */
  public SegyFileHeader.SweepTaperType getSweepTaperType()
  {
    return sweepTaperType_;
  }

  /**
   * Return the alias filter frequency (if used) of this trace.
   *
   * @return  The alias filter frequency of this trace. Hz.
   */
  public int getAliasFilterFrequency()
  {
    return aliasFilterFrequency_;
  }

  /**
   * Return the alias filter slope of this trace.
   *
   * @return  The alias filter slope of this trace. dB/octave.
   */
  public int getAliasFilterSlope()
  {
    return aliasFilterSlope_;
  }

  /**
   * Return the notch filter frequency of this trace.
   *
   * @return  The notch filter frequency of this trace. Hz.
   */
  public int getNotchFilterFrequency()
  {
    return notchFilterFrequency_;
  }

  /**
   * Return the notch filter slope of this trace.
   *
   * @return  The notch filter slope of this trace. dB/octave.
   */
  public int getNotchFilterSlope()
  {
    return notchFilterSlope_;
  }

  /**
   * Return the low-cut frequency (if used) of this trace.
   *
   * @return  The low-cut frequency of this trace. Hz.
   */
  public int getLowCutFrequency()
  {
    return lowCutFrequency_;
  }

  /**
   * Return the high-cut frequency (if used) of this trace.
   *
   * @return  The high-cut frequency of this trace. Hz.
   */
  public int getHighCutFrequency()
  {
    return highCutFrequency_;
  }

  /**
   * Return the low-cut slope of this trace.
   *
   * @return  The low-cut slope of this trace. dB/octave.
   */
  public int getLowCutSlope()
  {
    return lowCutSlope_;
  }

  /**
   * Return the high-cut slope of this trace.
   *
   * @return  The high-cut slope of this trace.
   */
  public int getHighCutSlope()
  {
    return highCutSlope_;
  }

  /**
   * Return the time of recording of this trace.
   *
   * @return  The time of recording this trace. Null if not specified.
   */
  public Date getRecordingTime()
  {
    Calendar calendar = new GregorianCalendar();

    int year = year_ < 100 ? year_ + 1900 : year_;
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.DAY_OF_YEAR, dayOfYear_);
    calendar.set(Calendar.HOUR, hour_);
    calendar.set(Calendar.MINUTE, minute_);
    calendar.set(Calendar.SECOND, second_);

    return calendar.getTime();
  }

  /**
   * Return the time type of this trace.
   * <p>
   * If nonzero, overrides Binary File Header bytes 3517-3518.
   *
   * @return  The time type of this trace.
   */
  public TimeType getTimeType()
  {
    return timeType_;
  }

  /**
   * Return the trace weighting factor of this trace.
   * <p>
   * Defined as 2^-N volts for the least significant bit.
   * (N = 0, 1, ..., 32767)
   *
   * @return  The trace weighting factor of this trace.
   */
  public int getTraceWeightingFactor()
  {
    return traceWeightingFactor_;
  }

  /**
   * Return the geophone group no of roll sweep position of this trace.
   * <p>
   * This is the geophone group number of roll switch position one.
   *
   * @return  The geophone group no of roll sweep position of this trace.
   */
  public int getGeophoneGroupNoOfRollSweepPosition()
  {
    return geophoneGroupNoOfRollSweepPosition_;
  }

  /**
   * Return the geophone group no of original trace no of this trace.
   * <p>
   * This is the geophone group number of trace number one within original
   * field record.
   *
   * @return  The geophone group no of original trace no of this trace.
   */
  public int getGeophoneGroupNoOfOriginalTraceNo()
  {
    return geophoneGroupNoOfOriginalTraceNo_;
  }

  /**
   * Return the geophone group number of last trace of this trace.
   * <p>
   * This is the geophone group number of last trace within original field record.
   *
   * @return  The geophone group number of last trace of this trace.
   */
  public int getGeophoneGroupNoOfLastTrace()
  {
    return geophoneGroupNoOfLastTrace_;
  }

  /**
   * Return the gap size of this trace.
   * <p>
   * This is the total number of groups dropped.
   *
   * @return  The gap size of this trace.
   */
  public int getGapSize()
  {
    return gapSize_;
  }

  /**
   * Return the taper overtravel of this trace.
   * <p>
   * This is the over travel associated with taper at
   * beginning or end of line.
   *
   * @return  The taper overtravel of this trace.
   */
  public Overtravel getOvertravelTaper()
  {
    return overtravelTaper_;
  }

  /**
   * Return the CDP X coordinate of this trace.
   * <p>
   * X coordinate of ensemble (CDP) position of this trace
   * The coordinate reference system should be identified
   * through an extended header Location Data stanza.
   *
   * @return  The CDP X coordinate of this trace.
   */
  public double getXCdp()
  {
    return xCdp_;
  }

  /**
   * Return the CDP Y coordinate of this trace.
   * <p>
   * Y coordinate of ensemble (CDP) position of this trace
   * The coordinate reference system should be identified
   * through an extended header Location Data stanza.
   *
   * @return  The CDP Y coordinate of this trace.
   */
  public double getYCdp()
  {
    return yCdp_;
  }

  /**
   * Return the inline number of this trace.
   * <p>
   * For 3D poststack data, this field should be used for the inline number.
   * If one inline per SEG Y file is being recorded, this value should be the
   * same for all traces in the file and the same value will be recorded
   * in bytes 3205-3208 of the Binary File Header.
   *
   * @return  The inline number of this trace.
   */
  public int getInlineNo()
  {
    return inlineNo_;
  }

  /**
   * Return the crossline number of this trace.
   * <p>
   * For 3D poststack data, this field should be used for the crossline number.
   * This will typically be the same value as the ensemble (CDP)  number in Trace Header
   * bytes 21-24, but this does not have to be the case.
   *
   * @return  The crossline number of this trace.
   */
  public int getCrosslineNo()
  {
    return crosslineNo_;
  }

  /**
   * Return the shot point number of this trace.
   * <p>
   * This is probably only applicable to 2D poststack data. Note that it is
   * assumed that the shotpoint number refers to the source location nearest
   * to the ensemble (CDP) location for a particular trace.  If this is not the
   * case, there should be a comment in the Textual File Header explaining what
   * the shotpoint number actually refers to.
   *
   * @return  The shot point number of this trace.
   */
  public double getShotPoint()
  {
    return shotPoint_;
  }

  /**
   * Return unit for the trace values of this trace.
   *
   * @return  The unit for the trace values of this trace.
   */
  public TraceValueUnit getTraceValueUnit()
  {
    return traceValueUnit_;
  }

  /**
   * Return the transduction constant of this trace.
   * <p>
   * The multiplicative constant used to convert the Data Trace samples to the
   * Transduction Units (specified in Trace Header bytes 211-212).
   *
   * @return  The transduction constant of this trace.
   */
  public double getTransductionConstant()
  {
    return transductionConstant_;
  }

  /**
   * Return the transduction unit of this trace.
   * <p>
   * This is the unit of measurement of the Data Trace samples after
   * they have been multiplied by the Transduction Constant specified
   * in Trace Header bytes 205-210.
   *
   * @return  The transduction unit of this trace.
   */
  public TraceValueUnit getTransductionUnit()
  {
    return transductionUnit_;
  }

  /**
   * Return the device identifier of this trace.
   * <p>
   * The unit number or id number of the device associated with the Data Trace
   * (i.e. 4368 for vibrator serial number 4368 or 20316 for gun 16 on string
   * 3 on vessel 2).  This field allows traces to be associated across trace
   * ensembles independently of the trace number (Trace Header bytes 25-28).
   *
   * @return  The device identifier of this trace.
   */
  public int getDeviceId()
  {
    return deviceId_;
  }

  /**
   * Return the source type / orientation of this trace.
   * <p>
   * Defines the type and the orientation of the energy source.
   * The terms vertical, cross-line and in-line refer to the three axes of an
   * orthogonal coordinate system.  The absolute azimuthal orientation of the
   * coordinate system axes can be defined in the Bin Grid Definition Stanza.
   *
   * @return  The source type / orientation of this trace.
   */
  public SourceType getSourceTypeAndOrientation()
  {
    return sourceTypeAndOrientation_;
  }

  /**
   * Return the source energy direction of this trace.
   *
   * @return  The source energy direction of this trace.
   *          Array of three in degrees.
   */
  public double[] getSourceEnergyDirection()
  {
    return sourceEnergyDirection_.clone();
  }

  /**
   * Return the source measurement of this trace.
   * <p>
   * Describes the source effort used to generate the trace.
   * The measurement can be simple, qualitative measurements such as the total
   * weight of explosive used or the peak air gun pressure or the number of
   * vibrators times the sweep duration.  Although these simple measurements
   * are acceptable, it is preferable to use true measurement units of energy
   * or work.
   *
   * @return  The source measurement of this trace.
   */
  public double getSourceMeasurement()
  {
    return sourceMeasurement_;
  }

  /**
   * Return the unit of the source measurement of this trace.
   *
   * @return  The unit of the source measurement of this trace.
   */
  public SourceUnit getSourceUnit()
  {
    return sourceUnit_;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();

    s.append("Trace sequence no.......................: ");
    s.append(traceSequenceNo_);
    s.append("\n");

    s.append("Trace sequence no reel..................: ");
    s.append(traceSequenceNoReel_);
    s.append("\n");

    s.append("Original field record no................: ");
    s.append(originalFieldRecordNo_);
    s.append("\n");

    s.append("Original field record trace no..........: ");
    s.append(originalFieldRecordTraceNo_);
    s.append("\n");

    s.append("Energy source point no..................: ");
    s.append(energySourcePointNo_);
    s.append("\n");

    s.append("CMP ensamble............................: ");
    s.append(cmpEnsamble_);
    s.append("\n");

    s.append("Ensamble trace no.......................: ");
    s.append(ensambleTraceNo_);
    s.append("\n");

    s.append("Trace type..............................: ");
    s.append(traceType_);
    s.append("\n");

    s.append("# vertically summed traces..............: ");
    s.append(nVerticallySummedTraces_);
    s.append("\n");

    s.append("# horizontally stacked traces...........: ");
    s.append(nHorizontallyStackedTraces_);
    s.append("\n");

    s.append("Data usage..............................: ");
    s.append(dataUsage_);
    s.append("\n");

    s.append("Source receiver distance................: ");
    s.append(sourceReceiverDistance_);
    s.append("\n");

    s.append("Receiver elevation......................: ");
    s.append(receiverElevation_);
    s.append("\n");

    s.append("Surface elevation.......................: ");
    s.append(surfaceElevation_);
    s.append("\n");

    s.append("Source depth............................: ");
    s.append(sourceDepth_);
    s.append("\n");

    s.append("Datum elevation receiver................: ");
    s.append(datumElevationReceiver_);
    s.append("\n");

    s.append("Datum elevation source..................: ");
    s.append(datumElevationSource_);
    s.append("\n");

    s.append("Water depth source......................: ");
    s.append(waterDepthSource_);
    s.append("\n");

    s.append("Water depth receiver....................: ");
    s.append(waterDepthReceiver_);
    s.append("\n");

    s.append("X.......................................: ");
    s.append(x_);
    s.append("\n");

    s.append("Y.......................................: ");
    s.append(y_);
    s.append("\n");

    s.append("X group.................................: ");
    s.append(xGroup_);
    s.append("\n");

    s.append("Y group.................................: ");
    s.append(yGroup_);
    s.append("\n");

    s.append("Coordinate unit.........................: ");
    s.append(coordinateUnit_);
    s.append("\n");

    s.append("Weathering velocity.....................: ");
    s.append(weatheringVelocity_);
    s.append("\n");

    s.append("Subweathering velocity..................: ");
    s.append(subweatheringVelocity_);
    s.append("\n");

    s.append("Uphole time at source...................: ");
    s.append(upholeTimeAtSource_);
    s.append(" ms");
    s.append("\n");

    s.append("Uphole time at receiver.................: ");
    s.append(upholeTimeAtReceiver_);
    s.append(" ms");
    s.append("\n");

    s.append("Source statics correction...............: ");
    s.append(sourceStaticsCorrection_);
    s.append(" ms");
    s.append("\n");

    s.append("Group statics correction................: ");
    s.append(groupStaticsCorrection_);
    s.append(" ms");
    s.append("\n");

    s.append("Total statics applied...................: ");
    s.append(totalStaticsApplied_);
    s.append(" ms");
    s.append("\n");

    s.append("Lag time A..............................: ");
    s.append(lagTimeA_);
    s.append(" ms");
    s.append("\n");

    s.append("Lag time B..............................: ");
    s.append(lagTimeB_);
    s.append(" ms");
    s.append("\n");

    s.append("Recording time delay....................: ");
    s.append(recordingTimeDelay_);
    s.append(" ms");
    s.append("\n");

    s.append("Mute time start.........................: ");
    s.append(muteTimeStart_);
    s.append(" ms");
    s.append("\n");

    s.append("Mute time end...........................: ");
    s.append(muteTimeEnd_);
    s.append(" ms");
    s.append("\n");

    s.append("# samples...............................: ");
    s.append(nSamples_);
    s.append("\n");

    s.append("Sample interval.........................: ");
    s.append(sampleInterval_);
    s.append(" \u00b5s");
    s.append("\n");

    s.append("Field gain type.........................: ");
    s.append(fieldGainType_);
    s.append("\n");

    s.append("Instrument gain constant................: ");
    s.append(instrumentGainConstant_);
    s.append(" dB");
    s.append("\n");

    s.append("Instrument early gain...................: ");
    s.append(instrumentEarlyGain_);
    s.append(" dB");
    s.append("\n");

    s.append("Correlated..............................: ");
    s.append(isCorrelated_ ? "Yes" : "No");
    s.append("\n");

    s.append("Sweep frequency start...................: ");
    s.append(sweepFrequencyAtStart_);
    s.append(" Hz");
    s.append("\n");

    s.append("Sweep frequency end.....................: ");
    s.append(sweepFrequencyAtEnd_);
    s.append(" Hz");
    s.append("\n");

    s.append("Sweep length............................: ");
    s.append(sweepLength_);
    s.append(" ms");
    s.append("\n");

    s.append("Sweep type..............................: ");
    s.append(sweepType_);
    s.append("\n");

    s.append("Sweep taper length start................: ");
    s.append(sweepTaperLengthAtStart_);
    s.append(" ms");
    s.append("\n");

    s.append("Sweep taper length end..................: ");
    s.append(sweepTaperLengthAtEnd_);
    s.append(" ms");
    s.append("\n");

    s.append("Sweep taper type........................: ");
    s.append(sweepTaperType_);
    s.append("\n");

    s.append("Alias filter frequency..................: ");
    s.append(aliasFilterFrequency_);
    s.append(" Hz");
    s.append("\n");

    s.append("Alias filter slope......................: ");
    s.append(aliasFilterSlope_);
    s.append(" dB/octave");
    s.append("\n");

    s.append("Notch filter frequency..................: ");
    s.append(notchFilterFrequency_);
    s.append(" Hz");
    s.append("\n");

    s.append("Notch filter slope......................: ");
    s.append(notchFilterSlope_);
    s.append(" dB/octave");
    s.append("\n");

    s.append("Low cut frequency.......................: ");
    s.append(lowCutFrequency_);
    s.append(" Hz");
    s.append("\n");

    s.append("High cut frequency......................: ");
    s.append(highCutFrequency_);
    s.append(" Hz");
    s.append("\n");

    s.append("Low cut slope...........................: ");
    s.append(lowCutSlope_);
    s.append(" dB/octave");
    s.append("\n");

    s.append("High cut slope..........................: ");
    s.append(highCutSlope_);
    s.append(" dB/octave");
    s.append("\n");

    s.append("Year....................................: ");
    s.append(year_);
    s.append("\n");

    s.append("Day of year.............................: ");
    s.append(dayOfYear_);
    s.append("\n");

    s.append("Hour....................................: ");
    s.append(hour_);
    s.append("\n");

    s.append("Minute..................................: ");
    s.append(minute_);
    s.append("\n");

    s.append("Second..................................: ");
    s.append(second_);
    s.append("\n");

    s.append("Time basis..............................: ");
    s.append(timeType_);
    s.append("\n");

    s.append("Trace weighting factor..................: ");
    s.append(traceWeightingFactor_);
    s.append("\n");

    s.append("Geophone group no of roll sweep position: ");
    s.append(geophoneGroupNoOfRollSweepPosition_);
    s.append("\n");

    s.append("Geophone group no of original trace no..: ");
    s.append(geophoneGroupNoOfOriginalTraceNo_);
    s.append("\n");

    s.append("Geophone group no of last trace.........: ");
    s.append(geophoneGroupNoOfLastTrace_);
    s.append("\n");

    s.append("Gap size................................: ");
    s.append(gapSize_);
    s.append("\n");

    s.append("Overtravel taper........................: ");
    s.append(overtravelTaper_);
    s.append("\n");

    s.append("X (CDP).................................: ");
    s.append(xCdp_);
    s.append("\n");

    s.append("Y (CDP).................................: ");
    s.append(yCdp_);
    s.append("\n");

    s.append("Inline no...............................: ");
    s.append(inlineNo_);
    s.append("\n");

    s.append("Crossline no............................: ");
    s.append(crosslineNo_);
    s.append("\n");

    s.append("Shotpoint...............................: ");
    s.append(shotPoint_);
    s.append("\n");

    s.append("Trace value unit........................: ");
    s.append(traceValueUnit_);
    s.append("\n");

    s.append("Transduction constant...................: ");
    s.append(transductionConstant_);
    s.append("\n");

    s.append("Transduction unit.......................: ");
    s.append(transductionUnit_);
    s.append("\n");

    s.append("Device ID...............................: ");
    s.append(deviceId_);
    s.append("\n");

    s.append("Source type and orientation.............: ");
    s.append(sourceTypeAndOrientation_);
    s.append("\n");

    s.append("Source energy direction.................: ");
    s.append(sourceEnergyDirection_[0] + ","  + sourceEnergyDirection_[1] + "," + sourceEnergyDirection_[2]);
    s.append(" deg");
    s.append("\n");

    s.append("Source measurement......................: ");
    s.append(sourceMeasurement_);
    s.append("\n");

    s.append("Source unit.............................: ");
    s.append(sourceUnit_);
    s.append("\n");

    return s.toString();
  }
}
