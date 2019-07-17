package no.petroware.seismicio.segy;

import java.util.Date;

/**
 * Model the optional <em>Tape Label</em> that may appear at the very
 * beginning of a SEG Y tape, see SEG Y rev2 reference Appendix B.
 * <p>
 * From the documentation:
 * <p>
 * <em>
 * In order to bring SEG Y into line with SEG D
 * Rev 3.0, a label may be written at the front
 * of a SEG Y file on unformatted, removable
 * media such as magnetic tape.  This is a
 * single record consisting of 128 bytes of
 * ASCII characters..  A SEG Y tape label is
 * optional and is only valid on SEG Y files
 * written to unformatted, removable media.
 * However, a label must be present if the
 * blocking scheme described in Appendix C is
 * being used.  In this case the label must
 * appear as a separate 128-byte record at the
 * beginning of the file.  There must be no file
 * mark between the label record and the first
 * data record.
 * If the recording medium supports multiple
 * partitions, each partition is treated in
 * isolation as if it were a separate unit.  Thus,
 * if labels are being used, each partition must
 * begin with a label.  Data from one partition
 * can not "run-over" into a subsequent
 * partition.  Each partition must be capable of
 * being decoded in isolation.  On one
 * recording medium, it is permissible to mix
 * partitions containing SEG Y data with
 * partitions containing non-SEG Y formatted
 * information.
 * </em>
 *
 * @author <a href="mailto:info@petroware.no">Petroware AS</a>
 */
public final class SegyTapeLabel
{
  /** Storage unit sequence num,ber. */
  private final int storageUnitSequenceNumber_;

  /** SEG Y revision. */
  private final String segyRevision_;

  /** Storage unit structure. */
  private final String storageUnitStructure_;

  /** Binding edition. */
  private final String bindingEdition_;

  /** Maximum block size. */
  private final int maxBlockSize_;

  /** Producer organization code. */
  private final String producerOranizationCode_;

  /** Creation date. */
  private final Date creationDate_;

  /** Serial number. */
  private final String serialNumber_;

  /** Storage set identifier. */
  private final String storageSetId_;

  /**
   * Create a new SEG Y tabe label instance.
   *
   * @param storageUnitSequenceNumber  The storage unit sequence number. [1,9999].
   * @param segyRevision               SEG Y revision. Non-null.
   * @param storageUnitStructure       Storage unit structure. Non-null.
   * @param bindingEdition             Binding edition. Non-null.
   * @param maxBlockSize               Maximum block size. [0,2^32-1].
   * @param producerOranizationCode    Producer oganization code. Non-null.
   * @param creationDate               Creation date. Non-null.
   * @param serialNumber               Serial number. May be null.
   * @param storageSetId               Storae set ID. Non-null.
   */
  public SegyTapeLabel(int storageUnitSequenceNumber,
                       String segyRevision,
                       String storageUnitStructure,
                       String bindingEdition,
                       int maxBlockSize,
                       String producerOranizationCode,
                       Date creationDate,
                       String serialNumber,
                       String storageSetId)
  {
    if (storageUnitSequenceNumber < 1 || storageUnitSequenceNumber > 9999)
      throw new IllegalArgumentException("Invalid storageUnitSequenceNumber: " + storageUnitSequenceNumber);

    if (segyRevision == null)
      throw new IllegalArgumentException("segyRevision cannot be null");

    if (storageUnitStructure == null)
      throw new IllegalArgumentException("storageUnitStructure cannot be null");

    if (bindingEdition == null)
      throw new IllegalArgumentException("bindingEdition cannot be null");

    if (maxBlockSize < 0)
      throw new IllegalArgumentException("Invalid maxBlockSize: " + maxBlockSize);

    if (producerOranizationCode == null)
      throw new IllegalArgumentException("producerOranizationCode cannot be null");

    if (creationDate == null)
      throw new IllegalArgumentException("creationDate cannot be null");

    if (storageSetId == null)
      throw new IllegalArgumentException("storageSetId cannot be null");

    storageUnitSequenceNumber_ = storageUnitSequenceNumber;
    segyRevision_ = segyRevision;
    storageUnitStructure_ = storageUnitStructure;
    bindingEdition_ = bindingEdition;
    maxBlockSize_ = maxBlockSize;
    producerOranizationCode_ = producerOranizationCode;
    creationDate_ = new Date(creationDate.getTime());
    serialNumber_ = serialNumber;
    storageSetId_ = storageSetId;
  }

  /**
   * Return the storage unit sequence number of this SEG Y tape label.
   * <p>
   * <em>
   * The Storage Unit Sequence Number is an
   * integer in the range 1 to 9999 that indicates
   * the order in which the current storage unit
   * occurs in the storage set.  The first storage
   * unit of a storage set has sequence number
   * 1, the second 2 and so on.  This number is
   * represented using the characters 0 to 9,
   * right justified with leading blanks if needed
   * to fill out the field (no leading zeros).  The
   * right-most character is in byte 4 of the label.
   * This field is optional.
   * If not used, it must be blank (filled with blank characters).
   * This implies that this is the only storage unit
   * </em>
   *
   * @return  Storage unit sequence number of this SEG Y tape label.
   */
  public int getStorageUnitSequenceNumber()
  {
    return storageUnitSequenceNumber_;
  }

