package catalog.content_front.filters;

import java.util.Arrays;
import java.util.HashSet;

import catalog.content_main.RersourceCategories;
import catalog.content_main.ResourceFilterIds;
import catalog.filter_front.FilterFront;
import catalog.filter_front.FilterFrontCheckEq;
import catalog.filter_front.FilterFrontCheckIn;
import catalog.filter_front.FilterFrontInterval;
import catalog.storage.tables.FilterCheese;
import catalog.storage.tables.Resource;

public class FilterFrontCheese {
  public static FilterFront<?, ?> renderSingle(String id) {
    switch (id) {
      // @formatter:off
      case ResourceFilterIds.cheese: return new FilterFrontCheckIn<>(
        Resource.resource_category, new HashSet<>(Arrays.asList(RersourceCategories.cheese)))
        .id(ResourceFilterIds.cheese);
      
      case ResourceFilterIds.cheese_blue: return new FilterFrontCheckEq<>(
        FilterCheese.cheese_blue, true)
        .id(ResourceFilterIds.cheese_blue);

      case ResourceFilterIds.cheese_fat_gte: return new FilterFrontInterval<>(
        FilterCheese.cheese_fat_fract, "gte")
        .id(ResourceFilterIds.cheese_fat_gte);

      case ResourceFilterIds.cheese_fat_lte: return new FilterFrontInterval<>(
        FilterCheese.cheese_fat_fract, "lte")
        .id(ResourceFilterIds.cheese_fat_lte);

      case ResourceFilterIds.cheese_moisture_hard: return new FilterFrontCheckIn<>(
        FilterCheese.cheese_moist_fract, new HashSet<>(Arrays.asList("hard")))
        .id(ResourceFilterIds.cheese_moisture_hard);

      case ResourceFilterIds.cheese_moisture_semi: return new FilterFrontCheckIn<>(
        FilterCheese.cheese_moist_fract, new HashSet<>(Arrays.asList("semi")))
        .id(ResourceFilterIds.cheese_moisture_semi);

      case ResourceFilterIds.cheese_moisture_soft: return new FilterFrontCheckIn<>(
        FilterCheese.cheese_moist_fract, new HashSet<>(Arrays.asList("soft")))
        .id(ResourceFilterIds.cheese_moisture_soft);
      
      default: throw new IllegalArgumentException();
      // @formatter:on
    }
  }
}
