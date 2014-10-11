# WIP
## Reference
https://developer.atlassian.com/display/CONFDEV/Confluence+REST+API+Examples
## [Relevant methods](https://developer.atlassian.com/display/CONFDEV/Remote+Confluence+Methods)
* Spaces
  * getSpace(String spaceKey)
    - returns a single Space. If the spaceKey does not exist versions (3.0+) will return a null object.
* Page 
  * getPage(String spaceKey, String pageTitle)
    - returns a single Page
  * storePage(Page  page)
    - For adding, the Page given as an argument should have space, title and content fields at a minimum.
    - For updating, the Page given should have id, space, title, content and version fields at a minimum.

## [Relevant data Objects](https://developer.atlassian.com/display/CONFDEV/Remote+Confluence+Data+Objects)
