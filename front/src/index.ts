"use strict"
console.clear()

////////////////////////////////////////////////////////////////////////////////
// Constants.
////////////////////////////////////////////////////////////////////////////////
const HTML_CLASSES = {
  catalog_row_filter: 'catalog_row_filter',
  catalog_row_items: 'catalog_row_items',
  catalog_row_pages: 'catalog_row_pages',
  filter_input_box: 'filter_input_box',
}

////////////////////////////////////////////////////////////////////////////////
// Init.
////////////////////////////////////////////////////////////////////////////////
function main() {
  const filterUpdater = new FilterUpdater()

  filterUpdater.catalogFilter.addEventListener('change', (event) => {
    const target = event.target
    if (!(target instanceof HTMLInputElement)) return
    if (!target.classList.contains(HTML_CLASSES.filter_input_box)) return

    filterUpdater.handleInputChange()
  })

  addEventListener('popstate', async (event) => {
    filterUpdater.handleWindowPopstate()
  })

  document.querySelectorAll(`.${HTML_CLASSES.catalog_row_filter} details`)
    .forEach((filterDetails) => {
      filterDetails.addEventListener('toggle', (event) => {
        const target = event.target
        if (!(target instanceof HTMLDetailsElement)) throw new Error()

        filterUpdater.handleDetailsToggle()
      })
    })
}

////////////////////////////////////////////////////////////////////////////////
// Filter updater.
////////////////////////////////////////////////////////////////////////////////
class FilterUpdater {
  public readonly catalogFilter: HTMLElement
  public readonly catalogItems: HTMLElement
  public readonly catalogPages: HTMLElement
  private readonly encodedNeededSections: string = `sections=${UtilUrl.encode(JSON.stringify(["items", "pages"]))}`

  constructor() {
    this.catalogFilter = UtilElems.getElemByCssSelector(document.body, `.${HTML_CLASSES.catalog_row_filter}`, HTMLDivElement)
    this.catalogItems = UtilElems.getElemByCssSelector(document.body, `.${HTML_CLASSES.catalog_row_items}`, HTMLDivElement)
    this.catalogPages = UtilElems.getElemByCssSelector(document.body, `.${HTML_CLASSES.catalog_row_pages}`, HTMLDivElement)
  }

  public async handleWindowPopstate() {
    const encodedHref = window.location.href
    const urlObj = new URL(encodedHref)
    if (urlObj.pathname !== '/catalog') return

    for (const key of urlObj.searchParams.keys()) {
      switch (key) {
        case 'filterState': break
        case 'page': break
        default: throw new Error()
      }
    }

    let fetchUrl = '/api/catalog'
    if (urlObj.search) fetchUrl += urlObj.search
    const response = await fetch(fetchUrl)
    const sections: unknown = await response.json()

    if (!UtilTypify.hasProperties(sections, ['filter', 'items', 'pages'] as const)) throw new Error()
    const filter: unknown = sections['filter']
    const items: unknown = sections['items']
    const pages: unknown = sections['pages']

    if (typeof filter !== 'string') throw new Error()
    if (typeof items !== 'string') throw new Error()
    if (typeof pages !== 'string') throw new Error()

    this.catalogFilter.innerHTML = filter
    this.catalogItems.innerHTML = items
    this.catalogPages.innerHTML = pages
  }

  public async handleInputChange() {
    try {
      const searchParams: string[] = []
      const filterStateParam = this.genFilterStateParam()
      if (filterStateParam) searchParams.push(filterStateParam)

      const fetchUrl = `/api/catalog?${[...searchParams, this.encodedNeededSections].join('&')}`
      const response = await fetch(fetchUrl)
      const sections: unknown = await response.json()

      if (!UtilTypify.hasProperties(sections, ['items', 'pages'] as const)) throw new Error()
      const items: unknown = sections['items']
      const pages: unknown = sections['pages']

      if (typeof items !== 'string') throw new Error()
      if (typeof pages !== 'string') throw new Error()

      this.catalogItems.innerHTML = items
      this.catalogPages.innerHTML = pages

      // https://stackoverflow.com/questions/11932869/how-to-dynamically-change-url-without-reloading
      // https://developer.mozilla.org/en-US/docs/Web/API/History_API
      // https://stackoverflow.com/questions/20937280/how-to-change-url-without-changing-browser-history
      // https://stackoverflow.com/questions/44553005/how-to-browser-back-when-using-pushstate
      let searchParamsStr = ''
      if (searchParams.length > 0) searchParamsStr = `?${searchParams.join('&')}`
      history.pushState({ page: 1 }, '0', searchParamsStr)
    } catch (error) {
      this.catalogItems.innerHTML = '<h1>404 Not Found</h1>No context found for request'
      this.catalogPages.innerHTML = ''
    }
  }

