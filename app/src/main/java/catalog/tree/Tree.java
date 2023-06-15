package catalog.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Tree<AbstractT extends Tree<AbstractT, ?>, SpecificT extends Tree<?, SpecificT>> {
  public final UUID uid = UUID.randomUUID();
  public SpecificT self;
  public Map<String, AbstractT> idMap; // Only the root element will have idMap. In other elems idMap equal null.
  public Class<? extends AbstractT> abstractClass;
  public Class<? extends SpecificT> specificClass;

  public AbstractT root;
  public AbstractT parent;
  public AbstractT prevSibling;
  public AbstractT nextSibling;
  public AbstractT firstChild;
  public AbstractT lastChild;

  // Moved id definition to children classes because in some functionality we might 
  // want to work with list of "relevant" for the functionality properties. 
  // Properties of this class will be considered technical for such functionality.
  protected abstract void setId(String id);
  protected abstract String getId();

  public <A extends AbstractT, S extends SpecificT> Tree(Class<A> abstractClass, Class<S> specificClass) {
    if (!abstractClass.isInstance(this)) {
      throw new IllegalArgumentException();
    }
    if (!specificClass.isInstance(this)) {
      throw new IllegalArgumentException();
    }
    this.abstractClass = abstractClass;
    this.specificClass = specificClass;
    self = specificClass.cast(this);
    root = abstractClass.cast(this);
    idMap = new HashMap<>();
  }

  public Tree(Class<AbstractT> abstractClass, Class<SpecificT> specificClass, AbstractT parent) {
    this(abstractClass, specificClass);
    parent.addLastChild(abstractClass.cast(this));
  }

  public final SpecificT id(String id) {
    if (root.idMap.containsKey(id))
      throw new IllegalArgumentException();

    var oldId = getId();

    if (oldId != null) {
      var elemFromMap = root.idMap.get(oldId);
      if (elemFromMap != this) {
        throw new IllegalArgumentException();
      }
      root.idMap.remove(oldId);
    }

    root.idMap.put(id, abstractClass.cast(this));
    setId(id);
    return self;
  }

  public AbstractT getElementById(String id) {
    return root.idMap.get(id);
  }

  public AbstractT getElemByUid(UUID uid) {
    if (uid.equals(uid))
      return abstractClass.cast(this);

    AbstractT child = firstChild;
    while (child != null) {
      AbstractT elem = child.getElemByUid(uid);
      if (elem != null)
        return elem;
      child = child.nextSibling;
    }

    return null;
  }

  public SpecificT setParent(AbstractT parent) {
    parent.addLastChild(abstractClass.cast(this));
    return self;
  }

  public SpecificT addFirstChild(AbstractT elem) {
    addChild(elem);

    if (firstChild == null) {
      firstChild = elem;
      lastChild = elem;
      return self;
    }

    firstChild.prevSibling = elem;
    elem.nextSibling = firstChild;
    firstChild = elem;
    return self;
  }

  public SpecificT addLastChild(AbstractT elem) {
    addChild(elem);

    if (firstChild == null) {
      firstChild = elem;
      lastChild = elem;
      return self;
    }

    lastChild.nextSibling = elem;
    elem.prevSibling = lastChild;
    lastChild = elem;
    return self;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Util protected.
  //////////////////////////////////////////////////////////////////////////////
  protected void checkThatLinksAreEmpty() {
    // Functionality to remove an element from a tree is not impelemented. So bofore
    // inserting an element into a tree this check should be done to assure that the
    // element is not used in another tree.
    if (this.root != this
        || this.self != this
        || this.parent != null
        || this.prevSibling != null
        || this.nextSibling != null)
      throw new IllegalArgumentException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Util private.
  //////////////////////////////////////////////////////////////////////////////
  private void addChild(AbstractT elem) {
    elem.checkThatLinksAreEmpty();

    for (var key : elem.idMap.keySet()) {
      if (root.idMap.containsKey(key))
        throw new IllegalArgumentException();
    }
    root.idMap.putAll(elem.idMap);
    elem.idMap = null;

    elem.root = root;
    elem.parent = abstractClass.cast(this);
  }
}
