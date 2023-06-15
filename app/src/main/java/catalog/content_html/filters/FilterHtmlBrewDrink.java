package catalog.content_html.filters;

import catalog.content_front.CatalogFilterFront;
import catalog.content_html.ContentHtmlClasses;
import catalog.content_html.ContentHtmlUtil;
import catalog.content_main.ResourceFilterIds;
import catalog.html.HtmlDocument;
import catalog.html.HtmlElemFromFilterFront;
import catalog.html.HtmlElement;
import catalog.html.elements.HtmlDetailsElement;
import catalog.html.elements.HtmlDivElement;

public class FilterHtmlBrewDrink {
  public static HtmlDetailsElement render() {
    // Brew drink.
    HtmlDivElement brewDrinkWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Brew drink"))
        .addLastChild(HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.brew_drink)));

    // Coffee.
    HtmlDivElement coffeeWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Coffee"))
        .addLastChild(HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.coffee)));

    HtmlDivElement coffeeBeansWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Coffee beans"))
        .addLastChild(HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.coffee_beans)));

    HtmlDivElement coffeeGroundWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Ground coffee"))
        .addLastChild(HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.coffee_ground)));

    // Render "coffee" section:
    HtmlDetailsElement coffeeSection = ContentHtmlUtil.genDetailsSection(
        coffeeWrapDiv, ResourceFilterIds.coffee_details,
        new HtmlElement<?>[] { coffeeBeansWrapDiv, coffeeGroundWrapDiv });

    // Herbal drink.
    HtmlDivElement herbalDrinkWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Herbal drink"))
        .addLastChild(HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.herbaldrink)));

    // Tea.
    HtmlDivElement teaWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Tea"))
        .addLastChild(HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.tea)));

    HtmlDivElement teaBlackWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Black tea"))
        .addLastChild(HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.tea_black)));

    HtmlDivElement teaGreenWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Green tea"))
        .addLastChild(HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.tea_green)));

    // Render "tea" section:
    HtmlDetailsElement teaSection = ContentHtmlUtil.genDetailsSection(
        teaWrapDiv, ResourceFilterIds.tea_details, new HtmlElement<?>[] { teaBlackWrapDiv, teaGreenWrapDiv });

    //////////////////////////////////////////////////////////////////////////////
    // Main.
    //////////////////////////////////////////////////////////////////////////////
    // Render "brew drink" section:
    HtmlDetailsElement brewDrinkSection = ContentHtmlUtil.genDetailsSection(
        brewDrinkWrapDiv, ResourceFilterIds.brew_drink_details,
        new HtmlElement[] { coffeeSection, herbalDrinkWrapDiv, teaSection });

    return brewDrinkSection;
  }
}
