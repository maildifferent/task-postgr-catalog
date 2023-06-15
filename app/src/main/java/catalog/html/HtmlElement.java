package catalog.html;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import catalog.tree.Tree;

public abstract class HtmlElement<SpecificT extends HtmlElement<SpecificT>> extends Tree<HtmlElement<?>, SpecificT> {
  public final String tagName;
  public final List<String> class0 = new LinkedList<>();
  public String id; // Method id() should be used to set the id property.
  public String innerText;
  public String innerHTML;
  public String style;
  public String onchange;

  public HtmlElement(Class<SpecificT> specificClass, String tagName) {
    super(HtmlElement.class, specificClass);
    this.tagName = tagName;
  }

  @Override
  protected void setId(String id) {
    this.id = id;
  }

  @Override
  protected String getId() {
    return this.id;
  }

  public final SpecificT addToClassList(String... classNames) {
    class0.addAll(Arrays.asList(classNames));
    return self;
  }

  public final SpecificT innerText(String innerText) {
    this.innerHTML = null;
    this.innerText = innerText;
    return self;
  }

  public final SpecificT innerHTML(String innerHTML) {
    this.innerText = null;
    this.innerHTML = innerHTML;
    return self;
  }

  public final SpecificT style(String style) {
    this.style = style;
    return self;
  }

  public final SpecificT onchange(String onchange) {
    this.innerText = null;
    this.onchange = onchange;
    return self;
  }
}