package catalog.content_html;

import java.math.BigDecimal;

import catalog.content_main.ResourceObj;
import catalog.html.HtmlDocument;
import catalog.html.elements.HtmlDivElement;

public class CatalogItemHtml {
  public static HtmlDivElement render(ResourceObj resource) {
    HtmlDivElement mainDiv = HtmlDocument.createElement(HtmlDivElement.class)
        .addToClassList(ContentHtmlClasses.catalog_item_card);

    String weight = Integer.toString(resource.measurelot_weight_qty) + " " + resource.measurelot_weight_uon;

    BigDecimal priceBD = BigDecimal.valueOf(resource.price);
    var hundred = new BigDecimal(100);
    priceBD = priceBD.divide(hundred);
    String price = priceBD.toString() + " " + resource.price_cur;

    String item = """
        <a class=\"catalog_item_link_image\" href=\"#\">
          <div class=\"catalog_item_image_placeholder\">
            <img class=\"catalog_item_image\" src=\"/pub/images{{imgsrc}}\">
          </div>
        </a>
        <div class=\"catalog_item_content\">
          <a class=\"catalog_item_link_name\" href=\"#\">
            <div class=\"catalog_item_name\">{{name}}</div>
          </a>
          <div class=\"catalog_item_weight\">Weight: {{weight}}</div>
          <div class=\"catalog_item_price\"><div>Price:</div><div>{{price}}</div></div>
        </div>
        """;
    item = item.replace("{{imgsrc}}", resource.image_src);
    item = item.replace("{{name}}", resource.resource_name);
    item = item.replace("{{weight}}", weight);
    item = item.replace("{{price}}", price);

    mainDiv.innerHTML = item;
    return mainDiv;
  }
}
