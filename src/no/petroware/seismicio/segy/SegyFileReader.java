package no.petroware.seismicio.segy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.petroware.seismicio.util.FileChannelReader;

/**
 * Class for reading SEG Y seismic files.
 * <p>
 * See http://www.seg.org/documents/10161/77915/seg_y_rev1.pdf
 *
 * @author <a href="mailto:info@petroware.no">Petroware AS</a>
 */
public final class SegyFileReader
{
  /** The logger instance. */
  private static final Logger logger_ = Logger.getLogger(SegyFileReader.class.getName());

  /** The disk file back-end. Non-null. */
  private final File file_;

  /** SEG Y version of file. */
  // private final SegyVersion segyVersion_;

  /** The file input stream. */
  private FileInputStream fileInputStream_;

  /** The file reader. */
  private FileChannelReader reader_;

  /** Min data value. */
  private double minValue_;

  /** Max data value. */
  private double maxValue_;

  /**
   * Create a SEG Y file reader for the specified disk file.
   *
   * @param file  File to read. Non-null.
   * @throws IllegalArgumentException  If file is null.
   */
  public SegyFileReader(File file)
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    file_ = file;
    // segyVersion_ = SegyVersion.STANDARD;
    minValue_ = +Double.MAX_VALUE;
    maxValue_ = -Double.MAX_VALUE;
  }

  /**
   * Check if the specified file appears to be a SEG Y file.
   *
   * @param file     File to check. Non-null.
   * @param content  A number of bytes from the start of the file.
   *                 Null to classify on file name only.
   * @return Probability that the file is a SEG Y file. [0.0,1.0].
   * @throws IllegalArgumentException  If file is null.
   */
  public static double isSegyFile(File file, byte[] content)
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    if (!file.isFile())
      return 0.0;

    if (!file.exists())
      return 0.0;

    if (file.length() < 3440)
      return 0.0;

    String fileName = file.getName().toLowerCase(Locale.US);
    boolean isFileNameMatching = fileName.endsWith(".sgy") || fileName.endsWith(".segy");
    boolean isContentMatching = false;

    if (content != null) {
      isContentMatching = true;

      // TODO: Add tests here.

      if (isContentMatching && isFileNameMatching)
        return 0.95; // File name and content is matching
      else if (isContentMatching)
        return 0.10; // Content is matching, but file name is not
      else if (isFileNameMatching)
        return 0.95; // File name is matching, but content is not
    }

    if (isFileNameMatching)
      return 0.95;   // File name is matching. Content is not considered.
    else
      return 0.001;   // File name is not matching. Content is not considered.
  }

  /**
   * Return number of trackes in the file.
   *
   * @param fileHeader
   * @return
   */
  private int computeNTraces(SegyFileHeader fileHeader)
  {
    int fileSize = (int) file_.length();

    int dataSize = fileHeader.getDataFormat().getSize();
    int nSamplesPerTrace = fileHeader.getNSamples();
    int nBytesPerTrace = nSamplesPerTrace * dataSize;

    return (fileSize - SegyTextHeader.LENGTH - 400) / (nBytesPerTrace + 240);
  }

  private void skipTraceData(SegyFileHeader fileHeader)
    throws IOException
  {
    int nBytes = fileHeader.getNSamples() * fileHeader.getDataFormat().getSize();
    reader_.skip(nBytes);
  }

  /**
   * Check if the specified array of character data appears to be EBCDIC
   * more than ASCII.
   * <p>
   * EBCDIC has alphabetic characters and numerals in the [128,255] interval,
   * which when converted to Java bytes will appear as negative. ASCII will
   * have no negative byte values.
   *
   * @param bytes  Bytes to check. Non null.
   * @return       True if the bytes appears to be EBCDIC more than ASCII.
   */
  private static boolean isEbcdic(byte[] bytes)
  {
    assert bytes != null : "bytes cannot be null";

    for (int i = 0; i < bytes.length; i++)
      if (bytes[i] < 0)
        return true;

    return false;
  }

  /**
   * Read the 3200 byte text header of the SEG Y file.
   *
   * @return  The text header of the SEG Y file. Never null.
   */
  private SegyTextHeader readTextHeader()
    throws IOException
  {
    byte[] textHeader = new byte[SegyTextHeader.LENGTH];

    reader_.get(textHeader);

    // Check if the header seems to be EBCDIC or ASCII
    String characterSet = isEbcdic(textHeader) ? "IBM500" : "ISO-8859-1";

    // Create the associated text header object
    return new SegyTextHeader(new String(textHeader, characterSet));
  }

  /**
   * Read the 400 byte file header at the current file position.
   *
   * @return  The file header instance. Never null.
   */
  private SegyFileHeader readFileHeader()
    throws IOException
  {
    // 1-4
    int jobNo = reader_.getInt();

    // 5-8. For 3-D poststack data, this will typically contain the in-line number.
    int lineNo = reader_.getInt();

    // 9-12.
    int reelNo = reader_.getInt();

    // 13-14. Number of data traces per ensemble. Mandatory for prestack data.
    int nTracesPerShot = reader_.getShort();

    // 15-16. Number of auxiliary traces per ensemble. Mandatory for prestack data.
    int nAuxillaryTraces = reader_.getShort();

    // 17-18. Sample interval in microseconds (micro s). Mandatory for all data types.
    int sampleRate = reader_.getShort();

    // 19-20. Sample interval in microseconds (micro s) of original field recording.
    int sampleRateOriginal = reader_.getShort();

    // 21-22.
    // Number of samples per data trace.  Mandatory for all types of data.
    // Note: The sample interval and number of samples in the Binary File Header
    // should be for the primary set of seismic data traces in the file.
    int nSamples = reader_.getShort();

    // 23-24. Number of samples per data trace for original field recording.
    int nSamplesOriginal = reader_.getShort();

    // 25-26. Data sample format code. Mandatory for all data.
    int dataFormatTag = reader_.getShort();
    SegyFileHeader.DataFormat dataFormat = SegyFileHeader.DataFormat.get(dataFormatTag);
    if (dataFormat == null) {
      logger_.log(Level.WARNING, "Unexpected data format value: " + dataFormatTag + ". Using UNKNOWN instead");
      dataFormat = SegyFileHeader.DataFormat.UNKNOWN;
    }

    // 27-28. Ensemble fold — The expected number of data traces per trace ensemble
    //(e.g. the CMP fold). Highly recommended for all types of data.
    int cmpFold = reader_.getShort();

    // 29-30.
    int sortingTag = reader_.getShort();
    SegyFileHeader.Sorting sorting = SegyFileHeader.Sorting.get(sortingTag);
    if (sorting == null) {
      logger_.log(Level.WARNING, "Unexpected sorting value: " + sortingTag + ". Using UNKNOWN instead");
      sorting = SegyFileHeader.Sorting.UNKNOWN;
    }

    // 31-32.
    int verticalSum = reader_.getShort();

    // 33-34.
    int sweepFrequencyAtStart = reader_.getShort();

    // 35-36.
    int sweepFrequencyAtEnd = reader_.getShort();

    // 37-38.
    int sweepLength = reader_.getShort();

    // 39-40.
    int sweepTypeTag = reader_.getShort();
    SegyFileHeader.SweepType sweepType = SegyFileHeader.SweepType.get(sweepTypeTag);
    if (sweepType == null) {
      logger_.log(Level.WARNING, "Unexpected sweep type value: " + sweepTypeTag + ". Using UNKNOWN instead");
      sweepType = SegyFileHeader.SweepType.UNKNOWN;
    }

    // 41-42.
    int sweepChannelNo = reader_.getShort();

    // 43-44.
    int sweepTaperLengthAtStart = reader_.getShort();

    // 45-46.
    int sweepTaperLengthAtEnd = reader_.getShort();

    // 47-48.
    int sweepTaperTypeTag = reader_.getShort();
    SegyFileHeader.SweepTaperType sweepTaperType = SegyFileHeader.SweepTaperType.get(sweepTaperTypeTag);
    if (sweepTaperType == null) {
      logger_.log(Level.WARNING, "Unexpected sweep taper type value: " + sweepTaperTypeTag + ". Using UNKNOWN instead");
      sweepTaperType = SegyFileHeader.SweepTaperType.UNKNOWN;
    }

    // 49-50.
    int isTracesCorrelated = reader_.getShort();

    // 51-52.
    int isBinaryGainRecoveryTag = reader_.getShort();
    boolean isBinaryGainRecovery  = isBinaryGainRecoveryTag == 1;

    // 53-54.
    int amplitudeRecoveryTypeTag = reader_.getShort();
    SegyFileHeader.AmplitudeRecoveryType amplitudeRecoveryType = SegyFileHeader.AmplitudeRecoveryType.get(amplitudeRecoveryTypeTag);
    if (amplitudeRecoveryType == null) {
      logger_.log(Level.WARNING, "Unexpected amplitude recovery type value: " + amplitudeRecoveryTypeTag + ". Using OTHER instead");
      amplitudeRecoveryType = SegyFileHeader.AmplitudeRecoveryType.OTHER;
    }

    // 55-56.
    int lengthUnitTag = reader_.getShort();
    SegyFileHeader.LengthUnit lengthUnit = SegyFileHeader.LengthUnit.get(lengthUnitTag);
    if (lengthUnit == null) {
      logger_.log(Level.WARNING, "Unexpected length unit value: " + lengthUnitTag + ". Using UNKNOWN instead");
      lengthUnit = SegyFileHeader.LengthUnit.UNKNOWN;
    }

    // 57-58.
    int impulseSignalPolarityTag = reader_.getShort();
    SegyFileHeader.ImpulseSignalPolarity impulseSignalPolarity = SegyFileHeader.ImpulseSignalPolarity.get(impulseSignalPolarityTag);
    if (impulseSignalPolarity == null) {
      logger_.log(Level.WARNING, "Unexpected polarity value: " + impulseSignalPolarityTag + ". Using UNKNOWN instead");
      impulseSignalPolarity = SegyFileHeader.ImpulseSignalPolarity.UNKNOWN;
    }

    // 59-60.
    int vibratoryPolarityTag = reader_.getShort();
    SegyFileHeader.VibratoryPolarity vibratoryPolarity = SegyFileHeader.VibratoryPolarity.get(vibratoryPolarityTag);
    if (vibratoryPolarity == null) {
      logger_.log(Level.WARNING, "Unexpected vibratory polarity value: " + vibratoryPolarityTag + ". Assume 0.0 degrees.");
      vibratoryPolarity = SegyFileHeader.VibratoryPolarity.get(0.0);
    }

    // 261-500. Unassigned.
    reader_.skip(240);

    // 501-502.
    int segyVersionTag = reader_.getShort();
    SegyVersion segyVersion = SegyVersion.get(segyVersionTag);
    if (segyVersion == null) {
      logger_.log(Level.WARNING, "Unexpected SEG Y version value: " + segyVersionTag + ". Using STANDARD instead");
      segyVersion = SegyVersion.STANDARD;
    }

    // 503-504.
    int isTraceFixedLengthTag = reader_.getShort();
    boolean isTraceFixedLength = isTraceFixedLengthTag == 1;

    // 505-506. Number of 3200-byte, Extended Textual File Header records following the
    //          Binary Header.
    int nExtendedRecords = reader_.getShort();

    // 507-600. Unassigned.
    reader_.skip(94);

    SegyFileHeader fileHeader = new SegyFileHeader(jobNo,
                                                   lineNo,
                                                   reelNo,
                                                   nTracesPerShot,
                                                   nAuxillaryTraces,
                                                   sampleRate,
                                                   sampleRateOriginal,
                                                   nSamples,
                                                   nSamplesOriginal,
                                                   dataFormat,
                                                   cmpFold,
                                                   sorting,
                                                   verticalSum,
                                                   sweepFrequencyAtStart,
                                                   sweepFrequencyAtEnd,
                                                   sweepLength,
                                                   sweepType,
                                                   sweepChannelNo,
                                                   sweepTaperLengthAtStart,
                                                   sweepTaperLengthAtEnd,
                                                   sweepTaperType,
                                                   isBinaryGainRecovery,
                                                   amplitudeRecoveryType,
                                                   lengthUnit,
                                                   impulseSignalPolarity,
                                                   vibratoryPolarity,
                                                   segyVersion,
                                                   isTraceFixedLength,
                                                   nExtendedRecords);

    return fileHeader;
  }

  /**
   * Read trace header at the current file location.
   *
   * @return  Trace header read. Never null.
   */
  private SegyTraceHeader readTraceHeader()
    throws IOException
  {
    // Byte 1-4
    int traceSequenceNo = reader_.getInt();

    // Byte 5-8
    int traceSequenceNoReel = reader_.getInt();

    // Byte 9-12
    int originalFieldRecordNo = reader_.getInt();

    // Byte 13-16
    int originalFieldRecordTraceNo = reader_.getInt();

    // Byte 17-20
    int energySourcePointNo = reader_.getInt();

    // Byte 21-24
    int cmpEnsamble = reader_.getInt();

    // Byte 25-28
    int ensambleTraceNo = reader_.getInt();

    // Byte 29-30
    int traceTypeTag = reader_.getShort();
    SegyTraceHeader.TraceType traceType = SegyTraceHeader.TraceType.get(traceTypeTag);
    if (traceType == null) {
      logger_.log(Level.WARNING, "Unexpected trace type value: " + traceTypeTag + ". Using UNKNOWN instead");
      traceType = SegyTraceHeader.TraceType.UNKNOWN;
    }

    // Byte 31-32
    int nVerticallySummedTraces = reader_.getShort();

    // Byte 33-34
    int nHorizontallyStackedTraces = reader_.getShort();

    // Byte 35-36
    int dataUsageTag = reader_.getShort();
    SegyTraceHeader.DataUsage dataUsage = SegyTraceHeader.DataUsage.get(dataUsageTag);
    if (dataUsage == null) {
      logger_.log(Level.WARNING, "Unexpected data usage value: " + dataUsageTag + ". Using UNKNOWN instead");
      dataUsage = SegyTraceHeader.DataUsage.UNKNOWN;
    }

    // Byte 37-40
    int sourceReceiverDistance = reader_.getInt();

    // Byte 41-68
    int receiverElevationInt = reader_.getInt();
    int surfaceElevationInt = reader_.getInt();
    int sourceDepthInt = reader_.getInt();
    int datumElevationReceiverInt = reader_.getInt();
    int datumElevationSourceInt = reader_.getInt();
    int waterDepthSourceInt = reader_.getInt();
    int waterDepthReceiverInt = reader_.getInt();

    // Byte 69-70
    int scaleFactorElevation = reader_.getShort();
    if (scaleFactorElevation == 0) // TODO: Not strictly according to documentation
      scaleFactorElevation = 1;    // but it works!

    double receiverElevation = scaleFactorElevation >= 0 ?
                               (double) receiverElevationInt * scaleFactorElevation :
                               (double) receiverElevationInt / -scaleFactorElevation;
    double surfaceElevation = scaleFactorElevation >= 0 ?
                              (double) surfaceElevationInt * scaleFactorElevation :
                               (double) surfaceElevationInt / -scaleFactorElevation;
    double sourceDepth = scaleFactorElevation >= 0 ?
                         (double) sourceDepthInt * scaleFactorElevation :
                               (double) sourceDepthInt / -scaleFactorElevation;
    double datumElevationReceiver = scaleFactorElevation >= 0 ?
                                    (double) datumElevationReceiverInt * scaleFactorElevation :
                               (double) datumElevationReceiverInt / -scaleFactorElevation;
    double datumElevationSource = scaleFactorElevation >= 0 ?
                                  (double) datumElevationSourceInt * scaleFactorElevation :
                               (double) datumElevationSourceInt / -scaleFactorElevation;
    double waterDepthSource = scaleFactorElevation >= 0 ?
                              (double) waterDepthSourceInt * scaleFactorElevation :
                               (double) waterDepthSourceInt / -scaleFactorElevation;
    double waterDepthReceiver = scaleFactorElevation >= 0 ?
                                (double) waterDepthReceiverInt * scaleFactorElevation :
                                (double) waterDepthReceiverInt / -scaleFactorElevation;

    // Byte 71-72
    int scaleFactorCoordinates = reader_.getShort();
    if (scaleFactorCoordinates == 0) // TODO: Not strictly according to documentation
      scaleFactorCoordinates = 1;    // but it works!

    // Byte 73-76
    int xInt = reader_.getInt();
    double x = scaleFactorCoordinates >= 0 ?
               (double) xInt * scaleFactorCoordinates :
               (double) xInt / -scaleFactorCoordinates;

    // Byte 77-80
    int yInt = reader_.getInt();
    double y = scaleFactorCoordinates >= 0 ?
               (double) yInt * scaleFactorCoordinates :
               (double) yInt / -scaleFactorCoordinates;

    // Byte 81-84
    int xGroupInt = reader_.getInt();
    double xGroup = scaleFactorCoordinates >= 0 ?
                    (double) xGroupInt * scaleFactorCoordinates :
                    (double) xGroupInt / -scaleFactorCoordinates;

    // Byte 85-88
    int yGroupInt = reader_.getInt();
    double yGroup = scaleFactorCoordinates >= 0 ?
                    (double) yGroupInt * scaleFactorCoordinates :
                    (double) yGroupInt / -scaleFactorCoordinates;

    // Byte 89-90
    int coordinateUnitTag = reader_.getShort();
    SegyTraceHeader.CoordinateUnit coordinateUnit = SegyTraceHeader.CoordinateUnit.get(coordinateUnitTag);
    if (coordinateUnit == null) {
      logger_.log(Level.WARNING, "Unexpected coordinate unit value: " + coordinateUnitTag + ". Using UNKNOWN instead");
      coordinateUnit = SegyTraceHeader.CoordinateUnit.UNKNOWN;
    }

    // Byte 91-92
    int weatheringVelocity = reader_.getShort();

    // Byte 93-94
    int subweatheringVelocity = reader_.getShort();

    // Byte 95-114
    int upholeTimeAtSourceInt = reader_.getShort();
    int upholeTimeAtReceiverInt = reader_.getShort();
    int sourceStaticsCorrectionInt = reader_.getShort();
    int groupStaticsCorrectionInt = reader_.getShort();
    int totalStaticsAppliedInt = reader_.getShort();
    int lagTimeAInt = reader_.getShort();
    int lagTimeBInt = reader_.getShort();
    int recordingTimeDelayInt = reader_.getShort();
    int muteTimeStartInt = reader_.getShort();
    int muteTimeEndInt = reader_.getShort();

    // Byte 115-116
    int nSamples = reader_.getShort();

    // Byte 117-118
    int sampleInterval = reader_.getShort();

    // Byte 119-120
    int fieldGainTypeTag = reader_.getShort();
    SegyTraceHeader.GainType fieldGainType = SegyTraceHeader.GainType.get(fieldGainTypeTag);
    if (fieldGainType == null) {
      logger_.log(Level.WARNING, "Unexpected field gain type value: " + fieldGainTypeTag + ". Using UNKNOWN instead");
      fieldGainType = SegyTraceHeader.GainType.UNKNOWN;
    }

    // Byte 121-122
    int instrumentGainConstant = reader_.getShort();

    // Byte 123-124
    int instrumentEarlyGain = reader_.getShort();

    // Byte 125-126
    int isCorrelatedTag = reader_.getShort();
    boolean isCorrelated = isCorrelatedTag == 2;

    // Byte 127-128
    int sweepFrequencyAtStart = reader_.getShort();

    // Byte 129-130
    int sweepFrequencyAtEnd = reader_.getShort();

    // Byte 131-132
    int sweepLength = reader_.getShort();

    // Byte 133-134
    int sweepTypeTag = reader_.getShort();
    SegyFileHeader.SweepType sweepType = SegyFileHeader.SweepType.get(sweepTypeTag);
    if (sweepType == null) {
      logger_.log(Level.WARNING, "Unexpected sweep type value: " + sweepTypeTag + ". Using UNKNOWN instead");
      sweepType = SegyFileHeader.SweepType.UNKNOWN;
    }

    // Byte 135-136
    int sweepTaperLengthAtStart = reader_.getShort();

    // Byte 137-138
    int sweepTaperLengthAtEnd = reader_.getShort();

    // Byte 139-140
    int sweepTaperTypeTag = reader_.getShort();
    SegyFileHeader.SweepTaperType sweepTaperType = SegyFileHeader.SweepTaperType.get(sweepTaperTypeTag);
    if (sweepTaperType == null) {
      logger_.log(Level.WARNING, "Unexpected sweep taper type value: " + sweepTaperTypeTag + ". Using UNKNOWN instead");
      sweepTaperType = SegyFileHeader.SweepTaperType.UNKNOWN;
    }

    // Byte 141-142
    int aliasFilterFrequency = reader_.getShort();

    // Byte 143-144
    int aliasFilterSlope = reader_.getShort();

    // Byte 145-146
    int notchFilterFrequency = reader_.getShort();

    // Byte 147-148
    int notchFilterSlope = reader_.getShort();

    // Byte 149-150
    int lowCutFrequency = reader_.getShort();

    // Byte 151-152
    int highCutFrequency = reader_.getShort();

    // Byte 153-154
    int lowCutSlope = reader_.getShort();

    // Byte 155-156
    int  highCutSlope = reader_.getShort();

    // Byte 157-158
    int year = reader_.getShort();

    // Byte 159-160
    int dayOfYear = reader_.getShort();

    // Byte 161-162
    int hour = reader_.getShort();

    // Byte 163-164
    int minute = reader_.getShort();

    // Byte 165-166
    int second = reader_.getShort();

    // Byte 167-168
    int timeTypeTag = reader_.getShort();
    SegyTraceHeader.TimeType timeType = SegyTraceHeader.TimeType.get(timeTypeTag);
    if (timeType == null) {
      logger_.log(Level.WARNING, "Unexpected time type value: " + timeTypeTag + ". Using UNKNOWN instead");
      timeType = SegyTraceHeader.TimeType.UNKNOWN;
    }

    // Byte 169-170
    int traceWeightingFactor = reader_.getShort();

    // Byte 171-172
    int geophoneGroupNoOfRollSweepPosition = reader_.getShort();

    // Byte 173-174
    int geophoneGroupNoOfOriginalTraceNo = reader_.getShort();

    // Byte 175-176
    int geophoneGroupNoOfLastTrace = reader_.getShort();

    // Byte 177-178
    int gapSize = reader_.getShort();

    // Byte 179-180
    int overtravelTaperTag = reader_.getShort();
    SegyTraceHeader.Overtravel overtravelTaper = SegyTraceHeader.Overtravel.get(overtravelTaperTag);
    if (overtravelTaper == null) {
      logger_.log(Level.WARNING, "Unexpected overtravel value: " + overtravelTaperTag + ". Using UNKNOWN instead");
      overtravelTaper = SegyTraceHeader.Overtravel.UNKNOWN;
    }

    // Byte 181-184
    int xCdpInt = reader_.getInt();
    double xCdp = scaleFactorCoordinates > 0 ?
                  (double) xCdpInt * scaleFactorCoordinates :
                  (double) xCdpInt / -scaleFactorCoordinates;

    // Byte 185-188
    int yCdpInt = reader_.getInt();
    double yCdp = scaleFactorCoordinates > 0 ?
                  (double) yCdpInt * scaleFactorCoordinates :
                  (double) yCdpInt / -scaleFactorCoordinates;

    // Byte 189-192
    int inlineNo = reader_.getInt();

    // Byte 193-196
    int crosslineNo = reader_.getInt();

    // Byte 197-202
    int sp = reader_.getInt();
    int spModifier = reader_.getShort();
    double shotPoint = spModifier == 0 ? sp :
                       spModifier > 0 ? (double) sp * spModifier : (double) sp / -spModifier;

    // Byte 203-204
    int traceValueUnitTag = reader_.getShort();
    SegyTraceHeader.TraceValueUnit traceValueUnit = SegyTraceHeader.TraceValueUnit.get(traceValueUnitTag);
    if (traceValueUnit == null) {
      logger_.log(Level.WARNING, "Unexpected trace value unit value: " + traceValueUnitTag + ". Using UNKNOWN instead");
      traceValueUnit = SegyTraceHeader.TraceValueUnit.UNKNOWN;
    }

    // Byte 205-210
    int transductionMantissa = reader_.getInt();
    int transductionExponent = reader_.getShort();
    double transductionConstant = transductionMantissa * Math.pow(10, transductionExponent);

    // Byte 211-212
    int transductionUnitTag = reader_.getShort();
    SegyTraceHeader.TraceValueUnit transductionUnit = SegyTraceHeader.TraceValueUnit.get(transductionUnitTag);
    if (transductionUnit == null) {
      logger_.log(Level.WARNING, "Unexpected unit value: " + transductionUnitTag + ". Using UNKNOWN instead");
      transductionUnit = SegyTraceHeader.TraceValueUnit.UNKNOWN;
    }

    // Byte 213-214
    int deviceId = reader_.getShort();

    // Byte 215-216
    int timeModifier = reader_.getShort();
    if (timeModifier == 0) // According to the documentation, page 19.
      timeModifier = 1;

    double upholeTimeAtSource = timeModifier > 0 ?
                                (double) upholeTimeAtSourceInt * timeModifier :
                                (double) upholeTimeAtSourceInt / -timeModifier;
    double upholeTimeAtReceiver = timeModifier > 0 ?
                                  (double) upholeTimeAtReceiverInt * timeModifier :
                                  (double) upholeTimeAtReceiverInt / -timeModifier;
    double sourceStaticsCorrection = timeModifier > 0 ?
                                     (double) sourceStaticsCorrectionInt * timeModifier :
                                     (double) sourceStaticsCorrectionInt / -timeModifier;
    double groupStaticsCorrection = timeModifier > 0 ?
                                    (double) groupStaticsCorrectionInt * timeModifier :
                                    (double) groupStaticsCorrectionInt / -timeModifier;
    double totalStaticsApplied = timeModifier > 0 ?
                                 (double) totalStaticsAppliedInt * timeModifier :
                                 (double) totalStaticsAppliedInt / -timeModifier;
    double lagTimeA = timeModifier > 0 ?
                      (double) lagTimeAInt * timeModifier :
                      (double) lagTimeAInt / -timeModifier;
    double lagTimeB = timeModifier > 0 ?
                                (double) lagTimeBInt * timeModifier :
                      (double) lagTimeBInt / -timeModifier;
    double recordingTimeDelay = timeModifier > 0 ?
                                (double) recordingTimeDelayInt * timeModifier :
                                (double) recordingTimeDelayInt / -timeModifier;
    double muteTimeStart = timeModifier > 0 ?
                                (double) muteTimeStartInt * timeModifier :
                           (double) muteTimeStartInt / -timeModifier;
    double muteTimeEnd = timeModifier > 0 ?
                                (double) muteTimeEndInt * timeModifier :
                         (double) muteTimeEndInt / -timeModifier;

    // Byte 217-218
    int sourceTypeAndOrientationTag = reader_.getShort();
    SegyTraceHeader.SourceType sourceTypeAndOrientation = SegyTraceHeader.SourceType.get(sourceTypeAndOrientationTag);
    if (sourceTypeAndOrientation == null) {
      logger_.log(Level.WARNING, "Unexpected source type value: " + sourceTypeAndOrientationTag + ". Using UNKNOWN instead");
      sourceTypeAndOrientation = SegyTraceHeader.SourceType.UNKNOWN;
    }

    // Byte 219-224
    int sourceEnergyDirectionCode1 = reader_.getShort();
    int sourceEnergyDirectionCode2 = reader_.getShort();
    int sourceEnergyDirectionCode3 = reader_.getShort();
    double[] sourceEnergyDirection = new double[] {sourceEnergyDirectionCode1 / 10.0,
                                                   sourceEnergyDirectionCode2 / 10.0,
                                                   sourceEnergyDirectionCode3 / 10.0};

    // Byte 225-230
    int sourceMeasurementMantissa = reader_.getInt();
    int sourceMeasurementExponent = reader_.getShort();
    double sourceMeasurement = sourceMeasurementMantissa * Math.pow(10, sourceMeasurementExponent);

    // Byte 231-232
    int sourceUnitTag = reader_.getShort();
    SegyTraceHeader.SourceUnit sourceUnit = SegyTraceHeader.SourceUnit.get(sourceUnitTag);
    if (sourceUnit == null) {
      logger_.log(Level.WARNING, "Unexpected source unit value: " + sourceUnitTag + ". Using UNKNOWN instead");
      sourceUnit = SegyTraceHeader.SourceUnit.UNKNOWN;
    }

    // Byte 233-240
    reader_.skip(8);

    return new SegyTraceHeader(traceSequenceNo,
                               traceSequenceNoReel,
                               originalFieldRecordNo,
                               originalFieldRecordTraceNo,
                               energySourcePointNo,
                               cmpEnsamble,
                               ensambleTraceNo,
                               traceType,
                               nVerticallySummedTraces,
                               nHorizontallyStackedTraces,
                               dataUsage,
                               sourceReceiverDistance,
                               receiverElevation,
                               surfaceElevation,
                               sourceDepth,
                               datumElevationReceiver,
                               datumElevationSource,
                               waterDepthSource,
                               waterDepthReceiver,
                               x,
                               y,
                               xGroup,
                               yGroup,
                               coordinateUnit,
                               weatheringVelocity,
                               subweatheringVelocity,
                               upholeTimeAtSource,
                               upholeTimeAtReceiver,
                               sourceStaticsCorrection,
                               groupStaticsCorrection,
                               totalStaticsApplied,
                               lagTimeA,
                               lagTimeB,
                               recordingTimeDelay,
                               muteTimeStart,
                               muteTimeEnd,
                               nSamples,
                               sampleInterval,
                               fieldGainType,
                               instrumentGainConstant,
                               instrumentEarlyGain,
                               isCorrelated,
                               sweepFrequencyAtStart,
                               sweepFrequencyAtEnd,
                               sweepLength,
                               sweepType,
                               sweepTaperLengthAtStart,
                               sweepTaperLengthAtEnd,
                               sweepTaperType,
                               aliasFilterFrequency,
                               aliasFilterSlope,
                               notchFilterFrequency,
                               notchFilterSlope,
                               lowCutFrequency,
                               highCutFrequency,
                               lowCutSlope,
                               highCutSlope,
                               year,
                               dayOfYear,
                               hour,
                               minute,
                               second,
                               timeType,
                               traceWeightingFactor,
                               geophoneGroupNoOfRollSweepPosition,
                               geophoneGroupNoOfOriginalTraceNo,
                               geophoneGroupNoOfLastTrace,
                               gapSize,
                               overtravelTaper,
                               xCdp,
                               yCdp,
                               inlineNo,
                               crosslineNo,
                               shotPoint,
                               traceValueUnit,
                               transductionConstant,
                               transductionUnit,
                               deviceId,
                               sourceTypeAndOrientation,
                               sourceEnergyDirection,
                               sourceMeasurement,
                               sourceUnit);
  }

  private Float[] readIbmFloats(int nSamples)
    throws IOException
  {
    Float[] data = new Float[nSamples];

    for (int i = 0; i < nSamples; i++) {
      int b0 = reader_.get() & 0xff;
      int b1 = reader_.get() & 0xff;
      int b2 = reader_.get() & 0xff;
      int b3 = reader_.get() & 0xff;

      // 0x80 = 10000000
      // 0x7f = 01111111
      // 8388608.0 = 2^23
      // 16777216.0 = 2^25

      int sign = (b0 & 0x80) >> 7;
      int exponent = (b0 & 0x7f);
      int fraction = (b1 << 16) + (b2 << 8) + b3;

      double a;

      if (sign == 0 && exponent == 0 && fraction == 0)
        a = 0.0;
      else
        a = fraction / 16777216.0 * Math.pow(16.0, exponent - 64);

      if (sign == 1)
        a = -a;

      if (a < minValue_)
        minValue_ = a;

      if (a > maxValue_)
        maxValue_ = a;

      data[i] = (float) a;
    }

    return data;
  }


  private Float[] readFloats(int nSamples)
    throws IOException
  {
    Float[] data = new Float[nSamples];

    for (int i = 0; i < nSamples; i++) {
      float a = reader_.getFloat();

      if (a < minValue_)
        minValue_ = (double) a;

      if (a > maxValue_)
        maxValue_ = (double) a;

      data[i] = a;
    }

    return data;
  }

  private Integer[] readIntegers(int nSamples)
    throws IOException
  {
    Integer[] data = new Integer[nSamples];

    for (int i = 0; i < nSamples; i++) {
      int a = reader_.getShort();

      if (a < minValue_)
        minValue_ = (double) a;

      if (a > maxValue_)
        maxValue_ = (double) a;

      data[i] = a;
    }

    return data;
  }

  private Integer[] readGainIntegers(int nSamples)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  private Short[] readShorts(int nSamples)
    throws IOException
  {
    Short[] data = new Short[nSamples];

    for (int i = 0; i < nSamples; i++) {
      short a = (short) reader_.getShort();

      if (a < minValue_)
        minValue_ = (double) a;

      if (a > maxValue_)
        maxValue_ = (double) a;

      data[i] = a;
    }

    return data;
  }

  private Byte[] readBytes(int nSamples)
    throws IOException
  {
    Byte[] data = new Byte[nSamples];

    for (int i = 0; i < nSamples; i++) {
      byte a = reader_.get();

      if (a < minValue_)
        minValue_ = (double) a;

      if (a > maxValue_)
        maxValue_ = (double) a;

      data[i] = a;
    }

    return data;
  }

  /**
   * Initialize the file stream and the memory mapped reading buffer.
   */
  private void initialize()
    throws IOException
  {
    try {
      fileInputStream_ = new FileInputStream(file_);
    }
    catch (FileNotFoundException exception) {
      throw new IOException("File not found: " + file_, exception);
    }

    FileChannel fileChannel = fileInputStream_.getChannel();

    reader_ = new FileChannelReader(fileChannel, 64 * 1024);
  }

  /**
   * Read the SEG Y file.
   *
   * @param shouldReadBulkData  True if trace data should be read, false
   *                            if only header data should be read.
   * @return The SegyFile file instance representing the content of the
   *                           SEG Y file. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  public SegyFile read(boolean shouldReadBulkData)
    throws IOException
  {
    initialize();

    // 3200 byte EBCDIC text header
    List<SegyTextHeader> textHeaders = new ArrayList<>();
    SegyTextHeader textHeader = readTextHeader();
    textHeaders.add(textHeader);

    // 240 byte binary header
    SegyFileHeader fileHeader = readFileHeader();

    // Additional 3200 byte text headers if any
    for (int i = 0; i < fileHeader.getNExtendedRecords(); i++) {
      textHeader = readTextHeader();
      textHeaders.add(textHeader);
    }

    List<SegyTrace> traces = new ArrayList<>();

    if (shouldReadBulkData) {
      int nTraces = computeNTraces(fileHeader);
      int nSamples = fileHeader.getNSamples();
      SegyFileHeader.DataFormat dataFormat = fileHeader.getDataFormat();

      for (int traceNo = 0; traceNo < nTraces; traceNo++) {
        SegyTraceHeader traceHeader = readTraceHeader();

        Object[] data = null;

        switch (dataFormat) {
          case FLOAT4 :
            data = (Object[]) readIbmFloats(nSamples);
            break;

          case INT4 :
            data = (Object[]) readIntegers(nSamples);
            break;

          case INT2 :
            data = (Object[]) readShorts(nSamples);
            break;

          case INT4_GAIN :
            data = (Object[]) readGainIntegers(nSamples);
            break;

          case FLOAT4_IEEE :
            data = (Object[]) readFloats(nSamples);
            break;

          case BYTE :
            data = (Object[]) readBytes(nSamples);
            break;

          default :
            assert false : "Unexpected dataFormat: " + dataFormat;
        }

        SegyTrace trace = new SegyTrace(traceHeader, dataFormat.getDataType(), data);
        traces.add(trace);
      }
    }

    fileInputStream_.close();

    SegyFile segyFile = new SegyFile(file_.getName(),
                                     null,
                                     textHeaders,
                                     fileHeader,
                                     traces,
                                     minValue_, maxValue_);

    return segyFile;
  }
}
