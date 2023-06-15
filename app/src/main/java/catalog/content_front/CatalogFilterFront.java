package catalog.content_front;

import java.util.HashMap;
import java.util.Map;

import catalog.content_front.filters.FilterFrontCheese;
import catalog.content_front.filters.FilterFrontOther;
import catalog.content_main.ResourceFilterIds;
import catalog.filter_front.FilterFront;
import catalog.filter_front.FilterFrontCheck;
import catalog.filter_front.FilterFrontInterval;
import catalog.html.HtmlState;

public class CatalogFilterFront {
  public static FilterFront<?, ?> renderSingle(String id) {
    if (id.startsWith(ResourceFilterIds.cheese))
      return FilterFrontCheese.renderSingle(id);
    return FilterFrontOther.renderSingle(id);
  }

  public static FilterFront<?, ?> renderTree() {
    FilterFront<?, ?> brew_drink = renderSingle(ResourceFilterIds.brew_drink);
    FilterFront<?, ?> cheese = renderSingle(ResourceFilterIds.cheese);
    FilterFront<?, ?> cheese_blue = renderSingle(ResourceFilterIds.cheese_blue);
    FilterFront<?, ?> cheese_fat_gte = renderSingle(ResourceFilterIds.cheese_fat_gte);
    FilterFront<?, ?> cheese_fat_lte = renderSingle(ResourceFilterIds.cheese_fat_lte);
    FilterFront<?, ?> cheese_moisture_hard = renderSingle(ResourceFilterIds.cheese_moisture_hard);
    FilterFront<?, ?> cheese_moisture_semi = renderSingle(ResourceFilterIds.cheese_moisture_semi);
    FilterFront<?, ?> cheese_moisture_soft = renderSingle(ResourceFilterIds.cheese_moisture_soft);
    FilterFront<?, ?> coffee = renderSingle(ResourceFilterIds.coffee);
    FilterFront<?, ?> coffee_beans = renderSingle(ResourceFilterIds.coffee_beans);
    FilterFront<?, ?> coffee_ground = renderSingle(ResourceFilterIds.coffee_ground);
    FilterFront<?, ?> herbaldrink = renderSingle(ResourceFilterIds.herbaldrink);
    FilterFront<?, ?> resources = renderSingle(ResourceFilterIds.resources);
    FilterFront<?, ?> tea = renderSingle(ResourceFilterIds.tea);
    FilterFront<?, ?> tea_black = renderSingle(ResourceFilterIds.tea_black);
    FilterFront<?, ?> tea_green = renderSingle(ResourceFilterIds.tea_green);

    // Hierarchy:
    resources
        .addLastChild(cheese)
        .addLastChild(brew_drink);

    cheese
        .addLastChild(cheese_blue)
        .addLastChild(cheese_fat_gte)
        .addLastChild(cheese_fat_lte)
        .addLastChild(cheese_moisture_hard)
        .addLastChild(cheese_moisture_semi)
        .addLastChild(cheese_moisture_soft);

    brew_drink
        .addLastChild(coffee)
        .addLastChild(herbaldrink)
        .addLastChild(tea);

    coffee
        .addLastChild(coffee_beans)
        .addLastChild(coffee_ground);

    tea
        .addLastChild(tea_black)
        .addLastChild(tea_green);

    return resources;
  }

  public static Map<String, FilterFront<?, ?>> genFromHtmlState(HtmlState htmlState) {
    if (htmlState == null)
      return null;

    var checked = htmlState.checked;
    var values = htmlState.value;
    int checkedLen = checked == null ? 0 : checked.size();
    int valuesLen = values == null ? 0 : values.size();
    Map<String, FilterFront<?, ?>> filterFrontsById = new HashMap<>(checkedLen + valuesLen);

    FilterFront<?, ?> filterFront;
    
    if (checked != null) {
      for (String id : checked) {
        filterFront = CatalogFilterFront.renderSingle(id);
        if (filterFront instanceof FilterFrontCheck casted) {
          casted.checked = true;
        } else {
          throw new IllegalArgumentException();
        }
        filterFrontsById.put(id, filterFront);
      }
    }

    if (values != null) {
      for (var entry : values.entrySet()) {
        String id = entry.getKey();
        String value = entry.getValue();
        filterFront = CatalogFilterFront.renderSingle(id);
        if (filterFront instanceof FilterFrontInterval casted) {
          casted.valueFromObj(value);
        } else {
          throw new IllegalArgumentException();
        }
        filterFrontsById.put(id, filterFront);
      }
    }

    return filterFrontsById;
  }
}
