package catalog.other;

import java.util.LinkedHashMap;
import java.util.LinkedList;

// Mapping:
// {} -> LinkedHashMap<String, Object>
// [] -> LinkedList<Object>
// num -> Integer
// str -> String
// bool -> Booelan
// null -> null
public class JsonParser {
  public static Object parse(String jsonStr) {
    var processor = new JsonProcessor(jsonStr);
    return processor.result;
  }
}

class JsonProcessor {
  private int shift = 0;
  private final char[] json;
  private String propName = null;
  public Object result;

  LinkedList<Character> stateStack = new LinkedList<>();
  Character curState = ' '; // Initial state.

  LinkedList<LinkedHashMap<String, Object>> objStack = new LinkedList<>();
  LinkedHashMap<String, Object> curObj = null;

  LinkedList<LinkedList<Object>> arrStack = new LinkedList<>();
  LinkedList<Object> curArr = null;

  public JsonProcessor(String jsonStr) {
    this.json = jsonStr.toCharArray();
    build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Main method.
  //////////////////////////////////////////////////////////////////////////////
  private void build() {
    if (json.length < 1)
      throw new IllegalArgumentException();
    int jsonLenMinusOne = json.length - 1;
    for (; shift < json.length; shift++) {
      skipWhiteSpace();
      if (shift > jsonLenMinusOne)
        break;
      processNextChar();
    }

    if (!stateStack.isEmpty())
      throw new IllegalArgumentException();

  }

  //////////////////////////////////////////////////////////////////////////////
  // Other methods.
  //////////////////////////////////////////////////////////////////////////////
  private void processNextChar() {
    char curChar = json[shift];
    switch (curChar) {
      // @formatter:off
      case '{': { openNewObj(); setState('{'); break; }
      case '}': { closeCurObj(); curState = stateStack.poll(); break; }
      case '[': { openNewArr(); setState('['); break; }
      case ']': { closeCurArr(); curState = stateStack.poll(); break; }
      case 'f': { isFalse(); shift += 4; setOther(false);  break; }
      case 't': { isTrue(); shift += 3; setOther(true);  break; }
      case 'n': { isNull(); shift += 3; setOther(null);  break; }

      case '0': ;
      case '1': ;
      case '2': ;
      case '3': ;
      case '4': ;
      case '5': ;
      case '6': ;
      case '7': ;
      case '8': ;
      case '9': { String str = parseNumber(); setOther(Integer.valueOf(str));  break; }

      case '"': { String str = parseString(); setStr(str); break; }
      case ':': { setState(':'); break; }
      case ',': { break; }

      default : { err(); break; }
      // @formatter:on
    }
  }

  private void setState(Character state) {
    if (curState != null)
      stateStack.push(curState);
    curState = state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Set tools.
  //////////////////////////////////////////////////////////////////////////////
  private void openNewObj() {
    LinkedHashMap<String, Object> newObj = new LinkedHashMap<>();
    setOther(newObj);
    if (curObj != null)
      objStack.push(curObj);
    curObj = newObj;
  }

  private void closeCurObj() {
    if (curState != '{')
      throw new IllegalArgumentException();
    curObj = objStack.poll();
  }

  private void openNewArr() {
    LinkedList<Object> newArr = new LinkedList<>();
    setOther(newArr);
    if (curArr != null)
      arrStack.push(curArr);
    curArr = newArr;
  }

  private void closeCurArr() {
    if (curState != '[')
      throw new IllegalArgumentException();
    curArr = arrStack.poll();
  }

  private void setStr(String str) {
    if (curState == '{') {
      setPropName(str);
    } else {
      setOther(str);
    }
  }

  private void setPropName(String str) {
    if (propName != null)
      throw new IllegalStateException();
    propName = str;
  }

  private void setPropValue(Object value) {
    if (curObj.containsKey(propName))
      throw new IllegalArgumentException();
    curObj.put(propName, value);
    propName = null;
  }

  private void pushArrValue(Object value) {
    curArr.addLast(value);
  }

  private void setOther(Object value) {
    if (curState.equals(':')) {
      setPropValue(value);
      curState = stateStack.remove();
    } else if (curState.equals('[')) {
      pushArrValue(value);
    } else if (curState.equals(' ')) {
      result = value;
      curState = null;
    } else
      throw new IllegalStateException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Parsing tools.
  //////////////////////////////////////////////////////////////////////////////
  private void isFalse() {
    if (json[shift + 1] == 'a' &&
        json[shift + 2] == 'l' &&
        json[shift + 3] == 's' &&
        json[shift + 4] == 'e') {

    } else {
      throw new IllegalArgumentException();
    }
  }

  private void isTrue() {
    if (json[shift + 1] == 'r' &&
        json[shift + 2] == 'u' &&
        json[shift + 3] == 'e') {

    } else {
      throw new IllegalArgumentException();
    }
  }

  private void isNull() {
    if (json[shift + 1] == 'u' &&
        json[shift + 2] == 'l' &&
        json[shift + 3] == 'l') {

    } else {
      throw new IllegalArgumentException();
    }
  }

  private String parseNumber() {
    int start = shift;
    int next = shift + 1;
    while (next < json.length) {
      // @formatter:off
      switch(json[next]){
          case '0': ;
          case '1': ;
          case '2': ;
          case '3': ;
          case '4': ;
          case '5': ;
          case '6': ;
          case '7': ;
          case '8': ;
          case '9': ;
          case '.': { next++; } break;

          default : { shift = next - 1; return new String(json, start, next - start); }
      }
      // @formatter:on
    }
    throw new Error(); // This statement should be unreachable.
  }

  private String parseString() {
    shift++;
    int start = shift;
    while (shift < json.length) {
      if (json[shift] == '"' && json[shift - 1] != '\\')
        return new String(json, start, shift - start);
      shift++;
    }
    throw new IllegalArgumentException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Util.
  //////////////////////////////////////////////////////////////////////////////
  static private void err() {
    throw new IllegalArgumentException();
  }

  private void skipWhiteSpace() {
    while (shift < json.length) {
      // @formatter:off
      switch (json[shift]) {
        case ' ':
        case '\r':
        case '\n':
        case '\t': shift++; break;
        default: return;
      }
      // @formatter:on
    }
  }
}
