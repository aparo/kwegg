package com.kwegg.commons.news;

public final class Guid {

	  /* Attributes */
	  private final String fValue;
	  private final boolean fIsPermaLink;

	  /**
	   * @param value The unique identifier.
	   * @param isPermaLink indicates whether this guid is a permalink to the item.
	   * {@code null} indicates that the feed had no permaLink attribute. See
	   * {@link #isPermaLink()} for more information.
	   */
	  public Guid(String value, Boolean isPermaLink) {
	    fValue = value;
	    if (isPermaLink == null)
	      fIsPermaLink = true;
	    else
	      fIsPermaLink = isPermaLink.booleanValue();
	  }

	  
	  public boolean isPermaLink() {
	    return fIsPermaLink;
	  }

	  
	  public String getValue() {
	    return fValue;
	  }

	  @Override
	  public int hashCode() {
	    final int PRIME = 31;
	    int result = 1;
	    result = PRIME * result + (fIsPermaLink ? 1231 : 1237);
	    result = PRIME * result + ((fValue == null) ? 0 : fValue.hashCode());
	    return result;
	  }

	  /**
	   * Compare the given type with this type for identity.
	   *
	   * @param guid to be compared.
	   * @return whether this object and <code>guid</code> are identical. It
	   * compares all the fields.
	   */
	  @Override
	  public boolean equals(Object guid) {
	    if (this == guid)
	      return true;

	    if (!(guid instanceof Guid))
	      return false;

	    synchronized (guid) {
	      Guid g = (Guid) guid;

	      return (fValue == null ? g.fValue == null : fValue.equals(g.fValue)) && fIsPermaLink == g.isPermaLink();
	    }
	  }

	  @Override
	  public String toString() {
	    return super.toString() + "Value = " + fValue + ", IsPermaLink = " + fIsPermaLink + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	  }
	}
