package catalog.html.elements;

import catalog.html.HtmlElement;

public class HtmlImageElement extends HtmlElement<HtmlImageElement> {
  public String src;

  public HtmlImageElement() {
    super(HtmlImageElement.class, "img");
  }

  public HtmlImageElement src(String src) {
    this.src = src;
    return this;
  }
}
