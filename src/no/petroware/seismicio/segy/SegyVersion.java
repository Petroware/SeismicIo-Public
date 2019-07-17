package no.petroware.seismicio.segy;

public enum SegyVersion
{
  STANDARD(0, "Standard"),
  GECO06(1, "Geco 06"),
  GECOSEIS54(2, "GecoSeis 54");

  private final int tag_;

  private final String description_;

  SegyVersion(int tag, String description)
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
    return tag_ + " (" + description_ + ")";
  }

  public static SegyVersion get(int tag)
  {
    for (SegyVersion version : SegyVersion.values()) {
      if (version.tag_ == tag)
        return version;
    }

    // Not found
    return null;
  }
}
