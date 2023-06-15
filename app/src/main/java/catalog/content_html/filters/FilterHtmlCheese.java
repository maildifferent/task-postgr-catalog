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
import catalog.html.elements.HtmlInputElement;
import catalog.html.elements.HtmlSummaryElement;

public class FilterHtmlCheese {
  public static HtmlDetailsElement render() {
    // Cheese.
    HtmlDivElement cheeseWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Cheese"))
        .addLastChild(HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.cheese)));

    // - Moisture.
    HtmlSummaryElement cheeseMoistureSum = HtmlDocument.createElement(HtmlSummaryElement.class)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_wrap)
            .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
                .addToClassList(ContentHtmlClasses.filter_line_text)
                .innerText("Moisture")));

    HtmlDivElement cheeseMoistureHardWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Hard cheese"))
        .addLastChild(
            HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.cheese_moisture_hard)));
    HtmlDivElement cheeseMoistureSemiWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Semi-hard cheese"))
        .addLastChild(
            HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.cheese_moisture_semi)));
    HtmlDivElement cheeseMoisturetSoftWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Soft cheese"))
        .addLastChild(
            HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.cheese_moisture_soft)));

    // Render "moisture" section:
    HtmlDetailsElement cheeseMoistureSectionDet = ContentHtmlUtil.genDetailsSection(
        cheeseMoistureSum, ResourceFilterIds.cheese_moisture_details,
        new HtmlElement<?>[] { cheeseMoistureHardWrapDiv, cheeseMoistureSemiWrapDiv, cheeseMoisturetSoftWrapDiv });

    // - Blue.
    HtmlDivElement cheeseBlueWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("Blue cheese"))
        .addLastChild(
            HtmlElemFromFilterFront.conv(CatalogFilterFront.renderSingle(ResourceFilterIds.cheese_blue)));

    // - Fat.
    HtmlSummaryElement cheeseFatSum = HtmlDocument.createElement(HtmlSummaryElement.class)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_wrap)
            .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
                .addToClassList(ContentHtmlClasses.filter_line_text)
                .innerText("Fat %")));

    HtmlInputElement cheeseFatGteInp = (HtmlInputElement) HtmlElemFromFilterFront.conv(
        CatalogFilterFront.renderSingle(ResourceFilterIds.cheese_fat_gte));
    cheeseFatGteInp.name("gte")
        .min(0)
        .max(100)
        .placeholder("%")
        .autocomplete("off");

    HtmlDivElement cheeseFatGteInpWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("From"))
        .addLastChild(cheeseFatGteInp);

    HtmlInputElement cheeseFatLteInp = (HtmlInputElement) HtmlElemFromFilterFront.conv(
        CatalogFilterFront.renderSingle(ResourceFilterIds.cheese_fat_lte));
    cheeseFatLteInp.name("lte")
        .min(0)
        .max(100)
        .placeholder("%")
        .autocomplete("off");

    HtmlDivElement cheeseFatLteInpWrapDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.filter_line_wrap)
        .addLastChild(HtmlDocument.createElement(HtmlDivElement.class)
            .addToClassList(ContentHtmlClasses.filter_line_text)
            .innerText("To"))
        .addLastChild(cheeseFatLteInp);

    // Render "fat" section:
    HtmlDetailsElement cheeseFatSectionDet = ContentHtmlUtil.genDetailsSection(cheeseFatSum,
        ResourceFilterIds.cheese_fat_details, new HtmlElement[] { cheeseFatGteInpWrapDiv, cheeseFatLteInpWrapDiv });

    //////////////////////////////////////////////////////////////////////////////
    // Main.
    //////////////////////////////////////////////////////////////////////////////
    // Render "cheese" section:
    HtmlDetailsElement cheeseSectionDet = ContentHtmlUtil.genDetailsSection(cheeseWrapDiv,
        ResourceFilterIds.cheese_details,
        new HtmlElement[] { cheeseMoistureSectionDet, cheeseBlueWrapDiv, cheeseFatSectionDet });

    return cheeseSectionDet;
  }
}
