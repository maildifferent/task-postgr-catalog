package catalog.html;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catalog.html.elements.HtmlDetailsElement;
import catalog.html.elements.HtmlInputElement;
import catalog.html.elements.HtmlSummaryElement;
import catalog.other.JsonParser;
import catalog.other.UtilTypify;

/**
 * Archive of the html parameters which are set to non default values.
 */
public class HtmlState {
  // Currently we record only the following html properties:
  public List<String> open; // Html elements id list with open == true.
  public List<String> checked; // Html elements id list with checked == true.
  public Map<String, String> value; // Value properties by html element ids.

  public Map<String, Object> toMap() {
    if (open == null && checked == null && value == null)
      return null;

    Map<String, Object> result = new HashMap<>(3);

    // @formatter:off
    if (open    != null)  result.put("open",    open);
    if (checked != null)  result.put("checked", checked);
    if (value   != null)  result.put("value",   value);
    // @formatter:on

    return result;
  }

  public void applyToElem(HtmlElement<?> rootElem) {
    HtmlElement<?> root = rootElem.root;

    if (open != null) {
      for (String id : open) {
        HtmlElement<?> elem = root.getElementById(id);
        if (!(elem instanceof HtmlDetailsElement))
          throw new IllegalArgumentException();
        openDetailsElems(elem);
      }
    }

    if (checked != null) {
      for (String id : checked) {
        HtmlElement<?> elem = root.getElementById(id);
        if (elem instanceof HtmlInputElement input) {
          if (!input.type.equals("checkbox"))
            throw new IllegalArgumentException();
          input.checked = true;
        } else {
          throw new IllegalArgumentException();
        }
        openDetailsElems(elem.parent);
      }
    }

    if (value != null) {
      for (var entry : value.entrySet()) {
        String id = entry.getKey();
        String value = entry.getValue();
        HtmlElement<?> elem = root.getElementById(id);
        if (elem instanceof HtmlInputElement casted) {
          casted.value(value);
        } else {
          throw new IllegalArgumentException();
        }
        openDetailsElems(elem.parent);
      }
    }
  }

  public static HtmlState createFromElement(HtmlElement<?> root) {
    // Not implemented yet.
    throw new IllegalArgumentException();
  }

  public static HtmlState createFromJson(String jsonStr) {
    if (jsonStr == null)
      return null;
    HtmlState result = new HtmlState();

    Object jsonObj = JsonParser.parse(jsonStr);
    Map<?, ?> map = (Map<?, ?>) jsonObj;

    for (var entry : map.entrySet()) {
      Object key = entry.getKey();
      Object value = entry.getValue();
      if (key instanceof String castKey) {
        switch (castKey) {
          // @formatter:off
          case "open": { result.open = UtilTypify.typifyList((List<?>) value, String.class); break; }
          case "checked": { result.checked = UtilTypify.typifyList((List<?>) value, String.class); break; }
          case "value": { result.value = UtilTypify.typifyStringMap((Map<?, ?>) value, String.class); break; }
          default: { throw new IllegalArgumentException(); }
          // @formatter:on
        }
      } else {
        throw new IllegalArgumentException();
      }
    }

    // @formatter:off
    if (result.open     != null && result.open.isEmpty())     result.open     = null;
    if (result.checked  != null && result.checked.isEmpty())  result.checked  = null;
    if (result.value    != null && result.value.isEmpty())    result.value    = null;

    if (result.open == null && result.checked == null && result.value == null) result = null;
    // @formatter:on

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Util. Private.
  //////////////////////////////////////////////////////////////////////////////
  private void openDetailsElems(HtmlElement<?> elem) {
    // If initial elem type is not <DETAILS> and the elem is inside <SUMMARY> then
    // we should not open the parent <DETAILS> element of this <SUMMARY> element.
    if (!(elem instanceof HtmlDetailsElement)) {
      boolean isInsideSummary = false;
      while (elem != null) {
        if (elem instanceof HtmlDetailsElement) {
          if (isInsideSummary) {
            elem = elem.parent;
          }
          break;
        }
        if (elem instanceof HtmlSummaryElement) {
          isInsideSummary = true;
        }
        elem = elem.parent;
      }
    }

    while (elem != null) {
      if (elem instanceof HtmlDetailsElement details) {
        details.open = true;
      }
      elem = elem.parent;
    }
  }
}
