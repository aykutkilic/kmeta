package org.petitparser.grammar.xml.ast;


/**
 * Abstract XML data node.
 *
 * @author Lukas Renggli (renggli@gmail.com)
 */
public abstract class XmlData extends XmlNode {

  private final String data;

  public XmlData(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    XmlData other = (XmlData) obj;
    return data.equals(other.data);
  }

  @Override
  public int hashCode() {
    return data.hashCode();
  }

}