  public handleDetailsToggle() {

    const encodedHref = window.location.href
    const urlObj = new URL(encodedHref)
    if (urlObj.pathname !== '/catalog') return
    console.log(urlObj)

    const searchParams: string[] = []
    const filterStateParam = this.genFilterStateParam()
    if (filterStateParam) searchParams.push(filterStateParam)

    for (const [key, value] of urlObj.searchParams.entries()) {
      console.log(key)
      console.log(value)
      if (key !== 'filterState') {
        searchParams.push(`${key}=${value}`)
      }
    }

    let searchParamsStr = ''
    if (searchParams.length > 0) searchParamsStr = `?${searchParams.join('&')}`
    history.replaceState({ page: 1 }, '0', searchParamsStr)
  }

  private genFilterStateParam(): string | null {
    const filterState = new FilterState(this.catalogFilter)

    if (Object.keys(filterState).length > 0) {
      const filterStateStr = JSON.stringify(filterState)
      console.log(filterStateStr)
      const encodedFilterState = UtilUrl.encode(filterStateStr)
      return `filterState=${encodedFilterState}`
    }

    return null
  }
}

////////////////////////////////////////////////////////////////////////////////
// Filter state.
////////////////////////////////////////////////////////////////////////////////
type HtmlElemInputValueT = Record<string, string>

class FilterState {
  public readonly open?: string[]
  public readonly checked?: string[]
  public readonly value?: HtmlElemInputValueT

  constructor(root: HTMLElement) {
    let openElems = Array.from(root.querySelectorAll('details[open]'))
    openElems = FilterState.removeOpenParents(root, openElems)
    const openDetails = openElems.map(elem => elem.id)

    const checkedInputElems = root.querySelectorAll('input[type="checkbox"]:checked')
    const checkedInput = Array.from(checkedInputElems).map(elem => elem.id)

    const valueInputElems = UtilElems.getElemsByCssSelector(root, 'input[type="number"]', HTMLInputElement)
    const valueInput: HtmlElemInputValueT = {}
    valueInputElems.filter(elem => elem.value !== "")
      .forEach(elem => valueInput[elem.id] = elem.value)

    if (openDetails.length > 0) this.open = openDetails
    if (checkedInput.length > 0) this.checked = checkedInput
    if (Object.keys(valueInput).length > 0) this.value = valueInput
  }

  private static removeOpenParents(root: HTMLElement, openElems: Element[]): Element[] {
    const ancestors = new Set<Element>()

    for (const elem of openElems) {
      let ancestor: Element | null = elem
      while (ancestor.parentNode instanceof Element && (ancestor = ancestor.parentNode.closest('details'))) {
        if (ancestor === root) break
        if (!root.contains(ancestor)) break
        ancestors.add(ancestor)
      }
    }

    openElems = openElems.filter(elem => !ancestors.has(elem))
    return openElems
  }
}

////////////////////////////////////////////////////////////////////////////////
// Util.
////////////////////////////////////////////////////////////////////////////////
const UtilUrl = {
  // https://meyerweb.com/eric/tools/dencoder/
  decode(encoded: string): string {
    return decodeURIComponent(encoded.replace(/\+/g, " "))
  },

  // https://meyerweb.com/eric/tools/dencoder/
  encode(decoded: string): string {
    return encodeURIComponent(decoded).replace(/'/g, "%27").replace(/"/g, "%22")
  }
}

const UtilElems = {
  getElemById<T>(
    id: string,
    domClass: { new(): T; prototype: T }
  ): T {
    const elem = document.getElementById(id)
    if (!elem) throw new Error()
    if (!(elem instanceof domClass)) throw new Error()
    return elem
  },

  getElemByCssSelector<T extends Element>(
    parent: Element,
    cssSelector: string,
    domClass: { new(): T; prototype: T }
  ): T {
    const elem = parent.querySelector(cssSelector)
    if (!elem) throw new Error()
    if (!(elem instanceof domClass)) throw new Error()
    return elem
  },

  getElemsByCssSelector<T extends Element>(
    parent: Element,
    cssSelector: string,
    domClass: { new(): T; prototype: T }
  ): T[] {
    const typedElems: T[] = []
    const elems = parent.querySelectorAll(cssSelector)
    for (const elem of elems) {
      if (!(elem instanceof domClass)) throw new Error()
      typedElems.push(elem)
    }
    return typedElems
  },
}

const UtilTypify = {
  // Usage example:
  // if (!hasProperties(tabqRes, ['rows', 'rowCount'] as const)) throw new Error()
  // const rows: unknown = tabqRes['rows']
  // const rowCount: unknown = tabqRes['rowCount']
  hasProperties<Obj, Prop extends string>(
    something: Obj, props: Readonly<Prop[]>
  ): something is unknown extends Obj ? Obj & { [key in Prop]: unknown } : Obj {
    if (typeof something !== 'object' || !something) throw new Error()
    for (const prop of props) {
      if (something[prop as unknown as keyof typeof something] === undefined) throw new Error()
    }
    return true
  },
}

////////////////////////////////////////////////////////////////////////////////
// Start.
////////////////////////////////////////////////////////////////////////////////
main()
