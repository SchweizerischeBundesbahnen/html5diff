[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=bugs)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=coverage)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)

This is a maintenance project of DaisyDiff in Java. The initial commit is a checkout of version 1.2 of [old DaisyDiff project](https://code.google.com/archive/p/daisydiff).

For more documentation see [daisydiff.github.io](https://daisydiff.github.io/).

> WARNING
The  maintenance of this repository by the [Nuxeo organization](https://github.com/nuxeo) is now strictly limited to critical security fixes.
If you need some other kind of maintenance, please check the repository's [forks](https://github.com/DaisyDiff/DaisyDiff/network/members) or fork it yourself.

# Standalone usage
```
java -jar daisydiff-1.2-NX4-SNAPSHOT-jar-with-dependencies.jar [oldHTML] [newHTML] [optional arguments]
```

Optional Arguments:
 * --file=[filename] - Write output to the specified file.
 * --type=[html/tag] - Use the html (default) diff algorithm or the tag diff.
 * --css=[cssfile1;cssfile2;cssfile3] - Add external CSS files.
 * --output=[html/xml] - Write html (default) or xml output.
 * --q  - Generate less console output.

Example:
```
java -jar daisydiff-1.2-NX4-SNAPSHOT-jar-with-dependencies.jar http://web.archive.org/web/20070107145418/http://news.bbc.co.uk/ http://web.archive.org/web/20070107182640/http://news.bbc.co.uk/ --css=http://web.archive.org/web/20070107145418/http://news.bbc.co.uk/nol/shared/css/news_r5.css
```

Requirements: Java 1.5 or 6

# Embedded usage
```
org.outerj.daisy.diff.DaisyDiff{

/**
 * Diffs two html files, outputting the result to the specified consumer.
 */
public static void diffHTML(InputSource oldSource, InputSource newSource, ContentHandler consumer, String prefix, Locale locale) throws SAXException, IOException;

/**
 * Diffs two html files word for word as source, outputting the result to
 * the specified consumer.
 */
public static void diffTag(String oldText, String newText, ContentHandler consumer) throws Exception;

}
```

Requirements: Java 1.5 or 6

To run Daisy Diff embedded in your application, you don't need the entire Jar file. A much smaller Jar file without Xerces and NekoHtml will suffice.


# PHP
The DaisyDiff algorithm has been integrated in MediaWiki. However, it had major errors and has been pulled out. More info at [www.mediawiki.org/wiki/Visual_Diff](http://www.mediawiki.org/wiki/Visual_Diff). See also [github.com/cdauth/htmldiff](https://github.com/cdauth/htmldiff).

# Acknowledgements

 * Guy Van den Broeck <guy@guyvdb.eu>
 * Daniel Dickison
 * Antoine Taillefer
 * Thomas Roger