  /**
   * Return the SEG Y revision of this SEG Y tape label.
   * <p>
   * <em>
   * The SEG Y Revision field indicates which
   * revision of SEG Y was used to record the
   * data on this tape.  SY2.0 indicates that the
   * data was formatted using SEG Y Revision 2.
   * This field is required.
   * </em>
   *
   * @return  The SEG Y revision of this SEG Y tape label.
   */
  public String getSegyRevision()
  {
    return segyRevision_;
  }

  /**
   * Return the storage unit structure of this SEG Y tape label.
   * <p>
   * <em>
   * Storage Unit Structure is a name indicating
   * the record structure of the storage unit.
   * This name is left justified with trailing blanks
   * if needed to fill out the field.  The leftmost
   * character is in byte 10 of the label.  For
   * SEG Y Revision 1 tapes, this field must
   * contain "RECORD".
   * This field is required.
   * "RECORD" - Records may be of variable
   * length, ranging up to the block size length
   * specified in the maximum block size field of
   * the storage unit label (if not zero).  If the
   * maximum block size specified is zero,
   * records may be of any length.
   * </em>
   *
   * @return  Storage unit structure of this SEG Y tape label.
   */
  public String getStorageUnitStructure()
  {
    return storageUnitStructure_;
  }

  /**
   * Return the binding edition of this SEG Y tape label.
   * <p>
   * <em>
   * Binding Edition is the character B in byte 16
   * of the label followed by a positive integer in
   * the range 1 to 999 (no leading zeros), left
   * justified with trailing blanks if needed to fill
   * out the field.  The integer value corresponds
   * to the edition of the Part 3 of the API RP66
   * standard used to describe the physical
   * binding of the logical format to the storage
   * unit. This field is required.
   * </em>
   *
   * @return  Binding edition of this SEG Y tape label. Never null.
   */
  public String getBindingEdition()
  {
    return bindingEdition_;
  }

  /**
   * Return the maximum block size of this SEG Y tape label.
   * <p>
   * <em>
   * Maximum Block Size is an integer in the
   * range of 0 to 4,294,967,295 (2^32-1),
   * indicating the maximum block length for the
   * storage unit, or 0 (zero) if undeclared.  This
   * number is represented using the characters
   * 0 to 9, right justified, with leading blanks if
   * necessary to fill out the field (no leading
   * zeros).  The rightmost character is byte 29
   * of the label.
   * A valid value or 0 (zero) must be recorded.
   * </em>
   *
   * @return  Maximum block size of this SEG Y tape label.
   */
  public int getMaxBlockSize()
  {
    return maxBlockSize_;
  }

  /**
   * Return the producer organization code of this SEG Y tape label.
   * <p>
   * <em>
   * Producer Organization Code is an integer in
   * the range of 0 to 4,294,967,295 (2^32-1)
   * indicating the organization code of the
   * storage unit producer.  This number is
   * represented using the characters 0 to 9,
   * right justified, with leading blanks if
   * necessary to fill out the field (no leading
   * zeros).  The rightmost character is byte 39
   * of the label. This field is required.
   * Organization codes are assigned by
   * Energistics, formerly the Petrotechnical
   * Open Standards Consortium (POSC), which
   * maintains the current list of codes. Please
   * refer to Appendix C of SEG D rev 3.0 for a
   * list of the currently assigned codes.
   * </em>
   *
   * @return  Producer organization code of this SEG Y tape label. Never null.
   */
  public String getProducerOranizationCode()
  {
    return producerOranizationCode_;
  }

  /**
   * Return the creation date of this SEG Y tape label.
   * <p>
   * <em>
   * Creation date is the earliest date that any
   * current information was recorded on the
   * storage unit.  The date is represented in the
   * form dd-MMM-yyyy, where yyyy is the year
   * (e.g. 1996), MMM is one of (JAN, FEB,
   * MAR, APR, MAY, JUN, JUL, AUG, SEP,
   * OCT, NOV, DEC) and dd is the day of the
   * month in the range 1 to 31.  Days 1 to 9
   * may have one leading blank.  The separator
   * is a hyphen (code 45).
   * This field is required.
   * </em>
   *
   * @return  Creation date of this SEG Y tape label.
   */
  public Date getCreationDate()
  {
    return new Date(creationDate_.getTime());
  }

  /**
   * Return the serial number of this SEG Y tape label.
   * <p>
   * <em>
   * Serial Number is an identifier used to
   * distinguish the storage unit from other
   * storage units in an archive of an enterprise.
   * The specification and management of serial
   * numbers is delegated to organizations using
   * this standard.  This field may be empty (i.e.
   * may contain all blanks, in which case no
   * serial number is specified).
   * </em>
   *
   * @return  Serial number of this SEG Y tape label.
   */
  public String getSerialNumber()
  {
    return serialNumber_;
  }

  /**
   * Return the storage set ID of this SEG Y tape label.
   * <p>
   * <em>
   * Storage Set Identifier is a descriptive name
   * for the storage set.  Every storage unit in
   * the same storage set shall have the same
   * value for the storage set identifier in its
   * storage unit label.  A value may have
   * embedded blanks and is non-blank if at
   * least one character is different from blank
   * (code 32<sub>10</sub>10).  This field is intended
   * to distinguish the storage set from other
   * storage sets, but is not required to be
   * unique.
   * A nonblank value shall be recorded.
   * </em>
   *
   * @return  Storage set ID of this SEG Y tape label.
   */
  public String getStorageSetId()
  {
    return storageSetId_;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return storageSetId_;
  }
}
