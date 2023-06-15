package catalog.html.elements;

import catalog.html.HtmlElement;

public class HtmlInputElement extends HtmlElement<HtmlInputElement> {
  public String type;
  public String value;
  public Boolean checked;
  public String name;
  public String placeholder;
  public String autocomplete;
  public Number min;
  public Number max;

  public HtmlInputElement() {
    super(HtmlInputElement.class, "input");
  }

  public HtmlInputElement type(String type) {
    this.type = type;
    return this;
  }

  public HtmlInputElement value(String value) {
    this.value = value;
    return this;
  }
  public HtmlInputElement value(Object value) {
    this.value = value.toString();
    return this;
  }

  public HtmlInputElement checked(Boolean checked) {
    this.checked = checked;
    return this;
  }

  public HtmlInputElement name(String name) {
    this.name = name;
    return this;
  }

  public HtmlInputElement min(Number min) {
    this.min = min;
    return this;
  }

  public HtmlInputElement max(Number max) {
    this.max = max;
    return this;
  }

  public HtmlInputElement placeholder(String placeholder) {
    this.placeholder = placeholder;
    return this;
  }

  public HtmlInputElement autocomplete(String autocomplete) {
    this.autocomplete = autocomplete;
    return this;
  }
}
