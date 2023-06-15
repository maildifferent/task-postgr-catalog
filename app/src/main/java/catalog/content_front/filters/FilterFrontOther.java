package catalog.content_front.filters;

import java.util.Arrays;
import java.util.HashSet;

import catalog.content_main.RersourceCategories;
import catalog.content_main.ResourceFilterIds;
import catalog.filter_front.FilterFront;
import catalog.filter_front.FilterFrontCheckIn;
import catalog.storage.tables.FilterSingleStr;
import catalog.storage.tables.Resource;

public class FilterFrontOther {
  public static FilterFront<?, ?> renderSingle(String id) {
    switch (id) {
      // @formatter:off
      case ResourceFilterIds.brew_drink: return new FilterFrontCheckIn<>(
        Resource.resource_category, new HashSet<>(Arrays.asList(
          RersourceCategories.coffee, 
          RersourceCategories.herbal_drink, 
          RersourceCategories.tea)))
        .id(ResourceFilterIds.brew_drink);

      case ResourceFilterIds.coffee: return new FilterFrontCheckIn<>(
        Resource.resource_category, new HashSet<>(Arrays.asList(RersourceCategories.coffee)))
        .id(ResourceFilterIds.coffee);
      case ResourceFilterIds.coffee_beans: return new FilterFrontCheckIn<>(
        FilterSingleStr.single_class_str, new HashSet<>(Arrays.asList("coffee_beans")))
        .id(ResourceFilterIds.coffee_beans);
      case ResourceFilterIds.coffee_ground: return new FilterFrontCheckIn<>(
        FilterSingleStr.single_class_str, new HashSet<>(Arrays.asList("coffee_ground")))
        .id(ResourceFilterIds.coffee_ground);

      case ResourceFilterIds.herbaldrink: return new FilterFrontCheckIn<>(
        Resource.resource_category, new HashSet<>(Arrays.asList(RersourceCategories.herbal_drink)))
        .id(ResourceFilterIds.herbaldrink);

      case ResourceFilterIds.resources: return new FilterFrontCheckIn<>(
        Resource.resource_category, new HashSet<>())
        .id(ResourceFilterIds.resources);

      case ResourceFilterIds.tea: return new FilterFrontCheckIn<>(
        Resource.resource_category, new HashSet<>(Arrays.asList(RersourceCategories.tea)))
        .id(ResourceFilterIds.tea);
      case ResourceFilterIds.tea_black: return new FilterFrontCheckIn<>(
        FilterSingleStr.single_class_str, new HashSet<>(Arrays.asList("tea_black")))
        .id(ResourceFilterIds.tea_black);
      case ResourceFilterIds.tea_green: return new FilterFrontCheckIn<>(
        FilterSingleStr.single_class_str, new HashSet<>(Arrays.asList("tea_green")))
        .id(ResourceFilterIds.tea_green);
      
      default: throw new IllegalArgumentException();
      // @formatter:on
    }
  }
}
