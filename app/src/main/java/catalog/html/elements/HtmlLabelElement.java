package catalog.html.elements;

import catalog.html.HtmlElement;

public class HtmlLabelElement extends HtmlElement<HtmlLabelElement> {
  public String for0;

  public HtmlLabelElement() {
    super(HtmlLabelElement.class, "label");
  }

  public HtmlLabelElement for0(String for0) {
    this.for0 = for0;
    return this;
  }
}
